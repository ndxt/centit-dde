package com.centit.dde.dao.json;

import com.centit.dde.adapter.dao.DataPacketDao;
import com.centit.dde.adapter.po.DataPacket;
import com.centit.support.database.utils.PageDesc;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author codefan@sina.com
 */
@Repository("dataPacketDao")
public class DataPacketDaoImpl implements DataPacketDao {

    @Override
    public void saveNewObject(DataPacket dataPacket) {

    }

    @Override
    public int saveObjectReferences(DataPacket dataPacket) {
        return 0;
    }

    @Override
    public int mergeObject(DataPacket dataPacket) {
        return 0;
    }

    @Override
    public int updateObject(String[] fields, DataPacket dataPacket) {
        return 0;
    }

    @Override
    public int deleteObjectReferences(DataPacket dataPacket) {
        return 0;
    }

    @Override
    public int deleteObjectById(Object dataPacket) {
        return 0;
    }

    @Override
    public DataPacket getObjectById(Object id) {
        return null;
    }

    @Override
    public void publishDataPacket(DataPacket dataPacket) {

    }

    @Override
    public DataPacket getObjectWithReferences(Object packetId) {
        return null;
    }

    @Override
    public List<DataPacket> listObjectsByProperties(Map<String, Object> params, PageDesc pageDesc) {
        return null;
    }

    @Override
    public List<DataPacket> listObjectsByProperties(Map<String, Object> params) {
        return null;
    }

    @Override
    public void updateDataPacketOptJson(String packetId, String dataPacketOptJson) {

    }

    @Override
    public int[] batchUpdateOptIdByApiId(String optId, List<String> apiIds) {
        return new int[0];
    }

    @Override
    public void updateDisableStatus(String packetId, String disable) {

    }

    @Override
    public void batchDeleteByPacketIds(String[] packetIds) {

    }

    @Override
    public void updatePackedLogLevel(int logLevel, List<String> packetIds) {

    }

    @Override
    public void updateApplicationLogLevel(int logLevel, String osId) {

    }

    @Override
    public int clearTrashStand(String osId) {
        return 0;
    }
}
