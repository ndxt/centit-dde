package com.centit.dde;

import com.centit.dde.bizopt.RedisBizOperation;
import com.centit.dde.core.BizOptFlow;
import com.centit.product.metadata.dao.SourceInfoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
public class RedisRegisterOperation {
    @Resource
    BizOptFlow bizOptFlow;

    @Autowired
    private SourceInfoDao sourceInfoDao;

    @PostConstruct
    void registerOperation(){
        //注册redis操作类
        bizOptFlow.registerOperation("redisOpt",new RedisBizOperation(sourceInfoDao));
    }
}
