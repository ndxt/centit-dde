package com.centit.dde.service;


import com.centit.dde.exception.SqlResolveException;
import com.centit.dde.po.ImportField;
import com.centit.dde.po.ImportOpt;
import com.centit.framework.jdbc.service.BaseEntityManager;
import com.centit.framework.model.basedata.IUserInfo;

import java.util.List;

public interface ImportOptManager extends BaseEntityManager<ImportOpt,Long> {

    String getMapinfoName(Long mapinfoId);
    void saveObject(ImportOpt object, IUserInfo loginUser);
    List<ImportField> listFields(String databaseCode, String tableName) throws SqlResolveException;

    public ImportOpt getObjectById(Long importId);

    public void deleteObjectById(Long importId);
}
