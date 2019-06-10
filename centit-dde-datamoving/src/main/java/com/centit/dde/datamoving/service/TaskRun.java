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

    /*   @PostConstruct
       public void init() {
           //TODO 设置一个5分钟执行一次 的定时任务调用 refreshTask
          // refreshTask();
       }*/
    public BizModel runTask(String logId, JSONObject jsonObject) {
        TaskLog taskLog = taskLogDao.getObjectById(logId);
        TaskExchange taskExchange = taskExchangeDao.getObjectById(taskLog.getTaskId());
        DataPacket dataPacket = dataPacketDao.getObjectWithReferences(taskExchange.getPacketId());
        DBPacketBizSupplier dbPacketBizSupplier = new DBPacketBizSupplier(dataPacket);
        dbPacketBizSupplier.setIntegrationEnvironment(integrationEnvironment);
        BizModel bizModel = dbPacketBizSupplier.get();
        TaskDetailLog detailLog = new TaskDetailLog();
        detailLog.setRunBeginTime(new Date());
        DatabaseBizOperation databaseBizOperation = new DatabaseBizOperation();
        databaseBizOperation.setIntegrationEnvironment(integrationEnvironment);
        databaseBizOperation.setMetaDataService(metaDataService);
        if (jsonObject==null){
            jsonObject =JSON.parseObject(taskExchange.getExchangeDescJson());
        }
        BizModel bizModel2 = databaseBizOperation.runOneStep(bizModel, jsonObject);
        detailLog.setRunEndTime(new Date());
        detailLog.setTaskId(taskLog.getTaskId());
        detailLog.setLogId(taskLog.getLogId());
        detailLog.setLogType(jsonObject.getString("operation"));
        DataSet dataSet = bizModel.fetchDataSetByName(jsonObject.getString("source"));
        String row = (String) dataSet.getFirstRow().get(WRITER_ERROR_TAG);
        long count = dataSet.getData().size();
        detailLog.setLogInfo(row);
        if ("ok".equals(row)) {
            detailLog.setSuccessPieces(count);
        } else {
            detailLog.setErrorPieces(count);
        }
        taskDetailLogDao.saveNewObject(detailLog);
        return bizModel2;
    }
}
