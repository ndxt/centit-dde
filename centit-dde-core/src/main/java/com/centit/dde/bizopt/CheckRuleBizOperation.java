package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.framework.common.ResponseData;
import com.centit.product.adapter.po.DataCheckRule;
import com.centit.product.metadata.service.DataCheckRuleService;
import com.centit.product.metadata.utils.DataCheckResult;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.json.JSONTransformer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckRuleBizOperation implements BizOperation {

    private DataCheckRuleService dataCheckRuleService;

    public CheckRuleBizOperation(DataCheckRuleService dataCheckRuleService) {
        this.dataCheckRuleService = dataCheckRuleService;
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        String dataSetId = bizOptJson.getString("source");
        DataSet dataSet = bizModel.getDataSet(dataSetId);
        if (dataSet == null) return ResponseData.makeErrorMessage("校验数据不能为空！");

        List<Map<String, Object>> dataAsList = dataSet.getDataAsList();
        DataCheckResult result = DataCheckResult.create();
        JSONArray rulesJson = bizOptJson.getJSONArray("config");
        String checkRuleResultMsgField =bizOptJson.getString("checkRuleResultMsgField");
        String checkRuleResultField =bizOptJson.getString("checkRuleResultField");
        JSONArray checkRuleMsg = bizOptJson.getJSONArray("checkRuleMsg");
        if (checkRuleMsg == null || checkRuleMsg.size() == 0) return ResponseData.makeErrorMessage("校验信息不能为空！");
        //是否返回校验信息，默认不返回
        boolean isReturnCheckMsg = checkRuleMsg.contains("checkRuleResultMsgField");
        //是否返回校验结果
        boolean isReturnCheckResult = checkRuleMsg.contains("checkRuleResultField");
        dataAsList.stream().forEach(dataInfo->{
            rulesJson.stream().forEach(ruleInfo->{
                JSONObject  dataCheckRuleInfo = JSON.parseObject(StringBaseOpt.castObjectToString(ruleInfo));
                String checkField = dataCheckRuleInfo.getString("checkField");
                Map<String, String>  paramMap = new HashMap<>();
                paramMap.put("checkValue",checkField);
                JSONArray checkParams = dataCheckRuleInfo.getJSONArray("checkParams");
                for (Object checkParam : checkParams) {
                    Map<String, Object> param = CollectionsOpt.objectToMap(checkParam);
                    String params = StringBaseOpt.objectToString(param.get("params"));
                    String paramValue = StringBaseOpt.castObjectToString(
                        JSONTransformer.transformer(param.get("paramValue"), new BizModelJSONTransform(bizModel)));
                    paramMap.put(params,paramValue);
                }
                JSONObject checkType = dataCheckRuleInfo.getJSONObject("checkType");
                DataCheckRule dataCheckRule = dataCheckRuleService.getObjectById("id");
                result.checkData(dataInfo,dataCheckRule,paramMap,isReturnCheckMsg,true);
                if (dataInfo instanceof Map){
                    Map<String, Object> map = CollectionsOpt.objectToMap(dataInfo);
                    if (isReturnCheckResult){
                        map.put(checkRuleResultField, result.getResult());
                    }
                    if (isReturnCheckMsg){
                        map.put(checkRuleResultMsgField,result.getErrorMessage());
                    }
                }
            });
            //清除上个对象的校验结果信息
            result.reset();
        });
        return BuiltInOperation.createResponseSuccessData(0);
    }
}
