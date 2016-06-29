package com.centit.sys.service.impl;

import com.centit.core.service.BaseEntityManagerImpl;
import com.centit.sys.dao.ScaffoldTestDao;
import com.centit.sys.po.ScaffoldTest;
import com.centit.sys.service.ScaffoldTestManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ScaffoldTestManagerImpl extends BaseEntityManagerImpl<ScaffoldTest>
        implements ScaffoldTestManager {
    private static final long serialVersionUID = 1L;
    public static final Log log = LogFactory.getLog(ScaffoldTestManager.class);

    private ScaffoldTestDao scaffoldTestDao;

    public void setScaffoldTestDao(ScaffoldTestDao baseDao) {
        this.scaffoldTestDao = baseDao;
        setBaseDao(this.scaffoldTestDao);
    }

}

