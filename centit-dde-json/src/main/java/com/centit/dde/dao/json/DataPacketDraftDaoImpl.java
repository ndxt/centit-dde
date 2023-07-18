package com.centit.dde.dao.json;

import com.centit.dde.adapter.dao.DataPacketDraftDao;
import com.centit.dde.adapter.po.DataPacketDraft;
import com.centit.support.database.utils.PageDesc;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author codefan@sina.com
 */
@Repository("dataPacketDraftDao")
public class DataPacketDraftDaoImpl implements DataPacketDraftDao{

    @Override
    public void saveNewObject(DataPacketDraft dataPacketCopy) {

    }

    @Override
    public int saveObjectReferences(DataPacketDraft dataPacketCopy) {
        return 0;
    }

    @Override
    public int updateObject(DataPacketDraft dataPacketCopy) {
        return 0;
    }

    @Override
    public int updateObject(String[] fields, DataPacketDraft dataPacketCopy) {
        return 0;
    }

    @Override
    public DataPacketDraft getObjectWithReferences(Object packetId) {
        return null;
    }

    @Override
    public int deleteObjectReferences(DataPacketDraft dataPacketCopy) {
        return 0;
    }

    @Override
    public int deleteObjectById(Object packetId) {
        return 0;
    }

    @Override
    public List<DataPacketDraft> listObjectsByProperties(Map<String, Object> params, PageDesc pageDesc) {
        return null;
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
    public int clearTrashStand(String osId) {
        return 0;
    }

    @Override
    public void updateDataPacketOptJson(String packetId, String dataPacketOptJson) {

    }

    @Override
    public void publishDataPacket(String optCode, DataPacketDraft dataPacketCopy) {

    }
}
