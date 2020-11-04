package com.centit.dde.core;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * 业务流
 * @author zhf
 */
public interface BizOptFlow {
    void registerOperation(String key, BizOperation opt);
    BizModel run(JSONObject bizOptJson,String logId,Map<String, Object> queryParams);
    BizModel debug(JSONObject bizOptJson);
}
