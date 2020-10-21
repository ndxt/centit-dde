package com.centit.dde.core;

import com.alibaba.fastjson.JSONObject;

/**
 * @author zhf
 */
@FunctionalInterface
public interface BizOperation{
    JSONObject runOpt(BizModel bizModel, JSONObject bizOptJson);

    default void debugOpt(BizModel bizModel, JSONObject bizOptJson){
        //return;
    }
}
