package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.framework.common.ResponseData;
import com.centit.workflow.service.FlowEngine;

import java.util.Map;

/**
 * 创建工作流节点
 */
public class CreateWorkFlowBizOperation implements BizOperation {

    private FlowEngine flowEngine;

    public CreateWorkFlowBizOperation(FlowEngine flowEngine) {
        this.flowEngine = flowEngine;
    }

    public CreateWorkFlowBizOperation() {}

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson) throws Exception {
        Map<String, Object> modelTag = bizModel.getModelTag();
        String requestBody= (String) modelTag.get("requestBody");
        return null;
    }
}
