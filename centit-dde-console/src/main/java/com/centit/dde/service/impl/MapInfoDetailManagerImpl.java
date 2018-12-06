package com.centit.dde.service.impl;

import com.centit.dde.dao.MapInfoDetailDao;
import com.centit.dde.po.MapInfoDetail;
import com.centit.dde.po.MapInfoDetailId;
import com.centit.dde.service.MapInfoDetailManager;
import com.centit.framework.ip.po.DatabaseInfo;
import com.centit.framework.jdbc.service.BaseEntityManagerImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
@Service
public class MapInfoDetailManagerImpl extends BaseEntityManagerImpl<MapInfoDetail,MapInfoDetailId, MapInfoDetailDao>
        implements MapInfoDetailManager {
    public static final Log log = LogFactory.getLog(MapInfoDetailManager.class);

    //private static final SysOptLog sysOptLog = SysOptLogFactoryImpl.getSysOptLog();
    
    private MapInfoDetailDao mapInfoDetailDao;

    @Resource
    @NotNull
    public void setMapInfoDetailDao(MapInfoDetailDao baseDao) {
        this.mapInfoDetailDao = baseDao;
        setBaseDao(this.mapInfoDetailDao);
    }

    
    public List<MapInfoDetail> listByMapinfoId(Long mapinfoId) {
        return mapInfoDetailDao.listByMapinfoId(mapinfoId);
    }
    
    public List<Map<String, String>> getGoalTableStruct(DatabaseInfo DatabaseInfo, String tableName) {
        return mapInfoDetailDao.getGoalTableStruct(DatabaseInfo, tableName);
    }

    public List<Map<String, String>> getSourceTableStruct(DatabaseInfo DatabaseInfo, String tableName) {
        return mapInfoDetailDao.getSourceTableStruct(DatabaseInfo, tableName);
    }

    public List<String> getTables(DatabaseInfo databaseInfo) {
        return mapInfoDetailDao.getTables(databaseInfo);
    }

    public List<Object> getTable(DatabaseInfo databaseInfo) {
        return mapInfoDetailDao.getTable(databaseInfo);
    }

    public void deleteMapinfoDetails(Long mapinfoId) {
        mapInfoDetailDao.deleteMapinfoDetails(mapinfoId);
    }

    public void updateExchangeMapinfo(Long mapinfoId, String soueceTableName, String goalTableName, String createSql) {
        mapInfoDetailDao.updateExchangeMapinfo(mapinfoId, soueceTableName, goalTableName, createSql);
    }

    public List<Map<String, String>> getSourceTableStructFromDatabase(Long mapinfoId) {
        return mapInfoDetailDao.getSourceTableStructFromDatabase(mapinfoId);
    }

    public List<Map<String, String>> getGoalTableStructFromDatabase(Long mapinfoId) {
        return mapInfoDetailDao.getGoalTableStructFromDatabase(mapinfoId);
    }

    public void updateSourceColumnSentence(Map<String, Object> structs, String mapinfoId) {
        mapInfoDetailDao.updateSourceColumnSentence(structs, mapinfoId);
    }

    public Long getMapinfoId() {
        return mapInfoDetailDao.getMapinfoId();
    }

    public List<String> getGoalColumnStrut(Long mapinfoId) {
        return this.mapInfoDetailDao.getGoalColumnStrut(mapinfoId);
    }

    public void saveMapinfoDetails(MapInfoDetail mapInfoDetail) {
        this.mapInfoDetailDao.saveMapinfoDetails(mapInfoDetail);
    }

}

