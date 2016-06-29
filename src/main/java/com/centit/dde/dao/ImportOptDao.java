package com.centit.dde.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.CollectionUtils;

import com.centit.core.dao.BaseDaoImpl;
import com.centit.core.dao.CodeBook;
import com.centit.dde.exception.SqlResolveException;
import com.centit.dde.po.DatabaseInfo;
import com.centit.dde.po.ImportOpt;
import com.centit.dde.util.ConnPool;
import com.centit.support.database.config.DirectConnDB;
import com.centit.support.utils.StringBaseOpt;

public class ImportOptDao extends BaseDaoImpl<ImportOpt> {
    private static final long serialVersionUID = 1L;

    public static final Log log = LogFactory.getLog(ImportOptDao.class);

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
        return getNextLongSequence("D_MAPINFOID");
    }
    public String getMapinfoName(Long mapinfoId) {
        String hql = "select t.importName from ImportOpt t where t.importId=?" ;
        List<String> listObjects = getHibernateTemplate().find(hql, mapinfoId);

        if (!CollectionUtils.isEmpty(listObjects)) {
            return listObjects.get(0).toString();
        }
        return "";

    }
    public void flush() {
        getHibernateTemplate().flush();

    }

    /**
     * 数据库中表是否存在
     *
     * @param importOpt
     * @param dbinfo
     * @return
     * @throws SqlResolveException
     */
    public boolean isExistsForTable(ImportOpt importOpt, DatabaseInfo dbinfo) throws SQLException {
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
            throw e;
        } finally {
            ConnPool.closeConn(conn);
        }
        return false;
    }

    private Connection getConn(DatabaseInfo dbinfo) throws SQLException {
        DirectConnDB connDb = new DirectConnDB();
        connDb.praiseConnUrl(dbinfo.getDatabaseUrl());
        connDb.setUser(dbinfo.getUsername());
        connDb.setPassword(StringBaseOpt.decryptBase64Des(dbinfo.getPassword()));
        return ConnPool.getConn(connDb);
    }
}
