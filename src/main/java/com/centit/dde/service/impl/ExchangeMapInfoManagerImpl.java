package com.centit.dde.service.impl;

import com.centit.dde.dao.ExchangeMapInfoDao;
import com.centit.dde.dao.MapInfoTriggerDao;
import com.centit.dde.exception.SqlResolveException;
import com.centit.dde.po.ExchangeMapInfo;
import com.centit.dde.po.MapInfoDetail;
import com.centit.dde.po.MapInfoDetailId;
import com.centit.dde.po.MapInfoTrigger;
import com.centit.dde.service.ExchangeMapInfoManager;
import com.centit.dde.util.ConnPool;
import com.centit.dde.util.SQLUtils;
import com.centit.framework.core.dao.PageDesc;
import com.centit.framework.hibernate.dao.DatabaseOptUtils;
import com.centit.framework.hibernate.service.BaseEntityManagerImpl;
import com.centit.framework.staticsystem.po.DatabaseInfo;
import com.centit.framework.staticsystem.service.IntegrationEnvironment;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.database.QueryUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.sql.*;
import java.util.*;

@Service
public class ExchangeMapInfoManagerImpl
        extends BaseEntityManagerImpl<ExchangeMapInfo,Long, ExchangeMapInfoDao> implements
        ExchangeMapInfoManager {

    public static final Logger log = LoggerFactory.getLogger(ExchangeMapInfoManager.class);

    @Resource
    protected IntegrationEnvironment integrationEnvironment;

    @Resource
    private MapInfoTriggerDao mapInfoTriggerDao;

    private ExchangeMapInfoDao exchangeMapInfoDao;

    @Resource
    @NotNull
    public void setExchangeMapInfoDao(ExchangeMapInfoDao baseDao) {
        this.exchangeMapInfoDao = baseDao;
        setBaseDao(this.exchangeMapInfoDao);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<ExchangeMapInfo> listImportExchangeMapinfo(List<Long> mapinfoId) {
        return exchangeMapInfoDao.listImportExchangeMapinfo(mapinfoId);
    }

    private void validator(ExchangeMapInfo object) throws SqlResolveException {
        if (!StringUtils.hasText(object.getQuerySql())) {
            throw new SqlResolveException(10001);//源sql语句为空
        }
        object.setQuerySql(object.getQuerySql().toUpperCase());

        DatabaseInfo dbinfo = integrationEnvironment.getDatabaseInfo(object.getSourceDatabaseName());
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
    public void save(ExchangeMapInfo object) throws SqlResolveException {

        //判断交换名称的唯一性
        if(checkName(object.getMapInfoId(), object.getMapInfoName())){
            log.error("交换名称已存在");
            throw new SqlResolveException("交换名称"+object.getMapInfoName()+"已存在,请更换！");
        }

        validator(object);

        ExchangeMapInfo dbObject = exchangeMapInfoDao.getObjectById(object.getMapInfoId());
        if (null == dbObject) {
            object.setMapInfoId(exchangeMapInfoDao.getNextLongSequence());
            if(object.getMapInfoTriggers() != null){
                for(MapInfoTrigger trigger : object.getMapInfoTriggers()){
                    trigger.setTriggerId(mapInfoTriggerDao.getTriggerId());
                }
            }
            setFieldTriggerCid(object);
            saveNewObject(object);
        } else {
            dbObject.copyNotNullProperty(object);


            setFieldTriggerCid(object);
            dbObject.replaceMapInfoDetails(object.getMapInfoDetails());
            dbObject.replaceMapInfoTriggers(object.getMapInfoTriggers());

            updateObject(dbObject);
        }

    }

    private boolean checkName(Long id, String name) {
        StringBuilder sql = new StringBuilder();
        Object obj;
        sql.append("select count(*) as mapCount from D_EXCHANGE_MAPINFO t where t.mapinfo_name = ? ");
        if(id != null){
            sql.append("and t.mapinfo_id <> ?");
            obj  = DatabaseOptUtils.getSingleObjectBySql(baseDao, sql.toString(),
                    new Object[]{name, id} );
        }else{
            obj  = DatabaseOptUtils.getSingleObjectBySql(baseDao, sql.toString(),
                    new Object[]{name} );
        }
        Long uc = NumberBaseOpt.castObjectToLong(obj);
        return (uc!=null && uc>0);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<ExchangeMapInfo> listObjectExcludeUsed(Map<String, Object> filterMap, PageDesc pageDesc) {
        return exchangeMapInfoDao.listObjectExcludeUsed(filterMap, pageDesc);
    }

    private static void setFieldTriggerCid(ExchangeMapInfo object) {
        MapInfoDetail md = null;
        MapInfoTrigger mt = null;
        for (int i = 0; i < object.getMapInfoDetails().size(); i++) {
            md = object.getMapInfoDetails().get(i);
            md.setCid(new MapInfoDetailId(object.getMapInfoId(), (long) i));
        }
        for (int i = 0; i < object.getMapInfoTriggers().size(); i++) {
            mt = object.getMapInfoTriggers().get(i);
            mt.setTiggerOrder((long) i);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<MapInfoDetail> resolveSQL(DatabaseInfo databaseInfo, String sql) {
        List<MapInfoDetail> mapInfoDetails = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;

        List<String> list = SQLUtils.splitSqlByFields(sql);


        if (databaseInfo != null) {
            try {
                connection = ConnPool.getConn(databaseInfo);
                statement = connection.createStatement();

                DatabaseMetaData dbMeta = connection.getMetaData();
                ResultSet pk = dbMeta.getPrimaryKeys(null, null, list.get(2).trim());
                List<String> pks = new ArrayList<>();
                while(pk.next()){
                    pks.add(pk.getString("COLUMN_NAME"));
                }
                List<String> fields = SQLUtils.getSqlFileds(sql);
                rs = statement.executeQuery(sql);
                ResultSetMetaData rsmd = rs.getMetaData();
                for(int i = 1; i <= rsmd.getColumnCount(); i++){
                    String columnName = rsmd.getColumnName(i);
                    MapInfoDetail mapInfoDetail = new MapInfoDetail();
                    mapInfoDetail.setColumnNo((long)i);
                    mapInfoDetail.setSourceFieldName(columnName);
                    mapInfoDetail.setSourceFieldSentence(fields.size() != rsmd.getColumnCount() ?
                                                                            columnName : fields.get(i - 1));
                    mapInfoDetail.setSourceFieldType(rsmd.getColumnTypeName(i));

                    mapInfoDetail.setDestFieldName(columnName);
                    mapInfoDetail.setDestFieldType(rsmd.getColumnTypeName(i));
                    mapInfoDetail.setIsNull(ResultSetMetaData.columnNoNulls == rsmd.isNullable(i) ? "N" : "Y");
                    mapInfoDetail.setIsPk(pks.contains(columnName) ? "Y" : "N");

                    mapInfoDetails.add(mapInfoDetail);
                }
            }catch (SQLException e) {
                log.error(e.getMessage(),e);
            }
        }
        return mapInfoDetails;
    }
}
