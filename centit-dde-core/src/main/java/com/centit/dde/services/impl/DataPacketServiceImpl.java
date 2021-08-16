package com.centit.dde.services.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.centit.dde.dao.DataPacketDao;
import com.centit.dde.po.DataPacket;
import com.centit.dde.po.DataPacketInterface;
import com.centit.dde.services.DataPacketService;
import com.centit.dde.utils.ConstantValue;
import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.algorithm.NumberBaseOpt;
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
 * @author zc
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DataPacketServiceImpl implements DataPacketService {
    @Autowired(required = false)
    private JedisPool jedisPool;

    @Autowired
    private DataPacketDao dataPacketDao;


    @Override
    public void createDataPacket(DataPacket dataPacket) {
        dataPacketDao.saveNewObject(dataPacket);
        dataPacketDao.saveObjectReferences(dataPacket);
    }

    @Override
    public void updateDataPacket(DataPacket dataPacket) {
        dataPacketDao.mergeObject(dataPacket);
        dataPacketDao.saveObjectReferences(dataPacket);
    }

    @Override
    public void updateDataPacketOptJson(String packetId, String dataPacketOptJson) {
        DatabaseOptUtils.batchUpdateObject(dataPacketDao, DataPacket.class,
            CollectionsOpt.createHashMap("dataOptDescJson", dataPacketOptJson),
            CollectionsOpt.createHashMap("packetId", packetId)
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



    @Override
    public void publishDataPacket(DataPacket dataPacket) {
        dataPacketDao.mergeObject(dataPacket);
        dataPacketDao.saveObjectReferences(dataPacket);
    }


}
