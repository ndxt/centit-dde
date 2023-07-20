package com.centit.dde.services.impl;

import com.centit.dde.adapter.dao.DataPacketDao;
import com.centit.dde.adapter.po.DataPacket;
import com.centit.dde.services.DataPacketService;
import com.centit.framework.components.CodeRepositoryUtil;
import com.centit.framework.model.basedata.IOptInfo;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.database.utils.PageDesc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zc
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DataPacketServiceImpl implements DataPacketService {
    private final static String OPTINFO_FORMCODE_COMMON = "C";
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
        dataPacketDao.updateDataPacketOptJson(packetId, dataPacketOptJson);
    }


    @Override
    public void deleteDataPacket(String packetId) {
        DataPacket dataPacket = dataPacketDao.getObjectWithReferences(packetId);
        dataPacketDao.deleteObjectReferences(dataPacket);
        dataPacketDao.deleteObjectById(packetId);

    }

    @Override
    public List<DataPacket> listDataPacket(Map<String, Object> params, PageDesc pageDesc) {
        return dataPacketDao.listObjectsByProperties(params, pageDesc);
    }

    @Override
    public List<Map<String, String>> listDataPacket(String optId, String topUnit) {
        optId = getOptIdWithCommon(optId, topUnit);
        List<DataPacket> dataPacketList = dataPacketDao.listObjectsByProperties(CollectionsOpt.createHashMap("optids", optId));
        List<Map<String, String>> mapList = new ArrayList<>();
        for (DataPacket dataPacket : dataPacketList) {
            Map<String, String> map = new HashMap<>(1);
            map.put(dataPacket.getPacketId(), dataPacket.getPacketName());
            mapList.add(map);
        }
        return mapList;
    }

    private String getOptIdWithCommon(String optId, String topUnit) {
        IOptInfo commonOptInfo = CodeRepositoryUtil.getCommonOptId(topUnit, optId);
        if (commonOptInfo != null) {
            String commonOptId = commonOptInfo.getOptId();
            return StringBaseOpt.concat(optId, ",", commonOptId);
        }
        return optId;
    }

    @Override
    public DataPacket getDataPacket(String packetId) {
        return dataPacketDao.getObjectWithReferences(packetId);
    }


    @Override
    public void publishDataPacket(DataPacket dataPacket) {
        dataPacketDao.publishDataPacket(dataPacket);
    }

    @Override
    public int[] batchUpdateOptIdByApiId(String optId, List<String> apiIds) {
        return dataPacketDao.batchUpdateOptIdByApiId(optId, apiIds);
    }

    @Override
    public void updateDisableStatus(String packetId,String disable) {
        dataPacketDao.updateDisableStatus(packetId, disable);
    }

    @Override
    public void batchDeleteByPacketIds(String[] packetIds) {;
        dataPacketDao.batchDeleteByPacketIds(packetIds);
    }

    /**
     * @Column(name = "log_level")
     *     @ApiModelProperty(value = "日志记录级别，1=ERROR,3=INFO,7=DEBUG")
     * @param logLevel
     * @param packetIds
     */
    @Override
    public void updatePackedLogLevel(int logLevel, List<String>  packetIds){
        dataPacketDao.updatePackedLogLevel(logLevel, packetIds);
    }

    @Override
    public void updateApplicationLogLevel(int logLevel, String osId){
        dataPacketDao.updateApplicationLogLevel(logLevel, osId);
    }

    @Override
    public int clearTrashStand(String osId) {
        return dataPacketDao.clearTrashStand(osId);
    }
}
