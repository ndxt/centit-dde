package com.centit.dde.services;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.po.DataPacket;
import com.centit.framework.system.po.OptMethod;
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

    DataPacket getDataPacket(String packetId);

    void publishDataPacket(DataPacket dataPacket);

    int[] batchUpdateOptIdByApiId(String optId, List<String> apiIds);

}
