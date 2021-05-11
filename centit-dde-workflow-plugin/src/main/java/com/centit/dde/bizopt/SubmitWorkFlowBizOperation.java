package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.framework.common.ResponseData;
import com.centit.workflow.service.FlowEngine;

public class SubmitWorkFlowBizOperation implements BizOperation {

    private FlowEngine flowEngine;

    public SubmitWorkFlowBizOperation() {
    }

    public SubmitWorkFlowBizOperation(FlowEngine flowEngine) {
        this.flowEngine = flowEngine;
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson) throws Exception {
        return null;
    }
}
