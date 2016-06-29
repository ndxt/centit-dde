package com.centit.dde.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.centit.core.service.BaseEntityManagerImpl;
import com.centit.dde.dao.MapinfoDetailDao;
import com.centit.dde.po.DatabaseInfo;
import com.centit.dde.po.MapinfoDetail;
import com.centit.dde.service.MapinfoDetailManager;

public class MapinfoDetailManagerImpl extends BaseEntityManagerImpl<MapinfoDetail>
        implements MapinfoDetailManager {
    private static final long serialVersionUID = 1L;
    public static final Log log = LogFactory.getLog(MapinfoDetailManager.class);

    //private static final SysOptLog sysOptLog = SysOptLogFactoryImpl.getSysOptLog();

    private MapinfoDetailDao mapinfoDetailDao;

    public void setMapinfoDetailDao(MapinfoDetailDao baseDao) {
        this.mapinfoDetailDao = baseDao;
        setBaseDao(this.mapinfoDetailDao);
    }

    public List<Map<String, String>> getGoalTableStruct(DatabaseInfo DatabaseInfo, String tableName) {
        return mapinfoDetailDao.getGoalTableStruct(DatabaseInfo, tableName);
    }

    public List<Map<String, String>> getSourceTableStruct(DatabaseInfo DatabaseInfo, String tableName) {
        return mapinfoDetailDao.getSourceTableStruct(DatabaseInfo, tableName);
    }

    public List<String> getTables(DatabaseInfo databaseInfo, String dataBaseType) {
        return mapinfoDetailDao.getTables(databaseInfo, dataBaseType);
    }

    public List<Object> getTable(DatabaseInfo databaseInfo, String dataBaseType) {
        return mapinfoDetailDao.getTable(databaseInfo, dataBaseType);
    }

    public void deleteMapinfoDetails(Long mapinfoId) {
        mapinfoDetailDao.deleteMapinfoDetails(mapinfoId);
    }

    public void updateExchangeMapinfo(Long mapinfoId, String soueceTableName, String goalTableName, String createSql) {
        mapinfoDetailDao.updateExchangeMapinfo(mapinfoId, soueceTableName, goalTableName, createSql);
    }

    public List<Map<String, String>> getSourceTableStructFromDatabase(Long mapinfoId) {
        return mapinfoDetailDao.getSourceTableStructFromDatabase(mapinfoId);
    }

    public List<Map<String, String>> getGoalTableStructFromDatabase(Long mapinfoId) {
        return mapinfoDetailDao.getGoalTableStructFromDatabase(mapinfoId);
    }

    public void updateSourceColumnSentence(Map<String, Object> structs, String mapinfoId) {
        mapinfoDetailDao.updateSourceColumnSentence(structs, mapinfoId);
    }

    public Long getMapinfoId() {
        return mapinfoDetailDao.getMapinfoId();
    }

    public List<String> getGoalColumnStrut(Long mapinfoId) {
        return this.mapinfoDetailDao.getGoalColumnStrut(mapinfoId);
    }

    public void saveMapinfoDetails(MapinfoDetail mapinfoDetail) {
        this.mapinfoDetailDao.saveMapinfoDetails(mapinfoDetail);
    }

}

