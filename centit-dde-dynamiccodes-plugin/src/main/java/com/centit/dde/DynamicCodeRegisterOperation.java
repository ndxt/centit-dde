package com.centit.dde;

import com.centit.dde.bizopt.DynamicCodeBizOperation;
import com.centit.dde.core.BizOptFlow;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
public class DynamicCodeRegisterOperation {
    @Resource
    BizOptFlow bizOptFlow;

    @PostConstruct
    void registerOperation(){
        bizOptFlow.registerOperation("readEs",new DynamicCodeBizOperation());
    }
}
