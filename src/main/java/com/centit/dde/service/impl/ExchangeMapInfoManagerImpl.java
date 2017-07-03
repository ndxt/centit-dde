package com.centit.dde.service.impl;

import com.centit.dde.dao.ExchangeMapinfoDao;
import com.centit.dde.exception.SqlResolveException;
import com.centit.dde.po.*;
import com.centit.dde.service.ExchangeMapInfoManager;
import com.centit.dde.util.ConnPool;
import com.centit.framework.core.dao.PageDesc;
import com.centit.framework.hibernate.service.BaseEntityManagerImpl;
import com.centit.framework.staticsystem.po.DatabaseInfo;
import com.centit.framework.staticsystem.service.StaticEnvironmentManager;
import com.centit.support.database.QueryUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.sql.*;
import java.util.*;

@Service
public class ExchangeMapInfoManagerImpl
        extends BaseEntityManagerImpl<ExchangeMapInfo,Long,ExchangeMapinfoDao> implements
        ExchangeMapInfoManager {

    public static final Log log = LogFactory.getLog(ExchangeMapInfoManager.class);

    @Resource
    protected StaticEnvironmentManager platformEnvironment;

    private ExchangeMapinfoDao exchangeMapinfoDao;

    @Resource(name = "exchangeMapinfoDao")
    @NotNull
    public void setExchangeMapinfoDao(ExchangeMapinfoDao baseDao) {
        this.exchangeMapinfoDao = baseDao;
        setBaseDao(this.exchangeMapinfoDao);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<ExchangeMapInfo> listImportExchangeMapinfo(List<Long> mapinfoId) {
        return exchangeMapinfoDao.listImportExchangeMapinfo(mapinfoId);
    }

    private void validator(ExchangeMapInfo object) throws SqlResolveException {
        if (!StringUtils.hasText(object.getQuerySql())) {
            throw new SqlResolveException(10001);
        }
        object.setQuerySql(object.getQuerySql().toUpperCase());

        DatabaseInfo dbinfo = platformEnvironment.getDatabaseInfo(object.getSourceDatabaseName());
        if (null == dbinfo) {
            throw new SqlResolveException(10002);
        }

        // 验证触发器的参数是否在字段中存在
        Set<String> params = new HashSet<>();
        Set<String> fields = new HashSet<>();

        String triggerSql = null;

        for (MapInfoTrigger et : object.getMapInfoTriggers()) {
            triggerSql = et.getTriggerSql();
            if (!StringUtils.hasText(triggerSql)) {
                throw new SqlResolveException(10003);
            }
            triggerSql = triggerSql.toUpperCase();
            et.setTriggerSql(triggerSql);

            List<String> names = QueryUtils.getSqlNamedParameters(triggerSql);
            if (1 == names.size()) {
                continue;
            }

            names.remove(names.size() - 1);

            params.addAll(names);
        }

        int pkNum = 0;
        String sourceFieldName = null;
        for (MapInfoDetail ef : object.getMapInfoDetails()) {
            sourceFieldName = ef.getSourceFieldName();
            if (!StringUtils.hasText(sourceFieldName)) {
                continue;
            }
            //源字段和常量不能同时为空
            if (!StringUtils.hasText(sourceFieldName) && !StringUtils.hasText(ef.getDestFieldDefault())) {
                throw new SqlResolveException(10003);
            }

            fields.add(sourceFieldName.toUpperCase());

            if ("1".equals(ef.getIsPk())) {
                pkNum++;
            }
        }

        // 未设置主键字段
        if (0 == pkNum) {
            throw new SqlResolveException(10004);
        }

        fields.add("TODAY");
        fields.add("SQL_ERROR_MSG");
        fields.add("SUCCEED_PIECES");
        fields.add("FAULT_PIECES");
        fields.add("SYNC_BEGIN_TIME");
        fields.add("SYNC_END_TIME");

        for (String param : params) {
            if (!fields.contains(param.toUpperCase())) {
                throw new SqlResolveException("触发器中参数名[" + param + "]不存在于字段名称中");
            }
        }

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void save(ExchangeMapInfo object) {
        object.setMapInfoName(object.getMapInfoName().trim());

        //判断交换名称的唯一性
        Map<String, Object> filterMap = new HashMap<>();
        filterMap.put("mapinfoNameEq", object.getMapInfoName());

        List<ExchangeMapInfo> listObjects = listObjects(filterMap);

        if (!CollectionUtils.isEmpty(listObjects)) {
            ExchangeMapInfo exchangeMapInfoDb = getObjectById(object.getMapInfoId());
            String message = "交换名称已存在";
            if (null == exchangeMapInfoDb) {
                log.error(message);
            } else {
                if (1 < listObjects.size() || !exchangeMapInfoDb.getMapInfoId().equals(listObjects.get(0).getMapInfoId())) {
                    log.error(message);
                }
            }
        }
        try {
            validator(object);
        }catch (SqlResolveException e){
            log.error("");
        }
        ExchangeMapInfo dbObject = exchangeMapinfoDao.getObjectById(object.getMapInfoId());
        if (null == dbObject) {
            object.setMapInfoId(exchangeMapinfoDao.getNextLongSequence());
            setFieldTriggerCid(object);
        } else {
            dbObject.copyNotNullProperty(object);

            dbObject.replaceMapinfoDetails(object.getMapInfoDetails());
            dbObject.replaceMapinfoTrrigers(object.getMapInfoTriggers());

            setFieldTriggerCid(dbObject);
            object=dbObject;
        }
        saveObject(object);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<ExchangeMapInfo> listObjectExcludeUsed(Map<String, Object> filterMap, PageDesc pageDesc) {
        return exchangeMapinfoDao.listObjectExcludeUsed(filterMap, pageDesc);
    }

    private static void setFieldTriggerCid(ExchangeMapInfo object) {
        MapInfoDetail md = null;
        MapInfoTrigger mt = null;
        for (int i = 0; i < object.getMapInfoDetails().size(); i++) {
            md = object.getMapInfoDetails().get(i);

            md.setCid(new MapinfoDetailId(object.getMapInfoId(), (long) i));
        }
        for (int i = 0; i < object.getMapInfoTriggers().size(); i++) {
            mt = object.getMapInfoTriggers().get(i);
            mt.setCid(new MapinfoTriggerId((long) i, object.getMapInfoId()));
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<MapInfoDetail> resolveSQL(DatabaseInfo databaseInfo, String sql) {
        List<MapInfoDetail> mapInfoDetails = null;
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;

        if (databaseInfo != null) {
            try {
                connection = ConnPool.getConn(databaseInfo);
                statement = connection.createStatement();

                rs = statement.executeQuery(sql);
                ResultSetMetaData rsmd = rs.getMetaData();
                for(int i = 0; i < rsmd.getColumnCount(); i++){
                    MapInfoDetail mapInfoDetail = new MapInfoDetail();
                    mapInfoDetail.getCid().setColumnNo((long)(i-1));
                    mapInfoDetail.setSoueceTableName(rsmd.getTableName(i));
                    mapInfoDetail.setSourceFieldName(rsmd.getColumnName(i));
                    mapInfoDetail.setSourceFieldSentence(rsmd.getColumnName(i));
                    mapInfoDetail.setSourceFieldType(rsmd.getColumnTypeName(i));
                    mapInfoDetails.add(mapInfoDetail);
                }
            }catch (SQLException e) {
                log.error("数据库连接出错");
            }
        }
        return mapInfoDetails;
    }

}
