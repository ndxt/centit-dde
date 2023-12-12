package com.centit.dde.adapter;

import java.util.Map;

public interface DdeDubboTaskRun {
     Object runTask(String packetId, Map<String, Object> queryParams);

    void refreshCache(String packetId);
}
