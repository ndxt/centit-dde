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
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.json.JSONTransformer;
import com.centit.workflow.commons.CreateFlowOptions;
import com.centit.workflow.po.FlowInstance;
import com.centit.workflow.service.FlowEngine;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * 创建工作流节点
 */
public class CreateWorkFlowBizOperation implements BizOperation {

    private FlowEngine flowEngine;

    public CreateWorkFlowBizOperation(FlowEngine flowEngine) {
        this.flowEngine = flowEngine;
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson) throws Exception {
        String id = bizOptJson.getString("id");
        CreateFlowOptions createFlowOptions = CreateFlowOptions.create();
        if (StringUtils.isBlank(bizOptJson.getString("flowCode"))){
            return  ResponseData.makeErrorMessage(500,"flowCode不能为空！");
        }
        if (StringUtils.isBlank(bizOptJson.getString("unitCode"))){
            return  ResponseData.makeErrorMessage(500,"unitCode不能为空！");
        }
        if (StringUtils.isBlank(bizOptJson.getString("userCode"))){
            return  ResponseData.makeErrorMessage(500,"userCode不能为空！");
        }
        if (StringUtils.isBlank(bizOptJson.getString("flowOptName"))){
            return  ResponseData.makeErrorMessage(500,"flowOptName不能为空！");
        }
        if (StringUtils.isBlank(bizOptJson.getString("flowOptTag"))){
            return  ResponseData.makeErrorMessage(500,"flowOptTag不能为空！");
        }
        String flowCode =bizOptJson.getString("flowCode");
        Object unitCode =JSONTransformer.transformer(bizOptJson.getString("unitCode"), new BizModelJSONTransform(bizModel));
        Object userCode =JSONTransformer.transformer(bizOptJson.getString("userCode"), new BizModelJSONTransform(bizModel));
        Object flowOptName =JSONTransformer.transformer(bizOptJson.getString("flowOptName"), new BizModelJSONTransform(bizModel));
        Object flowOptTag =JSONTransformer.transformer(bizOptJson.getString("flowOptTag"), new BizModelJSONTransform(bizModel));
        createFlowOptions.setFlowCode(flowCode);
        createFlowOptions.setFlowOptTag(StringBaseOpt.objectToString(flowOptTag));
        createFlowOptions.setUnitCode(StringBaseOpt.objectToString(unitCode));
        createFlowOptions.setUserCode(StringBaseOpt.objectToString(userCode));
        createFlowOptions.setFlowOptName(StringBaseOpt.objectToString(flowOptName));
        //根据表达式获取流程变量信息(非必填参数)
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
        //根据表达式获取办件角色信息(非必填参数)
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
        createFlowOptions.setFlowCode(bizOptJson.getString("flowCode"));
        createFlowOptions.setVariables(variables);
        createFlowOptions.setGlobalVariables(globalVariables);
        createFlowOptions.setFlowRoleUsers(flowRoleUsers);
        //字段信息
        JSONArray fieldInfos = bizOptJson.getJSONArray("config")==null?new JSONArray():
            bizOptJson.getJSONArray("config");
        for (Object fieldInfo : fieldInfos) {
            if (fieldInfo instanceof  Map){
                Map<String, Object> map = CollectionsOpt.objectToMap(fieldInfo);
                String columnName = StringBaseOpt.objectToString(map.get("columnName"));
                String expression = StringBaseOpt.objectToString(map.get("expression"));
                Object value =JSONTransformer.transformer(expression, new BizModelJSONTransform(bizModel));
                if( StringUtils.isBlank(columnName) || StringUtils.isBlank(expression) || value==null){
                    continue;
                }
                switch (columnName){
                    case "modelId":
                        createFlowOptions.setModelId(StringBaseOpt.castObjectToString(value));
                        break;
                    case "flowVersion":
                        createFlowOptions.setFlowVersion(NumberBaseOpt.castObjectToLong(value));
                        break;
                    case "parentNodeInstId":
                        createFlowOptions.setParentNodeInstId(StringBaseOpt.castObjectToString(value));
                        break;
                    case "parentFlowInstId":
                        createFlowOptions.setParentFlowInstId(StringBaseOpt.castObjectToString(value));
                        break;
                    case "flowGroupId":
                        createFlowOptions.setFlowGroupId(StringBaseOpt.castObjectToString(value));
                        break;
                    case "timeLimitStr":
                        createFlowOptions.setTimeLimitStr(StringBaseOpt.castObjectToString(value));
                        break;
                    case "skipFirstNode":
                        createFlowOptions.setSkipFirstNode(BooleanBaseOpt.castObjectToBoolean(value));
                        break;
                    case "lockOptUser":
                        createFlowOptions.setLockOptUser(BooleanBaseOpt.castObjectToBoolean(value));
                        break;
                    case "workUserCode":
                        createFlowOptions.setWorkUserCode(StringBaseOpt.castObjectToString(value));
                        break;
                    case "flowOrganizes":
                        Map<String, List<String>> flowOrganizes = CollectionsOpt.translateMapType(
                            CollectionsOpt.objectToMap(value),
                            StringBaseOpt::objectToString,
                            StringBaseOpt::objectToStringList);
                        createFlowOptions.setFlowOrganizes(flowOrganizes);
                        break;
                    case "nodeUnits":
                        Map<String, String> nodeUnits = CollectionsOpt.objectMapToStringMap(
                            CollectionsOpt.objectToMap(value));
                        createFlowOptions.setNodeUnits(nodeUnits);
                        break;
                    case "nodeOptUsers":
                        Map<String, Set<String>>  nodeOptUsers = CollectionsOpt.translateMapType(
                            CollectionsOpt.objectToMap(value),
                            StringBaseOpt::objectToString,
                            StringBaseOpt::objectToStringSet);
                        createFlowOptions.setNodeOptUsers(nodeOptUsers);
                        break;
                    default:
                        break;
                }
            }
        }
        FlowInstance   instance = flowEngine.createInstance(createFlowOptions);
        if (instance!=null){
            bizModel.putDataSet(id,new SimpleDataSet(instance));
        }
        return BuiltInOperation.getResponseSuccessData(bizModel.getDataSet(id).getSize());
    }
}
