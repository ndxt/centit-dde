package com.centit.dde.services;

import com.centit.dde.po.DataPacket;
import com.centit.support.database.utils.PageDesc;

import java.util.List;
import java.util.Map;

/**
 * @author zhf
 */
public interface DataPacketService {

    void createDataPacket(DataPacket dataPacket);

    void updateDataPacket(DataPacket dataPacket);

    void updateDataPacketOptJson(String packetId, String dataPacketOptJson);

    void deleteDataPacket(String packetId);

    List<DataPacket> listDataPacket(Map<String, Object> params, PageDesc pageDesc);
    List<Map<String,String>> listDataPacket(String optId);

    DataPacket getDataPacket(String packetId);

    void publishDataPacket(DataPacket dataPacket);

    int[] batchUpdateOptIdByApiId(String optId, List<String> apiIds);

}
