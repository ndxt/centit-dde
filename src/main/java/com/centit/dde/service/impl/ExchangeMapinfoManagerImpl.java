package com.centit.dde.service.impl;

import com.centit.dde.dao.ExchangeMapinfoDao;
import com.centit.dde.exception.SqlResolveException;
import com.centit.dde.po.*;
import com.centit.dde.service.ExchangeMapinfoManager;
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
import java.util.*;

@Service
public class ExchangeMapinfoManagerImpl
        extends BaseEntityManagerImpl<ExchangeMapinfo,Long,ExchangeMapinfoDao> implements
        ExchangeMapinfoManager {

    public static final Log log = LogFactory.getLog(ExchangeMapinfoManager.class);

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
    public List<ExchangeMapinfo> listImportExchangeMapinfo(List<Long> mapinfoId) {
        return exchangeMapinfoDao.listImportExchangeMapinfo(mapinfoId);
    }

    private void validator(ExchangeMapinfo object) throws SqlResolveException {
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

        for (MapinfoTrigger et : object.getMapinfoTriggers()) {
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
        for (MapinfoDetail ef : object.getMapinfoDetails()) {
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
    public void save(ExchangeMapinfo object) {
        object.setMapinfoName(object.getMapinfoName().trim());

        //判断交换名称的唯一性
        Map<String, Object> filterMap = new HashMap<String, Object>();
        filterMap.put("mapinfoNameEq", object.getMapinfoName());

        List<ExchangeMapinfo> listObjects = listObjects(filterMap);

        if (!CollectionUtils.isEmpty(listObjects)) {
            ExchangeMapinfo exchangeMapinfoDb = getObjectById(object.getMapinfoId());
            String message = "交换名称已存在";
            if (null == exchangeMapinfoDb) {
                log.error(message);
            } else {
                if (1 < listObjects.size() || !exchangeMapinfoDb.getMapinfoId().equals(listObjects.get(0).getMapinfoId())) {
                    log.error(message);
                }
            }
        }
        try {
            validator(object);
        }catch (SqlResolveException e){
            log.error("");
        }
        ExchangeMapinfo dbObject = exchangeMapinfoDao.getObjectById(object.getMapinfoId());
        if (null == dbObject) {
            object.setMapinfoId(exchangeMapinfoDao.getNextLongSequence());
            setFieldTriggerCid(object);
        } else {
            dbObject.copyNotNullProperty(object);

            dbObject.replaceMapinfoDetails(object.getMapinfoDetails());
            dbObject.replaceMapinfoTrrigers(object.getMapinfoTriggers());

            setFieldTriggerCid(dbObject);
            object=dbObject;
        }
        saveObject(object);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<ExchangeMapinfo> listObjectExcludeUsed(Map<String, Object> filterMap, PageDesc pageDesc) {
        return exchangeMapinfoDao.listObjectExcludeUsed(filterMap, pageDesc);
    }

    private static void setFieldTriggerCid(ExchangeMapinfo object) {
        MapinfoDetail md = null;
        MapinfoTrigger mt = null;
        for (int i = 0; i < object.getMapinfoDetails().size(); i++) {
            md = object.getMapinfoDetails().get(i);

            md.setCid(new MapinfoDetailId(object.getMapinfoId(), (long) i));
        }
        for (int i = 0; i < object.getMapinfoTriggers().size(); i++) {
            mt = object.getMapinfoTriggers().get(i);
            mt.setCid(new MapinfoTriggerId((long) i, object.getMapinfoId()));
        }
    }

}
