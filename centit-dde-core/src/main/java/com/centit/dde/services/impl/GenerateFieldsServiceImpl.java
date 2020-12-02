package com.centit.dde.services.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.centit.dde.core.SimpleDataSet;
import com.centit.dde.dataset.CsvDataSet;
import com.centit.dde.dataset.ExcelDataSet;
import com.centit.dde.dataset.SQLDataSetWriter;
import com.centit.dde.services.GenerateFieldsService;
import com.centit.dde.vo.ColumnSchema;
import com.centit.fileserver.common.FileStore;
import com.centit.framework.ip.po.DatabaseInfo;
import com.centit.framework.ip.service.IntegrationEnvironment;
import com.centit.support.common.ObjectException;
import com.centit.support.database.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;

/**
 * @author zhf
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class GenerateFieldsServiceImpl implements GenerateFieldsService {

    private static final Logger logger = LoggerFactory.getLogger(GenerateFieldsServiceImpl.class);
    @Autowired(required = false)
    private FileStore fileStore;

    private final IntegrationEnvironment integrationEnvironment;

    @Autowired
    public GenerateFieldsServiceImpl(IntegrationEnvironment integrationEnvironment) {
        this.integrationEnvironment = integrationEnvironment;
    }

    @Override
    public JSONArray queryViewSqlData(String databaseCode, String sql, Map<String, Object> params) {
        DatabaseInfo databaseInfo = integrationEnvironment.getDatabaseInfo(databaseCode);
        QueryAndParams qap = QueryAndParams.createFromQueryAndNamedParams(QueryUtils.translateQuery(sql, params));
        try {
            return TransactionHandler.executeQueryInTransaction(DataSourceDescription.valueOf(databaseInfo),
                (conn) -> DatabaseAccess.findObjectsAsJSON(conn,
                    QueryUtils.buildLimitQuerySQL(qap.getQuery(), 0, 20, false,
                        DBType.mapDBType(databaseInfo.getDatabaseUrl())),
                    qap.getParams()));
        } catch (SQLException | IOException e) {
            logger.error("执行查询出错，SQL：{},Param:{}", qap.getQuery(), qap.getParams());
            throw new ObjectException("执行查询出错!");
        }
    }

    @Override
    public Set<String> generateSqlParams(String sql) {
        return QueryUtils.getSqlTemplateParameters(sql);
    }

    @Override
    public List<ColumnSchema> generateExcelFields(Map<String, Object> params) {
        ExcelDataSet excelDataSet = new ExcelDataSet();
        try {
            excelDataSet.setFilePath(fileStore.getFile((String) params.get("FileId")).getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getColumnSchemas(Arrays.asList(excelDataSet.getColumns()));
    }

    @Override
    public List<ColumnSchema> generateCsvFields(Map<String, Object> params) {
        CsvDataSet csvDataSet = new CsvDataSet();
        try {
            csvDataSet.setFilePath(fileStore.getFile((String) params.get("FileId")).getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getColumnSchemas(Arrays.asList(csvDataSet.getColumns()));
    }

    @Override
    public List<ColumnSchema> generateJsonFields(Map<String, Object> params) {
        Class c = SimpleDataSet.class;
        Field[] columns = c.getDeclaredFields();
        List<String> sColumns=new ArrayList<>();
        for (Field s : columns) {
            sColumns.add(s.getName());
        }
        return getColumnSchemas(sColumns);
    }

    @Override
    public List<ColumnSchema> generatePostFields(String jsonString) {
        return getColumnSchemas(new ArrayList<>(JSON.parseObject(jsonString).keySet()));
    }

    @Override
    public List<ColumnSchema> generateSqlFields(String sql, Map<String, Object> params) {
        return getColumnSchemas(QueryUtils.getSqlFiledNames(sql));
    }

    private List<ColumnSchema> getColumnSchemas(List<String> columns) {
        List<ColumnSchema> columnSchemas = new ArrayList<>(10);
        if (columns != null) {
            for (String s : columns) {
                if (s != null && s.length() != 0) {
                    ColumnSchema col = new ColumnSchema();
                    col.setColumnCode(s);
                    col.setPropertyName(FieldType.mapPropName(s));
                    col.setColumnName(col.getPropertyName());
                    col.setDataType(FieldType.STRING);
                    col.setIsStatData("F");
                    columnSchemas.add(col);
                }
            }
            ColumnSchema col = new ColumnSchema();
            col.setColumnCode(SQLDataSetWriter.WRITER_ERROR_TAG);
            col.setPropertyName(FieldType.mapPropName(SQLDataSetWriter.WRITER_ERROR_TAG));
            col.setColumnName(col.getPropertyName());
            col.setDataType(FieldType.STRING);
            col.setIsStatData("F");
            columnSchemas.add(col);
        }
        return columnSchemas;
    }
}
