package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.framework.common.ResponseData;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.json.JSONTransformer;
import com.centit.workflow.service.FlowManager;
import org.apache.commons.lang3.StringUtils;

public class DeleteWorkFlowOperation implements BizOperation {

    private FlowManager flowManager;

    public DeleteWorkFlowOperation(FlowManager flowManager) {
        this.flowManager=flowManager;
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        String id = bizOptJson.getString("id");
        String flowInstId = StringBaseOpt.objectToString(
            JSONTransformer.transformer(
                bizOptJson.getString("flowInstId"),
                new BizModelJSONTransform(bizModel)));
        String userCode = StringBaseOpt.objectToString(
            JSONTransformer.transformer(
                bizOptJson.getString("userCode"),
                new BizModelJSONTransform(bizModel)));
        if (StringUtils.isBlank(flowInstId) || StringUtils.isBlank(userCode)){
            return  ResponseData.makeErrorMessage(ResponseData.ERROR_FIELD_INPUT_NOT_VALID,
                dataOptContext.getI18nMessage("error.701.field_is_blank", "flowInstId|userCode"));
        }
        boolean deleteFlowInstById = flowManager.deleteFlowInstById(flowInstId,userCode);
        bizModel.putDataSet(id,new DataSet(deleteFlowInstById));
        return BuiltInOperation.createResponseSuccessData(bizModel.getDataSet(id).getSize());
    }
}
