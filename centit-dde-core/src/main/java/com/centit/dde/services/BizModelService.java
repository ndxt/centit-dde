package com.centit.dde.services;

import com.centit.dde.core.DataOptContext;
import com.centit.dde.po.DataPacketInterface;

/**
 * @author zhf
 */
public interface BizModelService {

    Object fetchBizModel(DataPacketInterface dataPacket, DataOptContext optContext);
}
