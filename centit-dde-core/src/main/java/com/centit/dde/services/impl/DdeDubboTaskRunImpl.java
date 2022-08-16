package com.centit.dde.services.impl;

import com.centit.dde.adapter.DdeDubboTaskRun;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.po.DataPacket;
import com.centit.dde.services.DataPacketService;
import com.centit.dde.utils.ConstantValue;
import com.centit.dde.vo.DataPacketCache;
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
        DataOptContext dataOptContext = new DataOptContext();
        dataOptContext.setStackData(ConstantValue.REQUEST_PARAMS_TAG, queryParams);
        return taskRun.runTask(dataPacket, dataOptContext);
    }

    @Override
    public void refreshCache(String packetId) {
        DataPacketCache.evictCache(packetId);
    }
}
