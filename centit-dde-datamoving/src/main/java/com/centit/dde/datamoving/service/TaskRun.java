package com.centit.dde.datamoving.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.dao.TaskExchangeDao;
import com.centit.dde.dao.TaskLogDao;
import com.centit.dde.datamoving.dataopt.DatabaseBizOperation;
import com.centit.dde.po.TaskExchange;
import com.centit.dde.po.TaskLog;
import com.centit.framework.ip.service.IntegrationEnvironment;
import com.centit.product.dataopt.core.BizModel;
import com.centit.product.datapacket.dao.DataPacketDao;
import com.centit.product.datapacket.po.DataPacket;
import com.centit.product.datapacket.service.DBPacketBizSupplier;
import netscape.javascript.JSObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class TaskRun {
    @Autowired
    private TaskLogDao taskLogDao;
    @Autowired
    private TaskExchangeDao taskExchangeDao;
@Autowired
private DataPacketDao dataPacketDao;
@Autowired
private IntegrationEnvironment integrationEnvironment;
 /*   @PostConstruct
    public void init() {
        //TODO 设置一个5分钟执行一次 的定时任务调用 refreshTask
       // refreshTask();
    }*/
    public BizModel runTaks(String logid,JSONObject jsonObject){
        TaskLog taskLog = taskLogDao.getObjectById(logid);
        TaskExchange taskExchange = taskExchangeDao.getObjectById(taskLog.getTaskId());
        DataPacket dataPacket = dataPacketDao.getObjectWithReferences(taskExchange.getPacketId());
        DBPacketBizSupplier dbPacketBizSupplier = new DBPacketBizSupplier(dataPacket);
        dbPacketBizSupplier.setIntegrationEnvironment(integrationEnvironment);
        BizModel bizModel=dbPacketBizSupplier.get();

        DatabaseBizOperation databaseBizOperation = new DatabaseBizOperation();
        BizModel bizModel2=databaseBizOperation.runOneStep(bizModel,jsonObject);
        return  bizModel2;
    }
}
