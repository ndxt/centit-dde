package com.centit.dde.core;

import com.alibaba.fastjson.JSONObject;

/**
 * @author zhf
 */
@FunctionalInterface
public interface BizOperation{
    void runOpt(BizModel bizModel, JSONObject bizOptJson);
}
