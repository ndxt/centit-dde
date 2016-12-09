package com.centit.dde.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

import com.alibaba.fastjson.JSONArray;
import com.centit.dde.po.MapinfoDetailId;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.centit.dde.dao.MapinfoDetailDao;
import com.centit.dde.po.MapinfoDetail;
import com.centit.dde.service.MapinfoDetailManager;
import com.centit.framework.hibernate.service.BaseEntityManagerImpl;
import com.centit.framework.staticsystem.po.DatabaseInfo;
@Service
public class MapinfoDetailManagerImpl extends BaseEntityManagerImpl<MapinfoDetail,MapinfoDetailId,MapinfoDetailDao>
        implements MapinfoDetailManager {
    public static final Log log = LogFactory.getLog(MapinfoDetailManager.class);

    //private static final SysOptLog sysOptLog = SysOptLogFactoryImpl.getSysOptLog();
    @Resource(name="mapinfoDetailDao")
    @NotNull
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

    public JSONArray getSourceTableStructFromDatabase(Long mapinfoId) {
        return mapinfoDetailDao.getSourceTableStructFromDatabase(mapinfoId);
    }

    public JSONArray getGoalTableStructFromDatabase(Long mapinfoId) {
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

