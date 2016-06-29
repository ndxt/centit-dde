package com.centit.dde.service;

import java.util.List;

import com.centit.core.service.BaseEntityManager;
import com.centit.dde.po.MapinfoTrigger;

public interface MapinfoTriggerManager extends BaseEntityManager<MapinfoTrigger> {
    public List<MapinfoTrigger> listTrigger(Long mapinfoId);

    public Long getTriggerId();
}
