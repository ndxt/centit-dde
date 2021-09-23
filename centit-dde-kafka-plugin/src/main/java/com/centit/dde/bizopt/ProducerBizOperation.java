package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.config.DDEProducerConfig;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataSet;
import com.centit.dde.core.SimpleDataSet;
import com.centit.dde.entity.ProducerEntity;
import com.centit.framework.common.ResponseData;
import com.centit.product.metadata.dao.SourceInfoDao;
import com.centit.product.metadata.po.SourceInfo;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

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
        SourceInfo sourceInfo = sourceInfoDao.getDatabaseInfoById(producerEntity.getDataSourceID());
        DataSet dataSet = bizModel.getDataSet(producerEntity.getSourceID());
        KafkaProducer producer=null;
        AtomicBoolean result=new AtomicBoolean(false);
        try {
            ProducerRecord<String, String> record = new ProducerRecord<>(producerEntity.getTopic(), producerEntity.getPartition(), producerEntity.getId(),JSON.toJSONString(dataSet.getData()));
            producer = DDEProducerConfig.getKafkaProducer(producerEntity.getJsonObject(), sourceInfo);
            if (!producerEntity.getAsyn()){
                //同步发送   会阻塞
                Future future = producer.send(record);
                if (future.get()!=null){
                    result.set(true);
                }
            }else {
                //异步发送添加回调函数,展示信息 不会阻塞
                producer.send(record, (metadata, exception) -> {
                    if (metadata != null) {
                        result.set(true);
                    } else if (exception != null) {
                        exception.printStackTrace();
                    }
                });
            }
            SimpleDataSet simpleDataSet = new SimpleDataSet(result);
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
