package com.centit.dde.transaction;

import com.centit.support.database.utils.DataSourceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

/**
 * @author zhf
 */
public class SourceConnectThreadLocal extends ThreadLocal<SourceConnectThreadWrapper> {
    private static final Logger logger = LoggerFactory.getLogger(DataSourceDescription.class);

    /*@Override
    protected ConnectThreadWrapper initialValue() {
        return new ConnectThreadWrapper();
    }*/

    @Override
    public void remove() {
        SourceConnectThreadWrapper wrapper = super.get();
        if (wrapper != null) {
            try {
                wrapper.rollbackAllWork();
            } catch (SQLException e) {
                logger.error(e.getLocalizedMessage());
            } finally {
                wrapper.releaseAllConnect();
            }
        }
        super.remove();
    }

    public void superRemove() {
        super.remove();
    }
}
