package com.centit.dde.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

import com.centit.dde.po.MapInfoTrigger;
import com.centit.dde.po.MapinfoTriggerId;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.centit.dde.dao.MapinfoTriggerDao;
import com.centit.dde.service.MapInfoTriggerManager;
import com.centit.framework.hibernate.service.BaseEntityManagerImpl;
@Service
public class MapInfoTriggerManagerImpl extends BaseEntityManagerImpl<MapInfoTrigger,MapinfoTriggerId,MapinfoTriggerDao>
        implements MapInfoTriggerManager {
    public static final Log log = LogFactory.getLog(MapInfoTriggerManager.class);

    //private static final SysOptLog sysOptLog = SysOptLogFactoryImpl.getSysOptLog();
    
    private MapinfoTriggerDao mapinfoTriggerDao;

    @Resource(name="mapinfoTriggerDao")
    @NotNull
    public void setMapinfoTriggerDao(MapinfoTriggerDao baseDao) {
        this.mapinfoTriggerDao = baseDao;
        setBaseDao(this.mapinfoTriggerDao);
    }

    public List<MapInfoTrigger> listTrigger(Long mapinfoId) {
        return this.mapinfoTriggerDao.listTriggerByMapinfoId(mapinfoId);
    }

    public Long getTriggerId() {
        return this.mapinfoTriggerDao.getTriggerId();
    }

}

