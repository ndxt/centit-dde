package com.centit.dde;

import com.centit.dde.bizopt.MailBizOperation;
import com.centit.dde.core.BizOptFlow;
import com.centit.dde.utils.ConstantValue;
import com.centit.product.metadata.service.SourceInfoMetadata;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
public class MsgRegisterOperation {
    @Resource
    private BizOptFlow bizOptFlow;
    @Resource
    private SourceInfoMetadata sourceInfoMetadata;
    @PostConstruct
    void registerOperation() {
        //注册查询操作类
        bizOptFlow.registerOperation(ConstantValue.MSG_SEND,new MailBizOperation(sourceInfoMetadata));
    }
}
