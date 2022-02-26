package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.consumer.ConsumerEntity;
import com.centit.dde.consumer.KafkaConsumerConfig;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.SimpleDataSet;
import com.centit.framework.common.ResponseData;
import com.centit.product.adapter.po.SourceInfo;
import com.centit.product.metadata.dao.SourceInfoDao;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ConsumerBizOperation implements BizOperation {
    private SourceInfoDao sourceInfoDao;

    public ConsumerBizOperation(SourceInfoDao sourceInfoDao) {
        this.sourceInfoDao=sourceInfoDao;
    }

    /**
     * kafka 该方法只支持自动提交，无法支持手动提交
     * 这样就可能造成数据取出来消费掉了，但是后续的处理异常了，这个没法控制
     * @param bizModel
     * @param bizOptJson
     * @return
     */
    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson){
        ConsumerEntity consumerEntity = JSON.parseObject(JSON.toJSONString(bizOptJson), ConsumerEntity.class);
        SourceInfo sourceInfo = sourceInfoDao.getDatabaseInfoById(consumerEntity.getDatabaseId());
        JSONObject extProps = sourceInfo.getExtProps();
        extProps.put(ConsumerConfig.GROUP_ID_CONFIG,consumerEntity.getGroupId());
        KafkaConsumer consumer = KafkaConsumerConfig.getKafkaConsumer(extProps,sourceInfo);
        JSONArray topics = consumerEntity.getTopic();
        if (topics!=null || topics.size()==0){
           throw  new RuntimeException("topic不能为空！");
        }
        //正则表达式方式订阅消息
        if (topics.size()==1 && topics.getString(0).contains(".*")){
            consumer.subscribe(Pattern.compile(topics.getString(0)));
        }else {
            //设置主题，可多个
            consumer.subscribe(topics);
        }
        ConsumerRecords<String, String> records = consumer.poll(1000);
        //同步提交offset
        consumer.commitSync();
        if (consumer!=null){
            consumer.close();
        }
        List<String> values = new ArrayList<>();
        for (ConsumerRecord<String, String> record : records) {
            values.add(record.value());
        }
        SimpleDataSet simpleDataSet = new SimpleDataSet(values);
        bizModel.putDataSet(consumerEntity.getId(),simpleDataSet);
        return BuiltInOperation.getResponseSuccessData(simpleDataSet.getSize());
    }
}
