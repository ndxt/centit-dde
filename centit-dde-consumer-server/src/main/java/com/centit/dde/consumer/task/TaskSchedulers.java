package com.centit.dde.consumer.task;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.adapter.dao.DataPacketDao;
import com.centit.dde.adapter.po.DataPacket;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.services.impl.TaskRun;
import com.centit.dde.utils.ConstantValue;
import com.centit.framework.model.adapter.PlatformEnvironment;
import com.centit.framework.model.basedata.OsInfo;
import com.centit.product.metadata.dao.SourceInfoDao;
import com.centit.product.metadata.po.SourceInfo;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
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
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author zc
 * 消息触发任务类   自动执行api网关类型为消息触发任务的流程    给kafkaconsumer 处理消息使用
 *
 * 问题记录：
 * 默认每个topic只创建一个消费者  一个消费者消费topic的所有分区   如果相同的topic要创建多个消费者来进行消费的话就需要创建多个API 使其分组id相同
 *理论应该提供创建消费者个数的配置，但是怕创建的线程过多，导致服务器挂掉，所以默认只创建一个（比如一个topic有50个分区，按照理想状态创建的话那么就得创建50个线程）
 * 弊端：默认每个分区只创建一个消费者，这样带来的后果就是消费能力较差，容易造成消息积压 可以创建多个API 使其分组id相同，就是麻烦了点，这样一来也会出现上面说的问题，
 * API越多也就意味着创建的线程越多，只是一开始不用创建那边多线程罢了 ，不管哪种方式都会有这个问题
 */
@Service
public class TaskSchedulers {

    @Autowired
    ConsumerPoolConfig consumerPoolConfig;

    @Autowired
    private PlatformEnvironment platformEnvironment;

    private final static  Logger logger = LoggerFactory.getLogger(TaskSchedulers.class);
    private ExecutorService threadPool = Executors.newCachedThreadPool();
       /* new ThreadPoolExecutor(
            consumerPoolConfig.getCorePoolSize(),
            consumerPoolConfig.getMaximumPoolSize(),
            consumerPoolConfig.getKeepAliveTime(),
            TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<>(consumerPoolConfig.getQueueSize()),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.AbortPolicy());*/
    private final DataPacketDao dataPacketDao;
    private final SourceInfoDao sourceInfoDao;

    private static ConcurrentHashMap<String, Object> queryParams = new ConcurrentHashMap<>(2);
    private static ConcurrentHashMap<String, String> packetMD5 = new ConcurrentHashMap<>(20);

    @Autowired
    public TaskSchedulers(DataPacketDao dataPacketDao, SourceInfoDao sourceInfoDao) {
        this.dataPacketDao = dataPacketDao;
        this.sourceInfoDao=sourceInfoDao;
        queryParams.put("taskType", "4");
        queryParams.put("isValid", "T");
    }

    /**
     * 5minute
     */
    @Scheduled(fixedDelay = 1000 * 50)
    private void runTask(){
        List<DataPacket> list = new  CopyOnWriteArrayList(dataPacketDao.listObjectsByProperties(queryParams));
        if(list.size()==0 && packetMD5.size()>0){
            packetMD5.clear();
            return;
        }
        //置空这几个时间再计算MD5值
        list.stream().forEach(dataPacket -> {
            dataPacket.setUpdateDate(null);
            dataPacket.setLastRunTime(null);
            dataPacket.setNextRunTime(null);
        });
        List<String> packetIds = list.stream().map(DataPacket::getPacketId).collect(Collectors.toList());
        //移除map中过期的数据，保持最新的数据
        for (String packetId : packetMD5.keySet()) {
            if (!packetIds.contains(packetId)){
                logger.info(String.format("移除过期消息触发任务，任务ID：%s",packetId));
                packetMD5.remove(packetId);
            }
        }
        List<SourceInfo> sourceInfos = sourceInfoDao.listDatabase();
        Map<String, SourceInfo> sourceInfoMap = new HashMap<>();
        sourceInfos.stream().forEach(sourceInfo -> sourceInfoMap.put(sourceInfo.getDatabaseCode(),sourceInfo));
        for (DataPacket dataPacket : list) {
            if (!packetMD5.containsKey(dataPacket.getPacketId())){//不存在，第一次加载
                packetMD5.put(dataPacket.getPacketId(),dataPacketMD5(dataPacket));
                logger.info(String.format("创建消息触发任务,任务名：%s,任务ID：%s",dataPacket.getPacketName(),dataPacket.getPacketId()));
                run(dataPacket,sourceInfoMap);
            }else if (packetMD5.get(dataPacket.getPacketId())!=null
                && packetMD5.get(dataPacket.getPacketId()).equals(dataPacketMD5(dataPacket))){//数据没做任何修改
                logger.info(String.format("%s：任务数据没变化，跳过！",dataPacket.getPacketName()));
                continue;
            }else if (packetMD5.containsKey(dataPacket.getPacketId())
                && !packetMD5.get(dataPacket.getPacketId()).equals(dataPacketMD5(dataPacket))){
                logger.info(String.format("%s:任务数据有变化，重新执行!",dataPacket.getPacketName()));
                //替换新的MD5值
                packetMD5.put(dataPacket.getPacketId(),dataPacketMD5(dataPacket));
                run(dataPacket,sourceInfoMap);
            }
        }
    }

