package com.centit.dde;

import com.centit.dde.bizopt.ConsumerBizOperation;
import com.centit.dde.bizopt.ProducerBizOperation;
import com.centit.dde.core.BizOptFlow;
import com.centit.product.metadata.dao.SourceInfoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
public class KafkaRegisterOperation{
    @Resource
    BizOptFlow bizOptFlow;

    @Autowired
    private SourceInfoDao sourceInfoDao;

    @PostConstruct
    void registerOperation(){
        //消息推送实例
        bizOptFlow.registerOperation("kafkaSCZ",new ProducerBizOperation(sourceInfoDao));
        //消息订阅实例
        bizOptFlow.registerOperation("consumer",new ConsumerBizOperation(sourceInfoDao));
    }
}
