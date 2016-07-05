package com.centit.dde.service;

import java.util.List;
import java.util.Map;

import com.centit.dde.po.DatabaseInfo;
import com.centit.dde.po.MapinfoDetail;
import com.centit.framework.core.service.BaseEntityManager;

public interface MapinfoDetailManager extends BaseEntityManager<MapinfoDetail> {
    public List<Map<String, String>> getGoalTableStruct(DatabaseInfo DatabaseInfo, String tableName);

    public List<Map<String, String>> getSourceTableStruct(DatabaseInfo DatabaseInfo, String tableName);

    public List<String> getTables(DatabaseInfo databaseInfo, String dataBaseType);

    public void deleteMapinfoDetails(Long mapinfoId);

    public void updateExchangeMapinfo(Long mapinfoId, String soueceTableName, String goalTableName, String createSql);

    public List<Map<String, String>> getSourceTableStructFromDatabase(Long mapinfoId);

    public List<Map<String, String>> getGoalTableStructFromDatabase(Long mapinfoId);

    public void updateSourceColumnSentence(Map<String, Object> structs, String mapinfoId);

    public List<Object> getTable(DatabaseInfo databaseInfo, String dataBaseType);

    public Long getMapinfoId();

    public List<String> getGoalColumnStrut(Long mapinfoId);

    public void saveMapinfoDetails(MapinfoDetail mapinfoDetail);
}
