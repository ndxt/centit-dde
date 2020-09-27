package com.centit.product.dataopt.utils;

import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.common.LeftRightPair;
import com.centit.support.database.jsonmaptable.GeneralJsonObjectDao;
import com.centit.support.database.metadata.TableInfo;
import com.centit.support.database.utils.DatabaseAccess;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * 数据库批量操作 工具类，
 * 目的是为了提高批量处理的效率
 */
public abstract class DBBatchUtils {
    protected static final Logger logger = LoggerFactory.getLogger(DBBatchUtils.class);

    public static List<String> achieveAllFields(final List<Map<String, Object>> objects){
        HashSet<String> fields = new HashSet<>();
        for(Map<String, Object> map : objects){
            fields.addAll(map.keySet());
        }
        return new ArrayList<>(fields);
    }

    public static int batchInsertObjects(final Connection conn,
                                   final TableInfo tableInfo,
                                   final List<Map<String, Object>> objects) throws SQLException {
        List<String> fields = achieveAllFields( objects);
        String sql = GeneralJsonObjectDao.buildInsertSql(tableInfo, fields);
        LeftRightPair<String,List<String>> sqlPair = QueryUtils.transNamedParamSqlToParamSql(sql);
        int n = 0;
        QueryLogUtils.printSql(logger,sqlPair.getLeft(), sqlPair.getRight());
        try(PreparedStatement stmt = conn.prepareStatement(sqlPair.getLeft())){
            for(Map<String, Object> object : objects ) {
                DatabaseAccess.setQueryStmtParameters(stmt, sqlPair.getRight(), object);
                n += stmt.executeUpdate();
            }
        }catch (SQLException e) {
            throw DatabaseAccess.createAccessException(sqlPair.getLeft(), e);
        }
        return n;
    }

    public static int batchUpdateObjects(final Connection conn,
                                  final TableInfo tableInfo,
                                  //final Collection<String> fields,
                                  final List<Map<String, Object>> objects) throws SQLException {
        // 这个要重写，需要重新拼写sql语句， 直接拼写为？参数的sql语句据
        List<String> fields = achieveAllFields( objects);
        String sql = GeneralJsonObjectDao.buildUpdateSql(tableInfo, fields) +
            " where " +  GeneralJsonObjectDao.buildFilterSqlByPk(tableInfo,null);
        LeftRightPair<String,List<String>> sqlPair = QueryUtils.transNamedParamSqlToParamSql(sql);
        int n = 0;
        QueryLogUtils.printSql(logger,sqlPair.getLeft(), sqlPair.getRight());
        try(PreparedStatement stmt = conn.prepareStatement(sqlPair.getLeft())){
            for(Map<String, Object> object : objects ) {
//                if (!GeneralJsonObjectDao.checkHasAllPkColumns(tableInfo, object)) {
//                    throw new SQLException("缺少主键对应的属性。");
//                }
                DatabaseAccess.setQueryStmtParameters(stmt, sqlPair.getRight(), object);
                n += stmt.executeUpdate();
            }
        }catch (SQLException e) {
            throw DatabaseAccess.createAccessException(sqlPair.getLeft(), e);
        }
        return n;
    }

    public static int batchMergeObjects(final Connection conn,
                                  final TableInfo tableInfo,
                                  final List<Map<String, Object>> objects) throws SQLException {
        List<String> fields = achieveAllFields( objects);
        String sql = GeneralJsonObjectDao.buildInsertSql(tableInfo, fields);
        LeftRightPair<String,List<String>> insertSqlPair = QueryUtils.transNamedParamSqlToParamSql(sql);
        sql = GeneralJsonObjectDao.buildUpdateSql(tableInfo, fields);
        if(null!=sql) {
             sql+=" where " + GeneralJsonObjectDao.buildFilterSqlByPk(tableInfo, null);
        }else{
            sql="";
        }
        LeftRightPair<String,List<String>> updateSqlPair = QueryUtils.transNamedParamSqlToParamSql(sql);
        sql = "select count(*) as checkExists from " + tableInfo.getTableName()
            + " where " +  GeneralJsonObjectDao.buildFilterSqlByPk(tableInfo,null);
        LeftRightPair<String,List<String>> checkSqlPair = QueryUtils.transNamedParamSqlToParamSql(sql);
        int n =0;
        try(PreparedStatement checkStmt = conn.prepareStatement(checkSqlPair.getLeft());
            PreparedStatement insertStmt = conn.prepareStatement(insertSqlPair.getLeft());
            PreparedStatement updateStmt = conn.prepareStatement(updateSqlPair.getLeft())){
            for(Map<String, Object> object : objects ) {
//                if (!GeneralJsonObjectDao.checkHasAllPkColumns(tableInfo, object)) {
//                    throw new SQLException("缺少主键对应的属性。");
//                }
                DatabaseAccess.setQueryStmtParameters(checkStmt, checkSqlPair.getRight(), object);
                ResultSet rs = checkStmt.executeQuery();
                boolean exists = false;
                try {
                    Object obj = DatabaseAccess.fetchScalarObject(DatabaseAccess.fetchResultSetToObjectsList(rs));
                    if(obj!=null){
                        exists = NumberBaseOpt.castObjectToInteger(obj, 0) > 0;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(exists){
                    DatabaseAccess.setQueryStmtParameters(updateStmt, updateSqlPair.getRight(), object);
                    if(StringUtils.isNotBlank(updateSqlPair.getLeft())) {
                        n += updateStmt.executeUpdate();
                    }
                }else{
                    DatabaseAccess.setQueryStmtParameters(insertStmt, insertSqlPair.getRight(), object);
                    n += insertStmt.executeUpdate();
                }
            }
        }catch (SQLException e) {
            throw DatabaseAccess.createAccessException(insertSqlPair.getLeft(), e);
        }
        return n;
    }
}
