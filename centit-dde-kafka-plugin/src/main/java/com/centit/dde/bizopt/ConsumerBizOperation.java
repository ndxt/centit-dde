package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.config.DDEConsumerConfig;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.SimpleDataSet;
import com.centit.dde.entity.ConsumerEntity;
import com.centit.framework.common.ResponseData;
import com.centit.product.metadata.dao.SourceInfoDao;
import com.centit.product.metadata.po.SourceInfo;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConsumerBizOperation implements BizOperation {
    private SourceInfoDao sourceInfoDao;

    public ConsumerBizOperation( ) {
    }

    public ConsumerBizOperation(SourceInfoDao sourceInfoDao) {
        this.sourceInfoDao=sourceInfoDao;
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson){
        ConsumerEntity consumerEntity = JSON.parseObject(JSON.toJSONString(bizOptJson), ConsumerEntity.class);
        SourceInfo sourceInfo = sourceInfoDao.getDatabaseInfoById(consumerEntity.getDataSourceID());
        KafkaConsumer consumer = DDEConsumerConfig.getKafkaConsumer(consumerEntity.getJsonObject(),sourceInfo);
        List<String> result = new ArrayList<>();
        try {
            consumer.subscribe(Arrays.asList(consumerEntity.getTopic()));//设置主题，可多个
            ConsumerRecords<String, String> records = consumer.poll(1000);
            for (ConsumerRecord<String, String> record : records) {
                result.add(record.value());
            }
        }catch (Exception e){
            return BuiltInOperation.getResponseData(0, 500, bizOptJson.getString("SetsName")+"异常信息："+e.getMessage());
        }finally {
            if (consumer!=null){
                consumer.close();
            }
        }
        bizModel.putDataSet(consumerEntity.getId(),new SimpleDataSet(result));
        return BuiltInOperation.getResponseSuccessData(result.size());
    }
}
