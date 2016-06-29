package com.centit.app.service.impl;

import com.centit.app.dao.PublicinfologDao;
import com.centit.app.po.Publicinfolog;
import com.centit.app.service.PublicinfologManager;
import com.centit.core.service.BaseEntityManagerImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PublicinfologManagerImpl extends BaseEntityManagerImpl<Publicinfolog>
        implements PublicinfologManager {
    private static final long serialVersionUID = 1L;
    public static final Log log = LogFactory.getLog(PublicinfologManager.class);

    //private static final SysOptLog sysOptLog = SysOptLogFactoryImpl.getSysOptLog();

    private PublicinfologDao publicinfologDao;

    public void setPublicinfologDao(PublicinfologDao baseDao) {
        this.publicinfologDao = baseDao;
        setBaseDao(this.publicinfologDao);
    }

}

