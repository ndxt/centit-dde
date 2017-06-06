package com.centit.dde.service.impl;

import com.centit.dde.dao.ExportSqlDao;
import com.centit.dde.exception.SqlResolveException;
import com.centit.dde.po.*;
import com.centit.dde.service.ExportFieldManager;
import com.centit.dde.service.ExportSqlManager;
import com.centit.framework.hibernate.service.BaseEntityManagerImpl;
import com.centit.framework.model.basedata.IUserInfo;
import com.centit.framework.security.model.CentitUserDetails;
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
public class ExportSqlManagerImpl extends BaseEntityManagerImpl<ExportSql,Long,ExportSqlDao>
        implements ExportSqlManager {

    public static final Log log = LogFactory.getLog(ExportSqlManager.class);

    // private static final SysOptLog sysOptLog =
    // SysOptLogFactoryImpl.getSysOptLog();
    
    private ExportSqlDao exportSqlDao;

    @Resource
    private ExportFieldManager exportFieldManager;

    @Resource
    protected StaticEnvironmentManager platformEnvironment;
    
    public String getMapinfoName(Long mapinfoId) {
        return this.exportSqlDao.getMapinfoName(mapinfoId);
    }


    @Resource(name="exportSqlDao")
    @NotNull
    public void setExportSqlDao(ExportSqlDao baseDao) {
        this.exportSqlDao = baseDao;
        setBaseDao(this.exportSqlDao);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void validator(ExportSql object) throws SqlResolveException {
        if (!StringUtils.hasText(object.getQuerySql())) {
            throw new SqlResolveException(10001);
        }
        object.setQuerySql(object.getQuerySql().toUpperCase());

        DatabaseInfo dbInfo =  platformEnvironment.getDatabaseInfo(object.getSourceDatabaseName());
        if (null == dbInfo) {
            throw new SqlResolveException(10002);
        }

        // 验证sql有效性
        exportSqlDao.validateSql(object.getQuerySql(), dbInfo);

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
            if (0 == names.size()) {
                continue;
            }

//            names.remove(names.size() - 1);

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
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveObject(ExportSql object, IUserInfo userDetail) throws SqlResolveException {
        DatabaseInfo dbinfo =  platformEnvironment.getDatabaseInfo(object.getSourceDatabaseName());
        object.setSourceOsId(dbinfo.getOsId());

        // save exportsql
        ExportSql dbObject = exportSqlDao.loadObjectById(object.getExportId());
        if (null == dbObject) {
            object.setCreated(userDetail.getUserCode());
            object.setCreateTime(new Date());
            object.setExportId(exportSqlDao.getNextLongSequence());
            setExportFieldTriggerCid(object);
            saveObject(object);
        } else {
            dbObject.replaceExportFields(object.getExportFields() );
            dbObject.replaceExportTriggers(object.getExportTriggers());
            object = dbObject;
        }

        mergeObject(object);
    }

    @Transactional(propagation = Propagation.REQUIRED)
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
    @Transactional(propagation = Propagation.REQUIRED)
    public List<ExportField> listExportFieldsByQuerysql(ExportSql object) throws SqlResolveException {
        DatabaseInfo dbinfo =  platformEnvironment.getDatabaseInfo(object.getSourceDatabaseName());

        exportSqlDao.validateSql(object.getQuerySql(), dbinfo);

        return exportSqlDao.getTableMetadata(object, dbinfo).getExportFields();
    }

    public void save(ExportSql object, CentitUserDetails user){
        //判断名称的唯一性
        object.setExportName(object.getExportName().trim());

        Map<String, Object> filterMap = new HashMap<>();
        filterMap.put("eqExportName", object.getExportName());
        List<ExportSql> listObjects = listObjects(filterMap);

        if (!CollectionUtils.isEmpty(listObjects)) {
            String message = "导出名称已存在";
            ExportSql exportDB = getObjectById(object.getExportId());
            if (null == exportDB) {
                return;
            } else {
                if (1 < listObjects.size() || !exportDB.getExportId().equals(listObjects.get(0)
                        .getExportId())) {
                    return;
                }
            }
        }

        try {
            validator(object);

            saveObject(object, user);
        } catch (SqlResolveException e) {
            String message = null;
            if (0 == e.getErrorcode()) {
                message = e.getMessage();
            } else {
//                message = SysParametersUtils.getValue(String.valueOf(e.getErrorcode()));
            }
            return;
        }
    }

}
