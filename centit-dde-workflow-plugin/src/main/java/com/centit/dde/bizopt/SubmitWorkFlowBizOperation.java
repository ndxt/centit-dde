package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.SimpleDataSet;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.framework.common.ResponseData;
import com.centit.support.algorithm.BooleanBaseOpt;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.json.JSONTransformer;
import com.centit.workflow.commons.SubmitOptOptions;
import com.centit.workflow.service.FlowEngine;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class SubmitWorkFlowBizOperation implements BizOperation {

    private FlowEngine flowEngine;

    public SubmitWorkFlowBizOperation(FlowEngine flowEngine) {
        this.flowEngine = flowEngine;
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson) {
        String id = bizOptJson.getString("id");
        SubmitOptOptions submitOptOptions = SubmitOptOptions.create();
        if (StringUtils.isBlank(bizOptJson.getString("nodeInstId"))){
            return  ResponseData.makeErrorMessage(500,"nodeInstId不能为空！");
        }
        if (StringUtils.isBlank(bizOptJson.getString("unitCode"))){
            return  ResponseData.makeErrorMessage(500,"unitCode不能为空！");
        }
        if (StringUtils.isBlank(bizOptJson.getString("userCode"))){
            return  ResponseData.makeErrorMessage(500,"userCode不能为空！");
        }
        Object nodeInstId =JSONTransformer.transformer(bizOptJson.getString("nodeInstId"), new BizModelJSONTransform(bizModel));
        Object unitCode =JSONTransformer.transformer(bizOptJson.getString("unitCode"), new BizModelJSONTransform(bizModel));
        Object userCode =JSONTransformer.transformer(bizOptJson.getString("userCode"), new BizModelJSONTransform(bizModel));
        submitOptOptions.setNodeInstId(StringBaseOpt.objectToString(nodeInstId));
        submitOptOptions.setUnitCode(StringBaseOpt.objectToString(unitCode));
        submitOptOptions.setUserCode(StringBaseOpt.objectToString(userCode));

        //根据表达式获取流程变量信息
        Map<String, String> variablesInfo = BuiltInOperation.jsonArrayToMap(bizOptJson.getJSONArray("flowVariables"), "variableName", "expression");
        Map<String, Object> variables = new HashMap<>();
        //流程全局变量
        Map<String, Object> globalVariables = new HashMap<>();
        if (variablesInfo!=null && variablesInfo.size()>0){
            Object transform = JSONTransformer.transformer(variablesInfo, new BizModelJSONTransform(bizModel));
            JSONArray flowVariables = bizOptJson.getJSONArray("flowVariables")==null?new JSONArray():
                bizOptJson.getJSONArray("flowVariables");
            for (Object flowVariable : flowVariables) {
                if (transform != null && flowVariable instanceof Map && transform instanceof  Map){
                    Map<String, Object> data = CollectionsOpt.objectToMap(flowVariable);
                    Map<String, Object> transformer = CollectionsOpt.objectToMap(transform);
                    if (transformer.get(data.get("variableName")) !=null){
                        if (data.get("isGlobal")!=null && BooleanBaseOpt.castObjectToBoolean(data.get("isGlobal"))==true){//全局流程变量
                            globalVariables.put(StringBaseOpt.objectToString(data.get("variableName")),transformer.get(data.get("variableName")));
                        }else {
                            variables.put(StringBaseOpt.objectToString(data.get("variableName")),transformer.get(data.get("variableName")));
                        }
                    }
                }
            }
        }
        //根据表达式获取办件角色信息
        Map<String, List<String>> flowRoleUsers = new HashMap<>();
        Map<String, String> flowRoleUsersInfo = BuiltInOperation.jsonArrayToMap(bizOptJson.getJSONArray("role"), "roleCode", "expression");
        if (flowRoleUsersInfo!=null && flowRoleUsersInfo.size()>0){
            Object transform = JSONTransformer.transformer(flowRoleUsersInfo, new BizModelJSONTransform(bizModel));
            JSONArray roleInfo = bizOptJson.getJSONArray("role")==null?new JSONArray():
                bizOptJson.getJSONArray("role");
            for (Object role : roleInfo) {
                if (transform != null && role instanceof Map && transform instanceof  Map){
                    Map<String, Object> roleData = CollectionsOpt.objectToMap(role);
                    Map<String, Object> transformer = CollectionsOpt.objectToMap(transform);
                    Object roleCode = transformer.get(roleData.get("roleCode"));
                    if (roleCode==null){
                        continue;
                    }
                    if (roleCode instanceof List){
                        flowRoleUsers.put(StringBaseOpt.objectToString(roleData.get("roleCode")),StringBaseOpt.objectToStringList(roleCode));
                    }else if (roleCode instanceof String){
                        List<String> list = new ArrayList<>();
                        list.add(StringBaseOpt.objectToString(roleCode));
                        flowRoleUsers.put(StringBaseOpt.objectToString(roleData.get("roleCode")),list);
                    }
                }
            }
        }
        submitOptOptions.setVariables(variables);
        submitOptOptions.setGlobalVariables(globalVariables);
        submitOptOptions.setFlowRoleUsers(flowRoleUsers);

        JSONArray fieldInfos = bizOptJson.getJSONArray("config")==null?new JSONArray():
            bizOptJson.getJSONArray("config");
        for (Object fieldInfo : fieldInfos) {
            if (fieldInfo instanceof Map){
                Map<String, Object> map = CollectionsOpt.objectToMap(fieldInfo);
                String columnName = StringBaseOpt.objectToString(map.get("columnName"));
                String expression = StringBaseOpt.objectToString(map.get("expression"));
                Object value =JSONTransformer.transformer(expression, new BizModelJSONTransform(bizModel));
                if(StringUtils.isBlank(columnName) || StringUtils.isBlank(expression) || value==null){
                    continue;
                }
                switch (columnName){
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
                        Map<String, List<String>> flowOrganizes =  CollectionsOpt.translateMapType(
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
                        Map<String, Set<String>> nodeOptUsers =  CollectionsOpt.translateMapType(
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
        Map<String, Object>   objectMap = flowEngine.submitFlowOpt(submitOptOptions);
        bizModel.putDataSet(id,new SimpleDataSet(objectMap));
        return BuiltInOperation.getResponseSuccessData(bizModel.getDataSet(id).getSize());
    }
}
