package com.centit.dde;

import com.centit.dde.bizopt.DynamicCodeBizOperation;
import com.centit.dde.core.BizOptFlow;
import com.centit.dde.utils.ConstantValue;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
public class DynamicCodeRegisterOperation {
    @Resource
    BizOptFlow bizOptFlow;

    @PostConstruct
    void registerOperation(){
        bizOptFlow.registerOperation(ConstantValue.DYNAMIC_CODE,new DynamicCodeBizOperation());
    }
}
