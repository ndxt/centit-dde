package com.centit.dde.core;

import com.centit.dde.adapter.po.DataPacketInterface;

/**
 * 业务流
 * @author zhf
 */
public interface BizOptFlow {

    void registerOperation(String key, BizOperation opt);

    DataOptResult run(DataPacketInterface dataPacket, DataOptContext dataOptContext) throws Exception;

}
