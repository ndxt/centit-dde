package com.centit.app.service.impl;

import com.centit.app.dao.OaThreadDao;
import com.centit.app.po.OaThread;
import com.centit.app.service.OaThreadManager;
import com.centit.core.service.BaseEntityManagerImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class OaThreadManagerImpl extends BaseEntityManagerImpl<OaThread>
        implements OaThreadManager {
    private static final long serialVersionUID = 1L;
    public static final Log log = LogFactory.getLog(OaThreadManager.class);

    private OaThreadDao oaThreadDao;

    public void setOaThreadDao(OaThreadDao baseDao) {
        this.oaThreadDao = baseDao;
        setBaseDao(this.oaThreadDao);
    }

}

