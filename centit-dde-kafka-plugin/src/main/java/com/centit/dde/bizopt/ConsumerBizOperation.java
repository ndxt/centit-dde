package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.consumer.KafkaConsumerConfig;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.SimpleDataSet;
import com.centit.framework.common.ResponseData;
import com.centit.product.adapter.po.SourceInfo;
import com.centit.product.metadata.dao.SourceInfoDao;
import org.apache.commons.lang3.StringUtils;
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
     * @param dataOptContext
     * @return
     */
    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext)throws Exception{
        String databaseId = bizOptJson.getString("databaseId");
        String topic = bizOptJson.getString("topic");
        if (StringUtils.isBlank(topic) || StringUtils.isBlank(databaseId)){
            return ResponseData.makeErrorMessage("Kafka服务地址或topic不能为空！");
        }
        String groupId = bizOptJson.getString("groupId");
        SourceInfo sourceInfo = sourceInfoDao.getDatabaseInfoById(databaseId);
        if (sourceInfo==null){
            return ResponseData.makeErrorMessage("Kafka服务资源不存在或已被删除！");
        }
        JSONObject extProps = sourceInfo.getExtProps();
        if (extProps == null){
            extProps = new JSONObject();
        }
        extProps.put(ConsumerConfig.GROUP_ID_CONFIG,groupId);
        KafkaConsumer consumer = KafkaConsumerConfig.getKafkaConsumer(extProps,sourceInfo);
        List<String> values = new ArrayList<>();
        try {
            //同步提交offset
            consumer.subscribe(Pattern.compile(topic));
            ConsumerRecords<String, String> records = consumer.poll(1000);
            for (ConsumerRecord<String, String> record : records) {
                values.add(record.value());
            }
            consumer.commitSync();
        } finally {
            if (consumer!=null){
                consumer.close();
            }
        }
        String id = bizOptJson.getString("id");
        bizModel.putDataSet(id, new SimpleDataSet(values));
        return BuiltInOperation.createResponseSuccessData(values.size());
    }
}
