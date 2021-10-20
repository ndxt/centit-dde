package com.centit.dde.consumer.config;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.dao.DataPacketDao;
import com.centit.dde.po.DataPacket;
import com.centit.dde.services.impl.TaskRun;
import com.centit.product.metadata.dao.SourceInfoDao;
import com.centit.product.metadata.po.SourceInfo;
import org.apache.commons.codec.binary.Hex;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Pattern;

/**
 * @author zc
 * 消息触发任务类   自动执行api网关类型为消息触发任务的流程    给kafkaconsumer 处理消息使用
 */
@Service
public class TaskSchedulers {
    private final static  Logger logger = LoggerFactory.getLogger(TaskSchedulers.class);
    private final DataPacketDao dataPacketDao;
    private final SourceInfoDao sourceInfoDao;
    private static ConcurrentHashMap<String, Object> queryParams = new ConcurrentHashMap<>(2);
    private static ConcurrentHashMap<String, String> packetMD5 = new ConcurrentHashMap<>(20);

    @Autowired
    public TaskSchedulers(DataPacketDao dataPacketDao,PathConfig pathConfig, SourceInfoDao sourceInfoDao) {
        this.dataPacketDao = dataPacketDao;
        this.sourceInfoDao=sourceInfoDao;
        queryParams.put("taskType", "4");
        queryParams.put("isValid", "T");
        if (pathConfig.getOwnGroups() != null && pathConfig.getOwnGroups().length > 0) {
            queryParams.put("ownGroup_in", pathConfig.getOwnGroups());
        }
    }

