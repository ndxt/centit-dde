package com.centit.dde.services.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.centit.dde.po.DataPacketInterface;
import com.centit.dde.services.BizModelService;
import com.centit.dde.utils.ConstantValue;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.security.Md5Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.*;
import java.util.Map;

/**
 * @author zhf
 */
@Service
public class BizModelServiceImpl implements BizModelService {
    @Autowired(required = false)
    private JedisPool jedisPool;

    @Autowired
    private  BizModelService bizmodelService;

    @Autowired
    private TaskRun taskRun;
    private String key;
    private Jedis jedis;


    @Override
    public Object fetchBizModel(DataPacketInterface dataPacket, Map<String, Object> paramsMap,Map<String, Object> interimVariable) {
        //元数据标签做数据范围查询的时候需要该值，只能从这儿set进去
        interimVariable.put("metadata_optId",dataPacket.getOptId());
        if (notNeedBuf(dataPacket)) {
            return taskRun.runTask(dataPacket.getPacketId(), paramsMap,interimVariable);
        }
        Object bizModel = fetchBizModelFromBuf(dataPacket, paramsMap);
        if (bizModel==null){//第一次执行或者换成失效的时候执行
            bizModel = taskRun.runTask(dataPacket.getPacketId(), paramsMap,interimVariable);
            bizmodelService.setBizModelBuf(bizModel, dataPacket, paramsMap);
        }
        return bizModel;
    }

    private boolean notNeedBuf(DataPacketInterface dataPacket) {
        return jedisPool == null
            || dataPacket.getBufferFreshPeriodType() == null
            || dataPacket.getBufferFreshPeriodType() == ConstantValue.MINUS_ONE
            || dataPacket.getBufferFreshPeriod() == null
            || dataPacket.getBufferFreshPeriod()<=0;
    }

    private Object fetchBizModelFromBuf(DataPacketInterface dataPacket, Map<String, Object> paramsMap) {
        makeDataPacketBufId(dataPacket, paramsMap);
        setJedisPool();
        Object object = null;
        if (keyNotExpired()) {
            try {
                byte[] byt = jedis.get(key.getBytes());
                ByteArrayInputStream bis = new ByteArrayInputStream(byt);
                ObjectInputStream ois = new ObjectInputStream(bis);
                object = ois.readObject();
                bis.close();
                ois.close();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            jedis.close();
        }
        return object;
    }

    private void makeDataPacketBufId(DataPacketInterface dataPacket, Map<String, Object> paramsMap) {
        String dateString = DatetimeOpt.convertTimestampToString(dataPacket.getRecordDate());
        String params = JSON.toJSONString(paramsMap, SerializerFeature.MapSortField);
        StringBuffer temp;
        temp = new StringBuffer("packet:");
        temp.append(dataPacket.getPacketId())
            .append(":")
            .append(params)
            .append(dateString);
        key = Md5Encoder.encode(temp.toString());
    }

    private void setJedisPool() {
        jedis = jedisPool.getResource();
    }

    @Override
    public void setBizModelBuf(Object bizModel, DataPacketInterface dataPacket, Map<String, Object> paramsMap) {
        if (notNeedBuf(dataPacket) || keyNotExpired()) {
            return;
        }
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(bizModel);
            byte[] byt = bos.toByteArray();
            jedis.set(key.getBytes(), byt);
            int seconds = dataPacket.getBufferFreshPeriod();
            int iPeriod = dataPacket.getBufferFreshPeriodType();
            if (iPeriod == ConstantValue.ONE) {//按分
                jedis.expire(key.getBytes(), seconds * 60);
            } else if (iPeriod == ConstantValue.TWO) {//按时
                jedis.expire(key.getBytes(),  seconds*3600);
            } else if (iPeriod == ConstantValue.THREE) {//按天
                int days = seconds * 24 * 3600;
                jedis.expire(key.getBytes(), days);
            }
            bos.close();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        jedis.close();
    }

    private boolean keyNotExpired() {
        return jedis.get(key.getBytes()) != null && (jedis.get(key.getBytes()).length > 0);
    }
}
