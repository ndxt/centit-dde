package com.centit.dde.adapter.dao;

import com.centit.dde.adapter.po.DataPacket;
import com.centit.support.database.utils.PageDesc;

import java.util.List;
import java.util.Map;

/**
 * @author codefan@sina.com
 */

public interface DataPacketDao {

    void saveNewObject(DataPacket dataPacket);

    int saveObjectReferences(DataPacket dataPacket);

    int mergeObject(DataPacket dataPacket);

    int updateObject(String[] fields, DataPacket dataPacket);

    int deleteObjectReferences(DataPacket dataPacket);

    int deleteObjectById(Object dataPacket);

    DataPacket getObjectById(Object packetId);

    void publishDataPacket(DataPacket dataPacket);

    DataPacket getObjectWithReferences(Object packetId);

    List<DataPacket> listObjectsByProperties(Map<String, Object> params, PageDesc pageDesc);

    List<DataPacket> listObjectsByProperties(Map<String, Object> params);

    void updateDataPacketOptJson(String packetId, String dataPacketOptJson);

    int[] batchUpdateOptIdByApiId(String optId, List<String> apiIds);

    void updateDisableStatus(String packetId,String disable);

    void batchDeleteByPacketIds(String[] packetIds);

    void updatePublishPackedLogLevel(int logLevel, String  packetId);

    void updatePackedLogLevel(int logLevel, List<String>  packetIds);

    void updateApplicationLogLevel(int logLevel, String osId);

    int clearTrashStand(String osId);
}
