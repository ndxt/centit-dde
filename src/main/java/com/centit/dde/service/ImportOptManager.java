package com.centit.dde.service;

import com.centit.dde.exception.SqlResolveException;
import com.centit.dde.po.ImportOpt;
import com.centit.framework.core.service.BaseEntityManager;
import com.centit.framework.model.basedata.IUserInfo;

public interface ImportOptManager extends BaseEntityManager<ImportOpt,Long> {

    void validator(ImportOpt object) throws SqlResolveException;
    public String getMapinfoName(Long mapinfoId);
    void saveObject(ImportOpt object, IUserInfo loginUser) throws SqlResolveException;

}
