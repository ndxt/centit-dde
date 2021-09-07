package com.centit.dde.config;

import com.alibaba.fastjson.JSONObject;
import com.centit.product.metadata.po.SourceInfo;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.util.Assert;

import java.util.Properties;

public class DDEProducerConfig {

    public static KafkaProducer getKafkaProducer(JSONObject properties, SourceInfo sourceInfo){
        if (sourceInfo==null){
            Assert.isNull(new SourceInfo(),"连接信息不能为空，请配置kafka连接信息");
        }
        Properties proper=new Properties();
        //设置Key和Value的序列化类    配置默认值，页面填写了后直接覆盖
        proper.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        proper.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        if (properties != null) {
            properties.forEach((key,value)->{
                proper.put(key,value);
            });
        }
        //Kafka broker 列表  放最后，避免页面填写参数时填写该参数   直接覆盖页面填写的broker列表
        proper.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, sourceInfo.getDatabaseUrl());
        KafkaProducer producer = new KafkaProducer<String, String>(proper);
        return producer;
    }
}
