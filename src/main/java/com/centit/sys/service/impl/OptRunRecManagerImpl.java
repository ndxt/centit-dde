package com.centit.sys.service.impl;

import com.centit.core.service.BaseEntityManagerImpl;
import com.centit.sys.dao.OptRunRecDao;
import com.centit.sys.po.OptRunRec;
import com.centit.sys.service.OptRunRecManager;

public class OptRunRecManagerImpl extends BaseEntityManagerImpl<OptRunRec>
        implements OptRunRecManager {
    private static final long serialVersionUID = 1L;
    private OptRunRecDao optRunRecDao;

    public void setOptRunRecDao(OptRunRecDao baseDao) {
        this.optRunRecDao = baseDao;
        setBaseDao(this.optRunRecDao);
    }

}

