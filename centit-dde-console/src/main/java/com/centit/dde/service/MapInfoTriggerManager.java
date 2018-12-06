package com.centit.dde.service;

import com.centit.dde.po.MapInfoTrigger;
import com.centit.framework.jdbc.service.BaseEntityManager;

import java.io.Serializable;
import java.util.List;

public interface MapInfoTriggerManager extends BaseEntityManager<MapInfoTrigger, Serializable> {
    List<MapInfoTrigger> listTrigger(Long mapinfoId);

    Long getTriggerId();

    MapInfoTrigger getObjectById(MapInfoTrigger mapInfoTrigger);
}
