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
import com.centit.fileserver.common.FileStore;
import com.centit.framework.ip.service.IntegrationEnvironment;
import com.centit.dde.core.BizOptFlow;
import com.centit.product.metadata.service.DatabaseRunTime;
import com.centit.product.metadata.service.MetaDataService;
import com.centit.product.metadata.service.MetaObjectService;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.algorithm.UuidOpt;
import com.centit.support.database.utils.FieldType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author zhf
 */
@Service
public class TaskRun {
    private final TaskLogDao taskLogDao;
    private final TaskDetailLogDao taskDetailLogDao;
    private final DataPacketDao dataPacketDao;
    private final MetaDataService metaDataService;
    private final IntegrationEnvironment integrationEnvironment;
    private final BizOptFlow bizOptFlow;
    private FileStore fileStore;

    @Autowired(required = false)
    public void setFileStore(FileStore fileStore) {
        this.fileStore = fileStore;
    }


    private MetaObjectService metaObjectService;

    @Autowired(required = false)
    public void setMetaObjectService(MetaObjectService metaObjectService) {
        this.metaObjectService = metaObjectService;
    }

    private DatabaseRunTime databaseRunTime;

    @Autowired(required = false)
    public void setDatabaseRunTime(DatabaseRunTime databaseRunTime) {
        this.databaseRunTime = databaseRunTime;
    }


    private TaskLog taskLog;
    private Date beginTime;
    private TaskDetailLog detailLog;

    @Autowired
    public TaskRun(TaskLogDao taskLogDao,
                   TaskDetailLogDao taskDetailLogDao,
                   DataPacketDao dataPacketDao,
                   MetaDataService metaDataService,
                   IntegrationEnvironment integrationEnvironment,
                   BizOptFlow bizOptFlow) {
        this.taskLogDao = taskLogDao;
        this.taskDetailLogDao = taskDetailLogDao;
        this.dataPacketDao = dataPacketDao;
        this.metaDataService = metaDataService;
        this.integrationEnvironment = integrationEnvironment;
        this.bizOptFlow = bizOptFlow;
        //this.taskLog = new TaskLog();
        this.detailLog = new TaskDetailLog();
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
        StringBuffer message = new StringBuffer();
        StackTraceElement[] exceptionStack = e.getStackTrace();
        message.append(e.toString());
        int i = 0;
        for (StackTraceElement ste : exceptionStack) {
            message.append("\n\tat " + ste);
            if (i++ == 4) {
                break;
            }
        }
        return message.toString();
    }

    private void saveDetail(BizModel iResult, String info) {
        detailLog.setRunBeginTime(beginTime);
        detailLog.setTaskId(taskLog.getTaskId());
        detailLog.setLogId(taskLog.getLogId());
        StringBuilder msg = new StringBuilder();
        if (iResult != null) {
            for (DataSet dataset : iResult.getBizData().values()) {
                if (dataset.getData() != null) {
                    msg.append(dataset.getDataSetName());
                    msg.append(":");
                    msg.append(dataset.size());
                    msg.append("numbers");
                    if (dataset.size() > 0) {
                        msg.append(dataset.getData().get(0).get(FieldType.mapPropName(SQLDataSetWriter.WRITER_ERROR_TAG)));
                    }
                    msg.append(";");
                }
            }
        }
        String successSign = "ok";
        if (successSign.equals(info)) {
            detailLog.setLogType(info);
            detailLog.setLogInfo(msg.toString());
        } else {
            detailLog.setLogType("error");
            detailLog.setLogInfo(msg + ";" + info);
        }
        detailLog.setRunEndTime(new Date());
        detailLog.setLogDetailId(UuidOpt.getUuidAsString32());
        taskDetailLogDao.saveNewObject(detailLog);
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
