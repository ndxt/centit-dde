package com.centit.dde;

import com.centit.dde.bizopt.CreateWorkFlowBizOperation;
import com.centit.dde.bizopt.SubmitWorkFlowBizOperation;
import com.centit.dde.core.BizOptFlow;
import com.centit.dde.utils.ConstantValue;
import com.centit.workflow.service.FlowEngine;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;


@Component
public class RegisterOperation {
    @Resource
    BizOptFlow bizOptFlow;

    @Resource
    FlowEngine flowEngine;

    @PostConstruct
    void registerOperation(){
        //注册查询操作类
        bizOptFlow.registerOperation(ConstantValue.CREATE_WORKFLOW,new CreateWorkFlowBizOperation(flowEngine));
        //注册插入操作类
        bizOptFlow.registerOperation(ConstantValue.SUBMIT_WORKFLOW,new SubmitWorkFlowBizOperation(flowEngine));
    }
}
