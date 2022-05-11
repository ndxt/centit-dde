package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.dde.utils.ConstantValue;
import com.centit.framework.common.ResponseData;
import com.centit.framework.security.model.CentitUserDetails;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.json.JSONTransformer;
import com.centit.workflow.service.FlowManager;
import org.apache.commons.lang3.StringUtils;

/**
 * @author zhf
 */
public class ManagerWorkFlowBizOperation implements BizOperation {

    private FlowManager flowManager;

    public ManagerWorkFlowBizOperation(FlowManager flowManager) {
        this.flowManager=flowManager;
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext){
        String id = bizOptJson.getString("id");
        String flowInstId = StringBaseOpt.objectToString(
            JSONTransformer.transformer(
                bizOptJson.getString("flowInstId"),
                new BizModelJSONTransform(bizModel)));
        String userCode = ((CentitUserDetails)bizModel.getStackData(ConstantValue.SESSION_DATA_TAG)).getUserCode();
        Integer taskType = bizOptJson.getInteger("taskType");
        if (StringUtils.isBlank(flowInstId)){
            return  ResponseData.makeErrorMessage(500,"flowInstId不能为空！");
        }
        switch (taskType){
            case ConstantValue.STOP_WFINST:
                flowManager.stopInstance(flowInstId,userCode,dataOptContext.getOptId());
                break;
            case ConstantValue.SUSPEND_WFINST:
                flowManager.suspendInstance(flowInstId,userCode,dataOptContext.getOptId());
                break;
            case ConstantValue.ACTIVE_WFINST:
                flowManager.activizeInstance(flowInstId,userCode,dataOptContext.getOptId());
                break;
            default:
                break;
        }
        return BuiltInOperation.createResponseSuccessData(bizModel.getDataSet(id).getSize());
    }
}
