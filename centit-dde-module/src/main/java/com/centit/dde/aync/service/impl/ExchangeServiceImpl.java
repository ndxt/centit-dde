package com.centit.dde.aync.service.impl;

import com.centit.dde.aync.service.ExchangeService;
import com.centit.dde.po.DataPacket;
import com.centit.dde.po.TaskLog;
import com.centit.dde.services.DataPacketService;
import com.centit.dde.services.TaskLogManager;
import com.centit.dde.services.impl.TaskRun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author zhf
 */
@Service
public class ExchangeServiceImpl implements ExchangeService {
    private final DataPacketService dataPacketService;
    private final TaskLogManager taskLogManager;
    private final TaskRun taskRun;
    @Autowired
    public ExchangeServiceImpl(DataPacketService dataPacketService, TaskLogManager taskLogManager, TaskRun taskRun) {
        this.dataPacketService = dataPacketService;
        this.taskLogManager = taskLogManager;
        this.taskRun = taskRun;
    }

    @Async
    @Override
    public void runTask(String packetId) {
        DataPacket dataPacket = dataPacketService.getDataPacket(packetId);
        TaskLog taskLog = new TaskLog();
        taskLog.setTaskId(packetId);
        taskLog.setApplicationId(dataPacket.getApplicationId());
        taskLog.setRunBeginTime(new Date());
        taskLog.setRunType(dataPacket.getPacketName());
        taskLogManager.createTaskLog(taskLog);
        taskRun.runTask(taskLog.getLogId());
    }
}
