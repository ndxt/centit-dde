package com.centit.sys.util;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;
import org.springframework.util.StringUtils;

import com.centit.core.utils.PageDesc;
import com.centit.core.web.StartupListener;
import com.centit.sys.po.OptLog;
import com.centit.sys.service.OptLogManager;

public class SysOptLogger implements ISysOptLog, Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -1213704398618624206L;

    public static final Log log = LogFactoryImpl.getLog(SysOptLogger.class);

    private String optId;

    public SysOptLogger(String optId) {
        this.optId = optId;
    }

    private static void save(OptLog optLog) {
        OptLogManager optLogManager = getOptLogMag();

        optLogManager.saveObject(optLog);

        if (log.isInfoEnabled()) {
            log.info(optLog);
        }

    }

    private static OptLogManager getOptLogMag() {
        OptLogManager optLogManager = StartupListener.getCurrentWebApplicationContext().getBean("optLogManager",
                OptLogManager.class);
        return optLogManager;
    }

    @Override
    public void log(String usercode, String optId, String tagId, String optMethod, String optContent, String oldValue) {
        save(new OptLog(usercode, optId, tagId, optMethod, optContent, oldValue));
    }

    @Override
    public void log(String usercode, String tagId, String optMethod, String optContent, String oldValue) {
        this.log(usercode, this.optId, tagId, optMethod, optContent, oldValue);

    }

    @Override
    public void log(String usercode, String tagId, String optContent, String oldValue) {
        this.log(usercode, this.optId, tagId, Thread.currentThread().getStackTrace()[2].getMethodName(), optContent,
                oldValue);

    }

    @Override
    public void log(String usercode, String tagId, String optContent) {
        this.log(usercode, this.optId, tagId, Thread.currentThread().getStackTrace()[2].getMethodName(), optContent,
                null);

    }

    @Override
    public void log(OptLog optLog) {
        if (!StringUtils.hasText(optLog.getOptid())) {
            optLog.setOptid(this.optId);
        }
        if (!StringUtils.hasText(optLog.getOptmethod())) {
            optLog.setOptmethod(Thread.currentThread().getStackTrace()[2].getMethodName());
        }

        save(optLog);
    }

    @Override
    public List<OptLog> listOptLog(Map<String, Object> filterMap, PageDesc pageDesc) {
        return getOptLogMag().listObjects(filterMap, pageDesc);
    }

    @Override
    public List<OptLog> listOptLog(Map<String, Object> filterMap) {
        return getOptLogMag().listObjects(filterMap);
    }

}
