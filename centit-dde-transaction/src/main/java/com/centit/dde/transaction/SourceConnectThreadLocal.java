package com.centit.dde.transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhf
 */
class SourceConnectThreadLocal extends ThreadLocal<SourceConnectThreadWrapper> {
    private static final Logger logger = LoggerFactory.getLogger(SourceConnectThreadLocal.class);

    @Override
    public void remove() {
        SourceConnectThreadWrapper wrapper = super.get();
        if (wrapper != null) {
            try {
                wrapper.rollbackAllWork();
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage());
            } finally {
                wrapper.releaseAllConnect();
            }
        }
        super.remove();
    }

    void superRemove() {
        super.remove();
    }
}
