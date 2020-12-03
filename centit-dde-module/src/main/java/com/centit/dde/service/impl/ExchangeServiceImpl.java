package com.centit.dde.service.impl;

import com.centit.dde.service.ExchangeService;
import com.centit.dde.services.impl.TaskRun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

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

//    @Async
    @Override
    public Object runTask(String packetId,Map<String, Object> queryParams) {
        return taskRun.runTask(packetId,queryParams);
    }
}
