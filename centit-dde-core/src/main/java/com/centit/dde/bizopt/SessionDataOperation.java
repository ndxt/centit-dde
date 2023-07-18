package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.adapter.utils.ConstantValue;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.framework.common.ResponseData;
import com.centit.framework.security.model.CentitUserDetails;
import com.centit.support.algorithm.ReflectionOpt;

import java.util.HashMap;
import java.util.Map;

public class SessionDataOperation implements BizOperation {
    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        String id = bizOptJson.getString("id");
        JSONArray config = bizOptJson.getJSONArray("config");
        CentitUserDetails currentUserDetails = (CentitUserDetails)bizModel.getDataSet(ConstantValue.SESSION_DATA_TAG);
        // 这个本来就不应该有 userPin
        if(currentUserDetails != null && currentUserDetails.getUserInfo() != null) {
            currentUserDetails.getUserInfo().remove("userPin");
        }

        if (config == null || config.size() == 0){
            bizModel.putDataSet(id, new DataSet(currentUserDetails));
        } else {
            Map<String, Object> result = new HashMap<>(8);
            for (Object o : config) {
                JSONObject jsonObject = (JSONObject) o;
                String sessionKey = jsonObject.getString("sessionKey");
                Object sessionData = ReflectionOpt.attainExpressionValue(currentUserDetails, sessionKey);
                if(sessionData!=null) {
                    result.put(sessionKey, sessionData);
                }
            }
            bizModel.putDataSet(id, new DataSet(result));
        }
        return BuiltInOperation.createResponseSuccessData(bizModel.getDataSet(id).getSize());
    }
}
