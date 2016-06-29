package com.centit.dde.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.centit.core.service.BaseEntityManagerImpl;
import com.centit.dde.dao.ExportFieldDao;
import com.centit.dde.po.ExportField;
import com.centit.dde.service.ExportFieldManager;

public class ExportFieldManagerImpl extends BaseEntityManagerImpl<ExportField>
        implements ExportFieldManager {
    private static final long serialVersionUID = 1L;
    public static final Log log = LogFactory.getLog(ExportFieldManager.class);

    //private static final SysOptLog sysOptLog = SysOptLogFactoryImpl.getSysOptLog();

    private ExportFieldDao exportFieldDao;

    public void setExportFieldDao(ExportFieldDao baseDao) {
        this.exportFieldDao = baseDao;
        setBaseDao(this.exportFieldDao);
    }

}

