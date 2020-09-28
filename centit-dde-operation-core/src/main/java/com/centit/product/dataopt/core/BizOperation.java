package com.centit.product.dataopt.core;

import com.alibaba.fastjson.JSONObject;

/**
 * @author zhf
 */
public interface BizOperation{
    void doOpt(BizModel bizModel, JSONObject bizOptJson);
}
