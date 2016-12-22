package com.centit.dde.service.impl;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.centit.framework.model.basedata.IUserInfo;
import com.centit.framework.staticsystem.service.StaticEnvironmentManager;
import com.centit.support.database.QueryUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.centit.dde.dao.ImportOptDao;
import com.centit.dde.exception.SqlResolveException;
import com.centit.dde.po.ImportField;
import com.centit.dde.po.ImportFieldId;
import com.centit.dde.po.ImportOpt;
import com.centit.dde.po.ImportTrigger;
import com.centit.dde.po.ImportTriggerId;
import com.centit.dde.service.ImportOptManager;
import com.centit.framework.hibernate.service.BaseEntityManagerImpl;
import com.centit.framework.staticsystem.po.DatabaseInfo;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
@Service
public class ImportOptManagerImpl extends BaseEntityManagerImpl<ImportOpt,Long,ImportOptDao> implements ImportOptManager {

    public static final Log log = LogFactory.getLog(ImportOptManager.class);

    // private static final SysOptLog sysOptLog =
    // SysOptLogFactoryImpl.getSysOptLog();

    @Resource(name="importOptDao")
    @NotNull
    private ImportOptDao importOptDao;

    @Resource
    protected StaticEnvironmentManager platformEnvironment;

    @Override
    public String getMapinfoName(Long mapinfoId) {
        return this.importOptDao.getMapinfoName(mapinfoId);
    }

    @Override
    public void validator(ImportOpt object) throws SqlResolveException {
        DatabaseInfo dbinfo = platformEnvironment.getDatabaseInfo(object.getDestDatabaseName());
        if (null == dbinfo) {
            throw new SqlResolveException(10002);
        }

        // 验证触发器的参数是否在字段中存在
        Set<String> params = new HashSet<String>();
        Set<String> fields = new HashSet<String>();

        String triggerSql = null;

        for (ImportTrigger et : object.getImportTriggers()) {
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


        for (ImportField ef : object.getImportFields()) {

            sourceFieldName = ef.getSourceFieldName();
            if (!StringUtils.hasText(sourceFieldName)) {
                continue;
            }
            //源字段和常量不能同时为空
            if (!StringUtils.hasText(sourceFieldName) && !StringUtils.hasText(ef.getDestFieldDefault())) {
                throw new SqlResolveException(10003);
            }

            //目标字段为空
            if (!StringUtils.hasText(ef.getDestFieldName())) {
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
    public void saveObject(ImportOpt object, IUserInfo userDetail) throws SqlResolveException {
        // 判断导入的表是否存在

        DatabaseInfo databaseInfo = platformEnvironment.getDatabaseInfo(object.getDestDatabaseName());

        object.setSourceOsId(databaseInfo.getOsId());
        //将表名转换为大写
        object.setTableName(object.getTableName().toUpperCase());

        try {
            if (!importOptDao.isExistsForTable(object, databaseInfo)) {
                throw new SqlResolveException(20001);
            }
        } catch (SQLException e) {
            throw new SqlResolveException(20001, e);
        }

        // save exportsql
        ImportOpt dbObject = importOptDao.getObjectById(object.getImportId());
        if (null == dbObject) {
            object.setCreated(userDetail.getUserCode());
            object.setCreateTime(new Date());

            object.setImportId(importOptDao.getNextLongSequence());

            setImportFieldTriggerCid(object);
            saveObject(object);

            // importOptDao.flush();
        } else {
            dbObject.getImportFields().clear();
            dbObject.getImportTriggers().clear();
            importOptDao.flush();
            dbObject.setLastUpdateTime(new Date());

            dbObject.copyNotNullProperty(object);
            // copy database fields to convert fields

            setImportFieldTriggerCid(object);

            for (ImportField ef : object.getImportFields()) {
                dbObject.addImportField(ef);
            }
            for (ImportTrigger et : object.getImportTriggers()) {
                dbObject.addImportTrigger(et);
            }

            object = dbObject;

        }

        saveObject(object);

    }

    private void setImportFieldTriggerCid(ImportOpt object) {
        ImportField ef = null;
        ImportTrigger et = null;
        for (int i = 0; i < object.getImportFields().size(); i++) {
            ef = object.getImportFields().get(i);
            ef.setCid(new ImportFieldId((long) i, object.getImportId()));
        }
        for (int i = 0; i < object.getImportTriggers().size() && !CollectionUtils.isEmpty(object.getImportTriggers());
             i++) {
            et = object.getImportTriggers().get(i);
            et.setCid(new ImportTriggerId((long) i, object.getImportId()));
        }
    }
}
