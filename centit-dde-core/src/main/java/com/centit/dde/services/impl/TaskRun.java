package com.centit.dde.services.impl;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.DataSet;
import com.centit.dde.dao.DataPacketDao;
import com.centit.dde.dao.TaskDetailLogDao;
import com.centit.dde.dao.TaskLogDao;
import com.centit.dde.dataset.SQLDataSetWriter;
import com.centit.dde.po.DataPacket;
import com.centit.dde.po.TaskDetailLog;
import com.centit.dde.po.TaskLog;
import com.centit.dde.services.DBPacketBizSupplier;
import com.centit.dde.utils.DBBatchUtils;
import com.centit.fileserver.common.FileStore;
import com.centit.framework.ip.service.IntegrationEnvironment;
import com.centit.dde.core.BizOptFlow;
import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import com.centit.product.metadata.service.DatabaseRunTime;
import com.centit.product.metadata.service.MetaDataService;
import com.centit.product.metadata.service.MetaObjectService;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.algorithm.UuidOpt;
import com.centit.support.database.utils.FieldType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    private TaskLog taskLog;
    private Date beginTime;


    @Autowired
    public TaskRun(TaskLogDao taskLogDao,
                   TaskDetailLogDao taskDetailLogDao,
                   DataPacketDao dataPacketDao,
                   IntegrationEnvironment integrationEnvironment) {
        this.taskLogDao = taskLogDao;
        this.taskDetailLogDao = taskDetailLogDao;
        this.dataPacketDao = dataPacketDao;
        this.integrationEnvironment = integrationEnvironment;
        this.taskLog = new TaskLog();
    }

    private void runStep(DataPacket dataPacket) {
        JSONObject bizOptJson = dataPacket.getDataOptDescJson();
        if (bizOptJson.isEmpty()) {
            return;
        }
        BizModel bizModel = null;
        try {
            DBPacketBizSupplier dbPacketBizSupplier = new DBPacketBizSupplier(dataPacket);
            dbPacketBizSupplier.setIntegrationEnvironment(integrationEnvironment);
            dbPacketBizSupplier.setFileStore(fileStore);
            dbPacketBizSupplier.setBatchWise(dataPacket.getIsWhile());
            /*添加参数默认值传输*/
            dbPacketBizSupplier.setQueryParams(dataPacket.getPacketParamsValue());
            bizModel = bizOptFlow.run(dbPacketBizSupplier, bizOptJson);
            saveDetail(bizModel, "ok");
        } catch (Exception e) {
            saveDetail(bizModel, getStackTrace(e));
        }

    }

    private String getStackTrace(Exception e) {
        StringBuilder message = new StringBuilder();
        StackTraceElement[] exceptionStack = e.getStackTrace();
        message.append(e.toString());
        int i = 0;
        for (StackTraceElement ste : exceptionStack) {
            message.append("\n\tat ").append(ste);
            if (i++ == 4) {
                break;
            }
        }
        return message.toString();
    }

    private void saveDetail(BizModel iResult, String info) {
        List<TaskDetailLog> taskDetailLogs = new ArrayList<>();
        if (iResult != null) {
            for (DataSet dataset : iResult.getBizData().values()) {
                TaskDetailLog detailLog=new TaskDetailLog();
                detailLog.setRunBeginTime(beginTime);
                detailLog.setTaskId(taskLog.getTaskId());
                detailLog.setLogId(taskLog.getLogId());
                detailLog.setSuccessPieces(0L);
                detailLog.setErrorPieces(0L);
                StringBuilder msg = new StringBuilder();
                if (dataset.getData() != null) {
                    msg.append(dataset.getDataSetName()).append(":");
                    msg.append(dataset.size()).append("nums");
                    if (dataset.size() > 0) {
                        String error= (String) dataset.getData().get(0).get(FieldType.mapPropName(SQLDataSetWriter.WRITER_ERROR_TAG));
                        msg.append(error);
                        if("ok".equalsIgnoreCase(error)){
                            detailLog.setSuccessPieces((long) dataset.size());
                        }else{
                            detailLog.setErrorPieces((long) dataset.size());
                        }
                    }
                    msg.append(";");
                }
                detailLog.setLogType(info);
                detailLog.setLogInfo(msg.toString());
                detailLog.setRunEndTime(new Date());
                detailLog.setLogDetailId(UuidOpt.getUuidAsString32());
                taskDetailLogs.add(detailLog);
            }
        } else {
            TaskDetailLog detailLog=new TaskDetailLog();
            detailLog.setRunBeginTime(beginTime);
            detailLog.setTaskId(taskLog.getTaskId());
            detailLog.setLogId(taskLog.getLogId());
            detailLog.setLogType("error");
            detailLog.setLogInfo(info);
            detailLog.setRunEndTime(new Date());
            detailLog.setLogDetailId(UuidOpt.getUuidAsString32());
            taskDetailLogs.add(detailLog);
        }
        DatabaseOptUtils.batchSaveNewObjects(taskDetailLogDao,taskDetailLogs);
    }

    public void runTask(String logId) {
        beginTime = new Date();
        taskLog = taskLogDao.getObjectById(logId);
        DataPacket dataPacket = dataPacketDao.getObjectWithReferences(taskLog.getTaskId());
        dataPacket.setLastRunTime(new Date());
        runStep(dataPacket);
        taskLog.setRunEndTime(new Date());
        taskLogDao.updateObject(taskLog);
        dataPacket.setNextRunTime(new Date());
        if ("2".equals(dataPacket.getTaskType())
            && dataPacket.getIsValid()
            && !StringBaseOpt.isNvl(dataPacket.getTaskCron())) {
            CronSequenceGenerator cronSequenceGenerator = new CronSequenceGenerator(dataPacket.getTaskCron());
            dataPacket.setNextRunTime(cronSequenceGenerator.next(dataPacket.getLastRunTime()));
        }

        dataPacketDao.updateObject(dataPacket);
    }
}