    private  void run(DataPacket dataPacket, Map<String, SourceInfo> sourceInfoMap){
        threadPool.execute(()->{
            JSONObject extProps = dataPacket.getExtProps();
            SourceInfo sourceInfo = sourceInfoMap.get(extProps.getString("databaseId"));
            Assert.notNull(sourceInfo,"连接信息不能为空，请配置kafka连接信息");
            JSONObject properties = sourceInfo.getExtProps();
            Properties proper=new Properties();
            //没填默认值
            if (StringUtils.isNotBlank(extProps.getString("groupId"))){
                proper.put("group.id", extProps.getString("groupId"));
            }
            //设置Key和Value的序列化类    配置默认值，页面填写了后直接覆盖
            proper.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
            proper.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
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
            JSONArray topics = extProps.getJSONArray("topic");
            List<String> topicNames=new ArrayList<>();
            topics.stream().forEach(topicInfo->{
                JSONObject jsonObject = (JSONObject) topicInfo;
                topicNames.add(jsonObject.getString("name"));
            });
            //通过正则表达式订阅主题
            if (topicNames.size()==1 && topicNames.get(0).contains(".*")){
                consumer.subscribe(Pattern.compile(topicNames.get(0)));
            }else {
                consumer.subscribe(topicNames);//设置主题，可多个
            }
            //当数据修改或者删除后停止循环
            while (packetMD5.containsKey(dataPacket.getPacketId()) && packetMD5.get(dataPacket.getPacketId()).equals(dataPacketMD5(dataPacket))){
                ConsumerRecords<String, String> msgList = consumer.poll(Duration.ofMillis(1000));
                for (ConsumerRecord<String, String> record : msgList) {
                    try {
                        logger.debug(String.format("任务名：%s,topic:%s,key:%s,value:%s,offset:%s，分区：%s",
                            dataPacket.getPacketName(),
                            record.topic(),
                            record.key(),
                            record.value(),
                            record.offset(),
                            record.partition()
                        ));
                        String value = record.value();
                        //开始处理任务逻辑
                        TaskRun taskRun = ContextUtils.getBean(TaskRun.class);
                        DataOptContext dataOptContext = new DataOptContext();
                        if (platformEnvironment != null){
                            OsInfo osInfo = platformEnvironment.getOsInfo(dataPacket.getOsId());
                            dataOptContext.setStackData(ConstantValue.APPLICATION_INFO_TAG, osInfo);
                        }

                        dataOptContext.setStackData(ConstantValue.MESSAGE_QUEUE_TAG, JSON.parseObject(value));
                        taskRun.runTask(dataPacket, dataOptContext);
                        try {
                            consumer.commitAsync();//异步提交
                        }catch (Exception e) {
                            logger.error(String.format("异步提交offset异常,任务名%s，异常信息：%s" ,dataPacket.getPacketName(),e.getMessage()));
                        }finally {
                            try {
                                consumer.commitSync();//同步提交
                            }catch (Exception e){
                                logger.error(String.format("同步提交offset异常,任务名：%s，异常信息：%s",dataPacket.getPacketName(),e.getMessage()));
                            }
                        }
                    }catch (Exception e){
                        logger.error(String.format("消费异常,任务名：%s,异常信息：%s",dataPacket.getPacketName(),e.getMessage()));
                    }
                }
            }
            //数据更改或者删除后
            if (!packetMD5.containsKey(dataPacket.getPacketId()) || !packetMD5.get(dataPacket.getPacketId()).equals(dataPacketMD5(dataPacket))) {
                consumer.close();
                logger.info(String.format("移除过期消息触发任务，任务ID：%s，任务名称：%s",dataPacket.getPacketId(),dataPacket.getPacketName()));
            }
        });
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


}
