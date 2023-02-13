package com.centit.dde;

import com.centit.dde.core.BizOptFlow;
import com.centit.dde.utils.ConstantValue;
import com.centit.product.metadata.dao.SourceInfoDao;
import com.centit.search.service.ESServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
public class EsRegisterOperation {
    @Resource
    BizOptFlow bizOptFlow;

    @Autowired
    ESServerConfig esServerConfig;

    @Autowired
    private SourceInfoDao sourceInfoDao;

    @PostConstruct
    void registerOperation(){
        //注册查询操作类
        bizOptFlow.registerOperation(ConstantValue.ELASTICSEARCH_QUERY,
            new EsQueryBizOperation(esServerConfig, sourceInfoDao));
        //注册插入操作类
        bizOptFlow.registerOperation(ConstantValue.ELASTICSEARCH_WRITE,
            new EsWriteBizOperation(esServerConfig, sourceInfoDao));
    }

/*
    }*/
}
