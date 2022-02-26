package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.SimpleDataSet;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.WebOptUtils;
import com.centit.framework.filter.RequestThreadLocal;
import com.centit.framework.security.model.CentitUserDetails;
import com.centit.support.algorithm.ReflectionOpt;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class GetSessionDataBizOperation implements BizOperation {
    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson) throws Exception {
        String id = bizOptJson.getString("id");
        JSONArray config = bizOptJson.getJSONArray("config")==null?new JSONArray():bizOptJson.getJSONArray("config");
        HttpServletRequest request = RequestThreadLocal.getLocalThreadWrapperRequest();
        JSONObject result = new JSONObject();
        for (Object o : config) {
            JSONObject jsonObject = (JSONObject)o;
            String sessionKey = jsonObject.getString("sessionKey");
            if (request!=null&& StringUtils.isNotBlank(sessionKey)){
                Object sessionData = ReflectionOpt.attainExpressionValue(WebOptUtils.getCurrentUserDetails(request), sessionKey);
                if (sessionKey.equals("userInfo") && sessionData!=null ){
                    JSONObject userInfo = JSON.parseObject(JSON.toJSONString(sessionData));
                    userInfo.put("userPin",null);
                    result.put(sessionKey,userInfo);
                    continue;
                }
                if (sessionKey.equals("userInfo.userPin")){
                    result.put(sessionKey,null);
                    continue;
                }
                result.put(sessionKey,sessionData);
            }
        }
        if (config==null || config.size()==0){
            CentitUserDetails currentUserDetails = WebOptUtils.getCurrentUserDetails(request);
            result = JSON.parseObject(JSON.toJSONString(currentUserDetails));
            if (result.getJSONObject("userInfo")!=null){
                result.getJSONObject("userInfo").put("userPin",null);
            }
        }
        bizModel.putDataSet(id,new SimpleDataSet(result));
        return BuiltInOperation.getResponseSuccessData(bizModel.getDataSet(id).getSize());
    }
}
