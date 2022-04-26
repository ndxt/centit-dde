package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.*;
import com.centit.dde.producer.KafkaProducerConfig;
import com.centit.dde.producer.ProducerEntity;
import com.centit.framework.common.ResponseData;
import com.centit.product.adapter.po.SourceInfo;
import com.centit.product.metadata.dao.SourceInfoDao;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

public class ProducerBizOperation implements BizOperation {
    private SourceInfoDao sourceInfoDao;
    public ProducerBizOperation( ) {
    }
    public ProducerBizOperation(SourceInfoDao sourceInfoDao) {
        this.sourceInfoDao=sourceInfoDao;
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws ExecutionException, InterruptedException {
        ProducerEntity producerEntity = JSON.parseObject(JSON.toJSONString(bizOptJson), ProducerEntity.class);
        SourceInfo sourceInfo = sourceInfoDao.getDatabaseInfoById(producerEntity.getDatabaseId());
        DataSet dataSet = bizModel.getDataSet(producerEntity.getSource());
        ProducerRecord<String, String> record = new ProducerRecord<>(
            producerEntity.getTopic(),
            producerEntity.getPartition(),
            producerEntity.getKey(),
            JSON.toJSONString(dataSet.getData()));
        KafkaProducer  producer = KafkaProducerConfig.getKafkaProducer(sourceInfo);
        SimpleDataSet simpleDataSet = new SimpleDataSet();
        AtomicReference<String> resut= new AtomicReference<>("");
        if (!producerEntity.getIsAsyn()){
            //同步发送   会阻塞
            RecordMetadata res = (RecordMetadata)producer.send(record).get();
            if (res!=null){
                resut.set("消息发送成功，topic="+ res.topic()+"，分区="+res.partition()+"，offset="+res.offset());
            }
            simpleDataSet.setData(resut);
            bizModel.putDataSet(producerEntity.getId(),simpleDataSet);
        }else {
            //异步发送添加回调函数,展示信息 不会阻塞
            producer.send(record, (metadata, exception) -> {
                if (metadata != null) {
                    resut.set("消息发送成功，topic="+ metadata.topic()+"，分区="+metadata.partition()+"，offset="+metadata.offset());
                } else if (exception != null) {
                    resut.set("消息发送失败，异常信息："+exception.getMessage());
                }
                simpleDataSet.setData(resut);
                bizModel.putDataSet(producerEntity.getId(),simpleDataSet);
            });
        }
        if (producer!=null){
            producer.close();
        }
        return  BuiltInOperation.getResponseSuccessData(simpleDataSet.getSize());
    }
}
