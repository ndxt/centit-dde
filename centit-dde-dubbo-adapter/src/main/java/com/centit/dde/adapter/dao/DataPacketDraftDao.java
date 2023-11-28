package com.centit.dde.adapter.dao;

import com.alibaba.fastjson2.JSONArray;
import com.centit.dde.adapter.po.DataPacketDraft;
import com.centit.support.database.utils.PageDesc;

import java.util.List;
import java.util.Map;

/**
 * @author codefan@sina.com
 */

public interface DataPacketDraftDao {
    void saveNewObject(DataPacketDraft dataPacketCopy);

    int saveObjectReferences(DataPacketDraft dataPacketCopy);

    int updateObject(DataPacketDraft dataPacketCopy);

    int updateObject(String[] fields, DataPacketDraft dataPacketCopy);

    DataPacketDraft getObjectWithReferences(Object packetId);

    int deleteObjectReferences(DataPacketDraft dataPacketCopy);

    int deleteObjectById(Object packetId);

    JSONArray listDataPacketDraft(Map<String, Object> params, PageDesc pageDesc);

    //List<DataPacketDraft> listObjectsByProperties(Map<String, Object> params, PageDesc pageDesc);

    int[] batchUpdateOptIdByApiId(String optId, List<String> apiIds);

    void updateDisableStatus(String packetId, String disable);

    void batchDeleteByPacketIds(String[] packetIds);

    int clearTrashStand(String osId);

    void updateDataPacketOptJson(String packetId, String dataPacketOptJson);

    void publishDataPacket(String optCode, DataPacketDraft dataPacketCopy);
}
