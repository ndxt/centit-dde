package com.centit.dde.config;

import com.alibaba.fastjson.JSONObject;
import com.centit.product.metadata.po.SourceInfo;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.util.Assert;

import java.util.Properties;

public class DDEConsumerConfig {

    public static KafkaConsumer getKafkaConsumer(JSONObject properties, SourceInfo sourceInfo){
        if (sourceInfo==null){
            Assert.isNull(new SourceInfo(),"连接信息不能为空，请配置kafka连接信息");
        }
        Properties proper=new Properties();
        //没填默认值
        proper.put("group.id", "dde-consumer-group");
        //设置Key和Value的序列化类    配置默认值，页面填写了后直接覆盖
        proper.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        proper.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        if (properties != null) {
            properties.forEach((key,value)->{
                proper.put(key,value);
            });
        }
        //Kafka broker 列表  放最后，避免页面填写参数时填写该参数   直接覆盖页面填写的broker列表
        proper.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, sourceInfo.getDatabaseUrl());
        //开启自动提交  默认5s刷新一次
        proper.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(proper);
        return consumer;
    }
}
