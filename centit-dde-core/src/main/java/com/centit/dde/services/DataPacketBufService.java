package com.centit.dde.services;

import com.centit.dde.po.DataPacketInterface;

import java.util.Map;

/**
 * @author zhf
 */
public interface DataPacketBufService {
    void setDataPacketBuf(Object bizModel, DataPacketInterface dataPacket, Map<String, Object> paramsMap);

    Object fetchDataPacketDataFromBuf(DataPacketInterface dataPacket, Map<String, Object>  paramsMap);


}
