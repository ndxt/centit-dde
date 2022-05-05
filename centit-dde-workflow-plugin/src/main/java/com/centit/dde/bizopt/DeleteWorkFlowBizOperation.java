package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONObject;
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

public class DeleteWorkFlowBizOperation implements BizOperation {

    private FlowManager flowManager;

    public DeleteWorkFlowBizOperation(FlowManager flowManager) {
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
            return  ResponseData.makeErrorMessage(500,"flowInstId或userCode不能为空！");
        }
        boolean deleteFlowInstById = flowManager.deleteFlowInstById(flowInstId,userCode);
        bizModel.putDataSet(id,new DataSet(deleteFlowInstById));
        return BuiltInOperation.createResponseSuccessData(bizModel.getDataSet(id).getSize());
    }
}
