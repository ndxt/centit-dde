package com.centit.dde.service;

import com.centit.dde.po.ImportField;
import com.centit.dde.po.ImportOpt;
import com.centit.framework.jdbc.service.BaseEntityManager;
import com.centit.framework.security.model.CentitUserDetails;

import java.util.List;

public interface ImportOptManager extends BaseEntityManager<ImportOpt,Long> {

    String getMapinfoName(Long mapinfoId);
    void saveObject(ImportOpt object, CentitUserDetails loginUser);
    List<ImportField> listFields(String databaseCode, String tableName);

    public ImportOpt getObjectById(Long importId);

    public void deleteObjectById(Long importId);
}
