package com.centit.dde.services.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizOptFlow;
import com.centit.dde.dao.DataPacketDao;
import com.centit.dde.dao.DataPacketDraftDao;
import com.centit.dde.dao.TaskDetailLogDao;
import com.centit.dde.dao.TaskLogDao;
import com.centit.dde.po.*;
import com.centit.dde.utils.ConstantValue;
import com.centit.framework.common.WebOptUtils;
import com.centit.framework.filter.RequestThreadLocal;
import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import com.centit.framework.model.adapter.NotificationCenter;
import com.centit.framework.model.basedata.NoticeMessage;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.common.ObjectException;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhf
 */
@Service
@Slf4j
public class TaskRun {
    private final TaskLogDao taskLogDao;
    private final TaskDetailLogDao taskDetailLogDao;
    private final DataPacketDraftDao dataPacketCopyDao;
    private final DataPacketDao dataPacketDao;
    private final BizOptFlow bizOptFlow;

    @Autowired(required = false)
    private NotificationCenter notificationCenter;

    @Autowired
    public TaskRun(TaskLogDao taskLogDao, TaskDetailLogDao taskDetailLogDao,
                   DataPacketDraftDao dataPacketCopyDao, DataPacketDao dataPacketDao,
                   BizOptFlow bizOptFlow) {
        this.taskLogDao = taskLogDao;
        this.taskDetailLogDao = taskDetailLogDao;
        this.dataPacketCopyDao = dataPacketCopyDao;
        this.dataPacketDao = dataPacketDao;
        this.bizOptFlow = bizOptFlow;
    }

    public Object runTask(DataPacketInterface dataPacketInterface, Map<String, Object> callStackData){

        String runType = StringBaseOpt.castObjectToString(callStackData.get(ConstantValue.RUN_TYPE_TAG), ConstantValue.RUN_TYPE_NORMAL);

        TaskLog taskLog = new TaskLog();
        taskLog.setRunBeginTime(new Date());
        buildLogInfo(taskLog, runType, dataPacketInterface);

        if ( (ConstantValue.LOGLEVEL_CHECK_INFO & dataPacketInterface.getLogLevel()) != 0){//不记录任何日志
            //保存日志基本信息
            taskLogDao.saveNewObject(taskLog);
        } else {
            callStackData.put(ConstantValue.BUILD_LOG_INFO_TAG,taskLog);
        }
        try {
            Object runResult = runStep(dataPacketInterface, taskLog.getLogId(), callStackData);
            //更新API信息
            updateApiData(runType,dataPacketInterface);
            if ((ConstantValue.LOGLEVEL_CHECK_INFO & dataPacketInterface.getLogLevel()) != 0){//不记录任何日志
                //更新日志信息
                updateLog(taskLog);
            }
            return runResult;
        } catch (Exception e) {
            dealException(taskLog, dataPacketInterface, e);
        }
        return new Object();
    }

    private Object runStep(DataPacketInterface dataPacketInterface, String logId, Map<String, Object> callStackData) throws Exception {
        JSONObject bizOptJson = dataPacketInterface.getDataOptDescJson();
        if (bizOptJson.isEmpty()) {
            throw new ObjectException("运行步骤为空");
        }
        Map<String, Object> mapObject = new HashMap<>(dataPacketInterface.getPacketParamsValue());
       // if (queryParams != null) {
        //    mapObject.putAll(queryParams);
        //}
        return bizOptFlow.run(dataPacketInterface, logId, callStackData);
    }

    private void dealException(TaskLog taskLog, DataPacketInterface dataPacketInterface, Exception e) {
        saveDetail(ObjectException.extortExceptionMessage(e, 4), taskLog);
        taskLog.setOtherMessage("error");
        taskLog.setRunEndTime(new Date());
        taskLogDao.mergeObject(taskLog);
        notificationCenter.sendMessage("system", "system",
            NoticeMessage.create().operation("dde").method("run").subject("任务执行异常")
                .content(dataPacketInterface.getPacketId() + ":" + dataPacketInterface.getPacketName()));
    }

    private void saveDetail(String info, TaskLog taskLog) {
        TaskDetailLog detailLog = new TaskDetailLog();
        detailLog.setRunBeginTime(new Date());
        detailLog.setTaskId(taskLog.getTaskId());
        detailLog.setLogId(taskLog.getLogId());
        detailLog.setLogType("error");
        detailLog.setLogInfo(info);
        detailLog.setRunEndTime(new Date());
        taskDetailLogDao.saveNewObject(detailLog);
    }

