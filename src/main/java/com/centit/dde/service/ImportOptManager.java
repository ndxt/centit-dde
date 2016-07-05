package com.centit.dde.service;

import com.centit.dde.exception.SqlResolveException;
import com.centit.dde.po.ImportOpt;
import com.centit.framework.core.service.BaseEntityManager;

public interface ImportOptManager extends BaseEntityManager<ImportOpt> {

    void validator(ImportOpt object) throws SqlResolveException;
    public String getMapinfoName(Long mapinfoId);
    void saveObject(ImportOpt object, FUserDetail loginUser) throws SqlResolveException;

}
