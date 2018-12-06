package com.centit.dde.service.impl;

import com.centit.dde.dao.ExportFieldDao;
import com.centit.dde.po.ExportField;
import com.centit.dde.service.ExportFieldManager;
import com.centit.framework.jdbc.service.BaseEntityManagerImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Service
public class ExportFieldManagerImpl extends BaseEntityManagerImpl<ExportField, Serializable,ExportFieldDao>
        implements ExportFieldManager {
    public static final Log log = LogFactory.getLog(ExportFieldManager.class);

    //private static final SysOptLog sysOptLog = SysOptLogFactoryImpl.getSysOptLog();
    
    private ExportFieldDao exportFieldDao;
    
   
    @Resource(name = "exportFieldDao")
    @NotNull
    public void setExportFieldDao(ExportFieldDao baseDao) {
        this.exportFieldDao = baseDao;
        setBaseDao(this.exportFieldDao);
    }

}

