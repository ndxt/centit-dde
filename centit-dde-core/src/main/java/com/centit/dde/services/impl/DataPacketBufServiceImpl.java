package com.centit.dde.services.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.centit.dde.po.DataPacketInterface;
import com.centit.dde.services.DataPacketBufService;
import com.centit.dde.utils.ConstantValue;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.security.Md5Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.*;
import java.util.Date;
import java.util.Map;

/**
 * @author zhf
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DataPacketBufServiceImpl implements DataPacketBufService {
    @Autowired(required = false)
    private JedisPool jedisPool;
    private String key;
    private Jedis jedis;

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
    private void setJedisPool(){
        jedis = jedisPool.getResource();
    }

    private boolean haveDataBuf(DataPacketInterface dataPacket) {
        return jedisPool != null && dataPacket.getBufferFreshPeriod() != null;
    }

    @Override
    public Object fetchDataPacketDataFromBuf(DataPacketInterface dataPacket, Map<String, Object> paramsMap) {
        if(!haveDataBuf(dataPacket)){
            return null;
        }
        makeDataPacketBufId(dataPacket, paramsMap);
        Object object = null;
        if (NumberBaseOpt.castObjectToInteger(dataPacket.getBufferFreshPeriod()) >= 0) {
            setJedisPool();
            if ((jedis.get(key.getBytes()) != null) && (jedis.get(key.getBytes()).length > 0)) {
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
                return object;
            }
        }
        return null;
    }


    private boolean needDataBuf(DataPacketInterface dataPacket, Map<String, Object> paramsMap) {
        if (haveDataBuf(dataPacket)) {
            return jedis.get(key.getBytes()) == null || (jedis.get(key.getBytes()).length == 0);
        }
        return false;
    }

    @Override
    public void setDataPacketBuf(Object bizModel, DataPacketInterface dataPacket, Map<String, Object> paramsMap) {
        if(!needDataBuf(dataPacket,paramsMap)){
            return;
        }
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(bizModel);

            byte[] byt = bos.toByteArray();
            jedis.set(key.getBytes(), byt);
            int seconds;
            int iPeriod = NumberBaseOpt.castObjectToInteger(dataPacket.getBufferFreshPeriod());
            if (iPeriod == ConstantValue.ONE) {
                //一日
                seconds = 24 * 3600;
                jedis.expire(key.getBytes(), seconds);
            } else if (iPeriod == ConstantValue.TWO) {
                //按周
                seconds = DatetimeOpt.calcSpanDays(new Date(), DatetimeOpt.seekEndOfWeek(new Date())) * 24 * 3600;
                jedis.expire(key.getBytes(), seconds);
            } else if (iPeriod == ConstantValue.THREE) {
                //按月
                seconds = DatetimeOpt.calcSpanDays(new Date(), DatetimeOpt.seekEndOfMonth(new Date())) * 24 * 3600;
                jedis.expire(key.getBytes(), seconds);
            } else if (iPeriod == ConstantValue.FOUR) {
                //按年
                seconds = DatetimeOpt.calcSpanDays(new Date(), DatetimeOpt.seekEndOfYear(new Date())) * 24 * 3600;
                jedis.expire(key.getBytes(), seconds);
            } else if (iPeriod >= ConstantValue.SIXTY) {
                //按秒
                jedis.expire(key.getBytes(), iPeriod);
            }
            bos.close();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        jedis.close();
    }

}
