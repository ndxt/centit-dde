package com.centit.dde.utils;

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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    public static int batchInsertObjects(final Connection conn,
                                         final TableInfo tableInfo,
                                         final List<Map<String, Object>> objects, Map fieldsMap) throws SQLException {
        List<String> fields = new ArrayList<>();
        if (fieldsMap == null) {
            fields = achieveAllFields(objects);
        } else {
            Collections.addAll(fields, (String[]) fieldsMap.keySet().toArray(new String[0]));
        }
        String sql = GeneralJsonObjectDao.buildInsertSql(tableInfo, fields);
        LeftRightPair<String, List<String>> sqlPair = QueryUtils.transNamedParamSqlToParamSql(sql);
        int n = 0;
        QueryLogUtils.printSql(logger, sqlPair.getLeft(), sqlPair.getRight());
        try (PreparedStatement stmt = conn.prepareStatement(sqlPair.getLeft())) {
            for (Map<String, Object> object : objects) {
                if (fieldsMap != null) {
                    object = DataSetOptUtil.mapDataRow(object, fieldsMap.entrySet());
                    prepareObjectForSave(tableInfo, object);
                }
                DatabaseAccess.setQueryStmtParameters(stmt, sqlPair.getRight(), object);
                n++;
                stmt.addBatch();
                if (n % INT_BATCH_NUM == 0) {
                    stmt.executeBatch();
                    stmt.clearBatch();
                }
            }
            stmt.executeBatch();
            stmt.clearBatch();
        } catch (SQLException e) {
            throw DatabaseAccess.createAccessException(sqlPair.getLeft(), e);
        }
        return n;
    }

    public static int batchUpdateObjects(final Connection conn,
                                         final TableInfo tableInfo,
                                         //final Collection<String> fields,
                                         final List<Map<String, Object>> objects, Map fieldsMap) throws SQLException {
        // 这个要重写，需要重新拼写sql语句， 直接拼写为？参数的sql语句据
        List<String> fields = new ArrayList<>();
        if (fieldsMap == null) {
            fields = achieveAllFields(objects);
        } else {
            Collections.addAll(fields, (String[]) fieldsMap.keySet().toArray(new String[0]));
        }
        String sql = GeneralJsonObjectDao.buildUpdateSql(tableInfo, fields) +
            " where " + GeneralJsonObjectDao.buildFilterSqlByPk(tableInfo, null);
        LeftRightPair<String, List<String>> sqlPair = QueryUtils.transNamedParamSqlToParamSql(sql);
        int n = 0;
        QueryLogUtils.printSql(logger, sqlPair.getLeft(), sqlPair.getRight());
        try (PreparedStatement stmt = conn.prepareStatement(sqlPair.getLeft())) {
            for (Map<String, Object> object : objects) {
                if (fieldsMap != null) {
                    object = DataSetOptUtil.mapDataRow(object, fieldsMap.entrySet());
                }
                DatabaseAccess.setQueryStmtParameters(stmt, sqlPair.getRight(), object);
                n += stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw DatabaseAccess.createAccessException(sqlPair.getLeft(), e);
        }
        return n;
    }

    public static int batchMergeObjects(final Connection conn,
                                        final TableInfo tableInfo,
                                        final List<Map<String, Object>> objects, Map fieldsMap) throws SQLException {
        List<String> fields = new ArrayList<>();
        if (fieldsMap == null) {
            fields = achieveAllFields(objects);
        } else {
            Collections.addAll(fields, (String[]) fieldsMap.keySet().toArray(new String[0]));
        }
        String sql = GeneralJsonObjectDao.buildInsertSql(tableInfo, fields);
        LeftRightPair<String, List<String>> insertSqlPair = QueryUtils.transNamedParamSqlToParamSql(sql);
        sql = GeneralJsonObjectDao.buildUpdateSql(tableInfo, fields);
        if (null != sql) {
            sql += " where " + GeneralJsonObjectDao.buildFilterSqlByPk(tableInfo, null);
        } else {
            sql = "";
        }
        LeftRightPair<String, List<String>> updateSqlPair = QueryUtils.transNamedParamSqlToParamSql(sql);
        sql = "select count(*) as checkExists from " + tableInfo.getTableName()
            + " where " + GeneralJsonObjectDao.buildFilterSqlByPk(tableInfo, null);
        LeftRightPair<String, List<String>> checkSqlPair = QueryUtils.transNamedParamSqlToParamSql(sql);
        int n = 0, insert = 0, update = 0;
        boolean exists = false;
        try (PreparedStatement checkStmt = conn.prepareStatement(checkSqlPair.getLeft());
             PreparedStatement insertStmt = conn.prepareStatement(insertSqlPair.getLeft());
             PreparedStatement updateStmt = conn.prepareStatement(updateSqlPair.getLeft())) {
            for (Map<String, Object> object : objects) {
                if (fieldsMap != null) {
                    object = DataSetOptUtil.mapDataRow(object, fieldsMap.entrySet());
                    prepareObjectForSave(tableInfo, object);
                }
                DatabaseAccess.setQueryStmtParameters(checkStmt, checkSqlPair.getRight(), object);
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
                    DatabaseAccess.setQueryStmtParameters(updateStmt, updateSqlPair.getRight(), object);
                    if (StringUtils.isNotBlank(updateSqlPair.getLeft())) {
                        update++;
                        updateStmt.addBatch();
                        if (update % INT_BATCH_NUM == 0) {
                            updateStmt.executeBatch();
                            updateStmt.clearBatch();
                        }
                    }
                } else {
                    DatabaseAccess.setQueryStmtParameters(insertStmt, insertSqlPair.getRight(), object);
                    insert++;
                    insertStmt.addBatch();
                    if (insert % INT_BATCH_NUM == 0) {
                        insertStmt.executeBatch();
                        insertStmt.clearBatch();
                    }
                }
            }
            updateStmt.executeBatch();
            updateStmt.clearBatch();
            insertStmt.executeBatch();
            insertStmt.clearBatch();
        } catch (SQLException e) {
            if (exists) {
                throw DatabaseAccess.createAccessException(updateSqlPair.getLeft(), e);
            } else {
                throw DatabaseAccess.createAccessException(insertSqlPair.getLeft(), e);
            }
        }
        return n;
    }


    private static void prepareObjectForSave(TableInfo tableInfo, Map<String, Object> object) {
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
                    default:
                        break;

                }
            }
        }
    }
}
