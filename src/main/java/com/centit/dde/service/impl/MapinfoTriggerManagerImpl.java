package com.centit.dde.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

import com.centit.dde.po.MapinfoTriggerId;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.centit.dde.dao.MapinfoTriggerDao;
import com.centit.dde.po.MapinfoTrigger;
import com.centit.dde.service.MapinfoTriggerManager;
import com.centit.framework.hibernate.service.BaseEntityManagerImpl;
@Service
public class MapinfoTriggerManagerImpl extends BaseEntityManagerImpl<MapinfoTrigger,MapinfoTriggerId,MapinfoTriggerDao>
        implements MapinfoTriggerManager {
    public static final Log log = LogFactory.getLog(MapinfoTriggerManager.class);

    //private static final SysOptLog sysOptLog = SysOptLogFactoryImpl.getSysOptLog();
    @Resource(name="mapinfoTriggerDao")
    @NotNull
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

