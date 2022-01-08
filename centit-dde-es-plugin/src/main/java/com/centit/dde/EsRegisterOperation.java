package com.centit.dde;

import com.centit.dde.bizopt.EsReadBizOperation;
import com.centit.dde.bizopt.EsWriteBizOperation;
import com.centit.dde.core.BizOptFlow;
import com.centit.product.metadata.dao.SourceInfoDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
@Slf4j
public class EsRegisterOperation {
    @Resource
    BizOptFlow bizOptFlow;

    @Autowired
    private SourceInfoDao sourceInfoDao;

    @PostConstruct
    void registerOperation(){
        //注册查询操作类
        bizOptFlow.registerOperation("EsReadVo",new EsReadBizOperation(sourceInfoDao));
        //注册插入操作类
        bizOptFlow.registerOperation("EsWriteVo",new EsWriteBizOperation(sourceInfoDao));
    }
}