    private static String dataPacketMD5(DataPacket dataPacket) {
        StringBuffer stringBuffer = new StringBuffer(100);
        stringBuffer.append(dataPacket);
        String taskMd5 = "";
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[8192];
            int length;
            InputStream is = new ByteArrayInputStream(stringBuffer.toString().getBytes());
            while ((length = is.read(buffer)) != -1) {
                md5.update(buffer, 0, length);
            }
            taskMd5 = new String(Hex.encodeHex(md5.digest()));
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }
        return taskMd5;
    }

    private void refreshTask(){
        List<DataPacket> list =  new CopyOnWriteArrayList<>(dataPacketDao.listObjectsByProperties(queryParams));
        if (list.size()==0){
            packetMD5.clear();
            return;
        }
        List<String> packetIds = new ArrayList<>();
        for (DataPacket dataPacket : list) {
            packetIds.add(dataPacket.getPacketId());
        }
        //移除map中过期的数据，保持最新的数据
        for (Map.Entry<String, String> entry : packetMD5.entrySet()) {
            String key = entry.getKey();
            if (!packetIds.contains(key)){
                System.out.println("------------------------"+key);
                logger.info("停止过期任务，任务ID:"+key);
                packetMD5.remove(key);
            }
        }
        List<SourceInfo> sourceInfos = sourceInfoDao.listDatabase();
        Map<String, SourceInfo> sourceInfoMap = new HashMap<>();
        for (SourceInfo sourceInfo : sourceInfos) {
            sourceInfoMap.put(sourceInfo.getDatabaseCode(),sourceInfo);
        }
        for (DataPacket dataPacket : list) {
            if (!packetMD5.containsKey(dataPacket.getPacketId())){//不存在，第一次加载
                packetMD5.put(dataPacket.getPacketId(),dataPacketMD5(dataPacket));
                logger.debug("添加任务MAD5信息，任务名:"+dataPacket.getPacketName());
                run(dataPacket,sourceInfoMap);
            }else if (packetMD5.get(dataPacket.getPacketId())!=null
                && packetMD5.get(dataPacket.getPacketId()).equals(dataPacketMD5(dataPacket))){//数据没做任何修改
                logger.debug("任务数据没变化，不需停止任务，任务名："+dataPacket.getPacketName());
                continue;
            }else if (packetMD5.containsKey(dataPacket.getPacketId())
                && !packetMD5.get(dataPacket.getPacketId()).equals(dataPacketMD5(dataPacket))){
                logger.debug("任务数据有变动，重新执行任务,任务名："+dataPacket.getPacketName());
                //替换新的MD5值
                packetMD5.put(dataPacket.getPacketId(),dataPacketMD5(dataPacket));
                run(dataPacket,sourceInfoMap);
            }
        }
    }

    /**
     * 5minute
     */
    @Scheduled(fixedDelay = 1000 * 50)
    public void work(){
        refreshTask();
    }

    private static void run(DataPacket dataPacket, Map<String, SourceInfo> sourceInfoMap){
        new Thread(()->{
            logger.debug("创建消息触发任务,任务名:"+dataPacket.getPacketName());
            JSONObject extProps = dataPacket.getExtProps();
            SourceInfo sourceInfo = sourceInfoMap.get(extProps.getString("databaseId"));
            if (sourceInfo==null){
                Assert.isNull(new SourceInfo(),"连接信息不能为空，请配置kafka连接信息");
            }
            JSONObject properties = sourceInfo.getExtProps();
            Properties proper=new Properties();
            //没填默认值
            proper.put("group.id", extProps.getString("groupId"));
            //设置Key和Value的序列化类    配置默认值，页面填写了后直接覆盖
            proper.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
            proper.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
            //{"groupId":"A","messageKey":"dde.demo.topic","databaseId":"DAHImTgWR3usBAYGknNgww"}
            //Kafka broker 列表  放最后，避免页面填写参数时填写该参数   直接覆盖页面填写的broker列表
            proper.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, sourceInfo.getDatabaseUrl());
            //开启自动提交  默认5s刷新一次   为了保证消息不丢失改成手动提交
            proper.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
            if (properties != null) {
                properties.forEach((key,value)->{
                    proper.put(key,value);
                });
            }
            KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(proper);
            // 指定订阅的分区
          /*  TopicPartition topicPartition = new TopicPartition(topic, 0);
            consumer.assign(Arrays.asList(topicPartition));*/
            String messageKey = extProps.getString("messageKey");
            String[] topicArray = messageKey.split(",");
            //通过正则表达式订阅主题
            if (topicArray.length==1 && topicArray[0].contains(".*")){
                consumer.subscribe(Pattern.compile(topicArray[0]));
            }else {
                consumer.subscribe(Arrays.asList(topicArray));//设置主题，可多个
            }
            //当数据修改或者删除后停止循环
            while (packetMD5.containsKey(dataPacket.getPacketId()) && packetMD5.get(dataPacket.getPacketId()).equals(dataPacketMD5(dataPacket))){
                ConsumerRecords<String, String> msgList = consumer.poll(1000);
                for (ConsumerRecord<String, String> record : msgList) {
                    try{
                        logger.debug("任务名：" + dataPacket.getPacketName()+",topic:" + record.topic() + ",key:" + record.key() + ",value:" + record.value() + ",offset:" + record.offset() + "，分区：" + record.partition());
                        String value = record.value();
                        Map<String, Object> kafkaData = new HashMap<>();
                        kafkaData.put(dataPacket.getPacketId(),value);
                        TaskRun taskRun = ContextUtils.getBean(TaskRun.class);
                        taskRun.runTask(dataPacket.getPacketId(), kafkaData);
                        try {
                            consumer.commitAsync();//异步提交
                        }catch (Exception e) {
                            logger.error("异步提交offset异常,任务名：" + dataPacket.getPacketName() + "异常信息：" + e.getMessage());
                        }finally {
                            try {
                                consumer.commitSync();//同步提交
                            }catch (Exception e){
                                logger.error("同步提交offset异常,任务名："+dataPacket.getPacketName()+"异常信息："+e.getMessage());
                            }
                        }
                    }catch (Exception e){
                        logger.error("消费异常,任务名："+dataPacket.getPacketName()+"异常信息："+e.getMessage());
                    }
                }
            }
            //数据更改或者删除后
            if (!packetMD5.containsKey(dataPacket.getPacketId()) || !packetMD5.get(dataPacket.getPacketId()).equals(dataPacketMD5(dataPacket))) {
                logger.debug("停止消息触发任务,任务名："+dataPacket.getPacketName());
                consumer.close();
                return;
            }
        }).start();
    }
}
