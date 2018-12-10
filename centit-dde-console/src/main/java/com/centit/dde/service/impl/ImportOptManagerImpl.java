package com.centit.dde.service.impl;

import com.centit.dde.dao.ImportFieldDao;
import com.centit.dde.dao.ImportOptDao;
import com.centit.dde.exception.SqlResolveException;
import com.centit.dde.po.*;
import com.centit.dde.service.ImportOptManager;
import com.centit.dde.util.ConnPool;
import com.centit.framework.ip.po.DatabaseInfo;
import com.centit.framework.ip.service.IntegrationEnvironment;
import com.centit.framework.jdbc.service.BaseEntityManagerImpl;
import com.centit.framework.model.basedata.IUserInfo;
import com.centit.support.database.utils.QueryUtils;
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
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Service
public class ImportOptManagerImpl extends BaseEntityManagerImpl<ImportOpt,Long,ImportOptDao> implements ImportOptManager {

    public static final Log log = LogFactory.getLog(ImportOptManager.class);

    // private static final SysOptLog sysOptLog =
    // SysOptLogFactoryImpl.getSysOptLog();

    @Resource
    private ImportFieldDao importFieldDao;

    @Resource
    protected IntegrationEnvironment integrationEnvironment;

    private ImportOptDao importOptDao;
    @Resource(name="importOptDao")
    @NotNull
    public void setImportOptDao(ImportOptDao baseDao) {
        this.importOptDao = baseDao;
        setBaseDao(this.importOptDao);
    }

    @Override
    public String getMapinfoName(Long mapinfoId) {
        return this.importOptDao.getMapinfoName(mapinfoId);
    }

    private void validator(ImportOpt object) throws SqlResolveException {
        DatabaseInfo dbinfo = integrationEnvironment.getDatabaseInfo(object.getDestDatabaseName());
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
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveObject(ImportOpt object, IUserInfo userDetail) {
        try {
            checkObject(object);
            // 判断导入的表是否存在

            DatabaseInfo databaseInfo = integrationEnvironment.getDatabaseInfo(object.getDestDatabaseName());

            object.setSourceOsId(databaseInfo.getOsId());
            //将表名转换为大写
            object.setTableName(object.getTableName().toUpperCase());

            if (importOptDao.isExistsForTable(object, databaseInfo)) {

                // save exportsql
                ImportOpt dbObject =  null;
                if (object.getImportId()!=null)
                 dbObject = importOptDao.getObjectById(object.getImportId());
                if (null == dbObject) {
                    object.setCreated(userDetail.getUserCode());
                    object.setCreateTime(new Date());

                    object.setImportId(importOptDao.getNextLongSequence());

                    setImportFieldTriggerCid(object);
                    saveNewObject(object);

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
                    updateObject(dbObject);

                }
                importOptDao.saveObjectReferences(object);
            }
        }catch(SqlResolveException e){
            log.error("保存失败", e);
        }
    }

    private void setImportFieldTriggerCid(ImportOpt object) {
        ImportField ef = null;
        ImportTrigger et = null;
        for (int i = 0; i < object.getImportFields().size(); i++) {
            ef = object.getImportFields().get(i);
            ef.setImportId(object.getImportId());
            ef.setColumnNo((long) i);
        }
        for (int i = 0; i < object.getImportTriggers().size() && !CollectionUtils.isEmpty(object.getImportTriggers());
             i++) {
            et = object.getImportTriggers().get(i);
            et.setCid(new ImportTriggerId((long) i, object.getImportId()));
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<ImportField> listFields(String databaseCode, String tableName) throws SqlResolveException {
        List<ImportField> fields = new ArrayList<>();
        Connection connection = null;
        if (databaseCode != null && !"".equals(databaseCode)) {
            DatabaseInfo databaseInfo = integrationEnvironment.getDatabaseInfo(databaseCode);
            if (databaseInfo != null) {
                try {
                    connection = ConnPool.getConn(databaseInfo);
                    DatabaseMetaData metaData = connection.getMetaData();
                    ResultSet resultSet = metaData.getColumns(null, databaseInfo.getUsername().toUpperCase(), tableName, null);
                    while (resultSet.next()) {
                        ImportField field = new ImportField();
                        field.setSourceFieldName(resultSet.getString("COLUMN_NAME"));
                        field.setDestFieldName(resultSet.getString("COLUMN_NAME"));
                        field.setDestFieldType(resultSet.getString("TYPE_NAME"));
                        field.setIsNull(resultSet.getString("NULLABLE"));
//                        field.setDestFieldDefault(resultSet.getString(""));
                        field.setIsPk("0");
                        ResultSet pk = metaData.getPrimaryKeys(null, null, tableName);
                        while (pk.next()) {
                            if (pk.getString("COLUMN_NAME").equals(resultSet.getString("COLUMN_NAME"))) {
                                field.setIsPk("1");
                            }
                        }
                        fields.add(field);
                    }
                } catch (Exception e) {

                } finally {
                    try {
                        connection.close();
                    } catch (SQLException e) {

                    }
                }
            }
        }
        return fields;
    }

    private void checkObject(ImportOpt object) throws SqlResolveException {
        //判断名称的唯一性
        object.setImportName(object.getImportName().trim());

        Map<String, Object> filterMap = new HashMap<>();
        filterMap.put("importName", object.getImportName());
        //获取同名的配置
        List<ImportOpt> listObjects = listObjects(filterMap);

        if (!CollectionUtils.isEmpty(listObjects)) {
            ImportOpt importDB = getObjectById(object.getImportId());
            String message = "导入名称已存在";
            if (null == importDB) {//新增
                return;
            } else {//编辑
                if (1 < listObjects.size() || !importDB.getImportId().equals(listObjects.get(0).getImportId())) {

                    return;
                }
            }
        }
        validator(object);//校验目标数据库不为空、触发器参数存在于字段中
    }

    public ImportOpt getObjectById(Long importId) {
        return  importOptDao.getObjectById(importId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteObjectById(Long importId) {
        ImportOpt importOpt = importOptDao.getObjectById(importId);
        if (importOpt !=null) {
            importOptDao.deleteObjectById(importId);
            importOptDao.deleteObjectReferences(importOpt);
        }
    }
}
