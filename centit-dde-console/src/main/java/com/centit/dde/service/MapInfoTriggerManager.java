package com.centit.dde.service;

import com.centit.dde.po.MapInfoTrigger;
import com.centit.dde.po.MapInfoTriggerId;
import com.centit.framework.jdbc.service.BaseEntityManager;

import java.util.List;

public interface MapInfoTriggerManager extends BaseEntityManager<MapInfoTrigger,MapInfoTriggerId> {
    public List<MapInfoTrigger> listTrigger(Long mapinfoId);

    public Long getTriggerId();
}
