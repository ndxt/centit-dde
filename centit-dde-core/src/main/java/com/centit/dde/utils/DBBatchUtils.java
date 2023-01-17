package com.centit.dde.utils;

import com.centit.dde.core.BizModel;
import com.centit.product.adapter.po.MetaColumn;
import com.centit.product.adapter.po.MetaTable;
import com.centit.support.algorithm.*;
import com.centit.support.common.LeftRightPair;
import com.centit.support.database.jsonmaptable.GeneralJsonObjectDao;
import com.centit.support.database.metadata.TableField;
import com.centit.support.database.metadata.TableInfo;
import com.centit.support.database.utils.DatabaseAccess;
import com.centit.support.database.utils.FieldType;
import com.centit.support.database.utils.QueryLogUtils;
import com.centit.support.database.utils.QueryUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.*;
import java.util.*;

/**
 * 数据库批量操作 工具类，
 * 目的是为了提高批量处理的效率
 *
 * @author zhf
 */
public abstract class DBBatchUtils {
    protected static final Logger logger = LoggerFactory.getLogger(DBBatchUtils.class);
    private static int INT_BATCH_NUM = 1000;

    public static List<String> achieveAllFields(final List<Map<String, Object>> objects) {
        HashSet<String> fields = new HashSet<>();
        for (Map<String, Object> map : objects) {
            fields.addAll(map.keySet());
        }
        return new ArrayList<>(fields);
    }

    private static void fetchObjectsId(final List<Map<String, Object>> objects, ResultSet rs, MetaColumn column) throws SQLException {
        if(rs==null) return;
        int i = 0, l = objects.size();
        while(rs.next() && i<l){
            objects.get(i).put(column.getPropertyName(), rs.getObject(1));
            i++;
        }
        rs.close();
    }

    public static int insertObject(final BizModel bizModel, final Connection conn,
                                   final MetaTable tableInfo,
                                   final Map<String, Object> object, Map fieldsMap) throws SQLException {
        List<String> fields = new ArrayList<>();
        if (fieldsMap == null || fieldsMap.size()==0) {
            fields.addAll(object.keySet());
        } else {
            Collections.addAll(fields, (String[]) fieldsMap.keySet().toArray(new String[0]));
        }
        String sql = GeneralJsonObjectDao.buildInsertSql(tableInfo, fields);
        LeftRightPair<String, List<String>> sqlPair = QueryUtils.transNamedParamSqlToParamSql(sql);

        QueryLogUtils.printSql(logger, sqlPair.getLeft(), sqlPair.getRight());

        Map<String, Object> objectForSave =
            prepareObjectForSave(tableInfo, fieldsMap != null ?
                DataSetOptUtil.mapDataRow(bizModel, object, 0, 1, fieldsMap.entrySet()) : object);
        if(tableInfo.hasGeneratedKeys()){ //插入 并找回主键
            MetaColumn column = tableInfo.fetchGeneratedKey();

            try (PreparedStatement stmt = conn.prepareStatement(sqlPair.getLeft(), Statement.RETURN_GENERATED_KEYS)) {
                DatabaseAccess.setQueryStmtParameters(stmt, sqlPair.getRight(), objectForSave);
                stmt.executeUpdate();
                ResultSet rs = stmt.getGeneratedKeys();
                if(rs !=null && rs.next()) {
                    object.put(column.getPropertyName(), rs.getObject(1));
                    rs.close();
                }
            } catch (SQLException e) {
                throw DatabaseAccess.createAccessException(sqlPair.getLeft(), e);
            }
        } else { //插入
            try (PreparedStatement stmt = conn.prepareStatement(sqlPair.getLeft())) {
                DatabaseAccess.setQueryStmtParameters(stmt, sqlPair.getRight(), objectForSave);
                stmt.executeUpdate();
            } catch (SQLException e) {
                throw DatabaseAccess.createAccessException(sqlPair.getLeft(), e);
            }
        }
        return 1;
    }

