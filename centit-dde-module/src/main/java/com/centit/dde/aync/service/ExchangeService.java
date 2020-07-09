package com.centit.dde.aync.service;


import com.centit.product.dataopt.core.BizModel;

public interface ExchangeService {
    BizModel runTask(String packetId);
}
