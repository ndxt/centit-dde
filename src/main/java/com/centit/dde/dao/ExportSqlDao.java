package com.centit.dde.dao;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.CollectionUtils;

import com.centit.core.dao.BaseDaoImpl;
import com.centit.core.dao.CodeBook;
import com.centit.core.dao.SQLUtils;
import com.centit.dde.exception.SqlResolveException;
import com.centit.dde.po.DatabaseInfo;
import com.centit.dde.po.ExportField;
import com.centit.dde.po.ExportFieldId;
import com.centit.dde.po.ExportSql;
import com.centit.dde.util.ConnPool;
import com.centit.support.database.config.DirectConn;
import com.centit.support.database.config.DirectConnDB;
import com.centit.support.utils.StringBaseOpt;

public class ExportSqlDao extends BaseDaoImpl<ExportSql> {
    private static final long serialVersionUID = 1L;

    public static final Log logger = LogFactory.getLog(ExportSqlDao.class);

    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<String, String>();

            filterField.put("exportId", CodeBook.EQUAL_HQL_ID);

            filterField.put("sourceDatabaseName", CodeBook.LIKE_HQL_ID);

            filterField.put("sourceOsId", CodeBook.LIKE_HQL_ID);

            filterField.put("exportName", CodeBook.LIKE_HQL_ID);
            filterField.put("eqExportName", "exportName = ?");

            filterField.put("querySql", CodeBook.LIKE_HQL_ID);

            filterField.put("created", CodeBook.LIKE_HQL_ID);

            filterField.put("afterSqlBlock", CodeBook.LIKE_HQL_ID);

            filterField.put("exportDesc", CodeBook.LIKE_HQL_ID);

            filterField.put("lastUpdateTime", CodeBook.LIKE_HQL_ID);

            filterField.put("createTime", CodeBook.LIKE_HQL_ID);

            filterField.put("beforeSqlBlock", CodeBook.LIKE_HQL_ID);

            filterField.put("mapinfoId", CodeBook.LIKE_HQL_ID);

        }
        return filterField;
    }

    public void flush() {
        getHibernateTemplate().flush();
    }

    @Override
    public void saveObject(ExportSql o) {
        if (null == o.getExportId()) {
            o.setExportId(getNextLongSequence());
        }
        super.saveObject(o);
    }

    public String getMapinfoName(Long mapinfoId) {
        String hql = "select t.exportName from ExportSql t where t.exportId=?" ;
        List<String> listObjects = getHibernateTemplate().find(hql, mapinfoId);

        if (!CollectionUtils.isEmpty(listObjects)) {
            return listObjects.get(0).toString();
        }
        return "";

    }
    public Long getNextLongSequence() {
        return getNextLongSequence("D_MAPINFOID");
    }

    public List<String> listDbTables(DatabaseInfo dbinfo) throws SqlResolveException {
        List<String> tableNames = new ArrayList<String>();
        Connection conn = null;
        try {
            conn = getConn(dbinfo);
            DatabaseMetaData dbMetaData = conn.getMetaData();

            ResultSet rs = dbMetaData.getTables(null, dbinfo.getUsername().toUpperCase(), null,
                    new String[]{"TABLE", "VIEW"});
            //

            while (rs.next()) {
                tableNames.add(rs.getString("TABLE_NAME"));
            }
        } catch (SQLException e) {
            logger.error("读取表和视图出错", e);

            throw new SqlResolveException(20000, e);
        } finally {
            ConnPool.closeConn(conn);
        }

        return tableNames;
    }

    /**
     * 验证sql执行的正确性
     *
     * @param object
     * @throws SqlResolveException
     */
    public void validateSql(String querysql, DatabaseInfo dbinfo) throws SqlResolveException {
        if (!DirectConn.testConn(new DirectConnDB(dbinfo.getDatabaseUrl(), dbinfo.getUsername(), StringBaseOpt
                .decryptBase64Des(dbinfo.getPassword())))) {
            logger.error("连接数据库出错");
            throw new SqlResolveException(10002);
        }

        Connection conn = null;
        Statement statement = null;
        ResultSet rs = null;
        try {
            conn = getConn(dbinfo);

            statement = conn.createStatement();

            rs = statement.executeQuery(querysql);

            rs.next();
        } catch (SQLException e) {
            logger.error("验证数据库Sql执行语句报错", e);

            throw new SqlResolveException(10001, e);
        } finally {
            ConnPool.closeConn(conn);
        }
    }

    public ExportSql getTableMetadata(ExportSql exportSql, DatabaseInfo dbinfo) throws SqlResolveException {
        Connection conn = null;
        Statement statement = null;
        ResultSet rs = null;
        try {
            conn = getConn(dbinfo);

            statement = conn.createStatement();

            rs = statement.executeQuery(exportSql.getQuerySql());

            //while (rs.next()) {
            ResultSetMetaData rsmd = rs.getMetaData();
            List<String> sqlFileds = SQLUtils.getSqlFileds(exportSql.getQuerySql());
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                String columnName = rsmd.getColumnName(i);
                //select * from talbeName 时sqlFields解析字段长度为1 * ，与rsmd.getColumnCount() 解析长度不一致
                ExportField ef = new ExportField(new ExportFieldId(exportSql.getExportId(), (long) (i - 1)), columnName,
                        sqlFileds.size() != rsmd.getColumnCount() ? columnName : sqlFileds.get(i - 1));
                ef.setFieldType(rsmd.getColumnTypeName(i));
                ef.setIsNull(ResultSetMetaData.columnNoNulls == rsmd.isNullable(i) ? "0" : "1");

                exportSql.addExportField(ef);
            }

            return exportSql;
            //}
        } catch (SQLException e) {
            logger.error("读取表中的数据列及数据类型出错", e);
            throw new SqlResolveException(20000, e);
        } finally {
            ConnPool.closeConn(conn);
        }

        //return null;
    }


    private Connection getConn(DatabaseInfo dbinfo) throws SQLException {
        DirectConnDB connDb = new DirectConnDB();
        connDb.praiseConnUrl(dbinfo.getDatabaseUrl());
        connDb.setUser(dbinfo.getUsername());
        connDb.setPassword(StringBaseOpt.decryptBase64Des(dbinfo.getPassword()));
        return ConnPool.getConn(connDb);
    }


    public ExportSql fetchObjectById(Long exportId) {
        String hql = "select distinct s from ExportSql s join fetch s.exportTriggers join fetch s.exportFields where s.exportId = ?";

        List<ExportSql> listObjects = getHibernateTemplate().find(hql, exportId);

        if (!CollectionUtils.isEmpty(listObjects)) {
            return listObjects.get(0);
        }

        hql = "select distinct s from ExportSql s join fetch s.exportFields where s.exportId = ?";

        listObjects = getHibernateTemplate().find(hql, exportId);

        if (CollectionUtils.isEmpty(listObjects)) {
            ExportSql exportSql = getObjectById(exportId);
            exportSql.setExportTriggers(null);
            exportSql.setExportFields(null);

            return exportSql;
        }

        ExportSql exportSql = listObjects.get(0);
        exportSql.setExportTriggers(null);
        return exportSql;
    }
}
