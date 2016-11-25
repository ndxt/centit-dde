package com.centit.dde.dao;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSONArray;
import com.centit.dde.po.MapinfoDetail;
import com.centit.dde.po.MapinfoDetailId;
import com.centit.dde.util.ConnPool;
import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.hibernate.dao.BaseDaoImpl;
import com.centit.framework.hibernate.dao.DatabaseOptUtils;
import com.centit.support.database.DbcpConnect;
import com.centit.support.database.QueryUtils;
import com.centit.framework.staticsystem.po.DatabaseInfo;

public class MapinfoDetailDao extends BaseDaoImpl<MapinfoDetail,MapinfoDetailId> {
    
    public static final Log log = LogFactory.getLog(MapinfoDetailDao.class);

	/*
     * private Connection connSource = null ; private Connection connGoal = null
	 * ; private Statement stmtSource = null; private Statement stmtGoal = null;
	 * private PreparedStatement pstmtSource = null ; private PreparedStatement
	 * pstmtGoal = null ; private ResultSet rsSource = null ; private ResultSet
	 * rsGoal = null ; private ResultSetMetaData rsmdSource = null ; private
	 * ResultSetMetaData rsmdGoal = null ;
	 */

    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<String, String>();

            filterField.put("mapinfoId", CodeBook.EQUAL_HQL_ID);

            filterField.put("columnNo", CodeBook.EQUAL_HQL_ID);

            filterField.put("orderNo", CodeBook.LIKE_HQL_ID);

            filterField.put("sourceFieldName", CodeBook.LIKE_HQL_ID);

            filterField.put("sourceFieldSentence", CodeBook.LIKE_HQL_ID);

            filterField.put("sourceFieldType", CodeBook.LIKE_HQL_ID);

            filterField.put("destFieldName", CodeBook.LIKE_HQL_ID);

            filterField.put("destFieldType", CodeBook.LIKE_HQL_ID);

            filterField.put("isPk", CodeBook.LIKE_HQL_ID);

            filterField.put("destFieldDefault", CodeBook.LIKE_HQL_ID);