    public static int batchInsertObjects(final BizModel bizModel, final Connection conn,
                                         final MetaTable tableInfo,
                                         final List<Map<String, Object>> objects, Map fieldsMap) throws SQLException {
        List<String> fields = new ArrayList<>();
        if (fieldsMap == null || fieldsMap.size()==0) {
            fields = achieveAllFields(objects);
        } else {
            Collections.addAll(fields, (String[]) fieldsMap.keySet().toArray(new String[0]));
        }
        String sql = GeneralJsonObjectDao.buildInsertSql(tableInfo, fields);
        LeftRightPair<String, List<String>> sqlPair = QueryUtils.transNamedParamSqlToParamSql(sql);
        int rowIndex = 0;
        int rowCount = objects.size();

        QueryLogUtils.printSql(logger, sqlPair.getLeft(), sqlPair.getRight());

        if(tableInfo.hasGeneratedKeys()){ //批量插入 并找回主键
            MetaColumn column = tableInfo.fetchGeneratedKey();
            try (PreparedStatement stmt = conn.prepareStatement(sqlPair.getLeft(), Statement.RETURN_GENERATED_KEYS)) {
                List<Map<String, Object>> savedObjects = new ArrayList<>(INT_BATCH_NUM);
                for (Map<String, Object> object : objects) {
                    Map<String, Object> objectForSave =
                        prepareObjectForSave(tableInfo, fieldsMap != null ?
                            DataSetOptUtil.mapDataRow(bizModel, object, rowIndex, rowCount, fieldsMap.entrySet()) : object);
                    savedObjects.add(object);
                    DatabaseAccess.setQueryStmtParameters(stmt, sqlPair.getRight(), objectForSave);
                    rowIndex++;
                    stmt.addBatch();
                    if (rowIndex % INT_BATCH_NUM == 0) {
                        stmt.executeBatch();
                        ResultSet rs = stmt.getGeneratedKeys();
                        fetchObjectsId(savedObjects, rs, column);
                        savedObjects.clear();
                        stmt.clearBatch();
                    }
                }
                if(rowIndex % INT_BATCH_NUM > 0) {
                    stmt.executeBatch();
                    ResultSet rs = stmt.getGeneratedKeys();
                    fetchObjectsId(savedObjects, rs, column);
                    savedObjects.clear();
                    stmt.clearBatch();
                }
            } catch (SQLException e) {
                throw DatabaseAccess.createAccessException(sqlPair.getLeft(), e);
            }
        } else { //批量插入
            try (PreparedStatement stmt = conn.prepareStatement(sqlPair.getLeft())) {
                for (Map<String, Object> object : objects) {
                    Map<String, Object> objectForSave =
                        prepareObjectForSave(tableInfo, fieldsMap != null ?
                            DataSetOptUtil.mapDataRow(bizModel, object, rowIndex, rowCount, fieldsMap.entrySet()) : object);

                    DatabaseAccess.setQueryStmtParameters(stmt, sqlPair.getRight(), objectForSave);
                    rowIndex++;
                    stmt.addBatch();
                    if (rowIndex % INT_BATCH_NUM == 0) {
                        stmt.executeBatch();
                        stmt.clearBatch();
                    }
                }
                if (rowIndex % INT_BATCH_NUM > 0) {
                    stmt.executeBatch();
                    stmt.clearBatch();
                }
            } catch (SQLException e) {
                throw DatabaseAccess.createAccessException(sqlPair.getLeft(), e);
            }
        }
        return rowIndex;
    }

