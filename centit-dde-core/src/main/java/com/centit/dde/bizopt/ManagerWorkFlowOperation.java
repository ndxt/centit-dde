package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.dde.utils.ConstantValue;
import com.centit.framework.common.ResponseData;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.json.JSONTransformer;
import com.centit.workflow.po.NodeInstance;
import com.centit.workflow.service.FlowEngine;
import com.centit.workflow.service.FlowManager;
import org.apache.commons.lang3.StringUtils;

/**
 * @author zhf
 */
public class ManagerWorkFlowOperation implements BizOperation {

    private FlowManager flowManager;
    private FlowEngine flowEngine;

    public ManagerWorkFlowOperation(FlowManager flowManager, FlowEngine flowEngine) {
        this.flowEngine = flowEngine;
        this.flowManager = flowManager;
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) {
        BizModelJSONTransform transformer = new BizModelJSONTransform(bizModel);
        String id = bizOptJson.getString("id");
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
                    return ResponseData.makeErrorMessage(
                        ResponseData.ERROR_FIELD_INPUT_NOT_VALID,
                        dataOptContext.getI18nMessage("error.701.field_is_blank", "flowInstId"));
                }
                flowManager.stopInstance(flowInstId, dataOptContext.getCurrentUserDetail(), dataOptContext.getOptId());
                break;
            case ConstantValue.SUSPEND_WFINST:
                if (StringUtils.isBlank(flowInstId)) {
                    return ResponseData.makeErrorMessage(
                        ResponseData.ERROR_FIELD_INPUT_NOT_VALID,
                        dataOptContext.getI18nMessage("error.701.field_is_blank", "flowInstId"));
                }
                flowManager.suspendInstance(flowInstId, dataOptContext.getCurrentUserDetail(), dataOptContext.getOptId());
                break;
            case ConstantValue.ACTIVE_WFINST:
                if (StringUtils.isBlank(flowInstId)) {
                    return ResponseData.makeErrorMessage(
                        ResponseData.ERROR_FIELD_INPUT_NOT_VALID,
                        dataOptContext.getI18nMessage("error.701.field_is_blank", "flowInstId"));
                }
                flowManager.activizeInstance(flowInstId, dataOptContext.getCurrentUserDetail(), dataOptContext.getOptId());
                break;
            case ConstantValue.ROLLBACK_NODE:
                if (StringUtils.isBlank(nodeInstId)) {
                    return ResponseData.makeErrorMessage(
                        ResponseData.ERROR_FIELD_INPUT_NOT_VALID,
                        dataOptContext.getI18nMessage("error.701.field_is_blank", "nodeInstId"));
                }
                String  lastNodeInstId= flowEngine.rollBackNode(nodeInstId, dataOptContext.getCurrentUserDetail());
                bizModel.putDataSet(id,new DataSet(lastNodeInstId));
                break;
            case ConstantValue.RECLAIM_NODE:
                if (StringUtils.isBlank(nodeInstId)) {
                    return ResponseData.makeErrorMessage(
                        ResponseData.ERROR_FIELD_INPUT_NOT_VALID,
                        dataOptContext.getI18nMessage("error.701.field_is_blank", "nodeInstId"));
                }
                NodeInstance nodeInstance=flowEngine.reclaimNode(nodeInstId, userCode);
                bizModel.putDataSet(id,new DataSet(nodeInstance));
                break;
            //重新运行指定节点
            case ConstantValue.RESET_NODE:
                if (StringUtils.isBlank(nodeInstId)) {
                    return ResponseData.makeErrorMessage(
                        ResponseData.ERROR_FIELD_INPUT_NOT_VALID,
                        dataOptContext.getI18nMessage("error.701.field_is_blank", "nodeInstId"));
                }
                String closeNodeType = StringBaseOpt.castObjectToString(
                    JSONTransformer.transformer(
                        bizOptJson.getString("closeNodeType"), transformer), "A");
                NodeInstance nodeResetInstance=flowManager.resetFlowToThisNode(nodeInstId, closeNodeType, dataOptContext.getCurrentUserDetail());
                bizModel.putDataSet(id,new DataSet(nodeResetInstance));
                break;
            case ConstantValue.SUSPEND_NODE:
                if (StringUtils.isBlank(nodeInstId)) {
                    return ResponseData.makeErrorMessage(
                        ResponseData.ERROR_FIELD_INPUT_NOT_VALID,
                        dataOptContext.getI18nMessage("error.701.field_is_blank", "nodeInstId"));
                }
                flowManager.updateNodeState(nodeInstId, "P",dataOptContext.getCurrentUserDetail());
                break;
            case ConstantValue.ACTIVE_NODE:
                if (StringUtils.isBlank(nodeInstId)) {
                    return ResponseData.makeErrorMessage(
                        ResponseData.ERROR_FIELD_INPUT_NOT_VALID,
                        dataOptContext.getI18nMessage("error.701.field_is_blank", "nodeInstId"));
                }
                flowManager.updateNodeState(nodeInstId,"N", dataOptContext.getCurrentUserDetail());
                break;
            default:
                break;
        }
        return BuiltInOperation.createResponseSuccessData(1);
    }
}
