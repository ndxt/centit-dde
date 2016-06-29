package com.centit.dde.service;

import java.util.List;

import com.centit.dde.po.DatabaseInfo;
import com.centit.framework.core.service.BaseEntityManager;

public interface DatabaseInfoManager extends BaseEntityManager<DatabaseInfo> {
    public boolean connectionTest(DatabaseInfo databaseInfo);

    public List<Object> listDatabase();

}
