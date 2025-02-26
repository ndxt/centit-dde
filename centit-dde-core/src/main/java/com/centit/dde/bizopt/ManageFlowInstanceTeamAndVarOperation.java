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
import org.apache.commons.lang3.tuple.MutableTriple;
import org.apache.commons.lang3.tuple.Triple;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManageFlowInstanceTeamAndVarOperation implements BizOperation {
    private FlowEngine flowEngine;

    public ManageFlowInstanceTeamAndVarOperation(FlowEngine flowEngine) {
        this.flowEngine = flowEngine;
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) {
        String flowInstIdDesc = bizOptJson.getString("flowInstId");
        if (StringUtils.isBlank(flowInstIdDesc)) {
            return ResponseData.makeErrorMessage(ResponseData.ERROR_FIELD_INPUT_NOT_VALID,
                dataOptContext.getI18nMessage("error.701.field_is_blank", "flowInstId"));
        }
        BizModelJSONTransform bizModelJSONTransform = new BizModelJSONTransform(bizModel);
        String flowInstId = StringBaseOpt.castObjectToString(JSONTransformer.transformer(flowInstIdDesc, bizModelJSONTransform));
        List<Triple<String, String, String>> flowVariables = CollectionsOpt.mapCollectionToList(
            bizOptJson.getJSONArray("flowVariables"),
            (a) -> new MutableTriple<>(JSONObject.from(a).getString("variableName"),
                StringBaseOpt.objectToString(JSONTransformer.transformer(JSONObject.from(a).getString("token"), bizModelJSONTransform)),
                StringBaseOpt.objectToString(JSONTransformer.transformer(JSONObject.from(a).getString("expression"), bizModelJSONTransform))));
        JSONArray roleInfo = bizOptJson.getJSONArray("role");
        Map<String, List<String>> flowRoleUsers = new HashMap<>();
        for (Object role : roleInfo) {
            Map<String, Object> roleMap = CollectionsOpt.objectToMap(role);
            String roleCode = StringBaseOpt.objectToString(roleMap.get("roleCode"));
            Object expression = roleMap.get("expression");
            if (expression != null) {
                List<String> userCodeSet = StringBaseOpt.objectToStringList(JSONTransformer.transformer(expression, bizModelJSONTransform));
                flowEngine.assignFlowWorkTeam(flowInstId, roleCode, userCodeSet);
            }
        }
        flowEngine.updateFlowInstanceTeamAndVar(flowInstId, flowVariables, flowRoleUsers);
        return BuiltInOperation.createResponseSuccessData(1);
    }
}
