package com.centit.dde.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.centit.core.service.BaseEntityManagerImpl;
import com.centit.dde.dao.DatabaseInfoDao;
import com.centit.dde.po.DatabaseInfo;
import com.centit.dde.service.DatabaseInfoManager;

public class DatabaseInfoManagerImpl extends BaseEntityManagerImpl<DatabaseInfo>
        implements DatabaseInfoManager {
    private static final long serialVersionUID = 1L;
    public static final Log log = LogFactory.getLog(DatabaseInfoManager.class);

    //private static final SysOptLog sysOptLog = SysOptLogFactoryImpl.getSysOptLog();

    private DatabaseInfoDao databaseInfoDao;

    public void setDatabaseInfoDao(DatabaseInfoDao baseDao) {
        this.databaseInfoDao = baseDao;
        setBaseDao(this.databaseInfoDao);
    }

    public boolean connectionTest(DatabaseInfo databaseInfo) {
        return this.databaseInfoDao.connectionTest(databaseInfo);
    }

    public List<Object> listDatabase() {
        List<Object> database = databaseInfoDao.listDatabase();
        return database;
    }
}

