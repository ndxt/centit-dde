package com.centit.dde.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.centit.core.service.BaseEntityManagerImpl;
import com.centit.dde.dao.OsInfoDao;
import com.centit.dde.po.OsInfo;
import com.centit.dde.service.OsInfoManager;

public class OsInfoManagerImpl extends BaseEntityManagerImpl<OsInfo>
        implements OsInfoManager {
    private static final long serialVersionUID = 1L;
    public static final Log log = LogFactory.getLog(OsInfoManager.class);

    //private static final SysOptLog sysOptLog = SysOptLogFactoryImpl.getSysOptLog();

    private OsInfoDao osInfoDao;

    public void setOsInfoDao(OsInfoDao baseDao) {
        this.osInfoDao = baseDao;
        setBaseDao(this.osInfoDao);
    }

}

