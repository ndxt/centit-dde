package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.adapter.utils.ConstantValue;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.framework.common.ResponseData;
import com.centit.framework.model.security.CentitUserDetails;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.json.JSONTransformer;
import com.centit.workflow.service.FlowEngine;
import com.centit.workflow.service.FlowManager;
import org.apache.commons.lang3.StringUtils;

/**
 * @author zhf
 */
public class ManagerWorkFlowBizOperation implements BizOperation {

    private FlowManager flowManager;
    private FlowEngine flowEngine;

    public ManagerWorkFlowBizOperation(FlowManager flowManager, FlowEngine flowEngine) {
        this.flowEngine = flowEngine;
        this.flowManager = flowManager;
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) {
        String flowInstId = StringBaseOpt.objectToString(
            JSONTransformer.transformer(
                bizOptJson.getString("flowInstId"),
                new BizModelJSONTransform(bizModel)));
        String nodeInstId = StringBaseOpt.objectToString(
            JSONTransformer.transformer(
                bizOptJson.getString("nodeInstId"),
                new BizModelJSONTransform(bizModel)));

        String userCode="";
        if (bizModel.getStackData(ConstantValue.SESSION_DATA_TAG) instanceof CentitUserDetails) {
            userCode = ((CentitUserDetails) bizModel.getStackData(ConstantValue.SESSION_DATA_TAG)).getUserCode();
        }
        Integer taskType = bizOptJson.getInteger("taskType");

        switch (taskType) {
            case ConstantValue.STOP_WFINST:
                if (StringUtils.isBlank(flowInstId)) {
                    return ResponseData.makeErrorMessage(500, "flowInstId不能为空！");
                }
                flowManager.stopInstance(flowInstId, userCode, dataOptContext.getOptId());
                break;
            case ConstantValue.SUSPEND_WFINST:
                if (StringUtils.isBlank(flowInstId)) {
                    return ResponseData.makeErrorMessage(500, "flowInstId不能为空！");
                }
                flowManager.suspendInstance(flowInstId, userCode, dataOptContext.getOptId());
                break;
            case ConstantValue.ACTIVE_WFINST:
                if (StringUtils.isBlank(flowInstId)) {
                    return ResponseData.makeErrorMessage(500, "flowInstId不能为空！");
                }
                flowManager.activizeInstance(flowInstId, userCode, dataOptContext.getOptId());
                break;
            case ConstantValue.ROLLBACK_NODE:
                if (StringUtils.isBlank(nodeInstId)) {
                    return ResponseData.makeErrorMessage(500, "nodeInstId不能为空！");
                }
                flowEngine.rollBackNode(nodeInstId, userCode);
                break;
            case ConstantValue.RECLAIM_NODE:
                if (StringUtils.isBlank(nodeInstId)) {
                    return ResponseData.makeErrorMessage(500, "nodeInstId不能为空！");
                }
                flowEngine.reclaimNode(nodeInstId, userCode);
                break;
            default:
                break;
        }
        return BuiltInOperation.createResponseSuccessData(1);
    }
}