    public static int mergeObject(final BizModel bizModel, final Connection conn,
                                        final MetaTable tableInfo,
                                        final Map<String, Object> object, Map fieldsMap) throws SQLException {
        List<String> fields = new ArrayList<>();
        if (fieldsMap == null || fieldsMap.size()==0) {
            fields.addAll(object.keySet());
        } else {
            Collections.addAll(fields, (String[]) fieldsMap.keySet().toArray(new String[0]));
        }
        String sql = "select count(*) as checkExists from " + tableInfo.getTableName()
            + " where " + GeneralJsonObjectDao.buildFilterSqlByPkUseColumnName(tableInfo, null);
        LeftRightPair<String, List<String>> checkSqlPair = QueryUtils.transNamedParamSqlToParamSql(sql);

        try (PreparedStatement checkStmt = conn.prepareStatement(checkSqlPair.getLeft())) {
            Map<String, Object> objectForSave =
                prepareObjectForSave(tableInfo, fieldsMap != null ?
                    DataSetOptUtil.mapDataRow(bizModel, object, 0, 1, fieldsMap.entrySet()) : object);

            DatabaseAccess.setQueryStmtParameters(checkStmt, checkSqlPair.getRight(), objectForSave);
            ResultSet rs = checkStmt.executeQuery();
            boolean exists = false;
            try {
                Object obj = DatabaseAccess.fetchScalarObject(DatabaseAccess.fetchResultSetToObjectsList(rs));
                if (obj != null) {
                    exists = NumberBaseOpt.castObjectToInteger(obj, 0) > 0;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (exists) {
                sql = GeneralJsonObjectDao.buildUpdateSql(tableInfo, fields);
                if (null != sql) {
                    sql += " where " + GeneralJsonObjectDao.buildFilterSqlByPkUseColumnName(tableInfo, null);
                    LeftRightPair<String, List<String>> updateSqlPair = QueryUtils.transNamedParamSqlToParamSql(sql);
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateSqlPair.getLeft())) {
                        DatabaseAccess.setQueryStmtParameters(updateStmt, updateSqlPair.getRight(), objectForSave);
                        updateStmt.executeUpdate();
                    }
                } else {
                    throw new SQLException("update 数据库字段为空，无法生成正确的sql语句！");
                }
            } else {
                MetaColumn column = tableInfo.fetchGeneratedKey();
                boolean needFetchId = tableInfo.hasGeneratedKeys() && column != null;
                sql = GeneralJsonObjectDao.buildInsertSql(tableInfo, fields);
                LeftRightPair<String, List<String>> insertSqlPair = QueryUtils.transNamedParamSqlToParamSql(sql);

                if (needFetchId) {
                    try (PreparedStatement insertStmt = conn.prepareStatement(insertSqlPair.getLeft(), Statement.RETURN_GENERATED_KEYS)) {
                        DatabaseAccess.setQueryStmtParameters(insertStmt, insertSqlPair.getRight(), objectForSave);
                        insertStmt.executeUpdate();
                        ResultSet idRs = insertStmt.getGeneratedKeys();
                        if(idRs !=null && rs.next()) {
                            object.put(column.getPropertyName(), idRs.getObject(1));
                            idRs.close();
                        }
                    }
                } else {
                    try (PreparedStatement insertStmt = conn.prepareStatement(insertSqlPair.getLeft())) {
                        DatabaseAccess.setQueryStmtParameters(insertStmt, insertSqlPair.getRight(), objectForSave);
                        insertStmt.executeUpdate();
                    }
                }
            }
        }
        return 1;
    }


