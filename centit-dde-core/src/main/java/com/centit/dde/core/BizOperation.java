package com.centit.dde.core;

import com.alibaba.fastjson.JSONObject;
import com.centit.framework.common.ResponseData;

/**
 * @author zhf
 */
@FunctionalInterface
public interface BizOperation{
    //int
    ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception;

}
