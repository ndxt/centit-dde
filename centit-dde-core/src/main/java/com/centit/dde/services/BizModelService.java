package com.centit.dde.services;

import com.centit.dde.adapter.po.DataPacketInterface;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataOptResult;

/**
 * @author zhf
 */
public interface BizModelService {

    DataOptResult runBizModel(DataPacketInterface dataPacket, DataOptContext optContext);
}
