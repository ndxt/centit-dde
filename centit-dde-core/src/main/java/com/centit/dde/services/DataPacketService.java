package com.centit.dde.services;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.po.DataPacket;
import com.centit.product.dataopt.core.BizModel;

import com.centit.support.database.utils.PageDesc;

import java.util.List;
import java.util.Map;

public interface DataPacketService {

    /**
     * 新增数据包
     */
    void createDataPacket(DataPacket dataPacket);

    void updateDataPacket(DataPacket dataPacket);

    void updateDataPacketOptJson(String packetId, String dataPacketOptJson);

    void deleteDataPacket(String packetId);

    List<DataPacket> listDataPacket(Map<String, Object> params, PageDesc pageDesc);

    DataPacket getDataPacket(String packetId);

    BizModel fetchDataPacketData(String packetId, Map<String, Object> params);
    BizModel fetchDataPacketData(String packetId, Map<String, Object> params, JSONObject optsteps);
}
