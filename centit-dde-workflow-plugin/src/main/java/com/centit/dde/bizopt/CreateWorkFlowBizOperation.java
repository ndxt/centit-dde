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
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.json.JSONTransformer;
import com.centit.workflow.commons.CreateFlowOptions;
import com.centit.workflow.po.FlowInstance;
import com.centit.workflow.service.FlowEngine;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 创建工作流节点
 */
public class CreateWorkFlowBizOperation implements BizOperation {

    private FlowEngine flowEngine;

    public CreateWorkFlowBizOperation(FlowEngine flowEngine) {
        this.flowEngine = flowEngine;
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        String flowCode = bizOptJson.getString("flowCode");
        String unitCode = bizOptJson.getString("unitCode");
        String userCode = bizOptJson.getString("userCode");
        String flowOptName = bizOptJson.getString("flowOptName");
        String flowOptTag = bizOptJson.getString("flowOptTag");
        if (StringUtils.isBlank(flowCode)) {
            return ResponseData.makeErrorMessage(500, "flowCode不能为空！");
        }
        if (StringUtils.isBlank(unitCode)) {
            return ResponseData.makeErrorMessage(500, "unitCode不能为空！");
        }
        if (StringUtils.isBlank(userCode)) {
            return ResponseData.makeErrorMessage(500, "userCode不能为空！");
        }
        if (StringUtils.isBlank(flowOptName)) {
            return ResponseData.makeErrorMessage(500, "flowOptName不能为空！");
        }
        if (StringUtils.isBlank(flowOptTag)) {
            return ResponseData.makeErrorMessage(500, "flowOptTag不能为空！");
        }
        String id = bizOptJson.getString("id");
        String unitCodeTran = StringBaseOpt.castObjectToString(JSONTransformer.transformer(unitCode, new BizModelJSONTransform(bizModel)));
        String userCodeTran = StringBaseOpt.castObjectToString(JSONTransformer.transformer(userCode, new BizModelJSONTransform(bizModel)));
        String flowOptNameTran = StringBaseOpt.castObjectToString(JSONTransformer.transformer(flowOptName, new BizModelJSONTransform(bizModel)));
        String flowOptTagTran = StringBaseOpt.castObjectToString(JSONTransformer.transformer(flowOptTag, new BizModelJSONTransform(bizModel)));
        CreateFlowOptions createFlowOptions = CreateFlowOptions.create();
        createFlowOptions.setFlowCode(flowCode);
        createFlowOptions.setFlowOptTag(flowOptTagTran);
        createFlowOptions.setUnitCode(unitCodeTran);
        createFlowOptions.setUserCode(userCodeTran);
        createFlowOptions.setFlowOptName(flowOptNameTran);
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
                    Object transform = JSONTransformer.transformer(expression, new BizModelJSONTransform(bizModel));
                    boolean isGlobal = BooleanBaseOpt.castObjectToBoolean(flowVariable.get("isGlobal"), false);
                    if(transform!=null) {
                        if (isGlobal) {//全局流程变量
                            globalVariables.put(variableName, transform);
                        } else {
                            variables.put(variableName, transform);
                        }
                    }
                }
            }
            createFlowOptions.setVariables(variables);
            createFlowOptions.setGlobalVariables(globalVariables);
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
                    Object transform = JSONTransformer.transformer(expression, new BizModelJSONTransform(bizModel));
                    flowRoleUsers.put(roleCode, StringBaseOpt.objectToStringList(transform));
                }
            }
            createFlowOptions.setFlowRoleUsers(flowRoleUsers);
        }
        //字段信息
        JSONArray fieldInfos = bizOptJson.getJSONArray("config");
        if (fieldInfos != null) {
            for (Object fieldInfo : fieldInfos) {
                Map<String, Object> map = CollectionsOpt.objectToMap(fieldInfo);
                String expression = StringBaseOpt.objectToString(map.get("expression"));
                if (StringUtils.isBlank(expression)) continue;
                Object value = JSONTransformer.transformer(expression, new BizModelJSONTransform(bizModel));
                String columnName = StringBaseOpt.objectToString(map.get("columnName"));
                switch (columnName) {
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
                        Map<String, Set<String>> nodeOptUsers = CollectionsOpt.translateMapType(
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
        FlowInstance instance = flowEngine.createInstance(createFlowOptions);
        if (instance != null) {
            bizModel.putDataSet(id, new DataSet(instance));
        }
        return BuiltInOperation.createResponseSuccessData(bizModel.getDataSet(id).getSize());
    }
}
