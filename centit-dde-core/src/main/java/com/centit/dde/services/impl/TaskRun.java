package com.centit.dde.services.impl;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizOptFlow;
import com.centit.dde.dao.DataPacketCopyDao;
import com.centit.dde.dao.DataPacketDao;
import com.centit.dde.dao.TaskDetailLogDao;
import com.centit.dde.dao.TaskLogDao;
import com.centit.dde.po.*;
import com.centit.dde.utils.ConstantValue;
import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import com.centit.framework.model.adapter.NotificationCenter;
import com.centit.framework.model.basedata.NoticeMessage;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.common.ObjectException;
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
public class TaskRun {
    private final TaskLogDao taskLogDao;
    private final TaskDetailLogDao taskDetailLogDao;
    private final DataPacketCopyDao dataPacketCopyDao;
    private final DataPacketDao dataPacketDao;
    private final BizOptFlow bizOptFlow;
    @Autowired(required = false)
    private NotificationCenter notificationCenter;

    @Autowired
    public TaskRun(TaskLogDao taskLogDao, TaskDetailLogDao taskDetailLogDao,
                   DataPacketCopyDao dataPacketCopyDao, DataPacketDao dataPacketDao,
                   BizOptFlow bizOptFlow) {
        this.taskLogDao = taskLogDao;
        this.taskDetailLogDao = taskDetailLogDao;
        this.dataPacketCopyDao = dataPacketCopyDao;
        this.dataPacketDao = dataPacketDao;
        this.bizOptFlow = bizOptFlow;
    }

    public Object runTask(String packetId, Map<String, Object> queryParams) {
        String runType = ConstantValue.RUN_TYPE_NORMAL;
        if (queryParams != null && queryParams.containsKey("runType")) {
            runType = (String) queryParams.get("runType");
        }
        TaskLog taskLog = new TaskLog();
        Date beginTime = new Date();
        DataPacketInterface dataPacketInterface;
        if (ConstantValue.RUN_TYPE_COPY.equals(runType)) {
            DataPacketCopy dataPacketCopy = dataPacketCopyDao.getObjectWithReferences(packetId);
            dataPacketCopy.setLastRunTime(new Date());
            dataPacketInterface = dataPacketCopy;
            taskLog.setRunner("T");
            taskLog.setApplicationId(dataPacketCopy.getApplicationId());
            taskLog.setRunType(dataPacketCopy.getPacketName());
        } else {
            DataPacket dataPacket = dataPacketDao.getObjectWithReferences(packetId);
            dataPacket.setLastRunTime(new Date());
            dataPacketInterface = dataPacket;
            taskLog.setRunner("A");
            taskLog.setApplicationId(dataPacket.getApplicationId());
            taskLog.setRunType(dataPacket.getPacketName());
        }
        try {
            taskLog.setTaskId(packetId);
            taskLog.setRunBeginTime(beginTime);
            taskLogDao.saveNewObject(taskLog);
            Object runResult;
            runResult = runStep(dataPacketInterface, taskLog.getLogId(), queryParams);
            String two = "2";
            taskLog.setRunEndTime(new Date());
            dataPacketInterface.setNextRunTime(new Date());
            if (two.equals(dataPacketInterface.getTaskType())
                && dataPacketInterface.getIsValid()
                && !StringBaseOpt.isNvl(dataPacketInterface.getTaskCron())) {
                CronExpression cronExpression = new CronExpression(dataPacketInterface.getTaskCron());
                dataPacketInterface.setNextRunTime(cronExpression.getNextValidTimeAfter(dataPacketInterface.getLastRunTime()));
            }
            DatabaseOptUtils.doExecuteSql(dataPacketDao, "update q_data_packet set next_run_time=? where packet_id=?",
                new Object[]{dataPacketInterface.getNextRunTime(), dataPacketInterface.getPacketId()});
            TaskDetailLog taskDetailLog = taskDetailLogDao.getObjectByProperties(
                CollectionsOpt.createHashMap("logId", taskLog.getLogId(), "logInfo_ne", "ok"));
            taskLog.setOtherMessage(taskDetailLog == null ? "ok" : "error");
            taskLogDao.updateObject(taskLog);
            return runResult;
        } catch (Exception e) {
            dealException(taskLog, dataPacketInterface, e);
        }
        return new Object();
    }

    private Object runStep(DataPacketInterface dataPacketInterface, String logId, Map<String, Object> queryParams) throws Exception {
        JSONObject bizOptJson = dataPacketInterface.getDataOptDescJson();
        if (bizOptJson.isEmpty()) {
            throw new ObjectException("运行步骤为空");
        }
        Map<String, Object> mapObject = new HashMap<>(dataPacketInterface.getPacketParamsValue());
        if (queryParams != null) {
            mapObject.putAll(queryParams);
        }
        return bizOptFlow.run(dataPacketInterface, logId, mapObject);
    }

    private void dealException(TaskLog taskLog, DataPacketInterface dataPacketInterface, Exception e) {
        saveDetail(ObjectException.extortExceptionMessage(e, 4),
            taskLog);
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

}
