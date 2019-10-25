package com.centit.dde.datamoving.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.dao.TaskDetailLogDao;
import com.centit.dde.dao.TaskExchangeDao;
import com.centit.dde.dao.TaskLogDao;
import com.centit.dde.datamoving.dataopt.DatabaseBizOperation;
import com.centit.dde.po.TaskDetailLog;
import com.centit.dde.po.TaskExchange;
import com.centit.dde.po.TaskLog;
import com.centit.framework.ip.service.IntegrationEnvironment;
import com.centit.product.dataopt.core.BizModel;
import com.centit.product.dataopt.core.DataSet;
import com.centit.product.datapacket.dao.DataPacketDao;
import com.centit.product.datapacket.po.DataPacket;
import com.centit.product.datapacket.service.DBPacketBizSupplier;
import com.centit.product.metadata.service.MetaDataService;
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
    private TaskExchangeDao taskExchangeDao;
    @Autowired
    private TaskDetailLogDao taskDetailLogDao;
    @Autowired
    private DataPacketDao dataPacketDao;
    @Autowired
    private MetaDataService metaDataService;

    @Autowired
    private IntegrationEnvironment integrationEnvironment;

    private BizModel bizModel;
    private TaskLog taskLog;
    private Date beginTime;
    private DatabaseBizOperation databaseBizOperation;
    private DataPacket dataPacket;

    public BizModel runTask(String logId) {
        return runTask(logId, null);
    }

    public BizModel runStep(JSONObject bizOptJson) {
        JSONArray jsonArray = bizOptJson.getJSONArray("steps");
        for (Object jj : jsonArray) {
            databaseBizOperation.runOneStep(bizModel, JSONOpt.objectToJSONObject(jj));
            saveDetail(JSONOpt.objectToJSONObject(jj));
        }
        return bizModel;
    }

    private void setBizModel(String packetId) {
        dataPacket = dataPacketDao.getObjectWithReferences(packetId);
        DBPacketBizSupplier dbPacketBizSupplier = new DBPacketBizSupplier(dataPacket);
        dbPacketBizSupplier.setIntegrationEnvironment(integrationEnvironment);
        bizModel = dbPacketBizSupplier.get();
        databaseBizOperation = new DatabaseBizOperation();
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

    public BizModel runTask(String logId, JSONObject runJSON) {
        beginTime = new Date();
        taskLog = taskLogDao.getObjectById(logId);
        TaskExchange taskExchange = taskExchangeDao.getObjectById(taskLog.getTaskId());
        if (runJSON != null) {
            taskExchange.setExchangeDescJson(JSON.toJSONString(runJSON));
        }
        setBizModel(taskExchange.getPacketId());

        if (taskExchange.getExchangeDesc().getJSONArray("steps").size()==0){
            taskExchange.setExchangeDescJson(dataPacket.getDataOptDescJson());
        }

        if (taskExchange.getExchangeDesc() != null) {
            runStep(taskExchange.getExchangeDesc());
        }
        taskLog.setRunEndTime(new Date());
        taskLogDao.updateObject(taskLog);
        return bizModel;
    }
}
