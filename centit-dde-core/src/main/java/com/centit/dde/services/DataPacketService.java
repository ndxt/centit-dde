package com.centit.dde.services;

import com.centit.dde.po.DataPacket;
import com.centit.support.database.utils.PageDesc;

import java.util.List;
import java.util.Map;

/**
 * @author zhf
 */
public interface DataPacketService {

    void deleteDataPacket(String packetId);

    List<DataPacket> listDataPacket(Map<String, Object> params, PageDesc pageDesc);

    void releaseDataPacket(DataPacket dataPacket);

}
