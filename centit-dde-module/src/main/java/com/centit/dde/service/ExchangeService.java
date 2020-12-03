package com.centit.dde.service;


import com.centit.dde.core.BizModel;

import java.util.Map;

/**
 * @author zhf
 */
public interface ExchangeService {
    Object runTask(String packetId, Map<String, Object> queryParams);
}
