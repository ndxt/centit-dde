package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.SimpleDataSet;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.framework.common.ResponseData;
import com.centit.support.json.JSONTransformer;
import com.centit.workflow.commons.CreateFlowOptions;
import com.centit.workflow.po.FlowInstance;
import com.centit.workflow.service.FlowEngine;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Object unitCode =JSONTransformer.transformer(bizOptJson.getString("unitCode"), new BizModelJSONTransform(bizModel));
        createFlowOptions.setUnitCode((String)unitCode);
        Object userCode =JSONTransformer.transformer(bizOptJson.getString("userCode"), new BizModelJSONTransform(bizModel));
        createFlowOptions.setUserCode((String)userCode);
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
                    if (data.getBoolean("isGlobal") !=null && data.getBoolean("isGlobal")==true){//全局流程变量
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
        createFlowOptions.setFlowCode(bizOptJson.getString("flowCode"));
        createFlowOptions.setVariables(variables);
        createFlowOptions.setGlobalVariables(globalVariables);
        createFlowOptions.setFlowRoleUsers(flowRoleUsers);
        FlowInstance instance;
        try {
            instance = flowEngine.createInstance(createFlowOptions);
        } catch (Exception e) {
            bizModel.putDataSet(id,new SimpleDataSet(e.getMessage()));
            return BuiltInOperation.getResponseData(0, 500, e.getMessage());
        }
        if (instance!=null){
            bizModel.putDataSet(id,new SimpleDataSet(instance));
        }
        return BuiltInOperation.getResponseSuccessData(bizModel.getDataSet(id).getSize());
    }
}
