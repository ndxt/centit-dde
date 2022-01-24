package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.SimpleDataSet;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.WebOptUtils;
import com.centit.framework.filter.RequestThreadLocal;
import com.centit.support.algorithm.ReflectionOpt;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class GetSessionDataBizOperation implements BizOperation {
    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson) throws Exception {
        String id = bizOptJson.getString("id");
        JSONArray config = bizOptJson.getJSONArray("config");
        HttpServletRequest request = RequestThreadLocal.getLocalThreadWrapperRequest();
        JSONObject result = new JSONObject();
        for (Object o : config) {
            JSONObject jsonObject = (JSONObject)o;
            String sessionKey = jsonObject.getString("sessionKey");
            if (request!=null&& StringUtils.isNotBlank(sessionKey)){
                Object sessionData = ReflectionOpt.attainExpressionValue(WebOptUtils.getCurrentUserDetails(request), sessionKey);
                result.put(sessionKey,sessionData);
            }
        }
        SimpleDataSet simpleDataSet = new SimpleDataSet();
        simpleDataSet.setData(result);
        if (config==null || config.size()==0){
            simpleDataSet.setData(WebOptUtils.getCurrentUserDetails(request));
        }
        bizModel.putDataSet(id,simpleDataSet);
        return BuiltInOperation.getResponseSuccessData(simpleDataSet.getSize());
    }
}
