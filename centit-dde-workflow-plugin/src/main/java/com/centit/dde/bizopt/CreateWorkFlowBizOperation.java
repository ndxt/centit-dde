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

    public CreateWorkFlowBizOperation() {}

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
        Object unitCode =JSONTransformer.transformer(bizOptJson.getString("unitCode"), new BizModelJSONTransform(bizModel));
        createFlowOptions.setUnitCode((String)unitCode);
        Object userCode =JSONTransformer.transformer(bizOptJson.getString("userCode"), new BizModelJSONTransform(bizModel));
        createFlowOptions.setUserCode((String)userCode);
        Object flowOptName =JSONTransformer.transformer(bizOptJson.getString("flowOptName"), new BizModelJSONTransform(bizModel));
        createFlowOptions.setFlowOptName((String)flowOptName);
        Object flowOptTag =JSONTransformer.transformer(bizOptJson.getString("flowOptTag"), new BizModelJSONTransform(bizModel));
        createFlowOptions.setFlowOptTag((String)flowOptTag);
        //根据表达式获取流程变量信息(非必填参数)
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
                    if (data.getBoolean("isGlobal") !=null && data.getBoolean("isGlobal")==true){//全局流程变量
                        globalVariables.put(data.getString("variableName"),transformer.get(data.getString("variableName")));
                    }else {
                        variables.put(data.getString("variableName"),transformer.get(data.getString("variableName")));
                    }
                }
            }
        }
        //根据表达式获取办件角色信息(非必填参数)
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
        createFlowOptions.setFlowCode(bizOptJson.getString("flowCode"));
        createFlowOptions.setVariables(variables);
        createFlowOptions.setGlobalVariables(globalVariables);
        createFlowOptions.setFlowRoleUsers(flowRoleUsers);
        //字段信息
        JSONArray fieldInfos = bizOptJson.getJSONArray("config");
        for (Object fieldInfo : fieldInfos) {
            JSONObject fieldData= (JSONObject)fieldInfo;
            String columnName = fieldData.getString("columnName");
            String expression = fieldData.getString("expression");
            Object value =JSONTransformer.transformer(expression, new BizModelJSONTransform(bizModel));
            if(StringUtils.isBlank(expression) || value==null){
                continue;
            }
            // 这个为什么不用反射 JOSN.toJavaObject，不过这样效率可能会更高一点，但是添加属性时要记得同步
            // JOSN.toJavaObject
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
                    Map<String, List<String>> flowOrganizes =
                        JSON.parseObject(JSON.toJSONString(value), Map.class);
                    createFlowOptions.setFlowOrganizes(flowOrganizes);
                    break;
                case "nodeUnits":
                    Map<String, String> nodeUnits = CollectionsOpt.objectMapToStringMap(
                        CollectionsOpt.objectToMap(value));
                    createFlowOptions.setNodeUnits(nodeUnits);
                    break;
                case "nodeOptUsers":
                    Map<String, Set<String>>  nodeOptUsers = JSON.parseObject(JSON.toJSONString(value), Map.class);
                    createFlowOptions.setNodeOptUsers(nodeOptUsers);
                    break;
                default:
                    break;
            }
        }
        FlowInstance   instance = flowEngine.createInstance(createFlowOptions);
        if (instance!=null){
            bizModel.putDataSet(id,new SimpleDataSet(instance));
        }
        return BuiltInOperation.getResponseSuccessData(bizModel.getDataSet(id).getSize());
    }
}
