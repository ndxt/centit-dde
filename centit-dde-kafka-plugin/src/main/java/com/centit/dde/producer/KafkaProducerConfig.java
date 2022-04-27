package com.centit.dde.producer;

import com.alibaba.fastjson.JSONObject;
import com.centit.product.adapter.po.SourceInfo;
import com.centit.support.security.AESSecurityUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class KafkaProducerConfig {

    public static KafkaProducer getKafkaProducer(SourceInfo sourceInfo){
        Properties properties=new Properties();
        //设置Key和Value的序列化类    配置默认值，页面填写了后直接覆盖
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        JSONObject extProps = sourceInfo.getExtProps();
        if (extProps!=null){
            extProps.forEach((key,value)->{
                properties.put(key,value);
            });
        }
        String username = sourceInfo.getUsername();
        String passwd = sourceInfo.getPassword();
        //添加用户验证
        if(StringUtils.isNotBlank(username)&&StringUtils.isNotBlank(passwd)){
            String jassc = "org.apache.kafka.common.security.plain.PlainLoginModule required\n"
                + "username=\"" + username + "\"\n"
                + "password=\"" + AESSecurityUtils.decryptBase64String(passwd, "0123456789abcdefghijklmnopqrstuvwxyzABCDEF") + "\";";
            properties.setProperty("sasl.jaas.config", jassc);
        }
        //Kafka broker 列表  放最后，避免页面填写参数时填写该参数   直接覆盖页面填写的broker列表
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, sourceInfo.getDatabaseUrl());
        return new KafkaProducer<String, String>(properties);
    }
}
