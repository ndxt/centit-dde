package com.centit.dde.services.impl;

import com.centit.dde.adapter.DdeDubboTaskRun;
import com.centit.dde.adapter.po.DataPacket;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.services.DataPacketService;
import com.centit.dde.utils.ConstantValue;
import com.centit.framework.model.adapter.PlatformEnvironment;
import com.centit.framework.model.basedata.OsInfo;
import com.centit.support.common.ObjectException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("ddeDubboTaskRunImpl")
public class DdeDubboTaskRunImpl implements DdeDubboTaskRun {

    @Autowired
    TaskRun taskRun;

    @Autowired
    DataPacketService dataPacketService;

    @Autowired
    private PlatformEnvironment platformEnvironment;

    @Override
    public Object runTask(String packetId, Map<String, Object> queryParams) {
        DataPacket dataPacket = dataPacketService.getDataPacket(packetId);
        if(dataPacket==null){
            throw new ObjectException(ObjectException.DATA_NOT_FOUND_EXCEPTION, "配置信息有误，找不到对应的模块:" + packetId);
        }
        DataOptContext dataOptContext = new DataOptContext();
        OsInfo osInfo = platformEnvironment.getOsInfo(dataPacket.getOsId());
        dataOptContext.setStackData(ConstantValue.APPLICATION_INFO_TAG, osInfo);
        dataOptContext.setStackData(ConstantValue.REQUEST_PARAMS_TAG, queryParams);
        return taskRun.runTask(dataPacket, dataOptContext);
    }

    @Override
    public void refreshCache(String packetId) {

    }

}
