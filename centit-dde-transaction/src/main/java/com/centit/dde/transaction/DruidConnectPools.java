package com.centit.dde.transaction;

import com.alibaba.druid.pool.DruidDataSource;
import com.centit.support.database.metadata.IDatabaseInfo;
import com.centit.support.database.utils.DataSourceDescription;
import com.centit.support.database.utils.DbcpConnectPools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author zhf
 */
abstract class DruidConnectPools {
    private static final Logger logger = LoggerFactory.getLogger(DruidConnectPools.class);
    private static final
    Map<DataSourceDescription, DruidDataSource > DRUID_DATA_SOURCE_POOLS
        = new ConcurrentHashMap<>();
    private DruidConnectPools() {
        throw new IllegalAccessError("Utility class");
    }

    private static DruidDataSource  mapDataSource(DataSourceDescription dsDesc) {
        DruidDataSource ds = new DruidDataSource ();
        ds.setDriverClassName(dsDesc.getDriver());
        ds.setUsername(dsDesc.getUsername());
        ds.setPassword(dsDesc.getPassword());
        ds.setUrl(dsDesc.getConnUrl());
        ds.setInitialSize(dsDesc.getInitialSize());
        ds.setMaxActive(dsDesc.getMaxTotal());
        ds.setMaxWait(dsDesc.getMaxWaitMillis());
        ds.setMinIdle(dsDesc.getMinIdle());
        ds.setValidationQuery("SELECT COUNT(*) FROM DUAL");
        ds.setTestOnBorrow(true);
        ds.setTestWhileIdle(true);
        ds.setTestOnReturn(false);
        ds.setValidationQueryTimeout(1);
        ds.setTimeBetweenEvictionRunsMillis(60000);
        return ds;
    }

    private static synchronized DruidDataSource  getDataSource(DataSourceDescription dsDesc) {
        DruidDataSource  ds = DRUID_DATA_SOURCE_POOLS.get(dsDesc);
        if (ds == null) {
            ds = mapDataSource(dsDesc);
            DRUID_DATA_SOURCE_POOLS.put(dsDesc, ds);
        }
        return ds;
    }

    static synchronized Connection getDbcpConnect(DataSourceDescription dsDesc) throws SQLException {
        DruidDataSource ds = getDataSource(dsDesc);
        Connection conn = ds.getConnection();
        conn.setAutoCommit(false);
        return conn;
    }

    public static DruidDataSource getDataSource(IDatabaseInfo dbInfo) {
        return DruidConnectPools.getDataSource(DataSourceDescription.valueOf(dbInfo));
    }

    public static Connection getDbcpConnect(IDatabaseInfo dbInfo) throws SQLException {
        return DbcpConnectPools.getDbcpConnect(DataSourceDescription.valueOf(dbInfo));
    }


    public static Map<String, Integer> getDataSourceStats(DataSourceDescription dsDesc) {
        DruidDataSource bds = DRUID_DATA_SOURCE_POOLS.get(dsDesc);
        if (bds == null) {
            return null;
        }
        Map<String, Integer> map = new HashMap<>(2);
        map.put("active_number", bds.getActiveCount());
        map.put("idle_number", bds.getMaxIdle());
        return map;
    }

    /**
     * 关闭数据源
     */
    public static synchronized void shutdownDataSource() {
        for (Map.Entry<DataSourceDescription, DruidDataSource> dbs : DRUID_DATA_SOURCE_POOLS.entrySet()) {
            dbs.getValue().close();
        }
    }

    public static synchronized boolean testDataSource(DataSourceDescription dsDesc) {
        DruidDataSource ds = mapDataSource(dsDesc);
        boolean connOk = false;
        try {
            Connection conn = ds.getConnection();
            if (conn != null) {
                connOk = true;
                conn.close();
            }
            ds.close();
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        } finally {
            ds.close();
        }
        return connOk;
    }

    static void closeConnect(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
}
