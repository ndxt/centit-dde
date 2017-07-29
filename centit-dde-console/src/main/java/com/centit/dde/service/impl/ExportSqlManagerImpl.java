package com.centit.dde.service.impl;

import com.centit.dde.dao.ExportSqlDao;
import com.centit.dde.exception.SqlResolveException;
import com.centit.dde.po.*;
import com.centit.dde.service.ExportFieldManager;
import com.centit.dde.service.ExportSqlManager;
import com.centit.dde.util.ConnPool;
import com.centit.framework.hibernate.service.BaseEntityManagerImpl;
import com.centit.framework.model.basedata.IUserInfo;
import com.centit.framework.staticsystem.po.DatabaseInfo;
import com.centit.framework.staticsystem.service.IntegrationEnvironment;
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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import static com.centit.dde.util.ConnPool.getConn;

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
    protected IntegrationEnvironment integrationEnvironment;
    
    @Resource(name="exportSqlDao")
    @NotNull
    public void setExportSqlDao(ExportSqlDao baseDao) {
        this.exportSqlDao = baseDao;
        setBaseDao(this.exportSqlDao);
    }

    public String getMapinfoName(Long mapinfoId) {
        return this.exportSqlDao.getMapinfoName(mapinfoId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveObject(ExportSql object, IUserInfo userDetail) {
        DatabaseInfo dbinfo =  integrationEnvironment.getDatabaseInfo(object.getSourceDatabaseName());
        try {
            checkObject(object);
            object.setSourceOsId(dbinfo.getOsId());
            ExportSql dbObject = exportSqlDao.loadObjectById(object.getExportId());
            if (null == dbObject) {
                object.setCreated(userDetail.getUserCode());
                object.setCreateTime(new Date());
                object.setExportId(exportSqlDao.getNextLongSequence());
                setExportFieldTriggerCid(object);
            } else {
                dbObject.copyNotNullProperty(object);
                dbObject.setLastUpdateTime(new Date());
                dbObject.replaceExportFields(object.getExportFields());
                dbObject.replaceExportTriggers(object.getExportTriggers());
                object = dbObject;
            }
            saveObject(object);
        }catch (SqlResolveException e){
            log.error("保存出错");
        }
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
    @Transactional(propagation = Propagation.REQUIRED)
    public List<ExportField> listExportFieldsByQuerysql(ExportSql object) throws SqlResolveException {
        DatabaseInfo dbinfo =  integrationEnvironment.getDatabaseInfo(object.getSourceDatabaseName());

        validateSql(object.getQuerySql(), dbinfo);

        return exportSqlDao.getTableMetadata(object, dbinfo).getExportFields();
    }

    private void checkObject(ExportSql object) throws SqlResolveException{
        //判断名称的唯一性
        object.setExportName(object.getExportName().trim());

        Map<String, Object> filterMap = new HashMap<>();
        filterMap.put("eqExportName", object.getExportName());
        List<ExportSql> listObjects = listObjects(filterMap);

        if (!CollectionUtils.isEmpty(listObjects)) {
            String message = "导出名称已存在";
            ExportSql exportDB = getObjectById(object.getExportId());
            if (null == exportDB) {
                log.error(message);
                return;
            } else {
                if (1 < listObjects.size() || !exportDB.getExportId().equals(listObjects.get(0).getExportId())) {
                    log.error(message);
                    return;
                }
            }
        }
        validator(object);
    }

    private void validator(ExportSql object) throws SqlResolveException {
        if (!StringUtils.hasText(object.getQuerySql())) {
            log.error("sql语句为空");
            throw new SqlResolveException(10001);
        }
        object.setQuerySql(object.getQuerySql().toUpperCase());

        DatabaseInfo dbInfo =  integrationEnvironment.getDatabaseInfo(object.getSourceDatabaseName());
        if (null == dbInfo) {
            log.error("数据库信息错误");
            throw new SqlResolveException(10002);
        }

        // 验证sql有效性
        validateSql(object.getQuerySql(), dbInfo);

        // 验证触发器的参数是否在字段中存在
        Set<String> params = new HashSet<>();
        Set<String> fields = new HashSet<>();

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

    /**
     * 验证sql执行的正确性
     * @Param
     * @Param object
     * @throws SqlResolveException
     */
    private void validateSql(String querysql, DatabaseInfo dbinfo) throws SqlResolveException {
        if (!ConnPool.testConn(dbinfo)) {
            logger.error("连接数据库出错");
            throw new SqlResolveException(10002);
        }
        Connection conn = null;
        Statement statement = null;
        ResultSet rs = null;
        try {
            conn = getConn(dbinfo);
            statement = conn.createStatement();
            rs = statement.executeQuery(querysql);

            rs.next();
        } catch (SQLException e) {
            logger.error("验证数据库Sql执行语句报错", e);

            throw new SqlResolveException(10001, e);
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
