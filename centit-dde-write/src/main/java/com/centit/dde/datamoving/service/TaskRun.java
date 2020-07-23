package com.centit.dde.datamoving.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.dao.DataPacketDao;
import com.centit.dde.dao.TaskDetailLogDao;
import com.centit.dde.dao.TaskLogDao;
import com.centit.dde.datamoving.dataopt.DatabaseBizOperation;
import com.centit.dde.po.DataPacket;
import com.centit.dde.po.TaskDetailLog;
import com.centit.dde.po.TaskLog;
import com.centit.dde.services.DBPacketBizSupplier;
import com.centit.fileserver.common.FileStore;
import com.centit.framework.ip.service.IntegrationEnvironment;
import com.centit.product.dataopt.core.BizModel;
import com.centit.product.dataopt.core.DataSet;
import com.centit.product.metadata.service.MetaDataService;
import com.centit.support.common.ObjectException;
import com.centit.support.json.JSONOpt;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final DatabaseBizOperation databaseBizOperation;
    @Autowired(required = false)
    private  FileStore fileStore;
    private BizModel bizModel;
    private TaskLog taskLog;
    private Date beginTime;
    private TaskDetailLog detailLog;
    @Autowired
    public TaskRun( TaskLogDao taskLogDao, TaskDetailLogDao taskDetailLogDao, DataPacketDao dataPacketDao, MetaDataService metaDataService, IntegrationEnvironment integrationEnvironment, DatabaseBizOperation databaseBizOperation) {
        this.taskLogDao = taskLogDao;
        this.taskDetailLogDao = taskDetailLogDao;
        this.dataPacketDao = dataPacketDao;
        this.metaDataService = metaDataService;
        this.integrationEnvironment = integrationEnvironment;
        this.databaseBizOperation = databaseBizOperation;
    }


    private void runStep(JSONObject bizOptJson) {
        if (bizOptJson.isEmpty()) {
            return;
        }
        JSONArray jsonArray = bizOptJson.getJSONArray("steps");
        try {
            for (Object jj : jsonArray) {
                databaseBizOperation.runOneStep(bizModel, JSONOpt.objectToJSONObject(jj));
                saveDetail(JSONOpt.objectToJSONObject(jj), "");
            }
        } catch (ObjectException e) {
            saveDetail(JSONOpt.objectToJSONObject(e.getObjectData()), e.getMessage());
        } catch (Exception e) {
            saveDetail(JSONOpt.objectToJSONObject(jsonArray.get(0)), e.getMessage());
        }
    }

    private void setBizModel(String packetId) {
        DataPacket dataPacket = dataPacketDao.getObjectWithReferences(packetId);
        DBPacketBizSupplier dbPacketBizSupplier = new DBPacketBizSupplier(dataPacket);
        dbPacketBizSupplier.setIntegrationEnvironment(integrationEnvironment);
        dbPacketBizSupplier.setFileStore(fileStore);
        /*添加参数默认值传输*/
        dbPacketBizSupplier.setQueryParams(dataPacket.getPacketParamsValue());
        bizModel = dbPacketBizSupplier.get();
        databaseBizOperation.setIntegrationEnvironment(integrationEnvironment);
        databaseBizOperation.setMetaDataService(metaDataService);
    }


    private void saveDetail(JSONObject runJSON, String error) {
        detailLog.setRunBeginTime(beginTime);
        detailLog.setTaskId(taskLog.getTaskId());
        detailLog.setLogId(taskLog.getLogId());
        detailLog.setLogType(runJSON.getString("operation") + ":" + runJSON.getString("source"));
        DataSet dataSet = bizModel.fetchDataSetByName(runJSON.getString("source"));
        detailLog.setLogInfo(error);
        if ("ok".equals(detailLog.getLogInfo())) {
            detailLog.setSuccessPieces((long) dataSet.getData().size());
        } else {
            detailLog.setErrorPieces((long) dataSet.getData().size());
        }
        detailLog.setRunEndTime(new Date());
        taskDetailLogDao.saveNewObject(detailLog);
    }

    public BizModel runTask(String logId) {
        beginTime = new Date();
        taskLog = taskLogDao.getObjectById(logId);
        DataPacket dataPacket = dataPacketDao.getObjectById(taskLog.getTaskId());
        setBizModel(dataPacket.getPacketId());
        runStep(dataPacket.getDataOptDescJson());
        taskLog.setRunEndTime(new Date());
        taskLogDao.updateObject(taskLog);
        return bizModel;
    }
}
