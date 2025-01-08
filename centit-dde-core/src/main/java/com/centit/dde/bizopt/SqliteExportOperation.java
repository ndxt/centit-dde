package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.dataset.FileDataSet;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.framework.common.ResponseData;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.algorithm.UuidOpt;
import com.centit.support.common.ObjectException;
import com.centit.support.database.ddl.SqliteDDLOperations;
import com.centit.support.database.jsonmaptable.SqliteJsonObjectDao;
import com.centit.support.database.metadata.SimpleTableInfo;
import com.centit.support.database.utils.DatabaseAccess;
import com.centit.support.database.utils.FieldType;
import com.centit.support.file.FileIOOpt;
import com.centit.support.file.FileSystemOpt;
import com.centit.support.file.FileType;
import com.centit.support.json.JSONTransformer;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.sqlite.JDBC;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class SqliteExportOperation implements BizOperation {

    private String dbHome;

    public SqliteExportOperation(String appHome) {
        if (appHome.endsWith("/") || appHome.endsWith("\\")) {
            this.dbHome = appHome + "sqlite";
        } else {
            this.dbHome = appHome + File.separatorChar + "sqlite";
        }
        FileSystemOpt.createDirect(this.dbHome);
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        // 单表，多表，所有表， 指定主键
        String id = bizOptJson.getString("id");
        String source = bizOptJson.getString("source");
        DataSet dataSet = bizModel.getDataSet(source);
        if (dataSet == null) {
            return BuiltInOperation.createResponseData(0, 1,
                ObjectException.DATA_NOT_FOUND_EXCEPTION,
                dataOptContext.getI18nMessage("dde.604.data_source_not_found"));
        }
        //创建数据库
        String dbFileName = dbHome + File.separatorChar + "SQL" + UuidOpt.randomString(16) + ".db";
        String dataModel = bizOptJson.getString("dataModel"); // listAsTable(默认), multiTables
        if ("multiTables".equals(dataModel)) {
            // 多个表，属性名必须和表名一致
            String defaultKey = bizOptJson.getString("defaultPrimaryKey");
            Map<String, String> mapInfo = BuiltInOperation.jsonArrayToMap(
                bizOptJson.getJSONArray("primaryKeyMap"), "tableName", "primaryKey");
            writeTablesData2DB(dbFileName, dataSet.getData(), mapInfo, defaultKey);
        } else { // listAsTable(默认)
            String tableName = bizOptJson.getString("tableName");
            if(StringUtils.isBlank(tableName)){
                return BuiltInOperation.createResponseData(0, 1,
                    ResponseData.ERROR_FIELD_INPUT_NOT_VALID,
                    dataOptContext.getI18nMessage("error.701.field_is_blank", "tableName"));
            }
            String primaryKey = bizOptJson.getString("primaryKey");
            writeTableData2DB(dbFileName, dataSet.getDataAsList(), tableName, primaryKey);
        }

        String fileName = null;
        if (StringUtils.isNotBlank(bizOptJson.getString("fileName"))) {
            fileName = StringBaseOpt.castObjectToString(JSONTransformer.transformer(
                bizOptJson.getString("fileName"), new BizModelJSONTransform(bizModel, dataSet.getData())));
        }
        if (StringUtils.isBlank(fileName)) {
            fileName = DatetimeOpt.convertDateToString(DatetimeOpt.currentUtilDate(),"yyyyMMDD_HHmm")
                + ".db";
        }
        if(!"db".equalsIgnoreCase(FileType.getFileExtName(fileName))) {
            fileName = fileName + ".db";
        }

        byte[] fileBytes = FileIOOpt.readBytesFromFile(dbFileName);
        FileSystemOpt.deleteFile(dbFileName);
        FileDataSet fileDataSet = new FileDataSet(fileName, fileBytes.length, fileBytes);
        bizModel.putDataSet(id, fileDataSet);
        return BuiltInOperation.createResponseSuccessData(fileDataSet.getSize());
    }

    private void setTablePrimaryKey(SimpleTableInfo tableInfo, String primaryKey){
        if(StringUtils.isNotBlank(primaryKey)) {
            if(primaryKey.indexOf(',')>0){
                String [] keys = primaryKey.split(",");
                for (String pk : keys){
                    SqliteDDLOperations.setTablePrimaryKey(tableInfo, pk, false);
                }
            } else {
                SqliteDDLOperations.setTablePrimaryKey(tableInfo, primaryKey, false);
            }
        }
    }

    private void writeTableData(Connection connection, List<Map<String, Object>> data, String tableName, String primaryKey) throws SQLException, IOException {
        SimpleTableInfo tableInfo = SqliteDDLOperations.mapTableInfo(data, tableName);
        setTablePrimaryKey(tableInfo, primaryKey);
        SqliteDDLOperations operations = new SqliteDDLOperations();
        DatabaseAccess.doExecuteSql(connection, operations.makeCreateTableSql(tableInfo));
        SqliteJsonObjectDao jsonObjectDao = new SqliteJsonObjectDao(connection, tableInfo);
        for (Map<String, Object> object : data) {
            jsonObjectDao.saveNewObject(object);
        }
    }

    private void writeTableData2DB(String dbFileName, List<Map<String, Object>> data, String tableName, String primaryKey) throws SQLException, IOException {
        try (Connection connection = JDBC.createConnection("jdbc:sqlite:" + dbFileName, new Properties())) {
            writeTableData(connection, data, tableName, primaryKey);
        }
    }

    public Pair<String, String> fetchTableNameAndPrimaryKey(String propertyName, Map<String, String> pkMap, String defaultKey){
        if(propertyName.indexOf('_')>0) {
            for(Map.Entry<String, String> entry : pkMap.entrySet()){
                if(StringUtils.equalsIgnoreCase(entry.getKey(), propertyName)){
                    return new MutablePair<>(entry.getKey(), entry.getValue());
                }
            }
            return new MutablePair<>(propertyName, defaultKey);
        }

        String tableName = FieldType.humpNameToColumn(propertyName, true);
        for(Map.Entry<String, String> entry : pkMap.entrySet()){
            if(StringUtils.equalsIgnoreCase(entry.getKey(), tableName)){
                return new MutablePair<>(entry.getKey(), entry.getValue());
            }
            if(StringUtils.equalsIgnoreCase(entry.getKey(), propertyName)){
                if(StringUtils.equals(propertyName, propertyName.toLowerCase())){
                    return new MutablePair<>(entry.getKey(), entry.getValue());
                }
                return new MutablePair<>(tableName, entry.getValue());
            }
        }

        if(StringUtils.equals(propertyName, propertyName.toLowerCase())){
            return new MutablePair<>(propertyName, defaultKey);
        }
        return new MutablePair<>(tableName, defaultKey);
    }

    private void writeTableData(Connection connection, JSONArray objArray, String tableName, String primaryKey) throws SQLException, IOException {
        SimpleTableInfo tableInfo = SqliteDDLOperations.mapTableInfo(objArray, tableName);
        setTablePrimaryKey(tableInfo, primaryKey);
        SqliteDDLOperations operations = new SqliteDDLOperations();
        DatabaseAccess.doExecuteSql(connection, operations.makeCreateTableSql(tableInfo));
        SqliteJsonObjectDao jsonObjectDao = new SqliteJsonObjectDao(connection, tableInfo);
        for (Object object : objArray) {
            if(object instanceof JSONObject) {
                jsonObjectDao.saveNewObject((JSONObject)object);
            }
        }
    }

    private void writeTablesData2DB(String dbFileName, Object tablesData, Map<String, String> pkMap, String defaultKey) throws SQLException, IOException {
        if(!(tablesData instanceof Map)){
            return;
        }
        Map<String, Object> datasMap = (Map<String, Object>) tablesData;
        try (Connection connection = JDBC.createConnection("jdbc:sqlite:" + dbFileName, new Properties())) {
            for(Map.Entry<String, Object> ent : datasMap.entrySet()) {
                Pair<String, String> talNameAndKey = fetchTableNameAndPrimaryKey(ent.getKey(), pkMap, defaultKey);
                if(ent.getValue() instanceof JSONArray) {
                    JSONArray tbArray = (JSONArray) ent.getValue();
                    writeTableData(connection, tbArray, talNameAndKey.getLeft(), talNameAndKey.getRight());
                } else if(ent.getValue() instanceof List){
                    JSONArray tbArray = JSONArray.from(ent.getValue());
                    writeTableData(connection, tbArray, talNameAndKey.getLeft(), talNameAndKey.getRight());
                } else if(ent.getValue() instanceof Map){
                    JSONArray tbArray = new JSONArray();
                    JSONObject object = JSONObject.from(ent.getValue());
                    tbArray.add(object);
                    writeTableData(connection, tbArray, talNameAndKey.getLeft(), talNameAndKey.getRight());
                }
            }
        }
    }
}
