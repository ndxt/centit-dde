package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.framework.common.ResponseData;
import com.centit.support.algorithm.BooleanBaseOpt;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.common.ObjectException;
import com.centit.support.json.JSONTransformer;
import com.centit.workflow.commons.SubmitOptOptions;
import com.centit.workflow.service.FlowEngine;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SubmitWorkFlowBizOperation implements BizOperation {

    private FlowEngine flowEngine;

    public SubmitWorkFlowBizOperation(FlowEngine flowEngine) {
        this.flowEngine = flowEngine;
    }

    //process WorkFlowUserTask
    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) {
        String id = bizOptJson.getString("id");
        String nodeInstId = bizOptJson.getString("nodeInstId");
        String unitCode = bizOptJson.getString("unitCode");
        String userCode = bizOptJson.getString("userCode");
        if (StringUtils.isBlank(nodeInstId)) {
            return ResponseData.makeErrorMessage(500, "nodeInstId不能为空！");
        }
        if (StringUtils.isBlank(unitCode)) {
            return ResponseData.makeErrorMessage(500, "unitCode不能为空！");
        }
        if (StringUtils.isBlank(userCode)) {
            return ResponseData.makeErrorMessage(500, "userCode不能为空！");
        }
        SubmitOptOptions submitOptOptions = SubmitOptOptions.create();
        BizModelJSONTransform bizModelJSONTransform = new BizModelJSONTransform(bizModel);
        String nodeInstIdFormula = StringBaseOpt.castObjectToString(JSONTransformer.transformer(nodeInstId, bizModelJSONTransform));
        String unitCodeFormula = StringBaseOpt.castObjectToString(JSONTransformer.transformer(unitCode, bizModelJSONTransform));
        String userCodeFormula = StringBaseOpt.castObjectToString(JSONTransformer.transformer(userCode, bizModelJSONTransform));
        submitOptOptions.setNodeInstId(nodeInstIdFormula);
        submitOptOptions.setUnitCode(unitCodeFormula);
        submitOptOptions.setUserCode(userCodeFormula);
        //根据表达式获取流程变量信息
        //根据表达式    获取流程变量信息(非必填参数)
        JSONArray flowVariables = bizOptJson.getJSONArray("flowVariables");
        if (flowVariables != null) {
            Map<String, Object> variables = new HashMap<>();
            Map<String, Object> globalVariables = new HashMap<>();
            for (Object flowVariableInfo : flowVariables) {
                Map<String, Object> flowVariable = CollectionsOpt.objectToMap(flowVariableInfo);
                //字段名
                String variableName = StringBaseOpt.objectToString(flowVariable.get("variableName"));
                //字段值
                Object expression = flowVariable.get("expression");
                if (expression != null) {
                    Object transform = JSONTransformer.transformer(expression,bizModelJSONTransform);
                    if(transform!=null) {
                        boolean isGlobal = BooleanBaseOpt.castObjectToBoolean(flowVariable.get("isGlobal"), false);
                        if (isGlobal) {//全局流程变量
                            globalVariables.put(variableName, transform);
                        } else {
                            variables.put(variableName, transform);
                        }
                    }
                }
            }
            submitOptOptions.setVariables(variables);
            submitOptOptions.setGlobalVariables(globalVariables);
        }
        //根据表达式    获取办件角色信息(非必填参数)
        JSONArray roleInfo = bizOptJson.getJSONArray("role");
        if (roleInfo != null) {
            Map<String, List<String>> flowRoleUsers = new HashMap<>();
            for (Object role : roleInfo) {
                Map<String, Object> roleMap = CollectionsOpt.objectToMap(role);
                String roleCode = StringBaseOpt.objectToString(roleMap.get("roleCode"));
                Object expression = roleMap.get("expression");
                if (expression != null) {
                    Object transform = JSONTransformer.transformer(expression,bizModelJSONTransform);
                    flowRoleUsers.put(roleCode, StringBaseOpt.objectToStringList(transform));
                }
            }
            submitOptOptions.setFlowRoleUsers(flowRoleUsers);
        }
        JSONArray fieldInfos = bizOptJson.getJSONArray("config");
        if (fieldInfos != null) {
            for (Object fieldInfo : fieldInfos) {
                if (fieldInfo instanceof Map) {
                    Map<String, Object> map = CollectionsOpt.objectToMap(fieldInfo);
                    String columnName = StringBaseOpt.objectToString(map.get("columnName"));
                    String expression = StringBaseOpt.objectToString(map.get("expression"));
                    if (StringUtils.isBlank(expression)) continue;
                    Object value = JSONTransformer.transformer(expression,bizModelJSONTransform);
                    switch (columnName) {
                        case "grantorCode":
                            submitOptOptions.setGrantorCode(StringBaseOpt.objectToString(value));
                            break;
                        case "lockOptUser":
                            submitOptOptions.setLockOptUser(BooleanBaseOpt.castObjectToBoolean(value));
                            break;
                        case "workUserCode":
                            submitOptOptions.setWorkUserCode(StringBaseOpt.objectToString(value));
                            break;
                        case "flowOrganizes":
                            Map<String, List<String>> flowOrganizes = CollectionsOpt.translateMapType(
                                CollectionsOpt.objectToMap(value),
                                StringBaseOpt::objectToString,
                                StringBaseOpt::objectToStringList);
                            submitOptOptions.setFlowOrganizes(flowOrganizes);
                            break;
                        case "nodeUnits":
                            Map<String, String> nodeUnits = CollectionsOpt.objectMapToStringMap(
                                CollectionsOpt.objectToMap(value));
                            submitOptOptions.setNodeUnits(nodeUnits);
                            break;
                        case "nodeOptUsers":
                            Map<String, Set<String>> nodeOptUsers = CollectionsOpt.translateMapType(
                                CollectionsOpt.objectToMap(value),
                                StringBaseOpt::objectToString,
                                StringBaseOpt::objectToStringSet);
                            submitOptOptions.setNodeOptUsers(nodeOptUsers);
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        Map<String, Object> objectMap = flowEngine.submitFlowOpt(submitOptOptions);
        bizModel.putDataSet(id, new DataSet(objectMap));
        return BuiltInOperation.createResponseSuccessData(bizModel.getDataSet(id).getSize());
    }
}
