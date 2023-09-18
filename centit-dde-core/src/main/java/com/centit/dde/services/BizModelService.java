package com.centit.dde.services;

import com.centit.dde.adapter.po.DataPacketInterface;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataOptResult;

/**
 * 业务模块运行接口
 * @author zhf
 */
public interface BizModelService {
    /**
     * 运行API业务模块
     * @param dataPacket api定义信息
     * @param optContext 运行上下文
     * @return 运行结果
     */
    DataOptResult runBizModel(DataPacketInterface dataPacket, DataOptContext optContext);
}
