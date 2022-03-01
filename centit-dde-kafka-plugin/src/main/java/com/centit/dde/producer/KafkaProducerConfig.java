package com.centit.dde.producer;

import com.alibaba.fastjson.JSONObject;
import com.centit.product.adapter.po.SourceInfo;
import com.centit.support.security.AESSecurityUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.util.Assert;

import java.util.Properties;

public class KafkaProducerConfig {

    public static KafkaProducer getKafkaProducer(SourceInfo sourceInfo){
        if (sourceInfo==null){
            Assert.isNull(new SourceInfo(),"连接信息不能为空，请配置kafka连接信息");
        }
        JSONObject properties = sourceInfo.getExtProps();
        Properties proper=new Properties();
        //设置Key和Value的序列化类    配置默认值，页面填写了后直接覆盖
        proper.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        proper.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        if (properties != null) {
            properties.forEach((key,value)->{
                proper.put(key,value);
            });
        }
        String username = sourceInfo.getUsername();
        String passwd = sourceInfo.getPassword();
        //添加用户验证
        if(StringUtils.isNotBlank(username)&&StringUtils.isNotBlank(passwd)){
            String jassc = "org.apache.kafka.common.security.plain.PlainLoginModule required\n"
                + "username=\"" + username + "\"\n"
                + "password=\"" + AESSecurityUtils.decryptBase64String(passwd, "0123456789abcdefghijklmnopqrstuvwxyzABCDEF") + "\";";
            proper.setProperty("sasl.jaas.config", jassc);
        }
        //Kafka broker 列表  放最后，避免页面填写参数时填写该参数   直接覆盖页面填写的broker列表
        proper.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, sourceInfo.getDatabaseUrl());
        KafkaProducer producer = new KafkaProducer<String, String>(proper);
        return producer;
    }
}
