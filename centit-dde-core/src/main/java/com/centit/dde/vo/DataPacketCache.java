package com.centit.dde.vo;

import com.centit.dde.po.DataPacket;
import com.centit.support.common.CachedMap;

public abstract class DataPacketCache {
    public static CachedMap<String, DataPacket> dataPacketCachedMap;
    public static void evictCache(String dataPacketId) {
        dataPacketCachedMap.evictIdentifiedCache(dataPacketId);
    }
    public static void evictCache() {
        dataPacketCachedMap.evictCache();
    }
}
