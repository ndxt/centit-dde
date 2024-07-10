package com.centit.dde;

import com.centit.dde.bizopt.ConsumerBizOperation;
import com.centit.dde.bizopt.ProducerBizOperation;
import com.centit.dde.core.BizOptFlow;
import com.centit.product.metadata.service.SourceInfoMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class KafkaRegisterOperation{
    @Autowired
    BizOptFlow bizOptFlow;

    @Autowired
    private SourceInfoMetadata sourceInfoMetadata;

    @PostConstruct
    void registerOperation(){
        //消息推送实例
        bizOptFlow.registerOperation("kafkaSCZ",new ProducerBizOperation(sourceInfoMetadata));
        //消息订阅实例
        bizOptFlow.registerOperation("kafkaXF",new ConsumerBizOperation(sourceInfoMetadata));
    }
}
