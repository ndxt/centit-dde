package com.centit.dde.config;

import com.centit.dde.bizopt.ESBizOperation;
import com.centit.dde.core.BizOptFlow;
import com.centit.product.metadata.dao.SourceInfoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
public class RegisterOperation {
    @Resource
    BizOptFlow bizOptFlow;

    @Autowired
    private SourceInfoDao sourceInfoDao;

    @PostConstruct
    void registerOperation(){
        bizOptFlow.registerOperation("ES",new ESBizOperation(sourceInfoDao));
    }
}
