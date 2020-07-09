package com.centit.dde.datamoving.service;

import com.alibaba.fastjson.JSON;
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

import static com.centit.product.dataopt.dataset.SQLDataSetWriter.WRITER_ERROR_TAG;

@Service
public class TaskRun {
    @Autowired
    private TaskLogDao taskLogDao;
    @Autowired
    private TaskDetailLogDao taskDetailLogDao;
    @Autowired
    private DataPacketDao dataPacketDao;
    @Autowired
    private MetaDataService metaDataService;
    @Autowired
    private IntegrationEnvironment integrationEnvironment;
    @Autowired
    private DatabaseBizOperation databaseBizOperation;
    @Autowired(required = false)
    private FileStore fileStore;
    private BizModel bizModel;
    private TaskLog taskLog;
    private Date beginTime;
    private DataPacket dataPacket;



    public BizModel runStep(JSONObject bizOptJson) {
        if (bizOptJson.isEmpty()){
            return null;
        }
        JSONArray jsonArray = bizOptJson.getJSONArray("steps");
        try{
        for (Object jj : jsonArray) {
            databaseBizOperation.runOneStep(bizModel, JSONOpt.objectToJSONObject(jj));
            saveDetail(JSONOpt.objectToJSONObject(jj));
        }}
        catch (ObjectException e){
            saveDetail(JSONOpt.objectToJSONObject(e.getObjectData()),e.getMessage());
        }
        catch (Exception e){
            saveDetail(JSONOpt.objectToJSONObject(jsonArray.get(0)),e.getMessage());
        }
        return bizModel;
    }

    private void setBizModel(String packetId) {
        dataPacket = dataPacketDao.getObjectWithReferences(packetId);
        DBPacketBizSupplier dbPacketBizSupplier = new DBPacketBizSupplier(dataPacket);
        dbPacketBizSupplier.setIntegrationEnvironment(integrationEnvironment);
        dbPacketBizSupplier.setFileStore(fileStore);
        /*添加参数默认值传输*/
        dbPacketBizSupplier.setQueryParams(dataPacket.getPacketParamsValue());
        bizModel = dbPacketBizSupplier.get();
        databaseBizOperation.setIntegrationEnvironment(integrationEnvironment);
        databaseBizOperation.setMetaDataService(metaDataService);
    }


    private void saveDetail(JSONObject runJSON) {
        TaskDetailLog detailLog = new TaskDetailLog();
        detailLog.setRunBeginTime(beginTime);
        detailLog.setTaskId(taskLog.getTaskId());
        detailLog.setLogId(taskLog.getLogId());
        detailLog.setLogType(runJSON.getString("operation")+":"+runJSON.getString("source"));
        DataSet dataSet = bizModel.fetchDataSetByName(runJSON.getString("source"));
        detailLog.setLogInfo((String) dataSet.getFirstRow().get(WRITER_ERROR_TAG));
        if ("ok".equals(detailLog.getLogInfo())) {
            detailLog.setSuccessPieces((long) dataSet.getData().size());
        } else {
            detailLog.setErrorPieces((long) dataSet.getData().size());
        }
        detailLog.setRunEndTime(new Date());
        taskDetailLogDao.saveNewObject(detailLog);
    }
    private void saveDetail(JSONObject runJSON,String error) {
        TaskDetailLog detailLog = new TaskDetailLog();
        detailLog.setRunBeginTime(beginTime);
        detailLog.setTaskId(taskLog.getTaskId());
        detailLog.setLogId(taskLog.getLogId());
        detailLog.setLogType(runJSON.getString("operation")+":"+runJSON.getString("source"));
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
