package com.centit.dde.services;

import com.centit.dde.po.DataPacketInterface;

import java.util.Map;

/**
 * @author zhf
 */
public interface BizModelService {
    void setBizModelBuf(Object bizModel, DataPacketInterface dataPacket, Map<String, Object> paramsMap);

    Object fetchBizModel(DataPacketInterface dataPacket, Map<String, Object> paramsMap);


}
