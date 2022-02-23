package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataSet;
import com.centit.dde.core.SimpleDataSet;
import com.centit.dde.producer.KafkaProducerConfig;
import com.centit.dde.producer.ProducerEntity;
import com.centit.framework.common.ResponseData;
import com.centit.product.adapter.po.SourceInfo;
import com.centit.product.metadata.dao.SourceInfoDao;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class ProducerBizOperation implements BizOperation {
    private SourceInfoDao sourceInfoDao;
    public ProducerBizOperation( ) {
    }
    public ProducerBizOperation(SourceInfoDao sourceInfoDao) {
        this.sourceInfoDao=sourceInfoDao;
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson){
        ProducerEntity producerEntity = JSON.parseObject(JSON.toJSONString(bizOptJson), ProducerEntity.class);
        SourceInfo sourceInfo = sourceInfoDao.getDatabaseInfoById(producerEntity.getDatabaseId());
        DataSet dataSet = bizModel.getDataSet(producerEntity.getSource());
        KafkaProducer producer=null;
        try {
            ProducerRecord<String, String> record = new ProducerRecord<>(producerEntity.getTopic(), producerEntity.getPartition(), producerEntity.getId(),JSON.toJSONString(dataSet.getData()));
            //判断topic是否存在，不存在不发送，防止填错topic
            Properties properties= new Properties();
            properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, sourceInfo.getDatabaseUrl());
            //创建Topic
            AdminClient adminClient = KafkaAdminClient.create(properties);
            Set<String> topicNames = adminClient.listTopics().names().get();
            if (!topicNames.contains(producerEntity.getTopic())){
                return BuiltInOperation.getResponseData(0, 500,
                    bizOptJson.getString("SetsName")+"异常信息:topic_"+producerEntity.getTopic()+"不存在！");
            }
            producer = KafkaProducerConfig.getKafkaProducer(sourceInfo);
            AtomicReference<String> resut= new AtomicReference<>("");
            if (!producerEntity.getIsAsyn()){
                //同步发送   会阻塞
                Object res = producer.send(record).get();
                if (res!=null){
                    resut.set("发送成功！");
                }
            }else {
                //异步发送添加回调函数,展示信息 不会阻塞
                producer.send(record, (metadata, exception) -> {
                    if (metadata != null) {
                        resut.set("消息发送成功，topic："+ metadata.topic()+"，分区："+metadata.partition());
                    } else if (exception != null) {
                        resut.set("消息发送失败，异常信息："+exception.getMessage());
                    }
                });
            }
            SimpleDataSet simpleDataSet = new SimpleDataSet(resut);
            bizModel.putDataSet(producerEntity.getId(),simpleDataSet);
            return  BuiltInOperation.getResponseSuccessData(simpleDataSet.getSize());
        } catch (Exception e) {
            return BuiltInOperation.getResponseData(0, 500, bizOptJson.getString("SetsName")+"异常信息："+e.getMessage());
        } finally {
            if (producer!=null){
                producer.close();
            }
        }
    }
}
