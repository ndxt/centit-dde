package com.centit.dde.vo;

import com.centit.dde.po.DataPacketInterface;
import com.centit.support.common.CachedMap;

public abstract class DataPacketCache {
    public static CachedMap<String, DataPacketInterface> dataPacketCachedMap;
    public static void evictCache(String dataPacketId) {
        dataPacketCachedMap.evictIdentifiedCache(dataPacketId);
    }
    public static void evictCache() {
        dataPacketCachedMap.evictCahce();
    }
}