            filterField.put("isNull", CodeBook.LIKE_HQL_ID);

        }
        return filterField;
    }

    /**
     * @param
     * @return 查询数据库中的所有表
     * @throws SQLException
     */
    public List<String> getTables(DatabaseInfo databaseInfo, String dataBaseType) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSetMetaData rsmd = null;
        ResultSet rs = null;
        String sql = null;
        List<String> datas = new ArrayList<String>();
       
        try {
            conn = ConnPool.getConn(databaseInfo);
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            rsmd = pstmt.getMetaData();
            int columnCount = rsmd.getColumnCount();

            // 循环结果集
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    datas.add((String) rs.getObject(rsmd.getColumnLabel(i)));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return datas;
    }

    public List<Object> getTable(DatabaseInfo databaseInfo, String dataBaseType) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSetMetaData rsmd = null;
        ResultSet rs = null;
        String sql = null;

        List<Object> datas = new ArrayList<Object>();
       
        try {
            conn = ConnPool.getConn(databaseInfo);
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            rsmd = pstmt.getMetaData();
            int columnCount = rsmd.getColumnCount();

            // 循环结果集
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    List<Object> data = new ArrayList<Object>();
                    String[] dataTemp = {(String) rs.getObject(rsmd.getColumnLabel(i)),
                            (String) rs.getObject(rsmd.getColumnLabel(i))};
                    data.add(dataTemp);
                    datas.add(dataTemp);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                pstmt.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return datas;
    }

    /**
     * @param
     * @return 获取源表的字段，字段类型等信息
     */
    public List<Map<String, String>> getSourceTableStruct(DatabaseInfo sourceDatabaseInfo, String sourcetableName) {
        DbcpConnect connSource = null;
        PreparedStatement pstmtSource = null;
        ResultSet rsSource = null;
        ResultSetMetaData rsmdSource = null;
        List<Map<String, String>> datas = new ArrayList<Map<String, String>>();
        ArrayList<String> keyList = new ArrayList<String>();

        try {
          
            connSource =ConnPool.getConn(sourceDatabaseInfo);
            DatabaseMetaData meta = connSource.getMetaData();
            ResultSet rsKey = meta.getPrimaryKeys(null, null, sourcetableName);
            while (rsKey.next()) {
                keyList.add(rsKey.getString(4));
            }
            switch(connSource.getDatabaseType()){
            case SqlServer:
                pstmtSource = connSource.prepareStatement( "select top 1 * from " + sourcetableName);
                break;
            case Oracle:
                pstmtSource = connSource.prepareStatement( "select * from " + sourcetableName+" where rownum<2");
                break;
            case MySql:
                pstmtSource = connSource.prepareStatement( "select * from " + sourcetableName+" limit 1");
                break;
            case DB2:
                pstmtSource = connSource.prepareStatement( "select * from " + sourcetableName+" fetch first 1 row only");
                break;
            default:
                pstmtSource = connSource.prepareStatement( "select * from " + sourcetableName);
                break;				
            }
          
            rsSource = pstmtSource.executeQuery();
            rsmdSource = pstmtSource.getMetaData();
            int columnCount = rsmdSource.getColumnCount();
            Map<String, String> data = null;

            for (int i = 1; i <= columnCount; i++) {
                data = new HashMap<String, String>();
                data.put("COLUMNNAME", rsmdSource.getColumnName(i));
                data.put(
                        "COLUMNTYPE",
                        getColumnType(rsmdSource.getColumnTypeName(i), rsmdSource.getPrecision(i),
                                rsmdSource.getScale(i)));
                data.put("ISNULLABLE", String.valueOf(rsmdSource.isNullable(i)));
                if (keyList.contains(rsmdSource.getColumnName(i))) {
                    data.put("ISPK", "1");
                } else {
                    data.put("ISPK", "0");
                }
                datas.add(data);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rsSource != null)
                    rsSource.close();
                if (pstmtSource != null)
                    pstmtSource.close();
                if (connSource != null)
                    connSource.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return datas;
    }


    public JSONArray getSourceTableStructFromDatabase(Long mapinfoId) {
        String sql = "select t.source_field_name as ColumnName,t.source_field_type as ColumnType,t.source_field_sentence as SOURCECOLUMNSENTENCE from D_MAPINFO_DETAIL t where t.mapinfo_id="
                + mapinfoId + " order by t.column_no";
        return  DatabaseOptUtils.findObjectsAsJSonBySql(this, sql);
    }

    public JSONArray getGoalTableStructFromDatabase(Long mapinfoId) {
        String sql = "select t.dest_field_name as ColumnName,t.dest_field_type as ColumnType,t.is_pk as isPk,t.is_null as isNullable,t.dest_field_default as destfielddefault from D_MAPINFO_DETAIL "
                + "t where t.mapinfo_id=" + mapinfoId + " order by t.column_no";

        return  DatabaseOptUtils.findObjectsAsJSonBySql(this, sql);
    }

    /**
     * @param
     * @return 获取目标表的字段，字段类型等信息
     */
    public List<Map<String, String>> getGoalTableStruct(DatabaseInfo goalDatabaseInfo, String GoaltableName) {
        Connection connGoal = null;
        PreparedStatement pstmtGoal = null;
        ResultSet rsGoal = null;
        ResultSetMetaData rsmdGoal = null;
        List<Map<String, String>> datas = new ArrayList<Map<String, String>>();
        ArrayList<String> keyList = new ArrayList<String>();
        String sql = "select * from " + GoaltableName;
        try {
           
            connGoal = ConnPool.getConn(goalDatabaseInfo);
            DatabaseMetaData meta = connGoal.getMetaData();
            ResultSet rsKey = meta.getPrimaryKeys(null, null, GoaltableName);
            // String keyColumn = null;
            while (rsKey.next()) {
                keyList.add(rsKey.getString(4));
            }
            pstmtGoal = connGoal.prepareStatement(sql);
            rsGoal = pstmtGoal.executeQuery();
            rsmdGoal = pstmtGoal.getMetaData();
            int columnCount = rsmdGoal.getColumnCount();

            Map<String, String> data = null;

            for (int i = 1; i <= columnCount; i++) {
                data = new HashMap<String, String>();
                data.put("COLUMNNAME", rsmdGoal.getColumnName(i));
                data.put("COLUMNTYPE",
                        getColumnType(rsmdGoal.getColumnTypeName(i), rsmdGoal.getPrecision(i), rsmdGoal.getScale(i)));
                data.put("ISNULLABLE", String.valueOf(rsmdGoal.isNullable(i)));
                if (keyList.contains(rsmdGoal.getColumnName(i))) {
                    data.put("ISPK", "1");
                } else {
                    data.put("ISPK", "0");
                }

                datas.add(data);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rsGoal != null)
                    rsGoal.close();
                if (pstmtGoal != null)
                    pstmtGoal.close();
                if (connGoal != null)
                    connGoal.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return datas;
    }

    /**
     * @param ColumnTypeName ,Precision,Scale
     * @return ColumnType 通过字段类型名、字段长度、字段精度拼接字段类型，
     *         如：ColumnTypeName为number，Precision为10，Scale为2的拼接结果为NUMBER(10,2)
     */
    private static String getColumnType(String ColumnTypeName, int Precision, int Scale) {
        String ColumnType = null;
        if (ColumnTypeName.toUpperCase().equals("NUMBER") || ColumnTypeName.toUpperCase().equals("NUMERIC")
                || ColumnTypeName.toUpperCase().equals("DECIMAL")) {
            if (Precision == 0) {
                ColumnType = ColumnTypeName.toUpperCase();
            } else {
                if (Scale != -127) {
                    ColumnType = ColumnTypeName.toUpperCase() + "(" + Precision + "," + Scale + ")";
                } else {
                    ColumnType = ColumnTypeName.toUpperCase() + "(" + Precision + ")";
                }
            }
        } else if (Precision != 0) {
            ColumnType = ColumnTypeName.toUpperCase() + "(" + Precision + ")";
        } else {
            ColumnType = ColumnTypeName.toUpperCase();
        }
        return ColumnType;
    }

    /**
     * 保存对应关系明细前，先删除相关的明细，一面主键冲突。
     */
    public void deleteMapinfoDetails(Long mapinfoId) {
        // this.deleteObjectById(mapinfoId);

        String hql = "delete from MapinfoDetail d where d.cid.mapinfoId = ?";

        DatabaseOptUtils.doExecuteHql(this,hql, mapinfoId);
    }

    /**
     * 保存对应关系明细前，先删除相关的明细，一面主键冲突。
     */
    public void updateExchangeMapinfo(Long mapinfoId, String soueceTableName, String goalTableName, String createSql) {
        String sql = "update D_EXCHANGE_MAPINFO t set t.source_tablename="
                + QueryUtils.buildStringForQuery(soueceTableName) + ",t.dest_tablename="
                + QueryUtils.buildStringForQuery(goalTableName) + ",t.QUERY_SQL="
                + QueryUtils.buildStringForQuery(createSql) + " where t.mapinfo_id=" + mapinfoId;
        DatabaseOptUtils.doExecuteHql(this,sql);
    }

    @SuppressWarnings("unchecked")
    public void updateSourceColumnSentence(Map<String, Object> structs, String mapinfoId) {
        List<Object> value = (List<Object>) structs.get("sStrsAndsFieldDescs");
        List<String> sStrs = (List<String>) value.get(0);
        List<String> sFieldDescs = (List<String>) value.get(1);
        long length = this.countSourceField();
        if (sStrs.size() > length) {
            int i = 0;
            while (i < length) {
                String sql = "update d_mapinfo_detail t set t.source_field_sentence="
                        + QueryUtils.buildStringForQuery(sFieldDescs.get(i)) + ",t.source_field_name="
                        + QueryUtils.buildStringForQuery(sStrs.get(i)) + "  where t.column_no=" + (i + 1)
                        + " and t.mapinfo_id=" + mapinfoId;
                DatabaseOptUtils.doExecuteHql(this,sql);
                i++;
            }
            while (i >= length && i < sStrs.size()) {
                String sql = "insert into d_mapinfo_detail (mapinfo_id,column_no,source_field_name,source_field_sentence) values ("
                        + mapinfoId
                        + ","
                        + (i + 1)
                        + ","
                        + QueryUtils.buildStringForQuery(sFieldDescs.get(i))
                        + ","
                        + QueryUtils.buildStringForQuery(sStrs.get(i)) + ")";
                DatabaseOptUtils.doExecuteHql(this,sql);
                i++;
            }

        } else {
            for (int i = 0; i < length; i++) {
                for (int j = sStrs.size(); j < length; j++) {
                    sFieldDescs.add(j, null);
                    sStrs.add(j, null);
                }
                String sql = "update d_mapinfo_detail t set t.source_field_sentence="
                        + QueryUtils.buildStringForQuery(sFieldDescs.get(i)) + ",t.source_field_name="
                        + QueryUtils.buildStringForQuery(sStrs.get(i)) + " where t.column_no=" + (i + 1)
                        + " and t.mapinfo_id=" + mapinfoId;
                DatabaseOptUtils.doExecuteHql(this,sql);
            }
        }
    }

    private long countSourceField() {
        String hql = "select count(t.sourceFieldName) as length from MapinfoDetail t";

        return DatabaseOptUtils.getSingleIntByHql(this,hql);
    }

    public Long getMapinfoId() {
        return DatabaseOptUtils.getNextLongSequence(this,"D_MAPINFOID");
    }

    @SuppressWarnings("unchecked")
    public List<String> getGoalColumnStrut(Long mapinfoId) {
        String sql = "select t.dest_field_name from D_MAPINFO_DETAIL t where t.mapinfo_id=" + mapinfoId
                + " order by t.column_no";
        return (List<String>) DatabaseOptUtils.findObjectsBySql(this,sql);

    }

    public void saveMapinfoDetails(MapinfoDetail mapinfoDetail) {
        DatabaseOptUtils.flush(this.getCurrentSession());
        this.saveObject(mapinfoDetail);
    }

}
