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
import netscape.javascript.JSObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.Map;

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
    private TaskExchange taskExchange;
    private DataSet dataSet;
    private Date beginTime;
    private DataPacket dataPacket;
    public BizModel runTask(String logId) {
        return runTask(logId, null);
    }

    public BizModel runStep(String packetId){
        setBizModel(packetId);
        JSONArray jsonArray=dataPacket.getDataOptDesc().getJSONArray("steps");
        for(Object jj:jsonArray){
            runPersisdence(bizModel, JSONOpt.objectToJSONObject(jj));
        }
        return bizModel;
    }
    private void setBizModel(String packetId) {
        dataPacket = dataPacketDao.getObjectWithReferences(packetId);
        DBPacketBizSupplier dbPacketBizSupplier = new DBPacketBizSupplier(dataPacket);
        dbPacketBizSupplier.setIntegrationEnvironment(integrationEnvironment);
        bizModel = dbPacketBizSupplier.get();
    }

    private void runPersisdence(BizModel bizModel, JSONObject bizOptJson) {
        DatabaseBizOperation databaseBizOperation = new DatabaseBizOperation();
        databaseBizOperation.setIntegrationEnvironment(integrationEnvironment);
        databaseBizOperation.setMetaDataService(metaDataService);
        databaseBizOperation.runOneStep(bizModel, bizOptJson);
    }

    private void saveDetail() {
        TaskDetailLog detailLog = new TaskDetailLog();
        detailLog.setRunBeginTime(beginTime);
        detailLog.setTaskId(taskLog.getTaskId());
        detailLog.setLogId(taskLog.getLogId());
        detailLog.setLogType(JSON.parseObject(taskExchange.getExchangeDescJson()).getString("operation"));
        dataSet = bizModel.fetchDataSetByName(JSON.parseObject(taskExchange.getExchangeDescJson()).getString("source"));
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
        beginTime= new Date();
        taskLog = taskLogDao.getObjectById(logId);
        taskExchange = taskExchangeDao.getObjectById(taskLog.getTaskId());
        if (runJSON != null) {
            taskExchange.setExchangeDescJson(JSON.toJSONString(runJSON));
        }
        setBizModel(taskExchange.getPacketId());
        runPersisdence(bizModel, JSON.parseObject(taskExchange.getExchangeDescJson()));
        saveDetail();
        return bizModel;
    }
}
