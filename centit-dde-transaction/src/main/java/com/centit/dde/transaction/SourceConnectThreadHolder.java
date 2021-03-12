package com.centit.dde.transaction;

import com.centit.support.database.metadata.IDatabaseInfo;
import com.centit.support.database.utils.DataSourceDescription;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author zhf
 */
public class SourceConnectThreadHolder {
    private static SourceConnectThreadLocal threadLocal = new SourceConnectThreadLocal();

    private SourceConnectThreadHolder() {
        super();
    }

    public static SourceConnectThreadWrapper getConnectThreadWrapper() {
        SourceConnectThreadWrapper wrapper = threadLocal.get();
        if (wrapper == null) {
            wrapper = new SourceConnectThreadWrapper();
            threadLocal.set(wrapper);
        }
        return wrapper;
    }

    public static Object fetchConnect(DataSourceDescription description) throws SQLException {
        SourceConnectThreadWrapper wrapper = getConnectThreadWrapper();
        return wrapper.fetchConnect(description);
    }

    public static Object fetchConnect(IDatabaseInfo description) throws SQLException {
        return fetchConnect(DataSourceDescription.valueOf(description));
    }

    public static void commitAndRelease() throws SQLException {
        SourceConnectThreadWrapper wrapper = getConnectThreadWrapper();
        try {
            wrapper.commitAllWork();
        } finally {
            wrapper.releaseAllConnect();
            threadLocal.superRemove();
        }
    }

    public static void rollbackAndRelease() throws SQLException {
        SourceConnectThreadWrapper wrapper = getConnectThreadWrapper();
        try {
            wrapper.rollbackAllWork();
        } finally {
            wrapper.releaseAllConnect();
            threadLocal.superRemove();
        }
    }
}
