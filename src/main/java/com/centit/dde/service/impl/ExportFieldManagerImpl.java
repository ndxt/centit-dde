package com.centit.dde.service.impl;

import com.centit.dde.po.ExportFieldId;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.centit.dde.dao.ExportFieldDao;
import com.centit.dde.po.ExportField;
import com.centit.dde.service.ExportFieldManager;
import com.centit.framework.hibernate.service.BaseEntityManagerImpl;

public class ExportFieldManagerImpl extends BaseEntityManagerImpl<ExportField,ExportFieldId,ExportFieldDao>
        implements ExportFieldManager {
    public static final Log log = LogFactory.getLog(ExportFieldManager.class);

    //private static final SysOptLog sysOptLog = SysOptLogFactoryImpl.getSysOptLog();

    private ExportFieldDao exportFieldDao;

    public void setExportFieldDao(ExportFieldDao baseDao) {
        this.exportFieldDao = baseDao;
        setBaseDao(this.exportFieldDao);
    }

}

