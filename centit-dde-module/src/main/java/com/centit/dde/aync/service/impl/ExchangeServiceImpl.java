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
    private final TaskRun taskRun;
    @Autowired
    public ExchangeServiceImpl(TaskRun taskRun) {
        this.taskRun = taskRun;
    }

    @Async
    @Override
    public void runTask(String packetId) {
        taskRun.runTask(packetId);
    }
}
