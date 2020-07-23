package com.centit.dde.aync.service;


import com.centit.product.dataopt.core.BizModel;

import java.util.concurrent.CompletableFuture;

/**
 * @author zhf
 */
public interface ExchangeService {
    CompletableFuture<BizModel> runTask(String packetId);
}
