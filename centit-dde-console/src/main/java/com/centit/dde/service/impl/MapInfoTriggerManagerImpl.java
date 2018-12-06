package com.centit.dde.service.impl;

import com.centit.dde.dao.MapInfoTriggerDao;
import com.centit.dde.po.MapInfoTrigger;
import com.centit.dde.po.MapInfoTriggerId;
import com.centit.dde.service.MapInfoTriggerManager;
import com.centit.framework.jdbc.service.BaseEntityManagerImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;
@Service
public class MapInfoTriggerManagerImpl extends BaseEntityManagerImpl<MapInfoTrigger,MapInfoTriggerId, MapInfoTriggerDao>
        implements MapInfoTriggerManager {
    public static final Log log = LogFactory.getLog(MapInfoTriggerManager.class);

    //private static final SysOptLog sysOptLog = SysOptLogFactoryImpl.getSysOptLog();
    
    private MapInfoTriggerDao mapInfoTriggerDao;

    @Resource
    @NotNull
    public void setMapInfoTriggerDao(MapInfoTriggerDao baseDao) {
        this.mapInfoTriggerDao = baseDao;
        setBaseDao(this.mapInfoTriggerDao);
    }

    public List<MapInfoTrigger> listTrigger(Long mapinfoId) {
        return this.mapInfoTriggerDao.listTriggerByMapinfoId(mapinfoId);
    }

    public Long getTriggerId() {
        return this.mapInfoTriggerDao.getTriggerId();
    }

}

