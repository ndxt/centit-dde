package com.centit.dde.consumer;

import com.alibaba.fastjson2.JSONObject;
import com.centit.product.adapter.po.SourceInfo;
import com.centit.support.security.AESSecurityUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Properties;

public class KafkaConsumerConfig {

    public static KafkaConsumer getKafkaConsumer(JSONObject properties, SourceInfo sourceInfo){
        Properties proper=new Properties();
        //开启自动提交  默认5s刷新一次
        proper.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        //设置Key和Value的序列化类    配置默认值，页面填写了后直接覆盖
        proper.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        proper.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.forEach((key,value)->{
            if (key !=null && value!=null){
                proper.put(key,value);
            }
        });
        //Kafka broker 列表  放最后，避免页面填写参数时填写该参数   直接覆盖页面填写的broker列表
        proper.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, sourceInfo.getDatabaseUrl());
        String username = sourceInfo.getUsername();
        String passwd = sourceInfo.getPassword();
        //添加用户验证
        if(StringUtils.isNotBlank(username)&&StringUtils.isNotBlank(passwd)){
            String jassc = "org.apache.kafka.common.security.plain.PlainLoginModule required\n"
                + "username=\"" + username + "\"\n"
                + "password=\"" + AESSecurityUtils.decryptBase64String(passwd, "0123456789abcdefghijklmnopqrstuvwxyzABCDEF") + "\";";
            proper.setProperty("sasl.jaas.config", jassc);
        }
        return new KafkaConsumer<String, String>(proper);
    }
}
