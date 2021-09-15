package com.centit.dde.services.impl;

import com.centit.dde.core.BizOptFlow;
import com.centit.dde.dao.DataPacketDraftDao;
import com.centit.dde.po.DataPacket;
import com.centit.dde.po.DataPacketDraft;
import com.centit.dde.po.DataPacketParam;
import com.centit.dde.services.DataPacketDraftService;
import com.centit.dde.services.DataPacketService;
import com.centit.fileserver.common.FileStore;
import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.database.utils.PageDesc;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zhf
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DataPacketDraftServiceImpl implements DataPacketDraftService {
    @Autowired(required = false)
    private JedisPool jedisPool;

    @Autowired(required = false)
    private FileStore fileStore;

    @Autowired
    private DataPacketDraftDao dataPacketCopyDao;

    @Autowired
    private DataPacketService dataPacketService;

    @Autowired
    private BizOptFlow bizOptFlow;

    public DataPacketDraftServiceImpl() {

    }

    @Override
    public void createDataPacket(DataPacketDraft dataPacketCopy) {
        dataPacketCopyDao.saveNewObject(dataPacketCopy);
        dataPacketCopyDao.saveObjectReferences(dataPacketCopy);
    }

    @Override
    public void updateDataPacket(DataPacketDraft dataPacketCopy) {
        dataPacketCopyDao.updateObject(dataPacketCopy);
        dataPacketCopyDao.saveObjectReferences(dataPacketCopy);
    }


    @Override
    public void publishDataPacket(DataPacketDraft dataPacketCopy) {
        DataPacket dataPacket = new DataPacket();
        BeanUtils.copyProperties(dataPacketCopy,dataPacket);
        List<DataPacketParam> dataPacketParamList = new ArrayList<>();
        BeanUtils.copyProperties(dataPacketCopy.getPacketParams(),dataPacketParamList);
        dataPacket.setPacketParams(dataPacketParamList);
        dataPacketService.publishDataPacket(dataPacket);
    }


    @Override
    public void updateDataPacketOptJson(String packetId, String dataPacketOptJson) {
        DatabaseOptUtils.batchUpdateObject(dataPacketCopyDao, DataPacketDraft.class,
            CollectionsOpt.createHashMap("dataOptDescJson", dataPacketOptJson),
            CollectionsOpt.createHashMap("packetId", packetId)
        );
    }


    @Override
    public void deleteDataPacket(String packetId) {
        DataPacketDraft dataPacketCopy = dataPacketCopyDao.getObjectWithReferences(packetId);
        dataPacketCopyDao.deleteObjectById(packetId);
        dataPacketCopyDao.deleteObjectReferences(dataPacketCopy);
    }

    @Override
    public List<DataPacketDraft> listDataPacket(Map<String, Object> params, PageDesc pageDesc) {
        return dataPacketCopyDao.listObjectsByProperties(params, pageDesc);
    }

    @Override
    public DataPacketDraft getDataPacket(String packetId) {
        return dataPacketCopyDao.getObjectWithReferences(packetId);
    }

}
