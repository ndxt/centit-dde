package com.centit.dde.service;

import java.util.List;


import com.centit.dde.po.MapinfoTrigger;
import com.centit.dde.po.MapinfoTriggerId;
import com.centit.framework.core.service.BaseEntityManager;

public interface MapinfoTriggerManager extends BaseEntityManager<MapinfoTrigger,MapinfoTriggerId> {
    public List<MapinfoTrigger> listTrigger(Long mapinfoId);

    public Long getTriggerId();
}
