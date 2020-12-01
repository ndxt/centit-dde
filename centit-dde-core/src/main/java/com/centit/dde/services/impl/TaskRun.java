package com.centit.dde.services.impl;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOptFlow;
import com.centit.dde.dao.DataPacketDao;
import com.centit.dde.dao.TaskDetailLogDao;
import com.centit.dde.dao.TaskLogDao;
import com.centit.dde.po.DataPacket;
import com.centit.dde.po.TaskDetailLog;
import com.centit.dde.po.TaskLog;
import com.centit.fileserver.common.FileStore;
import com.centit.framework.ip.service.IntegrationEnvironment;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.common.ObjectException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * @author zhf
 */
@Service
public class TaskRun {
    private final TaskLogDao taskLogDao;
    private final TaskDetailLogDao taskDetailLogDao;
    private final DataPacketDao dataPacketDao;
    private final IntegrationEnvironment integrationEnvironment;
    private final BizOptFlow bizOptFlow;
    private FileStore fileStore;

    @Autowired(required = false)
    public void setFileStore(FileStore fileStore) {
        this.fileStore = fileStore;
    }


    @Autowired
    public TaskRun(TaskLogDao taskLogDao,
                   TaskDetailLogDao taskDetailLogDao,
                   DataPacketDao dataPacketDao,
                   IntegrationEnvironment integrationEnvironment, BizOptFlow bizOptFlow) {
        this.taskLogDao = taskLogDao;
        this.taskDetailLogDao = taskDetailLogDao;
        this.dataPacketDao = dataPacketDao;
        this.integrationEnvironment = integrationEnvironment;
        this.bizOptFlow = bizOptFlow;
    }

    private BizModel runStep(DataPacket dataPacket, String logId, Map<String, Object> queryParams) {
        JSONObject bizOptJson = dataPacket.getDataOptDescJson();
        if (bizOptJson.isEmpty()) {
            return null;
        }
        bizOptFlow.initStep(0);
        return bizOptFlow.run(bizOptJson, logId, queryParams == null ? dataPacket.getPacketParamsValue() : queryParams);
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

    public BizModel runTask(String packetId, Map<String, Object> queryParams) {
        TaskLog taskLog = new TaskLog();
        Date beginTime = new Date();
        try {
            DataPacket dataPacket = dataPacketDao.getObjectWithReferences(packetId);
            dataPacket.setLastRunTime(new Date());
            taskLog.setTaskId(packetId);
            taskLog.setApplicationId(dataPacket.getApplicationId());
            taskLog.setRunBeginTime(beginTime);
            taskLog.setRunType(dataPacket.getPacketName());
            taskLogDao.saveNewObject(taskLog);
            BizModel bizModel = runStep(dataPacket, taskLog.getLogId(), queryParams);
            taskLog.setRunEndTime(new Date());
            dataPacket.setNextRunTime(new Date());
            String two = "2";
            if (two.equals(dataPacket.getTaskType())
                && dataPacket.getIsValid()
                && !StringBaseOpt.isNvl(dataPacket.getTaskCron())) {
                CronSequenceGenerator cronSequenceGenerator = new CronSequenceGenerator(dataPacket.getTaskCron());
                dataPacket.setNextRunTime(cronSequenceGenerator.next(dataPacket.getLastRunTime()));
            }
            dataPacketDao.updateObject(dataPacket);
            TaskDetailLog taskDetailLog = taskDetailLogDao.getObjectByProperties(
                CollectionsOpt.createHashMap("logId", taskLog.getLogId(), "logInfo_ne", "ok"));
            taskLog.setOtherMessage(taskDetailLog == null ? "ok" : "error");
            taskLogDao.updateObject(taskLog);
            return bizModel;
        } catch (Exception e) {
            saveDetail(ObjectException.extortExceptionMessage(e, 4),
                taskLog);
            taskLog.setOtherMessage("error");
            taskLogDao.mergeObject(taskLog);
        }
        return null;
    }
}
