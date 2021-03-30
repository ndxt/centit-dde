package com.centit.dde.services.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.centit.dde.core.BizOptFlow;
import com.centit.dde.dao.DataPacketCopyDao;
import com.centit.dde.dao.DataPacketDao;
import com.centit.dde.po.DataPacket;
import com.centit.dde.po.DataPacketCopy;
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

import javax.annotation.Resource;
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

    @Autowired
    private DataPacketDao dataPacketDao;


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
    public void releaseDataPacket(DataPacket dataPacket) {
        dataPacketDao.mergeObject(dataPacket);
        dataPacketDao.saveObjectReferences(dataPacket);
    }

}
