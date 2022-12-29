package com.centit.dde.services.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizOptFlow;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataOptResult;
import com.centit.dde.dao.DataPacketDao;
import com.centit.dde.dao.DataPacketDraftDao;
import com.centit.dde.dao.TaskDetailLogDao;
import com.centit.dde.dao.TaskLogDao;
import com.centit.dde.po.*;
import com.centit.dde.utils.ConstantValue;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.WebOptUtils;
import com.centit.framework.filter.RequestThreadLocal;
import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import com.centit.framework.model.adapter.PlatformEnvironment;
import com.centit.framework.model.basedata.IOsInfo;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.algorithm.UuidOpt;
import com.centit.support.common.ObjectException;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

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
    @Autowired
    private PlatformEnvironment platformEnvironment;
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

    public void agentRunTask(String dataPacketId) {
        DataPacket dataPacket = dataPacketDao.getObjectById(dataPacketId);
//        CodeRepositoryCache.setPlatformEnvironment(platformEnvironment);
        runTask(dataPacket, new DataOptContext());
    }

    public DataOptResult runTask(DataPacketInterface dataPacketInterface, DataOptContext optContext) {
        TaskLog taskLog = buildLogInfo(optContext.getRunType(), dataPacketInterface);
        if (ConstantValue.LOGLEVEL_CHECK_ERROR != dataPacketInterface.getLogLevel()) {
            //保存日志基本信息
            taskLogDao.saveNewObject(taskLog);
        }

        optContext.setTaskLog(taskLog);
        try {
            IOsInfo osInfo;
            try {
                osInfo = platformEnvironment.getOsInfo(dataPacketInterface.getOsId());
            } catch (Exception e){
                osInfo=null;
            }
            if(osInfo!=null){
               optContext.setTopUnit(osInfo.getTopUnit());
            }
            DataOptResult runResult = runOptModule(dataPacketInterface, optContext);
            //更新API信息
            updateApiData(optContext.getRunType(), dataPacketInterface);
            if (ConstantValue.LOGLEVEL_CHECK_ERROR != dataPacketInterface.getLogLevel()) {
                updateLog(taskLog);
            }
            return runResult;
        } catch (Exception e) {
            dealException(taskLog, dataPacketInterface, e);
            //创建一个错误的返回结果
            return DataOptResult.createExceptionResult(ResponseData.makeErrorMessageWithData(
                taskLog, ResponseData.ERROR_OPERATION, ObjectException.extortExceptionMessage(e)));
        }
    }

    private DataOptResult runOptModule(DataPacketInterface dataPacketInterface, DataOptContext dataOptContext) throws Exception {
        JSONObject bizOptJson = dataPacketInterface.getDataOptDescJson();
        if (bizOptJson.isEmpty()) {
            throw new ObjectException("运行步骤为空");
        }
        return bizOptFlow.run(dataPacketInterface, dataOptContext);
    }

    private void dealException(TaskLog taskLog, DataPacketInterface dataPacketInterface, Exception e) {
        taskLog.setOtherMessage("error");
        taskLog.setRunEndTime(new Date());
        taskLogDao.mergeObject(taskLog);
        saveDetail(ObjectException.extortExceptionMessage(e, 4), taskLog);
    }

    private void saveDetail(String info, TaskLog taskLog) {
        TaskDetailLog detailLog = new TaskDetailLog();
        detailLog.setRunBeginTime(new Date());
        detailLog.setTaskId(taskLog.getTaskId());
        detailLog.setLogId(taskLog.getLogId());
        detailLog.setLogType("error");
        detailLog.setLogInfo(info);
        detailLog.setStepNo(taskLog.getStepNo());
        detailLog.setRunEndTime(new Date());
        taskDetailLogDao.saveNewObject(detailLog);
    }

    private TaskLog buildLogInfo(String runType, DataPacketInterface dataPacketInterface) {
        TaskLog taskLog = new TaskLog();
        taskLog.setRunBeginTime(new Date());
        taskLog.setApiType(ConstantValue.RUN_TYPE_DEBUG.equals(runType) ? 0 : 1);
        if (dataPacketInterface != null) {
            if (!StringBaseOpt.isNvl(dataPacketInterface.getTaskType()) && ConstantValue.TASK_TYPE_AGENT.equals(dataPacketInterface.getTaskType())) {
                taskLog.setRunner("定时任务");
            } else {
                taskLog.setRunner(WebOptUtils.getCurrentUserCode(RequestThreadLocal.getLocalThreadWrapperRequest()));
            }
        }
        taskLog.setLogId(UuidOpt.getUuidAsString32());
        taskLog.setOptId(dataPacketInterface.getOptId());
        taskLog.setApplicationId(dataPacketInterface.getOsId());
        taskLog.setRunType(dataPacketInterface.getPacketName());
        taskLog.setTaskId(dataPacketInterface.getPacketId());
        return taskLog;
    }

    private void updateLog(TaskLog taskLog) {
        taskLog.setRunEndTime(new Date());
        String sql = "SELECT count(*) as count FROM d_task_detail_log WHERE log_id=? and log_info not like ? ";
        int count = NumberBaseOpt.castObjectToInteger(DatabaseOptUtils.getScalarObjectQuery(taskDetailLogDao, sql, new Object[]{taskLog.getLogId(), "ok"}));
        String message = count > 0 ? "error" : "ok";
        taskLog.setOtherMessage(message);
        taskLogDao.updateObject(taskLog);
        log.debug("更新API执行日志，日志信息：{}", JSON.toJSONString(taskLog));
    }

    private void updateApiData(String runType, DataPacketInterface dataPacketInterface) throws Exception {
        dataPacketInterface.setLastRunTime(new Date());
        if (ConstantValue.TASK_TYPE_AGENT.equals(dataPacketInterface.getTaskType())
            && dataPacketInterface.getIsValid()
            && !StringBaseOpt.isNvl(dataPacketInterface.getTaskCron())) {
            CronExpression cronExpression = new CronExpression(dataPacketInterface.getTaskCron());
            dataPacketInterface.setNextRunTime(cronExpression.getNextValidTimeAfter(dataPacketInterface.getLastRunTime()));
        }else {
            dataPacketInterface.setNextRunTime(null);
        }
        if (ConstantValue.RUN_TYPE_DEBUG.equals(runType)) {
            dataPacketCopyDao.updateObject(new String[]{"lastRunTime"}, (DataPacketDraft) dataPacketInterface);
        } else {
            dataPacketDao.updateObject(new String[]{"lastRunTime","nextRunTime"},(DataPacket) dataPacketInterface);
            DataPacketDraft dataPacketDraft=new DataPacketDraft();
            dataPacketDraft.setPacketId(dataPacketInterface.getPacketId());
            dataPacketDraft.setLastRunTime(dataPacketInterface.getLastRunTime());
            dataPacketDraft.setNextRunTime(dataPacketInterface.getNextRunTime());
            dataPacketCopyDao.updateObject(new String[]{"lastRunTime","nextRunTime"},dataPacketDraft);
        }
    }
}
