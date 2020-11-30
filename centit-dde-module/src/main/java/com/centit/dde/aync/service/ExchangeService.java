package com.centit.dde.aync.service;


import com.centit.dde.core.BizModel;

import java.util.Map;

/**
 * @author zhf
 */
public interface ExchangeService {
    BizModel runTask(String packetId, Map<String, Object> queryParams);
}