    private void buildLogInfo(TaskLog taskLog,String runType,DataPacketInterface dataPacketInterface) {
        taskLog.setApiType(ConstantValue.RUN_TYPE_COPY.equals(runType) ? 0 : 1);
        if (dataPacketInterface !=null){
            if (!StringBaseOpt.isNvl(dataPacketInterface.getTaskType()) && "2".equals(dataPacketInterface.getTaskType())){
                taskLog.setRunner("定时任务");
            }else {
                taskLog.setRunner(WebOptUtils.getCurrentUserCode(RequestThreadLocal.getLocalThreadWrapperRequest()));
            }
        }
        taskLog.setApplicationId(dataPacketInterface.getOsId());
        taskLog.setRunType(dataPacketInterface.getPacketName());
        taskLog.setTaskId(dataPacketInterface.getPacketId());
        log.debug("新增API执行日志，日志信息：{}", StringBaseOpt.castObjectToString(taskLog));
    }

    private void updateLog(TaskLog taskLog) {
        taskLog.setRunEndTime(new Date());
        String sql ="SELECT count(log_detail_id) as count  FROM d_task_detail_log WHERE log_id=? and log_info <> ? ";
        int count = NumberBaseOpt.castObjectToInteger(DatabaseOptUtils.getScalarObjectQuery(taskDetailLogDao, sql, new Object[]{taskLog.getLogId(), "ok"}));
        String message= count>0?"error":"ok";
        taskLog.setOtherMessage(message);
        taskLogDao.updateObject(taskLog);
        log.debug("更新API执行日志，日志信息：{}", JSON.toJSONString(taskLog));
    }

    private void updateApiData(String runType,DataPacketInterface dataPacketInterface) throws Exception {
        dataPacketInterface.setLastRunTime(new Date());
        if (ConstantValue.FINAL_TWO.equals(dataPacketInterface.getTaskType())
            && dataPacketInterface.getIsValid()
            && !StringBaseOpt.isNvl(dataPacketInterface.getTaskCron())) {
            CronExpression cronExpression = new CronExpression(dataPacketInterface.getTaskCron());
            dataPacketInterface.setNextRunTime(cronExpression.getNextValidTimeAfter(dataPacketInterface.getLastRunTime()));
        }
        if (ConstantValue.RUN_TYPE_COPY.equals(runType)) {
            if (ConstantValue.FINAL_TWO.equals(dataPacketInterface.getTaskType())){//定时任务才更新
                dataPacketInterface.setNextRunTime(new Date());
                DatabaseOptUtils.doExecuteSql(dataPacketCopyDao, "update q_data_packet_draft set next_run_time=? where packet_id=?",
                    new Object[]{dataPacketInterface.getNextRunTime(), dataPacketInterface.getPacketId()});
            }
            dataPacketCopyDao.mergeObject((DataPacketDraft)dataPacketInterface);
        } else {
            if (ConstantValue.FINAL_TWO.equals(dataPacketInterface.getTaskType())) {//定时任务才更新
                dataPacketInterface.setNextRunTime(new Date());
                DatabaseOptUtils.doExecuteSql(dataPacketDao, "update q_data_packet set next_run_time=? where packet_id=?",
                    new Object[]{dataPacketInterface.getNextRunTime(), dataPacketInterface.getPacketId()});
                DatabaseOptUtils.doExecuteSql(dataPacketCopyDao, "update q_data_packet_draft set next_run_time=? where packet_id=?",
                    new Object[]{dataPacketInterface.getNextRunTime(), dataPacketInterface.getPacketId()});
            }
            dataPacketDao.mergeObject((DataPacket)dataPacketInterface);
            //将正式流程执行的时间同步到草稿表中
            DataPacket dataPacket = (DataPacket)dataPacketInterface;
            DatabaseOptUtils.doExecuteSql(dataPacketCopyDao, "update q_data_packet_draft set LAST_RUN_TIME=? where packet_id=?",
                new Object[]{dataPacket.getLastRunTime(), dataPacket.getPacketId()});
        }
        log.debug("更新API执行信息，执行类型：{}，更新信息{}", runType,JSON.toJSONString(dataPacketInterface));
    }
}
