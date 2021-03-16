package com.centit.dde.transaction;

import com.alibaba.druid.pool.DruidDataSource;
import com.centit.support.database.utils.DataSourceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author zhf
 */
abstract class AbstractDruidConnectPools {
    private static final Logger logger = LoggerFactory.getLogger(AbstractDruidConnectPools.class);
    private static final
    Map<DataSourceDescription, DruidDataSource> DRUID_DATA_SOURCE_POOLS
        = new ConcurrentHashMap<>();

    private AbstractDruidConnectPools() {
        throw new IllegalAccessError("Utility class");
    }

    private static DruidDataSource mapDataSource(DataSourceDescription dsDesc) {
        DruidDataSource ds = new DruidDataSource();
        ds.setDriverClassName(dsDesc.getDriver());
        ds.setUsername(dsDesc.getUsername());
        ds.setPassword(dsDesc.getPassword());
        ds.setUrl(dsDesc.getConnUrl());
        ds.setInitialSize(dsDesc.getInitialSize());
        ds.setMaxActive(dsDesc.getMaxTotal());
        ds.setMaxWait(dsDesc.getMaxWaitMillis());
        ds.setMinIdle(dsDesc.getMinIdle());
        if(dsDesc.getExtProps()==null || dsDesc.getExtProps().get("validationQuery")==null){
            ds.setValidationQuery("select 1");
        }else {
            ds.setValidationQuery((String) dsDesc.getExtProps().get("validationQuery"));
        }
        ds.setTestWhileIdle(true);
        ds.setValidationQueryTimeout(1);
        ds.setTimeBetweenEvictionRunsMillis(60000);
        return ds;
    }

    private static synchronized DruidDataSource getDataSource(DataSourceDescription dsDesc) {
        DruidDataSource ds = DRUID_DATA_SOURCE_POOLS.get(dsDesc);
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
