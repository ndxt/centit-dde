package com.centit.dde.transaction;

import com.alibaba.druid.pool.DruidDataSource;
import com.centit.dde.transaction.vo.ISourceInfo;
import com.centit.support.algorithm.BooleanBaseOpt;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.database.utils.DBType;
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
    Map<ISourceInfo, DruidDataSource> DRUID_DATA_SOURCE_POOLS
        = new ConcurrentHashMap<>();

    private AbstractDruidConnectPools() {
        throw new IllegalAccessError("Utility class");
    }

    private static DruidDataSource mapDataSource(ISourceInfo dsDesc) {
        DruidDataSource ds = new DruidDataSource();
        ds.setDriverClassName(DBType.getDbDriver(DBType.mapDBType(dsDesc.getDatabaseUrl())));
        ds.setUsername(dsDesc.getUsername());
        ds.setPassword(dsDesc.getClearPassword());
        ds.setUrl(dsDesc.getDatabaseUrl());
        ds.setInitialSize(NumberBaseOpt.castObjectToInteger(
            dsDesc.getExtProp("initialSize"), 5));
        ds.setMaxActive(NumberBaseOpt.castObjectToInteger(
            dsDesc.getExtProp("maxTotal"), 10));
        ds.setMaxWait(NumberBaseOpt.castObjectToInteger(
            dsDesc.getExtProp("maxWaitMillis"), 10000));
        ds.setMinIdle(NumberBaseOpt.castObjectToInteger(
            dsDesc.getExtProp("minIdle"), 5));
        ds.setValidationQuery(StringBaseOpt.castObjectToString(dsDesc.getExtProp("validationQuery"),
            "select 1"));
        ds.setTestWhileIdle(BooleanBaseOpt.castObjectToBoolean(
            dsDesc.getExtProp("testWhileIdle"), true));
        ds.setValidationQueryTimeout(NumberBaseOpt.castObjectToInteger(
            dsDesc.getExtProp("validationQueryTimeout"), 1000*10));
        ds.setKeepAlive(BooleanBaseOpt.castObjectToBoolean(
            dsDesc.getExtProp("keepAlive"), true));
        ds.setTimeBetweenEvictionRunsMillis(NumberBaseOpt.castObjectToInteger(
            dsDesc.getExtProp("timeBetweenEvictionRunsMillis"), 600000));
        ds.setMinEvictableIdleTimeMillis(NumberBaseOpt.castObjectToInteger(
            dsDesc.getExtProp("timeBetweenEvictionRunsMillis"), 300000));
        ds.setRemoveAbandoned(BooleanBaseOpt.castObjectToBoolean(
            dsDesc.getExtProp("removeAbandoned"), true));
        ds.setRemoveAbandonedTimeout(NumberBaseOpt.castObjectToInteger(
            dsDesc.getExtProp("removeAbandonedTimeout"), 80));
        ds.setLogAbandoned(BooleanBaseOpt.castObjectToBoolean(
            dsDesc.getExtProp("logAbandoned"), true));
        return ds;
    }

    static synchronized Connection getDbcpConnect(ISourceInfo dsDesc) throws SQLException {
        DruidDataSource ds = DRUID_DATA_SOURCE_POOLS.get(dsDesc);
        if (ds == null) {
            ds = mapDataSource(dsDesc);
            DRUID_DATA_SOURCE_POOLS.put(dsDesc, ds);
        }
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
