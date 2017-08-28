package com.centit.dde.service;

import java.util.List;
import java.util.Map;


import com.centit.dde.po.MapInfoDetail;
import com.centit.dde.po.MapInfoDetailId;
import com.centit.framework.hibernate.service.BaseEntityManager;
import com.centit.framework.ip.po.DatabaseInfo;

public interface MapInfoDetailManager extends BaseEntityManager<MapInfoDetail,MapInfoDetailId> {
    public List<Map<String, String>> getGoalTableStruct(DatabaseInfo DatabaseInfo, String tableName);

    public List<Map<String, String>> getSourceTableStruct(DatabaseInfo DatabaseInfo, String tableName);

    public List<String> getTables(DatabaseInfo databaseInfo);

    public void deleteMapinfoDetails(Long mapinfoId);

    public void updateExchangeMapinfo(Long mapinfoId, String soueceTableName, String goalTableName, String createSql);

    public List<Map<String, String>> getSourceTableStructFromDatabase(Long mapinfoId);

    public List<Map<String, String>> getGoalTableStructFromDatabase(Long mapinfoId);

    public void updateSourceColumnSentence(Map<String, Object> structs, String mapinfoId);

    public List<Object> getTable(DatabaseInfo databaseInfo);

    public Long getMapinfoId();

    public List<String> getGoalColumnStrut(Long mapinfoId);

    public void saveMapinfoDetails(MapInfoDetail mapInfoDetail);
    
    public List<MapInfoDetail> listByMapinfoId(Long mapinfoId);
    
}
