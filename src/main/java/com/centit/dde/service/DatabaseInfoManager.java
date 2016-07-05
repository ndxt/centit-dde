package com.centit.dde.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.centit.dde.po.DatabaseInfo;
import com.centit.framework.core.service.BaseEntityManager;

public interface DatabaseInfoManager extends BaseEntityManager<DatabaseInfo,String> {
    public boolean connectionTest(DatabaseInfo databaseInfo);

    public List<Object> listDatabase();
    public Serializable saveNewObject(DatabaseInfo databaseInfo);
    public void mergeObject(DatabaseInfo databaseInfo);
	public String getNextKey();

	public Map<String, DatabaseInfo> listObjectToDBRepo();
}
