package com.centit.dde.core;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.po.DataPacket;

import java.util.Map;

/**
 * 业务流
 * @author zhf
 */
public interface BizOptFlow {
    void registerOperation(String key, BizOperation opt);
    Object run(Object dataPacket,String logId,Map<String, Object> queryParams) throws Exception;
    BizModel debug(JSONObject bizOptJson);
}
