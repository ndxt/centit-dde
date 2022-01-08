package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.SimpleDataSet;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.framework.common.ResponseData;
import com.centit.support.json.JSONTransformer;
import com.centit.workflow.service.FlowManager;
import org.apache.commons.lang3.StringUtils;

public class DeleteWorkFlowBizOperation implements BizOperation {

    private FlowManager flowManager;

    public DeleteWorkFlowBizOperation() {
    }

    public DeleteWorkFlowBizOperation(FlowManager flowManager) {
        this.flowManager=flowManager;
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson) throws Exception {
        String id = bizOptJson.getString("id");
        String flowInstId = (String)JSONTransformer.transformer(bizOptJson.getString("flowInstId"), new BizModelJSONTransform(bizModel));
        String userCode = (String)JSONTransformer.transformer(bizOptJson.getString("userCode"), new BizModelJSONTransform(bizModel));
        if (StringUtils.isBlank(flowInstId) || StringUtils.isBlank(userCode)){
            return  ResponseData.makeErrorMessage(500,"flowInstId或userCode不能为空！");
        }
        boolean deleteFlowInstById = flowManager.deleteFlowInstById(flowInstId,userCode);
        bizModel.putDataSet(id,new SimpleDataSet(deleteFlowInstById));
        return BuiltInOperation.getResponseSuccessData(bizModel.getDataSet(id).getSize());
    }
}
