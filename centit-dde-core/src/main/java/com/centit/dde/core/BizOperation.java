package com.centit.dde.core;

import com.alibaba.fastjson.JSONObject;
import com.centit.framework.common.ResponseData;

import java.io.IOException;

/**
 * @author zhf
 */
@FunctionalInterface
public interface BizOperation{
    //int
    ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson) throws IOException;

    default void debugOpt(BizModel bizModel, JSONObject bizOptJson){
        //return;
    }
}
