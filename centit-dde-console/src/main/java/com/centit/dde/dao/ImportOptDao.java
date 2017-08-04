package com.centit.dde.dao;

import com.centit.dde.exception.SqlResolveException;
import com.centit.dde.po.ImportOpt;
import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.hibernate.dao.BaseDaoImpl;
import com.centit.framework.hibernate.dao.DatabaseOptUtils;
import com.centit.framework.ip.po.DatabaseInfo;
import com.centit.support.database.DataSourceDescription;
import com.centit.support.database.DbcpConnectPools;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ImportOptDao extends BaseDaoImpl<ImportOpt,Long> {

    public static final Log log = LogFactory.getLog(ImportOptDao.class);
    @Override
    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<String, String>();

            filterField.put("importId", CodeBook.EQUAL_HQL_ID);

            filterField.put("destDatabaseName", CodeBook.LIKE_HQL_ID);

            filterField.put("sourceOsId", CodeBook.LIKE_HQL_ID);

            filterField.put("importName", CodeBook.LIKE_HQL_ID);
            filterField.put("importNameEq", "importName = ?");

            filterField.put("tableName", CodeBook.LIKE_HQL_ID);

            filterField.put("created", CodeBook.LIKE_HQL_ID);

            filterField.put("afterImportBlock", CodeBook.LIKE_HQL_ID);

            filterField.put("beforeImportBlock", CodeBook.LIKE_HQL_ID);

            filterField.put("exportDesc", CodeBook.LIKE_HQL_ID);

            filterField.put("lastUpdateTime", CodeBook.LIKE_HQL_ID);

            filterField.put("createTime", CodeBook.LIKE_HQL_ID);

        }
        return filterField;
    }

    public Long getNextLongSequence() {
        return DatabaseOptUtils.getNextLongSequence(this,"D_MAPINFOID");
    }
    public String getMapinfoName(Long mapinfoId) {
        String hql = "select t.importName from ImportOpt t where t.importId=?" ;
        @SuppressWarnings("unchecked")
        List<String> listObjects = (List<String>)DatabaseOptUtils.findObjectsByHql(
                this,hql, new Object[]{mapinfoId});

        if (!CollectionUtils.isEmpty(listObjects)) {
            return listObjects.get(0).toString();
        }
        return "";

    }
    public void flush() {
        DatabaseOptUtils.flush(this.getCurrentSession());

    }

    /**
     * 数据库中表是否存在
     *
     * @param importOpt
     * @param dbinfo
     * @return
     * @throws SqlResolveException
     */
    public boolean isExistsForTable(ImportOpt importOpt, DatabaseInfo dbinfo){
        Connection conn = null;
        try {
            conn = getConn(dbinfo);

            String sql = "select * from user_tables where table_name = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, importOpt.getTableName());

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            log.error("数据库连接出错");
        } finally {
            try {
                conn.close();
            }catch (SQLException se){
                log.error("关闭连接出错",se);
            }
        }
        return false;
    }

    private static Connection getConn(DatabaseInfo dbinfo) throws SQLException {
        DataSourceDescription dbc = new DataSourceDescription();
        dbc.setDatabaseCode(dbinfo.getDatabaseCode());
        dbc.setConnUrl(dbinfo.getDatabaseUrl());
        dbc.setUsername(dbinfo.getUsername());
//        dbc.setPassword(dbinfo.getClearPassword());
        dbc.setPassword(dbinfo.getPassword());
        return DbcpConnectPools.getDbcpConnect(dbc);
    }
}
