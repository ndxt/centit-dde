package com.centit.dde.service.impl;

import com.centit.dde.dao.MapinfoTriggerDao;
import com.centit.dde.po.MapInfoTrigger;
import com.centit.dde.po.MapInfoTriggerId;
import com.centit.dde.service.MapInfoTriggerManager;
import com.centit.framework.hibernate.service.BaseEntityManagerImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;
@Service
public class MapInfoTriggerManagerImpl extends BaseEntityManagerImpl<MapInfoTrigger,MapInfoTriggerId,MapinfoTriggerDao>
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

