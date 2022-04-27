package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.framework.common.ResponseData;
import com.centit.product.adapter.po.DataCheckRule;
import com.centit.product.metadata.service.DataCheckRuleService;
import com.centit.product.metadata.utils.DataCheckResult;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.StringBaseOpt;

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
        String dataSetId = BuiltInOperation.getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        //校验信息所挂载的字段
        String checkRuleResultField =bizOptJson.getString("checkRuleResultField");
        //是否返回校验结果
        Boolean checkRuleResult =bizOptJson.getBoolean("checkRuleResult")==null?true:bizOptJson.getBoolean("checkRuleResult");
        //是否返回校验信息 默认不返回，如果默认返回的话会比较耗时
        Boolean checkRuleResultMsg = bizOptJson.getBoolean("checkRuleResultMsg")==null?false:bizOptJson.getBoolean("checkRuleResultMsg");
        DataSet dataSet = bizModel.getDataSet(dataSetId);
        JSONArray rulesJson = bizOptJson.getJSONArray("config");
        List<Map<String, Object>> dataAsList = dataSet.getDataAsList();
        DataCheckResult result = DataCheckResult.create();
        dataAsList.stream().forEach(data->{
            rulesJson.stream().forEach(ruleInfo->{
                if (ruleInfo instanceof  Map){
                    Map<String, Object> map = CollectionsOpt.objectToMap(ruleInfo);
                    String checkTypeId = StringBaseOpt.objectToString(map.get("checkTypeId"));
                    DataCheckRule dataCheckRule = dataCheckRuleService.getObjectById(checkTypeId);
                    List<Object> checkParams = CollectionsOpt.objectToList(map.get("checkParams"));
                    if (dataCheckRule!=null  && checkParams!=null){
                        Map<String, String>  paramMap = new HashMap<>();
                        String checkField = StringBaseOpt.objectToString(map.get("checkField"));
                        paramMap.put("checkValue",checkField);
                        for (int i = 0; i < checkParams.size(); i++) {
                            Map<String, Object> param = CollectionsOpt.objectToMap(checkParams.get(i));
                            for (Map.Entry<String, Object> entry : param.entrySet()) {
                                paramMap.put(entry.getKey(),StringBaseOpt.objectToString(entry.getValue()));
                            }
                        }
                        result.checkData(data,dataCheckRule,paramMap,checkRuleResultMsg);
                    }
                }
                if (data instanceof Map){
                    Map<String, Object> map = CollectionsOpt.objectToMap(data);
                    //设置校验结果信息
                    if (checkRuleResultMsg){
                        String errorMessage = result.getErrorMessage();
                        map.put(checkRuleResultField+"_msg",errorMessage);
                    }
                    //设置校验结果
                    if (checkRuleResult){
                        map.put(checkRuleResultField,result.getResult());
                    }
                }
            });
            //清除上个对象的校验结果信息
            result.reset();
        });
        return BuiltInOperation.createResponseSuccessData(0);
    }
}