    public static int batchMergeObjects(final BizModel bizModel, final Connection conn,
                                        final MetaTable tableInfo,
                                        final List<Map<String, Object>> objects,
                                        Map fieldsMap) throws SQLException {
        List<String> fields = new ArrayList<>();
        if (fieldsMap == null || fieldsMap.size()==0) {
            fields = achieveAllFields(objects);
        } else {
            Collections.addAll(fields, (String[]) fieldsMap.keySet().toArray(new String[0]));
        }
        String sql = GeneralJsonObjectDao.buildInsertSql(tableInfo, fields);
        LeftRightPair<String, List<String>> insertSqlPair = QueryUtils.transNamedParamSqlToParamSql(sql);
        sql = GeneralJsonObjectDao.buildUpdateSql(tableInfo, fields);
        if (null != sql) {
            sql += " where " + GeneralJsonObjectDao.buildFilterSqlByPkUseColumnName(tableInfo, null);
        } else {
            sql = "";
        }
        LeftRightPair<String, List<String>> updateSqlPair = QueryUtils.transNamedParamSqlToParamSql(sql);
        sql = "select count(*) as checkExists from " + tableInfo.getTableName()
            + " where " + GeneralJsonObjectDao.buildFilterSqlByPkUseColumnName(tableInfo, null);
        LeftRightPair<String, List<String>> checkSqlPair = QueryUtils.transNamedParamSqlToParamSql(sql);
        int n = 0, insert = 0, update = 0;
        boolean exists = false;
        PreparedStatement insertStmt = null;
        PreparedStatement updateStmt = null;

        MetaColumn column = tableInfo.fetchGeneratedKey();
        boolean needFetchId = tableInfo.hasGeneratedKeys() && column!=null;
        try (PreparedStatement checkStmt = conn.prepareStatement(checkSqlPair.getLeft())) {
            if (StringUtils.isNotBlank(insertSqlPair.getLeft())){
                if(needFetchId){
                    insertStmt = conn.prepareStatement(insertSqlPair.getLeft(), Statement.RETURN_GENERATED_KEYS);
                } else {
                    insertStmt = conn.prepareStatement(insertSqlPair.getLeft());
                }
            }

            if (StringUtils.isNotBlank(updateSqlPair.getLeft())){
                updateStmt = conn.prepareStatement(updateSqlPair.getLeft());
            }

            if (insertStmt == null && updateStmt == null){
                throw new SQLException("sql语句不能为空！");
            }
            List<Map<String, Object>> savedObjects = new ArrayList<>(INT_BATCH_NUM);
            int rowCount = objects.size();
            int rowIndex = 0;
            for (Map<String, Object> object : objects) {

                Map<String, Object> objectForSave =
                    prepareObjectForSave(tableInfo,fieldsMap != null?
                        DataSetOptUtil.mapDataRow(bizModel, object, rowIndex, rowCount, fieldsMap.entrySet()) : object);

                rowIndex++;
                DatabaseAccess.setQueryStmtParameters(checkStmt, checkSqlPair.getRight(), objectForSave);
                ResultSet rs = checkStmt.executeQuery();
                exists = false;
                try {
                    Object obj = DatabaseAccess.fetchScalarObject(DatabaseAccess.fetchResultSetToObjectsList(rs));
                    if (obj != null) {
                        exists = NumberBaseOpt.castObjectToInteger(obj, 0) > 0;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                n++;
                if (exists) {
                    if(updateStmt != null ){
                        DatabaseAccess.setQueryStmtParameters(updateStmt, updateSqlPair.getRight(), objectForSave);
                        if (StringUtils.isNotBlank(updateSqlPair.getLeft())) {
                            update++;
                            updateStmt.addBatch();
                            if (update % INT_BATCH_NUM == 0) {
                                updateStmt.executeBatch();
                                updateStmt.clearBatch();
                            }
                        }
                    }
                } else if (insertStmt != null){
                    DatabaseAccess.setQueryStmtParameters(insertStmt, insertSqlPair.getRight(), objectForSave);
                    if(needFetchId){
                        savedObjects.add(object);
                    }
                    insert++;
                    insertStmt.addBatch();
                    if (insert % INT_BATCH_NUM == 0) {
                        insertStmt.executeBatch();
                        if(needFetchId){
                            ResultSet idRs = insertStmt.getGeneratedKeys();
                            fetchObjectsId(savedObjects, idRs, column);
                            savedObjects.clear();
                        }
                        insertStmt.clearBatch();
                    }
                }
            }

            if (updateStmt != null && update % INT_BATCH_NUM > 0){
                updateStmt.executeBatch();
                updateStmt.clearBatch();
            }

            if (insertStmt != null && insert % INT_BATCH_NUM > 0){
                insertStmt.executeBatch();
                if(needFetchId){
                    ResultSet idRs = insertStmt.getGeneratedKeys();
                    fetchObjectsId(savedObjects, idRs, column);
                    savedObjects.clear();
                }
                insertStmt.clearBatch();
            }
        } catch (SQLException e) {
            if (exists) {
                throw DatabaseAccess.createAccessException(updateSqlPair.getLeft(), e);
            } else {
                throw DatabaseAccess.createAccessException(insertSqlPair.getLeft(), e);
            }
        }finally {
            if (updateStmt != null){
                try {
                    updateStmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (insertStmt != null){
                try {
                    insertStmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return n;
    }

    private static Map<String, Object> prepareObjectForSave(TableInfo tableInfo, Map<String, Object> object) {
        for (TableField col : tableInfo.getColumns()) {
            Object fieldValue = object.get(col.getPropertyName());
            if (fieldValue != null) {
                switch (col.getFieldType()) {
                    case FieldType.DATE:
                        /*object.put(col.getPropertyName(), DatetimeOpt.castObjectToSqlDate(fieldValue));
                        break;*/
                    case FieldType.DATETIME:
                    case FieldType.TIMESTAMP:
                        object.put(col.getPropertyName(), DatetimeOpt.castObjectToSqlTimestamp(fieldValue));
                        break;
                    case FieldType.INTEGER:
                    case FieldType.LONG:
                        object.put(col.getPropertyName(), NumberBaseOpt.castObjectToLong(fieldValue));
                        break;
                    case FieldType.MONEY:
                        object.put(col.getPropertyName(), NumberBaseOpt.castObjectToBigDecimal(fieldValue));
                        break;
                    case FieldType.FLOAT:
                    case FieldType.DOUBLE:
                        object.put(col.getPropertyName(), NumberBaseOpt.castObjectToDouble(fieldValue));
                        break;
                    case FieldType.STRING:
                    case FieldType.TEXT:
                        object.put(col.getPropertyName(), StringBaseOpt.castObjectToString(fieldValue));
                        break;
                    case FieldType.BOOLEAN:
                        object.put(col.getPropertyName(),
                            BooleanBaseOpt.castObjectToBoolean(fieldValue, false) ?
                                BooleanBaseOpt.ONE_CHAR_TRUE : BooleanBaseOpt.ONE_CHAR_FALSE);
                        break;
                    case FieldType.BYTE_ARRAY:
                    case FieldType.FILE:
                        object.put(col.getPropertyName(), ByteBaseOpt.castObjectToBytes(fieldValue));
                        break;
                    default:
                        break;

                }
            }
        }
        return object;
    }
}
