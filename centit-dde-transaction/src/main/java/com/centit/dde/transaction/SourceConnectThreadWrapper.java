package com.centit.dde.transaction;

import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.database.metadata.IDatabaseInfo;
import com.centit.support.database.utils.DataSourceDescription;

import java.io.Serializable;
import java.sql.Connection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhf
 */
class SourceConnectThreadWrapper implements Serializable {
    private final Map<DataSourceDescription, Object> connectPools;

    SourceConnectThreadWrapper() {
        this.connectPools = new ConcurrentHashMap<>(4);
    }

    Object fetchConnect(DataSourceDescription description) throws Exception {
        if (StringBaseOpt.isNvl(description.getSourceType()) || IDatabaseInfo.DATABASE.equals(description.getSourceType())) {
            Connection conn = (Connection) connectPools.get(description);
            if (conn == null) {
                conn = DruidConnectPools.getDbcpConnect(description);
                connectPools.put(description, conn);
            }
            return conn;
        }
        return null;
    }

    void commitAllWork() throws Exception {
        if (connectPools.size() == 0) {
            return;
        }
        for (Map.Entry<DataSourceDescription, Object> map : connectPools.entrySet()) {
            if (StringBaseOpt.isNvl(map.getKey().getSourceType()) || IDatabaseInfo.DATABASE.equals(map.getKey().getSourceType())) {
                Connection conn = (Connection) map.getValue();
                conn.commit();
            }
        }
    }

    void rollbackAllWork() throws Exception {
        if (connectPools.size() == 0) {
            return;
        }
        for (Map.Entry<DataSourceDescription, Object> map : connectPools.entrySet()) {
            if (StringBaseOpt.isNvl(map.getKey().getSourceType()) || IDatabaseInfo.DATABASE.equals(map.getKey().getSourceType())) {
                Connection conn = (Connection) map.getValue();
                conn.rollback();
            }
        }
    }

    void releaseAllConnect() {
        if (connectPools.size() == 0) {
            return;
        }
        for (Map.Entry<DataSourceDescription, Object> map : connectPools.entrySet()) {
            if (StringBaseOpt.isNvl(map.getKey().getSourceType()) || IDatabaseInfo.DATABASE.equals(map.getKey().getSourceType())) {
                Connection conn = (Connection) map.getValue();
                DruidConnectPools.closeConnect(conn);
            }
        }
        connectPools.clear();
    }
}

