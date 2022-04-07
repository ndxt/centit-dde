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
        submitOptOptions.setNodeInstId((String)nodeInstId);
        Object unitCode =JSONTransformer.transformer(bizOptJson.getString("unitCode"), new BizModelJSONTransform(bizModel));
        submitOptOptions.setUnitCode((String)unitCode);
        Object userCode =JSONTransformer.transformer(bizOptJson.getString("userCode"), new BizModelJSONTransform(bizModel));
        submitOptOptions.setUserCode((String)userCode);

        //根据表达式获取流程变量信息
        Map<String, String> variablesInfo = BuiltInOperation.jsonArrayToMap(bizOptJson.getJSONArray("flowVariables"), "variableName", "expression");
        Map<String, Object> variables = new HashMap<>();
        //流程全局变量
        Map<String, Object> globalVariables = new HashMap<>();
        if (variablesInfo!=null && variablesInfo.size()>0){
            JSONObject transformer = (JSONObject) JSONTransformer.transformer(variablesInfo, new BizModelJSONTransform(bizModel));
            JSONArray flowVariables = bizOptJson.getJSONArray("flowVariables");
            for (Object flowVariable : flowVariables) {
                JSONObject data =  (JSONObject)flowVariable;
                if (transformer !=null && transformer.get(data.getString("variableName")) !=null){
                    if (data.getBoolean("isGlobal")!=null && data.getBoolean("isGlobal")==true){//全局流程变量
                        globalVariables.put(data.getString("variableName"),transformer.get(data.getString("variableName")));
                    }else {
                        variables.put(data.getString("variableName"),transformer.get(data.getString("variableName")));
                    }
                }
            }
        }
        //根据表达式获取办件角色信息
        Map<String, List<String>> flowRoleUsers = new HashMap<>();
        Map<String, String> flowRoleUsersInfo = BuiltInOperation.jsonArrayToMap(bizOptJson.getJSONArray("role"), "roleCode", "expression");
        if (flowRoleUsersInfo!=null && flowRoleUsersInfo.size()>0){
            JSONObject transformer = (JSONObject) JSONTransformer.transformer(flowRoleUsersInfo, new BizModelJSONTransform(bizModel));
            JSONArray roleInfo = bizOptJson.getJSONArray("role");
            for (Object role : roleInfo) {
                JSONObject roleData =  (JSONObject)role;
                Object roleCode = transformer.get(roleData.getString("roleCode"));
                if (roleCode==null){
                    continue;
                }
                if (roleCode instanceof List){
                    flowRoleUsers.put(roleData.getString("roleCode"),JSON.parseArray(JSON.toJSONString(roleCode),String.class));
                }else if (roleCode instanceof String){
                    List<String> list = new ArrayList<>();
                    list.add((String)roleCode);
                    flowRoleUsers.put(roleData.getString("roleCode"),list);
                }
            }
        }
        submitOptOptions.setVariables(variables);
        submitOptOptions.setGlobalVariables(globalVariables);
        submitOptOptions.setFlowRoleUsers(flowRoleUsers);

        JSONArray fieldInfos = bizOptJson.getJSONArray("config");
        for (Object fieldInfo : fieldInfos) {
            JSONObject fieldData= (JSONObject)fieldInfo;
            String columnName = fieldData.getString("columnName");
            String expression = fieldData.getString("expression");
            Object value =JSONTransformer.transformer(expression, new BizModelJSONTransform(bizModel));
            if(StringUtils.isBlank(expression) || value==null){
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
                    Map<String, List<String>> flowOrganizes = JSON.parseObject(StringBaseOpt.objectToString(value), Map.class);
                    submitOptOptions.setFlowOrganizes(flowOrganizes);
                    break;
                case "nodeUnits":
                    Map<String, String> nodeUnits = JSON.parseObject(StringBaseOpt.objectToString(value), Map.class);
                    submitOptOptions.setNodeUnits(nodeUnits);
                    break;
                case "nodeOptUsers":
                    Map<String, Set<String>> nodeOptUsers = JSON.parseObject(StringBaseOpt.objectToString(value), Map.class);
                    submitOptOptions.setNodeOptUsers(nodeOptUsers);
                    break;
                default:
                    break;
            }
        }
        Map<String, Object>   objectMap = flowEngine.submitFlowOpt(submitOptOptions);
        bizModel.putDataSet(id,new SimpleDataSet(objectMap));
        return BuiltInOperation.getResponseSuccessData(bizModel.getDataSet(id).getSize());
    }
}
