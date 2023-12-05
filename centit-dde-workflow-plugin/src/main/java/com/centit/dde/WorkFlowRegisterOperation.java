package com.centit.dde;

import com.centit.dde.adapter.utils.ConstantValue;
import com.centit.dde.bizopt.*;
import com.centit.dde.core.BizOptFlow;
import com.centit.workflow.service.FlowEngine;
import com.centit.workflow.service.FlowManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;

@Component
public class WorkFlowRegisterOperation {

    @Autowired
    private BizOptFlow bizOptFlow;

    @Autowired
    private FlowEngine flowEngine;

    @Autowired
    private FlowManager flowManager;

    @PostConstruct
    void registerOperation(){
        //注册查询操作类
        bizOptFlow.registerOperation(ConstantValue.CREATE_WORKFLOW, new CreateWorkFlowBizOperation(flowEngine));
        //注册插入操作类
        bizOptFlow.registerOperation(ConstantValue.SUBMIT_WORKFLOW, new SubmitWorkFlowBizOperation(flowEngine));
        //注册删除节点
        bizOptFlow.registerOperation(ConstantValue.DELETE_WORKFLOW, new DeleteWorkFlowBizOperation(flowManager));

        bizOptFlow.registerOperation(ConstantValue.MANAGER_WORKFLOW, new ManagerWorkFlowBizOperation(flowManager, flowEngine));
        //注册查询待办节点
        bizOptFlow.registerOperation(ConstantValue.USER_TASK_WORKFLOW, new WorkFlowUserTaskBizOperation(flowEngine));

        bizOptFlow.registerOperation(ConstantValue.INST_NODES_WORKFLOW, new WorkFlowInstNodesBizOperation(flowManager));
    }
}
