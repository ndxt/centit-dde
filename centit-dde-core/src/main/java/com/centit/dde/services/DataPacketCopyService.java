package com.centit.dde.services;

import com.centit.dde.po.DataPacketCopy;
import com.centit.support.database.utils.PageDesc;

import java.util.List;
import java.util.Map;

/**
 * @author zhf
 */
public interface DataPacketCopyService {

    void createDataPacket(DataPacketCopy dataPacketCopy);

    void updateDataPacket(DataPacketCopy dataPacketCopy);

    void updateDataPacketOptJson(String packetId, String dataPacketOptJson);

    void deleteDataPacket(String packetId);

    List<DataPacketCopy> listDataPacket(Map<String, Object> params, PageDesc pageDesc);

    DataPacketCopy getDataPacket(String packetId);

    void publishDataPacket(DataPacketCopy dataPacketCopy);

}
