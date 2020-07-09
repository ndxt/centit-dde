package com.centit.dde.aync.service.impl;

import com.centit.dde.aync.service.ExchangeService;
import com.centit.dde.datamoving.service.TaskRun;
import com.centit.dde.po.DataPacket;
import com.centit.dde.po.TaskLog;
import com.centit.dde.services.DataPacketService;
import com.centit.dde.services.TaskLogManager;
import com.centit.product.dataopt.core.BizModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ExchangeServiceImpl implements ExchangeService {
    @Autowired
    private DataPacketService dataPacketService;
    @Autowired
    private TaskLogManager taskLogManager;
    @Autowired
    private TaskRun taskRun;
    @Async
    @Override
    public BizModel runTask(String packetId) {
//        System.out.println("=====" + Thread.currentThread().getName() + "=========");
        DataPacket dataPacket = dataPacketService.getDataPacket(packetId);
        TaskLog taskLog = new TaskLog();
        taskLog.setTaskId(packetId);
        taskLog.setRunBeginTime(new Date());
        taskLog.setRunType(dataPacket.getPacketName());
        taskLogManager.createTaskLog(taskLog);
        return taskRun.runTask(taskLog.getLogId());
    }
}
