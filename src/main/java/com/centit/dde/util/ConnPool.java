package com.centit.dde.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.centit.dde.po.DatabaseInfo;
import com.centit.support.database.config.DBType;
import com.centit.support.database.config.DirectConnDB;
import com.centit.support.utils.StringBaseOpt;

public class ConnPool {
    /**
     * 只能连接一个数据库，并且最多有 maxConn 连接，maxConn的默认值为1
     */
    private static final Log logger = LogFactory.getLog(ConnPool.class);

    private static int maxActive = 10;

    private static int maxIdle = 3;

    private static int maxWait = 1000;

    private static boolean defaultAutoCommit = true;

    private static boolean testOnBorrow = true;

    private static boolean removeAbandoned = true;

    private static int removeAbandonedTimeout = 1200;

    private static Map<DirectConnDB, BasicDataSource> connSpool = new HashMap<DirectConnDB, BasicDataSource>();

    // private static Log log = LogFactory.getLog(DBConn.class);

    private ConnPool() {

    }

    private static synchronized BasicDataSource newDataSource(DirectConnDB dbc) {
        BasicDataSource ds = new BasicDataSource();

        ds.setDriverClassName(dbc.getDriver());
        ds.setUrl(dbc.getUrl());
        ds.setUsername(dbc.getUser());
        ds.setPassword(dbc.getPassword());

        // 加载附加属性
        ds.setMaxActive(maxActive);
        ds.setMaxIdle(maxIdle);
        ds.setMaxWait(maxWait);
        ds.setDefaultAutoCommit(defaultAutoCommit);
        ds.setRemoveAbandoned(removeAbandoned);
        ds.setRemoveAbandonedTimeout(removeAbandonedTimeout);
        /**
         * 调取连接时检查有效性
         */
        ds.setTestOnBorrow(testOnBorrow);
        /**
         * 验证连接有效性的方式
         */
        ds.setValidationQuery(getValidationQuery(dbc.getUrl()));

        return ds;
    }

    public static BasicDataSource getDataSource(DatabaseInfo dbInfo) throws SQLException {
        DirectConnDB dbc = new DirectConnDB();
        dbc.praiseConnUrl(dbInfo.getDatabaseUrl());
        dbc.setUser(dbInfo.getUsername());
        dbc.setPassword(StringBaseOpt
                .decryptBase64Des(dbInfo.getPassword()));

        BasicDataSource ds = connSpool.get(dbc);
        if (ds == null) {
            ds = newDataSource(dbc);
            connSpool.put(dbc, ds);
        }
        return ds;
    }

    public static Connection getConn(DatabaseInfo dbInfo) throws SQLException {
        return getDataSource(dbInfo).getConnection();
    }

    public static Connection getConn(DirectConnDB dbc) throws SQLException {
        BasicDataSource ds = connSpool.get(dbc);
        if (ds == null) {
            ds = newDataSource(dbc);
            connSpool.put(dbc, ds);
        }
        return ds.getConnection();
    }

    public static synchronized void closeConnPool(DirectConnDB dbc) {
        BasicDataSource ds = connSpool.remove(dbc);
        if (ds != null) {
            try {
                ds.close();
            } catch (SQLException e) {

            }
        }
    }

    public static synchronized void closeConn(Connection conn) {
        try {
            if (null != conn && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            logger.error(e);
        }
    }

    public static synchronized void closeAll() {
        for (Map.Entry<DirectConnDB, BasicDataSource> ds : connSpool.entrySet()) {
            try {
                ds.getValue().close();
            } catch (SQLException e) {
                logger.error(e);
            }
        }
        connSpool.clear();
    }


    private static String getValidationQuery(String dbUrl) {
        DBType dbType = DBType.mapDBType(dbUrl);
        if (DBType.Oracle == dbType) {
            return "select 1 from dual";
        } else if (DBType.SqlServer == dbType || DBType.MySql == dbType) {
            return "select 1";
        }

        return "";
    }
}
