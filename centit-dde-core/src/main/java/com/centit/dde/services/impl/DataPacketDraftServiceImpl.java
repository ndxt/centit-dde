package com.centit.dde.services.impl;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.dao.DataPacketDraftDao;
import com.centit.dde.po.DataPacket;
import com.centit.dde.po.DataPacketDraft;
import com.centit.dde.po.DataPacketParam;
import com.centit.dde.services.DataPacketDraftService;
import com.centit.dde.services.DataPacketService;
import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import com.centit.framework.model.adapter.PlatformEnvironment;
import com.centit.framework.model.basedata.IOptMethod;
import com.centit.framework.system.po.OptMethod;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.database.utils.PageDesc;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zhf
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DataPacketDraftServiceImpl implements DataPacketDraftService {
    private final static String OPTMETHOD_OPTTYPE_API = "A";
    @Autowired
    private DataPacketDraftDao dataPacketCopyDao;

    @Autowired
    private DataPacketService dataPacketService;
    @Autowired(required = false)
    private PlatformEnvironment platformEnvironment;

    public DataPacketDraftServiceImpl() {

    }

    @Override
    public void createDataPacket(DataPacketDraft dataPacketCopy) {
        dataPacketCopyDao.saveNewObject(dataPacketCopy);
        dataPacketCopyDao.saveObjectReferences(dataPacketCopy);
        dataPacketCopy.setOptMethod(creatApiOptMethod(dataPacketCopy));
    }

    private IOptMethod creatApiOptMethod(DataPacketDraft dataPacket) {
        if (platformEnvironment != null) {
            OptMethod result = assemblyOptMethodGet(dataPacket);
            return platformEnvironment.addOptMethod(result);
        }
        return null;
    }

    private OptMethod assemblyOptMethodGet(DataPacketDraft dataPacket) {
        OptMethod result = new OptMethod();
        result.setOptId(dataPacket.getOptId());
        result.setOptName(dataPacket.getPacketName());
        result.setApiId(dataPacket.getPacketId());
        result.setOptMethod("api");
        result.setOptUrl("/dde/run/" + dataPacket.getPacketId());
        result.setOptReq("CR");
        result.setOptDesc("请求api网关接口");
        result.setOptType(OPTMETHOD_OPTTYPE_API);
        return result;
    }


    @Override
    public void updateDataPacket(DataPacketDraft dataPacketCopy) {
        dataPacketCopyDao.updateObject(dataPacketCopy);
        dataPacketCopyDao.saveObjectReferences(dataPacketCopy);
    }


    @Override
    public void publishDataPacket(DataPacketDraft dataPacketCopy) {
        DataPacket dataPacket = new DataPacket();
        BeanUtils.copyProperties(dataPacketCopy, dataPacket);
        List<DataPacketParam> dataPacketParamList = new ArrayList<>();
        BeanUtils.copyProperties(dataPacketCopy.getPacketParams(), dataPacketParamList);
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
