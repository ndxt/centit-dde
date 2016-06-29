package com.centit.dde.service;

import java.util.List;

import com.centit.core.service.BaseEntityManager;
import com.centit.dde.po.DatabaseInfo;

public interface DatabaseInfoManager extends BaseEntityManager<DatabaseInfo> {
    public boolean connectionTest(DatabaseInfo databaseInfo);

    public List<Object> listDatabase();

}
