package com.centit.product.dataopt.core;

import com.alibaba.fastjson.JSONObject;

/**
 * 业务流
 */
public interface BizOptFlow {
    void registOperation(String key, BizOperation opt);
    BizModel run(BizSupplier supplier, JSONObject bizOptJson);
}
