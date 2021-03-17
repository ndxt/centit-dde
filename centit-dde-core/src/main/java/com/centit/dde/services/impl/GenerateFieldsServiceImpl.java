package com.centit.dde.services.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.centit.dde.core.SimpleDataSet;
import com.centit.dde.dataset.CsvDataSet;
import com.centit.dde.dataset.ExcelDataSet;
import com.centit.dde.dataset.JSONDataSet;
import com.centit.dde.dataset.SqlDataSetWriter;
import com.centit.dde.services.GenerateFieldsService;
import com.centit.dde.vo.ColumnSchema;
import com.centit.fileserver.common.FileStore;
import com.centit.product.metadata.dao.SourceInfoDao;
import com.centit.product.metadata.po.SourceInfo;
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

    private final SourceInfoDao sourceInfoDao;

    @Autowired
    public GenerateFieldsServiceImpl(SourceInfoDao sourceInfoDao) {
        this.sourceInfoDao = sourceInfoDao;
    }

    @Override
    public JSONArray queryViewSqlData(String databaseCode, String sql, Map<String, Object> params) {
        SourceInfo sourceInfo = sourceInfoDao.getDatabaseInfoById(databaseCode);
        QueryAndParams qap = QueryAndParams.createFromQueryAndNamedParams(QueryUtils.translateQuery(sql, params));
        try {
            return TransactionHandler.executeQueryInTransaction(DataSourceDescription.valueOf(sourceInfo),
                (conn) -> DatabaseAccess.findObjectsAsJSON(conn,
                    QueryUtils.buildLimitQuerySQL(qap.getQuery(), 0, 20, false,
                        DBType.mapDBType(sourceInfo.getDatabaseUrl())),
                    qap.getParams()));
        } catch (SQLException | IOException e) {
            logger.error("执行查询出错，SQL：{},Param:{}", qap.getQuery(), qap.getParams());
            throw new ObjectException("执行查询出错!");
        }
    }

    @Override
    public JSONArray queryViewCsvData(String fileId) {
        CsvDataSet csvDataSet = new CsvDataSet();
        try {
            csvDataSet.setFilePath(
                fileStore.getFile(fileId).getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        SimpleDataSet dataSet = csvDataSet.load(null);
        JSONArray result = new JSONArray();
        for (int i = 0; i < dataSet.getDataAsList().size(); i++) {
            if (i >= 20) {
                break;
            } else {
                result.add(dataSet.getDataAsList().get(i));
            }
        }
        return result;
    }

    @Override
    public JSONArray queryViewJsonData(String fileId) {
        JSONDataSet jsonDataSet = new JSONDataSet();
        try {
            jsonDataSet.setFilePath(
                fileStore.getFile(fileId).getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        SimpleDataSet dataSet = jsonDataSet.load(null);
        JSONArray result = new JSONArray();
        for (int i = 0; i < dataSet.getDataAsList().size(); i++) {
            if (i >= 20) {
                break;
            } else {
                result.add(dataSet.getDataAsList().get(i));
            }
        }
        return result;
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
        List<String> sColumns = new ArrayList<>();
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
    public List<ColumnSchema> generateSqlFields(String databaseCode, String sql, Map<String, Object> params) {
        List<String> sColumn = QueryUtils.getSqlFiledNames(sql);
        if (sColumn == null || sColumn.size() == 0) {
            SourceInfo databaseInfo = sourceInfoDao.getDatabaseInfoById(databaseCode);
            QueryAndParams qap = QueryAndParams.createFromQueryAndNamedParams(QueryUtils.translateQuery(sql, params));
            String sSql = QueryUtils.buildLimitQuerySQL(qap.getQuery(), 0, 2, false,
                DBType.mapDBType(databaseInfo.getDatabaseUrl()));
            try (Connection conn = DbcpConnectPools.getDbcpConnect(databaseInfo);
                 PreparedStatement stmt = conn.prepareStatement(sSql)) {
                DatabaseAccess.setQueryStmtParameters(stmt, qap.getParams());
                try (ResultSet rs = stmt.executeQuery()) {
                    ResultSetMetaData rsd = rs.getMetaData();
                    int nc = rsd.getColumnCount();
                    sColumn = new ArrayList<>(nc);
                    for (int i = 1; i <= nc; i++) {
                        sColumn.add(rsd.getColumnName(i));
                    }
                }
            } catch (SQLException e) {
                logger.error("执行查询出错，SQL：{},Param:{}", sSql, qap.getParams());
            }
        }
        return getColumnSchemas(sColumn);
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
            col.setColumnCode(SqlDataSetWriter.WRITER_ERROR_TAG);
            col.setPropertyName(FieldType.mapPropName(SqlDataSetWriter.WRITER_ERROR_TAG));
            col.setColumnName(col.getPropertyName());
            col.setDataType(FieldType.STRING);
            col.setIsStatData("F");
            columnSchemas.add(col);
        }
        return columnSchemas;
    }
}
