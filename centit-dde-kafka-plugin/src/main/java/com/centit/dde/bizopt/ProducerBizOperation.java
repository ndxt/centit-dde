package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.producer.KafkaProducerConfig;
import com.centit.framework.common.ResponseData;
import com.centit.product.adapter.po.SourceInfo;
import com.centit.product.metadata.dao.SourceInfoDao;
import com.centit.support.algorithm.BooleanBaseOpt;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.algorithm.StringBaseOpt;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

public class ProducerBizOperation implements BizOperation {
    private SourceInfoDao sourceInfoDao;
    public ProducerBizOperation(SourceInfoDao sourceInfoDao) {
        this.sourceInfoDao=sourceInfoDao;
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws ExecutionException, InterruptedException {
        String databaseId  = bizOptJson.getString("databaseId");
        String topic=bizOptJson.getString("topic");
        if (StringUtils.isBlank(topic) || StringUtils.isBlank(databaseId)){
            return ResponseData.makeErrorMessage("Kafka服务地址或topic不能为空！");
        }
        String source=bizOptJson.getString("source");
        if (StringUtils.isBlank(source)){
            return ResponseData.makeErrorMessage("推送消息不能为空！");
        }
        SourceInfo sourceInfo = sourceInfoDao.getDatabaseInfoById(databaseId);
        if (sourceInfo==null){
            return ResponseData.makeErrorMessage("Kafka服务资源不存在或已被删除！");
        }
        KafkaProducer  producer = KafkaProducerConfig.getKafkaProducer(sourceInfo);
        //指定发送分区
        Integer partition = NumberBaseOpt.castObjectToInteger(bizOptJson.getInteger("partition"));
        //指定发送key
        String key = bizOptJson.getString("key");
        //是否异步发送
        Boolean isAsyn = BooleanBaseOpt.castObjectToBoolean(bizOptJson.getBoolean("isAsyn"),false);

        DataSet dataSet = bizModel.getDataSet(source);
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, partition, key, StringBaseOpt.castObjectToString(dataSet.getData()));
        AtomicReference<String> resut= new AtomicReference<>("");
        String id = bizOptJson.getString("id");
        try{
            if (!isAsyn){
                //同步发送   会阻塞
                RecordMetadata res = (RecordMetadata)producer.send(record).get();
                if (res!=null){
                    resut.set("消息发送成功，topic="+ res.topic()+"，分区="+res.partition()+"，offset="+res.offset());
                }
                bizModel.putDataSet(id,new DataSet(resut));
            }else {
                //异步发送添加回调函数,展示信息 不会阻塞
                producer.send(record, (metadata, exception) -> {
                    if (metadata != null) {
                        resut.set("消息发送成功，topic="+ metadata.topic()+"，分区="+metadata.partition()+"，offset="+metadata.offset());
                    } else if (exception != null) {
                        resut.set("消息发送失败，异常信息："+exception.getMessage());
                    }
                    bizModel.putDataSet(id,new DataSet(resut));
                });
            }
        }finally {
            if (producer!=null){
                producer.close();
            }
        }
        return  BuiltInOperation.createResponseSuccessData(bizModel.getDataSet(id).getSize());
    }
}
