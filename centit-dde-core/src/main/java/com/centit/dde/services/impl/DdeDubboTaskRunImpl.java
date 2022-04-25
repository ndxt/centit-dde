package com.centit.dde.services.impl;

import com.centit.dde.adapter.DdeDubboTaskRun;
import com.centit.dde.po.DataPacket;
import com.centit.dde.services.DataPacketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("ddeDubboTaskRunImpl")
public class DdeDubboTaskRunImpl implements DdeDubboTaskRun {

    @Autowired
    TaskRun taskRun;

    @Autowired
    DataPacketService dataPacketService;

    @Override
    public Object runTask(String packetId, Map<String, Object> queryParams) {
        DataPacket dataPacket = dataPacketService.getDataPacket(packetId);
        return taskRun.runTask(dataPacket,queryParams);
    }
}
