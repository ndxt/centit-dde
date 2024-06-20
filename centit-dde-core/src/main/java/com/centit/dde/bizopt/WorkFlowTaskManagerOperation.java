package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.framework.common.ResponseData;
import com.centit.workflow.service.FlowManager;

public class WorkFlowTaskManagerOperation implements BizOperation {

    private FlowManager flowManager;

    public WorkFlowTaskManagerOperation(FlowManager flowManager) {
        this.flowManager = flowManager;
    }

    /**
     * 任务转移，
     * 任务委托（增加 、删除、 查）
     */
    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        //flowManager.
        // 任务转移 moveUserTaskTo, 有两种方式，一个是转移部分，一个是全部

        //RoleRelegate 任务委托，这个好想不能删除，只能终止委托

        return null;
    }
}
