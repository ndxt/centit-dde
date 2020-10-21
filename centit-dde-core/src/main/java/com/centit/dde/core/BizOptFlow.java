package com.centit.dde.core;

import com.alibaba.fastjson.JSONObject;

/**
 * 业务流
 * @author zhf
 */
public interface BizOptFlow {
    void registerOperation(String key, BizOperation opt);
    BizModel run(BizSupplier supplier, JSONObject bizOptJson,String logId);
    BizModel debug(BizSupplier supplier, JSONObject bizOptJson);
}
