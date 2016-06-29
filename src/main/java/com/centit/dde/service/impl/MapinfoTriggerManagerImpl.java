package com.centit.dde.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.centit.core.service.BaseEntityManagerImpl;
import com.centit.dde.dao.MapinfoTriggerDao;
import com.centit.dde.po.MapinfoTrigger;
import com.centit.dde.service.MapinfoTriggerManager;

public class MapinfoTriggerManagerImpl extends BaseEntityManagerImpl<MapinfoTrigger>
        implements MapinfoTriggerManager {
    private static final long serialVersionUID = 1L;
    public static final Log log = LogFactory.getLog(MapinfoTriggerManager.class);

    //private static final SysOptLog sysOptLog = SysOptLogFactoryImpl.getSysOptLog();

    private MapinfoTriggerDao mapinfoTriggerDao;

    public void setMapinfoTriggerDao(MapinfoTriggerDao baseDao) {
        this.mapinfoTriggerDao = baseDao;
        setBaseDao(this.mapinfoTriggerDao);
    }

    public List<MapinfoTrigger> listTrigger(Long mapinfoId) {
        return this.mapinfoTriggerDao.listTriggerByMapinfoId(mapinfoId);
    }

    public Long getTriggerId() {
        return this.mapinfoTriggerDao.getTriggerId();
    }

}

