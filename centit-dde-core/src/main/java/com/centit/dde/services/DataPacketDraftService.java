package com.centit.dde.services;

import com.centit.dde.po.DataPacketDraft;
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

    List<DataPacketDraft> listDataPacket(Map<String, Object> params, PageDesc pageDesc);

    DataPacketDraft getDataPacket(String packetId);

    void publishDataPacket(DataPacketDraft dataPacketCopy);

    int[] batchUpdateOptIdByApiId(String optId,List<String> apiIds);

    void updateDisableStatus(String packetId,String disable);

}
