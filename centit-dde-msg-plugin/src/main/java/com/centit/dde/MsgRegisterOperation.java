package com.centit.dde;

import com.centit.dde.adapter.utils.ConstantValue;
import com.centit.dde.bizopt.MailBizOperation;
import com.centit.dde.core.BizOptFlow;
import com.centit.product.metadata.dao.SourceInfoDao;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
public class MsgRegisterOperation {
    @Resource
    private BizOptFlow bizOptFlow;
    @Resource
    private SourceInfoDao sourceInfoDao;
    @PostConstruct
    void registerOperation() {
        //注册查询操作类
        bizOptFlow.registerOperation(ConstantValue.MSG_SEND,new MailBizOperation(sourceInfoDao));
    }
}
