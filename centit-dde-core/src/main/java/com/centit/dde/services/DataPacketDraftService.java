package com.centit.dde.services;

import com.alibaba.fastjson2.JSONArray;
import com.centit.dde.adapter.po.DataPacketDraft;
import com.centit.support.database.utils.PageDesc;

import java.util.List;
import java.util.Map;

/**
 * @author zhf
 */
public interface DataPacketDraftService {

    void createDataPacket(DataPacketDraft dataPacketCopy);

    void updateDataPacket(DataPacketDraft dataPacketCopy);

    void updateDataPacketOptJson(String packetId, String dataPacketOptJson);

    void deleteDataPacket(String packetId);

    JSONArray listDataPacketForList(Map<String, Object> params, PageDesc pageDesc);

    DataPacketDraft getDataPacket(String packetId);

    void publishDataPacket(DataPacketDraft dataPacketCopy);

    int[] batchUpdateOptIdByApiId(String optId, List<String> apiIds);

    void updateDisableStatus(String packetId, String disable);

    void batchDeleteByPacketIds(String[] packetIds);

    //清空回收站
    int clearTrashStand(String osId);

}
