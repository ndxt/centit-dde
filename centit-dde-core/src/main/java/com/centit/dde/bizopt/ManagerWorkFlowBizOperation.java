package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.dde.utils.ConstantValue;
import com.centit.framework.common.ResponseData;
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
        BizModelJSONTransform transformer = new BizModelJSONTransform(bizModel);
        String flowInstId = StringBaseOpt.objectToString(
            JSONTransformer.transformer(
                bizOptJson.getString("flowInstId"), transformer));
        String nodeInstId = StringBaseOpt.objectToString(
            JSONTransformer.transformer(
                bizOptJson.getString("nodeInstId"), transformer));

        String userCode="";
        if (bizModel.fetchCurrentUserDetail() !=null) {
            userCode = bizModel.fetchCurrentUserDetail().getUserCode();
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
