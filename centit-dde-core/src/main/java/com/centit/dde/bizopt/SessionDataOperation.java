package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.framework.common.ResponseData;
import com.centit.framework.model.security.CentitUserDetails;
import com.centit.support.algorithm.ReflectionOpt;

import java.util.HashMap;
import java.util.Map;

public class SessionDataOperation implements BizOperation {
    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        String id = bizOptJson.getString("id");
        JSONArray config = bizOptJson.getJSONArray("config");
        CentitUserDetails currentUserDetails = bizModel.fetchCurrentUserDetail();
        if(currentUserDetails!=null) {
            if (config == null || config.isEmpty()) {
                bizModel.putDataSet(id, new DataSet(currentUserDetails));
            } else {
                Map<String, Object> result = new HashMap<>(8);
                for (Object o : config) {
                    JSONObject jsonObject = (JSONObject) o;
                    String sessionKey = jsonObject.getString("sessionKey");
                    Object sessionData = ReflectionOpt.attainExpressionValue(currentUserDetails, sessionKey);
                    if (sessionData != null) {
                        result.put(sessionKey, sessionData);
                    }
                }
                bizModel.putDataSet(id, new DataSet(result));
            }
            return BuiltInOperation.createResponseSuccessData(bizModel.getDataSet(id).getSize());
        }
        bizModel.putDataSet(id, new DataSet(new HashMap<>()));
        return BuiltInOperation.createResponseSuccessData(0);
    }
}
