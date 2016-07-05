package com.centit.dde.service.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.centit.dde.dao.DatabaseInfoDao;
import com.centit.dde.po.DatabaseInfo;
import com.centit.dde.service.DatabaseInfoManager;
import com.centit.framework.hibernate.dao.DatabaseOptUtils;
import com.centit.framework.hibernate.service.BaseEntityManagerImpl;
@Service("databaseInfoManager")
@Transactional
public class DatabaseInfoManagerImpl extends BaseEntityManagerImpl<DatabaseInfo,String,DatabaseInfoDao>
        implements DatabaseInfoManager {

    //private static final SysOptLog sysOptLog = SysOptLogFactoryImpl.getSysOptLog();

    @Override
    @Resource(name = "databaseInfoDao") 
    public void setBaseDao(DatabaseInfoDao baseDao) {
        super.baseDao = baseDao;
    }

    public boolean connectionTest(DatabaseInfo databaseInfo) {
        return baseDao.connectionTest(databaseInfo);
    }

    public List<Object> listDatabase() {
        List<Object> database = baseDao.listDatabase();
        return database;
    }

	@Override
	public Serializable saveNewObject(DatabaseInfo databaseInfo) {
		if(null==databaseInfo.getDatabaseCode())
			databaseInfo.setDatabaseCode(getNextKey());
		return baseDao.saveNewObject(databaseInfo);
	}

	@Override
	public String getNextKey() {
		String dbCode=DatabaseOptUtils.getNextKeyBySequence(baseDao, "S_DATABASECODE", 10);
		return dbCode;
	}
	
	public void mergeObject(DatabaseInfo databaseInfo){
		if(null==databaseInfo.getDatabaseCode())
			databaseInfo.setDatabaseCode(getNextKey());
		baseDao.mergeObject(databaseInfo);
	}

	@Override
    @Cacheable(value="DBInfo",key="'databaseMap'")
    @Transactional(readOnly = true)
	public Map<String, DatabaseInfo> listObjectToDBRepo() {
		List<DatabaseInfo> dbList=baseDao.listObjects();
		Map<String, DatabaseInfo>dbmap=new HashMap<String, DatabaseInfo>();
		if(dbList!=null){
            for (DatabaseInfo db : dbList) {
            	dbmap.put(db.getDatabaseCode(),db);
            }
        }
		return dbmap;
	}
	
	
}

