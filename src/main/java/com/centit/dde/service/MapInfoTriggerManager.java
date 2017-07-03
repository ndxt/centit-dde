package com.centit.dde.service;

import java.util.List;


import com.centit.dde.po.MapInfoTrigger;
import com.centit.dde.po.MapinfoTriggerId;
import com.centit.framework.core.service.BaseEntityManager;

public interface MapInfoTriggerManager extends BaseEntityManager<MapInfoTrigger,MapinfoTriggerId> {
    public List<MapInfoTrigger> listTrigger(Long mapinfoId);

    public Long getTriggerId();
}
