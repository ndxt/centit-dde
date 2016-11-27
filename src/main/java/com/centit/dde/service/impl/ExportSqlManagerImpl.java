package com.centit.dde.service.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.centit.framework.model.basedata.IUserInfo;
import com.centit.framework.staticsystem.service.StaticEnvironmentManager;
import com.centit.support.database.QueryUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import com.centit.dde.dao.ExportSqlDao;
import com.centit.dde.exception.SqlResolveException;
import com.centit.dde.po.ExportField;
import com.centit.dde.po.ExportFieldId;
import com.centit.dde.po.ExportSql;
import com.centit.dde.po.ExportTrigger;
import com.centit.dde.po.ExportTriggerId;
import com.centit.dde.service.ExportFieldManager;
import com.centit.dde.service.ExportSqlManager;
import com.centit.framework.hibernate.service.BaseEntityManagerImpl;
import com.centit.framework.staticsystem.po.DatabaseInfo;

import javax.annotation.Resource;

public class ExportSqlManagerImpl extends BaseEntityManagerImpl<ExportSql,Long,ExportSqlDao>
        implements ExportSqlManager {

    public static final Log log = LogFactory.getLog(ExportSqlManager.class);

    // private static final SysOptLog sysOptLog =
    // SysOptLogFactoryImpl.getSysOptLog();

    private ExportSqlDao exportSqlDao;

    private ExportFieldManager exportFieldManager;

    @Resource
    protected StaticEnvironmentManager platformEnvironment;

    public ExportFieldManager getExportFieldManager() {
        return exportFieldManager;
    }
    public String getMapinfoName(Long mapinfoId) {
        return this.exportSqlDao.getMapinfoName(mapinfoId);
    }
    public void setExportFieldManager(ExportFieldManager exportFieldManager) {
        this.exportFieldManager = exportFieldManager;
    }

    public void setExportSqlDao(ExportSqlDao baseDao) {
        this.exportSqlDao = baseDao;
        setBaseDao(this.exportSqlDao);
    }

    @Override
    public void validator(ExportSql object) throws SqlResolveException {
        if (!StringUtils.hasText(object.getQuerySql())) {
            throw new SqlResolveException(10001);
        }
        object.setQuerySql(object.getQuerySql().toUpperCase());

        DatabaseInfo dbinfo =  platformEnvironment.getDatabaseInfo(object.getSourceDatabaseName());
        if (null == dbinfo) {
            throw new SqlResolveException(10002);
        }

        // 验证sql有效性
        exportSqlDao.validateSql(object.getQuerySql(), dbinfo);

        // 验证触发器的参数是否在字段中存在
        Set<String> params = new HashSet<String>();
        Set<String> fields = new HashSet<String>();

        String triggerSql = null;

        for (ExportTrigger et : object.getExportTriggers()) {
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
        String fieldName = null;
        for (ExportField ef : object.getExportFields()) {
            fieldName = ef.getFieldName();

            if (!StringUtils.hasText(fieldName)) {
                throw new SqlResolveException(10003);
            }

            fields.add(fieldName.toUpperCase());

            if ("1".equals(ef.getIsPk())) {
                pkNum++;
            }
        }

        // 未设置主键字段
        if (0 == pkNum) {
            throw new SqlResolveException(10004);
        }

        for (String param : params) {
            if (!fields.contains(param.toUpperCase())) {
                throw new SqlResolveException("触发器中参数名[" + param + "]不存在于字段名称中");
            }
        }

    }

    @Override
    public void saveObject(ExportSql object, IUserInfo userDetail) throws SqlResolveException {
        DatabaseInfo dbinfo =  platformEnvironment.getDatabaseInfo(object.getSourceDatabaseName());
        object.setSourceOsId(dbinfo.getOsId());

        // save exportsql
        ExportSql dbObject = exportSqlDao.getObjectById(object.getExportId());
        if (null == dbObject) {
            object.setCreated(userDetail.getUserCode());
            object.setCreateTime(new Date());

            object.setExportId(exportSqlDao.getNextLongSequence());

            setExportFieldTriggerCid(object);
            saveObject(object);
        } else {
            dbObject.getExportFields().clear();
            dbObject.getExportTriggers().clear();

            exportSqlDao.flush();
            dbObject.setLastUpdateTime(new Date());

            dbObject.copyNotNullProperty(object);
            // copy database fields to convert fields

            setExportFieldTriggerCid(object);

            for (ExportField ef : object.getExportFields()) {
                dbObject.addExportField(ef);
            }

            for (ExportTrigger et : object.getExportTriggers()) {
                dbObject.addExportTrigger(et);
            }

            object = dbObject;

        }

        saveObject(object);

    }

    private void setExportFieldTriggerCid(ExportSql object) {
        ExportField ef = null;
        ExportTrigger er = null;
        for (int i = 0; i < object.getExportFields().size(); i++) {
            ef = object.getExportFields().get(i);

            ef.setCid(new ExportFieldId(object.getExportId(), (long) i));
        }
        for (int i = 0; i < object.getExportTriggers().size(); i++) {
            er = object.getExportTriggers().get(i);
            er.setCid(new ExportTriggerId((long) i, object.getExportId()));
        }
    }

    @Override
    public List<ExportField> listExportFieldsByQuerysql(ExportSql object) throws SqlResolveException {
        DatabaseInfo dbinfo =  platformEnvironment.getDatabaseInfo(object.getSourceDatabaseName());

        exportSqlDao.validateSql(object.getQuerySql(), dbinfo);

        return exportSqlDao.getTableMetadata(object, dbinfo).getExportFields();
    }

}
