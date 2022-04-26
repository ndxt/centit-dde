package com.centit.dde.core;

import com.centit.dde.po.DataPacketInterface;

/**
 * 业务流
 * @author zhf
 */
public interface BizOptFlow {

    void registerOperation(String key, BizOperation opt);

    Object run(DataPacketInterface dataPacket, DataOptContext dataOptContext) throws Exception;

}
