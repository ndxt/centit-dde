package com.centit.dde.core;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.Map;

/**
 * 业务流
 * @author zhf
 */
public interface BizOptFlow {
    void registerOperation(String key, BizOperation opt);
    Object run(JSONObject bizOptJson,String logId,Map<String, Object> queryParams) throws IOException;
    BizModel debug(JSONObject bizOptJson);
    void initStep(int step);
}
