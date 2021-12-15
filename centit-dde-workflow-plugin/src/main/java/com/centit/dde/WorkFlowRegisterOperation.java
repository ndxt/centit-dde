package com.centit.dde;

import com.centit.dde.bizopt.CreateWorkFlowBizOperation;
import com.centit.dde.bizopt.DeleteWorkFlowBizOperation;
import com.centit.dde.bizopt.SubmitWorkFlowBizOperation;
import com.centit.dde.core.BizOptFlow;
import com.centit.dde.utils.ConstantValue;
import com.centit.workflow.service.FlowEngine;
import com.centit.workflow.service.FlowManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;


@Component
public class WorkFlowRegisterOperation {
    @Resource
    private BizOptFlow bizOptFlow;

    @Resource
    private FlowEngine flowEngine;

    @Autowired
    private FlowManager flowManager;

    @PostConstruct
    void registerOperation(){
        //注册查询操作类
        bizOptFlow.registerOperation(ConstantValue.CREATE_WORKFLOW,new CreateWorkFlowBizOperation(flowEngine));
        //注册插入操作类
        bizOptFlow.registerOperation(ConstantValue.SUBMIT_WORKFLOW,new SubmitWorkFlowBizOperation(flowEngine));
        //注册删除节点
        bizOptFlow.registerOperation(ConstantValue.DELETE_WORKFLOW,new DeleteWorkFlowBizOperation(flowManager));
    }
}
