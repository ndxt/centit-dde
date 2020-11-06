package com.centit.dde.services.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOptFlow;
import com.centit.dde.dao.DataPacketDao;
import com.centit.dde.po.DataPacket;
import com.centit.dde.services.DataPacketService;
import com.centit.dde.utils.Constant;
import com.centit.fileserver.common.FileStore;
import com.centit.framework.ip.service.IntegrationEnvironment;
import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.database.utils.PageDesc;
import com.centit.support.security.Md5Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author zhf
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DataPacketServiceImpl implements DataPacketService {
    @Autowired(required = false)
    private  JedisPool jedisPool;

    @Autowired(required = false)
    private  FileStore fileStore;

    @Autowired
    private DataPacketDao dataPacketDao;

    @Autowired
    private IntegrationEnvironment integrationEnvironment;

    @Autowired
    private BizOptFlow bizOptFlow;

    public DataPacketServiceImpl( ) {

    }

    @Override
    public void createDataPacket(DataPacket dataPacket) {
        dataPacketDao.saveNewObject(dataPacket);
        dataPacketDao.saveObjectReferences(dataPacket);
    }

    @Override
    public void updateDataPacket(DataPacket dataPacket) {
        dataPacketDao.updateObject(dataPacket);
        dataPacketDao.saveObjectReferences(dataPacket);
    }

    @Override
    public void updateDataPacketOptJson(String packetId, String dataPacketOptJson){
        DatabaseOptUtils.batchUpdateObject(dataPacketDao, DataPacket.class,
            CollectionsOpt.createHashMap("dataOptDescJson",dataPacketOptJson ),
            CollectionsOpt.createHashMap("packetId",packetId )
        );
    }



    @Override
    public void deleteDataPacket(String packetId) {
        DataPacket dataPacket = dataPacketDao.getObjectWithReferences(packetId);
        dataPacketDao.deleteObjectById(packetId);
        dataPacketDao.deleteObjectReferences(dataPacket);
    }

    @Override
    public List<DataPacket> listDataPacket(Map<String, Object> params, PageDesc pageDesc) {
        return dataPacketDao.listObjectsByProperties(params, pageDesc);
    }

    @Override
    public DataPacket getDataPacket(String packetId) {
        return dataPacketDao.getObjectWithReferences(packetId);
    }


    private String makeDataPacketBufId(DataPacket dataPacket, Map<String, Object>  paramsMap){
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

    private BizModel fetchDataPacketDataFromBuf(DataPacket dataPacket, Map<String, Object>  paramsMap){
        if(jedisPool==null){
            return null;
        }
        String key =makeDataPacketBufId(dataPacket, paramsMap);
        Object object = null;
        if (dataPacket.getBufferFreshPeriod() >= 0) {
            Jedis jedis = jedisPool.getResource();
            if ((jedis.get(key.getBytes()) != null) && (jedis.get(key.getBytes()).length>0)) {
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
                if (object instanceof BizModel) {
                    return (BizModel) object;
                }
            }
        }
        return null;
    }

    private void setDataPacketBuf(BizModel bizModel, DataPacket dataPacket, Map<String, Object>  paramsMap){
        if(jedisPool==null){
            return;
        }
        String key =makeDataPacketBufId(dataPacket, paramsMap);
        Jedis jedis = jedisPool.getResource();
        if (jedis.get(key.getBytes())==null || (jedis.get(key.getBytes()).length==0)) {
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bos);
                oos.writeObject(bizModel);

                byte[] byt=bos.toByteArray();
                jedis.set(key.getBytes(),byt);
                int seconds;
                if (dataPacket.getBufferFreshPeriod() == Constant.ONE) {
                    //一日
                    seconds = 24*3600;
                    jedis.expire(key.getBytes(),seconds);
                } else if (dataPacket.getBufferFreshPeriod() == Constant.TWO) {
                    //按周
                    seconds = DatetimeOpt.calcSpanDays(new Date(), DatetimeOpt.seekEndOfWeek(new Date()))*24*3600;
                    jedis.expire(key.getBytes(),seconds);
                } else if (dataPacket.getBufferFreshPeriod() == Constant.THREE) {
                    //按月
                    seconds = DatetimeOpt.calcSpanDays(new Date(), DatetimeOpt.seekEndOfMonth(new Date()))*24*3600;
                    jedis.expire(key.getBytes(),seconds);
                } else if (dataPacket.getBufferFreshPeriod() == Constant.FOUR) {
                    //按年
                    seconds = DatetimeOpt.calcSpanDays(new Date(), DatetimeOpt.seekEndOfYear(new Date()))*24*3600;
                    jedis.expire(key.getBytes(),seconds);
                } else if (dataPacket.getBufferFreshPeriod() >= Constant.SIXTY) {
                    //按秒
                    jedis.expire(key.getBytes(),dataPacket.getBufferFreshPeriod());
                }
                bos.close();
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        jedis.close();
    }

    @Override
    public BizModel fetchDataPacketData(String packetId, Map<String, Object> paramsMap){
        return getBizModel(packetId, paramsMap,null);
    }



    @Override
    public BizModel fetchDataPacketData(String packetId, Map<String, Object> paramsMap,JSONObject optsteps){
        return getBizModel(packetId, paramsMap, optsteps);
    }

    private BizModel getBizModel(String packetId, Map<String, Object> paramsMap, JSONObject optsteps) {
        DataPacket dataPacket = this.getDataPacket(packetId);
        Map<String, Object> modelTag = dataPacket.getPacketParamsValue();
        if (paramsMap != null && paramsMap.size() > 0) {
            modelTag.putAll(paramsMap);
        }
        BizModel bizModel = fetchDataPacketDataFromBuf(dataPacket, modelTag);

        if(optsteps==null){
            optsteps = dataPacket.getDataOptDescJson();
        }
        if(optsteps!=null) {
            bizModel = bizOptFlow.run(optsteps,null,modelTag);
        }
        setDataPacketBuf(bizModel, dataPacket, modelTag);
        return bizModel;
    }
}
