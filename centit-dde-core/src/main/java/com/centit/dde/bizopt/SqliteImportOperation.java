package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.dataset.FileDataSet;
import com.centit.dde.utils.DataSetOptUtil;
import com.centit.framework.common.ResponseData;
import com.centit.support.algorithm.UuidOpt;
import com.centit.support.common.ObjectException;
import com.centit.support.database.metadata.JdbcMetadata;
import com.centit.support.database.metadata.SimpleTableInfo;
import com.centit.support.database.utils.DatabaseAccess;
import com.centit.support.database.utils.FieldType;
import com.centit.support.database.utils.QueryUtils;
import com.centit.support.file.FileIOOpt;
import com.centit.support.file.FileSystemOpt;
import org.apache.commons.lang3.StringUtils;
import org.sqlite.JDBC;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class SqliteImportOperation implements BizOperation {

    private String dbHome;

    public SqliteImportOperation(String appHome){
        if(appHome.endsWith("/") || appHome.endsWith("\\")) {
            this.dbHome = appHome + "sqlite";
        } else {
            this.dbHome = appHome + File.separatorChar + "sqlite";
        }
        FileSystemOpt.createDirect(this.dbHome);
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        //读取 单表，多表，所有表格
        String id = bizOptJson.getString("id");
        String source = bizOptJson.getString("source");
        DataSet dataSet = bizModel.getDataSet(source);
        if (dataSet == null){
            return BuiltInOperation.createResponseData(0, 1,
                ObjectException.DATA_NOT_FOUND_EXCEPTION,
                dataOptContext.getI18nMessage("dde.604.data_source_not_found"));
        }
        FileDataSet fileInfo = DataSetOptUtil.attainFileDataset(bizModel, dataSet, bizOptJson, true);
        InputStream inputStream = fileInfo.getFileInputStream();
        Object resObj;
        String dbFileName = dbHome + File.separatorChar + "SQL"+ UuidOpt.randomString(16)+".db";

        String readType = bizOptJson.getString("readType"); // singleAsList, multiAsMap, allAsMap(默认)
        if("singleAsList".equals(readType)){
            String tableName = bizOptJson.getString("tableName");
            if(StringUtils.isBlank(tableName)){
                return BuiltInOperation.createResponseData(0, 1,
                    ResponseData.ERROR_FIELD_INPUT_NOT_VALID,
                    dataOptContext.getI18nMessage("error.701.field_is_blank", "tableName"));
            }
            FileIOOpt.writeInputStreamToFile(inputStream, dbFileName);
            resObj = readDataFormTable(dbFileName, tableName);
        } else if("sqlAsList".equals(readType)){
            String querySQL = bizOptJson.getString("querySQL");
            if(StringUtils.isBlank(querySQL)){
                return BuiltInOperation.createResponseData(0, 1,
                    ResponseData.ERROR_FIELD_INPUT_NOT_VALID,
                    dataOptContext.getI18nMessage("error.701.field_is_blank", "querySQL"));
            }
            FileIOOpt.writeInputStreamToFile(inputStream, dbFileName);
            resObj = readDataUsingSql(dbFileName, querySQL);
        } else if("multiAsMap".equals(readType)){
            String tableNames = bizOptJson.getString("tableNames");
            if(StringUtils.isBlank(tableNames)){
                return BuiltInOperation.createResponseData(0, 1,
                    ResponseData.ERROR_FIELD_INPUT_NOT_VALID,
                    dataOptContext.getI18nMessage("error.701.field_is_blank", "tableNames"));
            }
            FileIOOpt.writeInputStreamToFile(inputStream, dbFileName);
            resObj = readDataFormTables(dbFileName, tableNames);
        } else {
            FileIOOpt.writeInputStreamToFile(inputStream, dbFileName);
            resObj = readDataFormTables(dbFileName);
        }
        FileSystemOpt.deleteFile(dbFileName);
        DataSet simpleDataSet = new DataSet(resObj);
        bizModel.putDataSet(id, simpleDataSet);
        return BuiltInOperation.createResponseSuccessData(simpleDataSet.getSize());
    }

    private JSONArray readDataFormTable(String dbFileName, String tableName) throws SQLException, IOException {
        try(Connection connection = JDBC.createConnection("jdbc:sqlite:" + dbFileName, new Properties())) {
            return DatabaseAccess.findObjectsAsJSON(connection, "SELECT * FROM " + QueryUtils.trimSqlIdentifier(tableName));
        }
    }

    private JSONArray readDataUsingSql(String dbFileName, String sqlSen) throws SQLException, IOException {
        try(Connection connection = JDBC.createConnection("jdbc:sqlite:" + dbFileName, new Properties())) {
            return DatabaseAccess.findObjectsAsJSON(connection, sqlSen);
        }
    }
    private JSONObject readDataFormTables(String dbFileName, String tableNames) throws SQLException, IOException {
        JSONObject object = new JSONObject();
        String tabNames [] = tableNames.split(",");
        if(tabNames == null || tabNames.length == 0){
            return object;
        }
        try(Connection connection = JDBC.createConnection("jdbc:sqlite:" + dbFileName, new Properties())) {
            for(String tn : tabNames) {
                String tableName = QueryUtils.trimSqlIdentifier(tn);
                if(StringUtils.isNotBlank(tableName)) {
                    JSONArray dataList = DatabaseAccess.findObjectsAsJSON(connection, "SELECT * FROM " + tableName);
                    object.put(FieldType.mapPropName(tableName), dataList);
                }
            }
            return object;
        }
    }

    private JSONObject readDataFormTables(String dbFileName) throws SQLException, IOException {
        JSONObject object = new JSONObject();
        try(Connection connection = JDBC.createConnection("jdbc:sqlite:" + dbFileName, new Properties())) {
            JdbcMetadata metadata = new JdbcMetadata();
            metadata.setDBConfig(connection);
            List<SimpleTableInfo> tableInfos = metadata.listTables(false, null);
            if(tableInfos!=null && !tableInfos.isEmpty()) {
                for (SimpleTableInfo ti : tableInfos) {
                    JSONArray dataList = DatabaseAccess.findObjectsAsJSON(connection, "SELECT * FROM " + ti.getTableName());
                    object.put(FieldType.mapPropName(ti.getTableName()), dataList);
                }
            }
            return object;
        }
    }

}
