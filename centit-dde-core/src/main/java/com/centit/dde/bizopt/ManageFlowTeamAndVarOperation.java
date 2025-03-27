package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.framework.common.ResponseData;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.json.JSONTransformer;
import com.centit.workflow.service.FlowEngine;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManageFlowTeamAndVarOperation implements BizOperation {
    private FlowEngine flowEngine;

    public ManageFlowTeamAndVarOperation(FlowEngine flowEngine) {
        this.flowEngine = flowEngine;
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) {
        String flowInstId, nodeInstId;
        String setType = bizOptJson.getString("setType"); // byFlow byNode
        BizModelJSONTransform bizModelJSONTransform = new BizModelJSONTransform(bizModel);
        if("byNode".equalsIgnoreCase(setType)){
            String nodeInstIdDesc = bizOptJson.getString("nodeInstId");
            if (StringUtils.isBlank(nodeInstIdDesc)) {
                return ResponseData.makeErrorMessage(ResponseData.ERROR_FIELD_INPUT_NOT_VALID,
                    dataOptContext.getI18nMessage("error.701.field_is_blank", "nodeInstId"));
            }
            nodeInstId = StringBaseOpt.castObjectToString(JSONTransformer.transformer(nodeInstIdDesc, bizModelJSONTransform));
            flowInstId = "";
        }else {
            String flowInstIdDesc = bizOptJson.getString("flowInstId");
            if (StringUtils.isBlank(flowInstIdDesc)) {
                return ResponseData.makeErrorMessage(ResponseData.ERROR_FIELD_INPUT_NOT_VALID,
                    dataOptContext.getI18nMessage("error.701.field_is_blank", "flowInstId"));
            }
            flowInstId = StringBaseOpt.castObjectToString(JSONTransformer.transformer(flowInstIdDesc, bizModelJSONTransform));
            nodeInstId = "";
        }

        Map<String, String> variables = new HashMap<>();
        JSONArray flowVariables = bizOptJson.getJSONArray("flowVariables");
        if (flowVariables != null) {
            for (Object flowVariableInfo : flowVariables) {
                Map<String, Object> flowVariable = CollectionsOpt.objectToMap(flowVariableInfo);
                //字段名
                String variableName = StringBaseOpt.objectToString(flowVariable.get("variableName"));
                //字段值
                Object expression = flowVariable.get("expression");
                if (expression != null) {
                    Object transform = JSONTransformer.transformer(expression, bizModelJSONTransform);
                    variables.put(variableName, StringBaseOpt.objectToString(transform));
                } else {
                    variables.put(variableName, "");
                }
            }
        }

        JSONArray roleInfo = bizOptJson.getJSONArray("role");
        Map<String, List<String>> flowRoleUsers = new HashMap<>();
        for (Object role : roleInfo) {
            Map<String, Object> roleMap = CollectionsOpt.objectToMap(role);
            String roleCode = StringBaseOpt.objectToString(roleMap.get("roleCode"));
            Object expression = roleMap.get("expression");
            if (expression != null) {
                List<String> userCodeSet = StringBaseOpt.objectToStringList(JSONTransformer.transformer(expression, bizModelJSONTransform));
                flowRoleUsers.put(roleCode, userCodeSet);
            }
        }
        flowEngine.updateFlowInstanceTeamAndVar(flowInstId, nodeInstId, variables, flowRoleUsers);
        return BuiltInOperation.createResponseSuccessData(1);
    }
}
