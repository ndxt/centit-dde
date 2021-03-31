package com.centit.dde.services.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.centit.dde.core.BizOptFlow;
import com.centit.dde.dao.DataPacketCopyDao;
import com.centit.dde.dao.DataPacketDao;
import com.centit.dde.po.DataPacket;
import com.centit.dde.po.DataPacketCopy;
import com.centit.dde.po.DataPacketParam;
import com.centit.dde.services.DataPacketCopyService;
import com.centit.dde.services.DataPacketService;
import com.centit.dde.utils.Constant;
import com.centit.fileserver.common.FileStore;
import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.database.utils.PageDesc;
import com.centit.support.security.Md5Encoder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author zhf
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DataPacketCopyServiceImpl implements DataPacketCopyService {
    @Autowired(required = false)
    private JedisPool jedisPool;

    @Autowired(required = false)
    private FileStore fileStore;

    @Autowired
    private DataPacketCopyDao dataPacketCopyDao;

    @Autowired
    private DataPacketService dataPacketService;

    @Autowired
    private BizOptFlow bizOptFlow;

    public DataPacketCopyServiceImpl() {

    }

    @Override
    public void createDataPacket(DataPacketCopy dataPacketCopy) {
        dataPacketCopyDao.saveNewObject(dataPacketCopy);
        dataPacketCopyDao.saveObjectReferences(dataPacketCopy);
    }

    @Override
    public void updateDataPacket(DataPacketCopy dataPacketCopy) {
        dataPacketCopyDao.updateObject(dataPacketCopy);
        dataPacketCopyDao.saveObjectReferences(dataPacketCopy);
    }


    @Override
    public void publishDataPacket(DataPacketCopy dataPacketCopy) {
        DataPacket dataPacket = new DataPacket();
        BeanUtils.copyProperties(dataPacketCopy,dataPacket);
        List<DataPacketParam> dataPacketParamList = new ArrayList<>();
        BeanUtils.copyProperties(dataPacketCopy.getPacketParams(),dataPacketParamList);
        dataPacket.setPacketParams(dataPacketParamList);
        dataPacketService.publishDataPacket(dataPacket);
    }


    @Override
    public void updateDataPacketOptJson(String packetId, String dataPacketOptJson) {
        DatabaseOptUtils.batchUpdateObject(dataPacketCopyDao, DataPacketCopy.class,
            CollectionsOpt.createHashMap("dataOptDescJson", dataPacketOptJson),
            CollectionsOpt.createHashMap("packetId", packetId)
        );
    }


    @Override
    public void deleteDataPacket(String packetId) {
        DataPacketCopy dataPacketCopy = dataPacketCopyDao.getObjectWithReferences(packetId);
        dataPacketCopyDao.deleteObjectById(packetId);
        dataPacketCopyDao.deleteObjectReferences(dataPacketCopy);
    }

    @Override
    public List<DataPacketCopy> listDataPacket(Map<String, Object> params, PageDesc pageDesc) {
        return dataPacketCopyDao.listObjectsByProperties(params, pageDesc);
    }

    @Override
    public DataPacketCopy getDataPacket(String packetId) {
        return dataPacketCopyDao.getObjectWithReferences(packetId);
    }


    private String makeDataPacketBufId(DataPacketCopy dataPacket, Map<String, Object> paramsMap) {
        String dateString = DatetimeOpt.convertTimestampToString(dataPacket.getRecordDate());
        String params = JSON.toJSONString(paramsMap, SerializerFeature.MapSortField);
        StringBuffer temp;
        temp = new StringBuffer("packet:");
        temp.append(dataPacket.getPacketId())
            .append(":")
            .append(params)
            .append(dateString);
        return Md5Encoder.encode(temp.toString());
    }

    @Override
    public Object fetchDataPacketDataFromBuf(DataPacketCopy dataPacket, Map<String, Object> paramsMap) {
        if (jedisPool == null || dataPacket.getBufferFreshPeriod()==null) {
            return null;
        }
        String key = makeDataPacketBufId(dataPacket, paramsMap);
        Object object = null;
        if (dataPacket.getBufferFreshPeriod() >= 0) {
            Jedis jedis = jedisPool.getResource();
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

    @Override
    public void setDataPacketBuf(Object bizModel, DataPacketCopy dataPacket, Map<String, Object> paramsMap) {
        if (jedisPool == null || dataPacket.getBufferFreshPeriod() == null) {
            return;
        }
        String key = makeDataPacketBufId(dataPacket, paramsMap);
        Jedis jedis = jedisPool.getResource();
        if (jedis.get(key.getBytes()) == null || (jedis.get(key.getBytes()).length == 0)) {
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bos);
                oos.writeObject(bizModel);

                byte[] byt = bos.toByteArray();
                jedis.set(key.getBytes(), byt);
                int seconds;
                if (dataPacket.getBufferFreshPeriod() == Constant.ONE) {
                    //一日
                    seconds = 24 * 3600;
                    jedis.expire(key.getBytes(), seconds);
                } else if (dataPacket.getBufferFreshPeriod() == Constant.TWO) {
                    //按周
                    seconds = DatetimeOpt.calcSpanDays(new Date(), DatetimeOpt.seekEndOfWeek(new Date())) * 24 * 3600;
                    jedis.expire(key.getBytes(), seconds);
                } else if (dataPacket.getBufferFreshPeriod() == Constant.THREE) {
                    //按月
                    seconds = DatetimeOpt.calcSpanDays(new Date(), DatetimeOpt.seekEndOfMonth(new Date())) * 24 * 3600;
                    jedis.expire(key.getBytes(), seconds);
                } else if (dataPacket.getBufferFreshPeriod() == Constant.FOUR) {
                    //按年
                    seconds = DatetimeOpt.calcSpanDays(new Date(), DatetimeOpt.seekEndOfYear(new Date())) * 24 * 3600;
                    jedis.expire(key.getBytes(), seconds);
                } else if (dataPacket.getBufferFreshPeriod() >= Constant.SIXTY) {
                    //按秒
                    jedis.expire(key.getBytes(), dataPacket.getBufferFreshPeriod());
                }
                bos.close();
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        jedis.close();
    }

}
