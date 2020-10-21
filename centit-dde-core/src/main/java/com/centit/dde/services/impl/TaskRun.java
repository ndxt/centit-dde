package com.centit.dde.services.impl;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOptFlow;
import com.centit.dde.core.DataSet;
import com.centit.dde.dao.DataPacketDao;
import com.centit.dde.dao.TaskDetailLogDao;
import com.centit.dde.dao.TaskLogDao;
import com.centit.dde.dataset.SQLDataSetWriter;
import com.centit.dde.po.DataPacket;
import com.centit.dde.po.TaskDetailLog;
import com.centit.dde.po.TaskLog;
import com.centit.dde.services.DBPacketBizSupplier;
import com.centit.fileserver.common.FileStore;
import com.centit.framework.ip.service.IntegrationEnvironment;
import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.algorithm.UuidOpt;
import com.centit.support.common.ObjectException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author zhf
 */
@Service
public class TaskRun {
    private final TaskLogDao taskLogDao;
    private final TaskDetailLogDao taskDetailLogDao;
    private final DataPacketDao dataPacketDao;
    private final IntegrationEnvironment integrationEnvironment;

    @Autowired(required = false)
    public void setBizOptFlow(BizOptFlow bizOptFlow) {
        this.bizOptFlow = bizOptFlow;
    }

    private BizOptFlow bizOptFlow;
    private FileStore fileStore;

    @Autowired(required = false)
    public void setFileStore(FileStore fileStore) {
        this.fileStore = fileStore;
    }


    @Autowired
    public TaskRun(TaskLogDao taskLogDao,
                   TaskDetailLogDao taskDetailLogDao,
                   DataPacketDao dataPacketDao,
                   IntegrationEnvironment integrationEnvironment) {
        this.taskLogDao = taskLogDao;
        this.taskDetailLogDao = taskDetailLogDao;
        this.dataPacketDao = dataPacketDao;
        this.integrationEnvironment = integrationEnvironment;
    }

    private BizModel runStep(DataPacket dataPacket, String logId) throws Exception {
        JSONObject bizOptJson = dataPacket.getDataOptDescJson();
        if (bizOptJson.isEmpty()) {
            return null;
        }
        DBPacketBizSupplier dbPacketBizSupplier = new DBPacketBizSupplier(dataPacket);
        dbPacketBizSupplier.setIntegrationEnvironment(integrationEnvironment);
        dbPacketBizSupplier.setFileStore(fileStore);
        dbPacketBizSupplier.setBatchWise(dataPacket.getIsWhile());
        /*添加参数默认值传输*/
        dbPacketBizSupplier.setQueryParams(dataPacket.getPacketParamsValue());
        return bizOptFlow.run(dbPacketBizSupplier, bizOptJson, logId);
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

    public void runTask(String packetId) {
        BizModel bizModel = null;
        TaskLog taskLog = new TaskLog();
        Date beginTime = new Date();
        Boolean result;
        try {
            DataPacket dataPacket = dataPacketDao.getObjectWithReferences(packetId);
            dataPacket.setLastRunTime(new Date());
            taskLog.setTaskId(packetId);
            taskLog.setApplicationId(dataPacket.getApplicationId());
            taskLog.setRunBeginTime(beginTime);
            taskLog.setRunType(dataPacket.getPacketName());
            taskLogDao.saveNewObject(taskLog);
            bizModel = runStep(dataPacket, taskLog.getLogId());
            taskLog.setRunEndTime(new Date());
            dataPacket.setNextRunTime(new Date());
            if ("2".equals(dataPacket.getTaskType())
                && dataPacket.getIsValid()
                && !StringBaseOpt.isNvl(dataPacket.getTaskCron())) {
                CronSequenceGenerator cronSequenceGenerator = new CronSequenceGenerator(dataPacket.getTaskCron());
                dataPacket.setNextRunTime(cronSequenceGenerator.next(dataPacket.getLastRunTime()));
            }
            dataPacketDao.updateObject(dataPacket);
            TaskDetailLog taskDetailLog = taskDetailLogDao.getObjectByProperties(
                CollectionsOpt.createHashMap("logId",taskLog.getLogId(),"logInfo_ne","ok"));
            taskLog.setOtherMessage(taskDetailLog==null ? "ok" : "error");
            taskLogDao.updateObject(taskLog);
        } catch (Exception e) {
            saveDetail(ObjectException.extortExceptionMessage(e, 4),
                taskLog);
            taskLog.setOtherMessage("error");
            taskLogDao.saveNewObject(taskLog);
        }
    }
}
