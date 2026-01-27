package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.framework.common.ResponseData;
import com.centit.product.metadata.po.DataCheckRule;
import com.centit.product.metadata.service.DataCheckRuleService;
import com.centit.product.metadata.utils.DataCheckResult;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.common.ObjectException;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckRuleOperation implements BizOperation {

    private DataCheckRuleService dataCheckRuleService;

    public CheckRuleOperation(DataCheckRuleService dataCheckRuleService) {
        this.dataCheckRuleService = dataCheckRuleService;
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        String dataSetId = bizOptJson.getString("source");
        DataSet dataSet = bizModel.getDataSet(dataSetId);
        if (dataSet == null) {
            return ResponseData.makeErrorMessage(
                ObjectException.DATA_NOT_FOUND_EXCEPTION,
                dataOptContext.getI18nMessage("dde.604.data_source_not_found"));
        }

        //校验信息勾选项
        JSONArray checkRuleMsg = bizOptJson.getJSONArray("checkRuleMsg");
        if (checkRuleMsg == null || checkRuleMsg.isEmpty()) {
            return ResponseData.makeErrorMessage(ResponseData.ERROR_FIELD_INPUT_NOT_VALID,
                dataOptContext.getI18nMessage("error.701.field_is_blank", "checkRuleMsg"));
        }
        //是否返回校验信息，默认不返回
        boolean isReturnCheckMsg = checkRuleMsg.contains("checkRuleResultMsgField");
        //是否返回校验结果
        boolean isReturnCheckResult = checkRuleMsg.contains("checkRuleResultField");
        //校验信息挂载字段
        String checkRuleResultMsgField = bizOptJson.getString("checkRuleResultMsgField");
        //校验结果挂载字段
        String checkRuleResultField = bizOptJson.getString("checkRuleResultField");
        //获取所有的校验规则id
        List<String> ruleIds = new ArrayList<>();
        JSONArray rulesJson = bizOptJson.getJSONArray("config");
        for (Object ruleInfo : rulesJson) {
            JSONObject dataCheckRuleInfo = (JSONObject) ruleInfo;
            String ruleId = dataCheckRuleInfo.getJSONObject("checkType").getString("key");
            if (StringUtils.isNotBlank(ruleId)) {
                ruleIds.add(ruleId);
            }
        }
        //这样只需要查询一次
        Map<String, Object> queryParam = new HashMap<>();
        queryParam.put("ruleId_in", ruleIds);
        List<DataCheckRule> dataCheckRuleList = dataCheckRuleService.listObjectsByProperties(queryParam);
        Map<String, DataCheckRule> dataCheckRuleMap = new HashMap<>();
        for (DataCheckRule dataCheckRule : dataCheckRuleList) {
            dataCheckRuleMap.put(dataCheckRule.getRuleId(), dataCheckRule);
        }

        DataCheckResult result = DataCheckResult.create();
        for (Map<String, Object> dataInfo : dataSet.getDataAsList()) {
            if (dataInfo == null) {
                continue;
            }
            for (Object ruleInfo : rulesJson) {
                JSONObject dataCheckRuleInfo = (JSONObject) ruleInfo;
                //组装校验参数
                Map<String, String> paramMap = new HashMap<>(4);
                JSONArray checkParams = dataCheckRuleInfo.getJSONArray("checkParams");
                if (checkParams != null) {
                    for (Object o : checkParams) {
                        JSONObject temp = (JSONObject) o;
                        String paramName = temp.getString("label");
                        Object paramValue = temp.get("value");
                        if (StringUtils.isNotBlank(paramName) && paramValue != null) {
                            String valueTrans = StringBaseOpt.castObjectToString(paramValue);
                            paramMap.put(paramName, valueTrans);
                        }
                    }
                }

                String ruleId = dataCheckRuleInfo.getJSONObject("checkType").getString("key");
                DataCheckRule dataCheckRule = dataCheckRuleMap.get(ruleId);

                String checkField = dataCheckRuleInfo.getString("checkField");
                paramMap.put(DataCheckRule.CHECK_VALUE_TAG, checkField);
                DataCheckResult dataCheckResult;
                if (dataCheckRule.getRuleFormula().toLowerCase().contains("isnotempty(")) {
                    dataCheckResult = result.checkData(dataInfo, dataCheckRule, paramMap, isReturnCheckMsg, false);
                } else {
                    dataCheckResult = result.checkData(dataInfo, dataCheckRule, paramMap, isReturnCheckMsg, true);
                }
                if (isReturnCheckResult) {
                    dataInfo.put(checkRuleResultField, dataCheckResult.getResult());
                }
                if (isReturnCheckMsg && StringUtils.isNotBlank(dataCheckResult.getErrorMessage())) {
                    dataInfo.put(checkRuleResultMsgField, "检验"+ checkField + "出错：" +dataCheckResult.getErrorMessage());
                }
            }
            //清除上个对象的校验结果信息
            result.reset();
        }
        return BuiltInOperation.createResponseSuccessData(0);
    }
}
