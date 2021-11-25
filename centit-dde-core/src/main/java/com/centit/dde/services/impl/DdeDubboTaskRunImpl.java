package com.centit.dde.services.impl;

import com.centit.dde.adapter.DdeDubboTaskRun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("ddeDubboTaskRunImpl")
public class DdeDubboTaskRunImpl implements DdeDubboTaskRun {

    @Autowired
    TaskRun taskRun;

    @Override
    public Object runTask(String packetId, Map<String, Object> queryParams) {
        return taskRun.runTask(packetId,queryParams);
    }
}
