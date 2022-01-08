package com.centit.dde;

import com.centit.dde.bizopt.ConsumerBizOperation;
import com.centit.dde.bizopt.ProducerBizOperation;
import com.centit.dde.core.BizOptFlow;
import com.centit.product.metadata.dao.SourceInfoDao;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class KafkaRegisterOperation implements InitializingBean {
    @Resource
    BizOptFlow bizOptFlow;

    @Autowired
    private SourceInfoDao sourceInfoDao;

    @Override
    public void afterPropertiesSet(){
        //注册查询操作类
        bizOptFlow.registerOperation("producer",new ProducerBizOperation(sourceInfoDao));
        //注册插入操作类
        bizOptFlow.registerOperation("consumer",new ConsumerBizOperation(sourceInfoDao));
    }
}
