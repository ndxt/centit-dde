package com.centit.dde.transaction;

import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.database.metadata.IDatabaseInfo;
import com.centit.support.database.utils.DataSourceDescription;
import com.centit.support.database.utils.DbcpConnectPools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhf
 */
public class SourceConnectThreadWrapper implements Serializable {
    private final Map<DataSourceDescription, Object> connectPools;
    public SourceConnectThreadWrapper() {
        this.connectPools = new ConcurrentHashMap<>(4);
    }

    public Object fetchConnect(DataSourceDescription description) throws SQLException {
        if(StringBaseOpt.isNvl(description.getSourceType()) || IDatabaseInfo.DATABASE.equals(description.getSourceType())) {
            Connection conn = (Connection) connectPools.get(description);
            if (conn == null) {
                conn = DbcpConnectPools.getDbcpConnect(description);
                connectPools.put(description, conn);
            }
            return conn;
        }
        return null;
    }

    public void commitAllWork() throws SQLException {
        if (connectPools.size() == 0) {
            return;
        }
        for (Map.Entry<DataSourceDescription, Object> map: connectPools.entrySet()) {
            if(StringBaseOpt.isNvl(map.getKey().getSourceType()) ||IDatabaseInfo.DATABASE.equals(map.getKey().getSourceType())) {
                Connection conn =(Connection)map.getValue();
                conn.commit();
            }
        }
    }

    public void rollbackAllWork() throws SQLException {
        if (connectPools.size() == 0) {
            return;
        }
        for (Map.Entry<DataSourceDescription, Object> map: connectPools.entrySet()) {
            if(StringBaseOpt.isNvl(map.getKey().getSourceType()) ||IDatabaseInfo.DATABASE.equals(map.getKey().getSourceType())) {
                Connection conn =(Connection)map.getValue();
                conn.rollback();
            }
        }
    }

    public void releaseAllConnect() {
        if (connectPools.size() == 0) {
            return;
        }
        for (Map.Entry<DataSourceDescription, Object> map: connectPools.entrySet()) {
            if(StringBaseOpt.isNvl(map.getKey().getSourceType()) ||IDatabaseInfo.DATABASE.equals(map.getKey().getSourceType())) {
                Connection conn =(Connection)map.getValue();
                DbcpConnectPools.closeConnect(conn);
            }
        }
        connectPools.clear();
    }
}

