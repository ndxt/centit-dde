package com.centit.sys.service.impl;

import com.centit.core.service.BaseEntityManagerImpl;
import com.centit.sys.dao.OptVariableDao;
import com.centit.sys.po.OptVariable;
import com.centit.sys.service.OptVariableManager;

public class OptVariableManagerImpl extends BaseEntityManagerImpl<OptVariable>
        implements OptVariableManager {
    private static final long serialVersionUID = 1L;
    private OptVariableDao optVariableDao;

    public void setOptVariableDao(OptVariableDao baseDao) {
        this.optVariableDao = baseDao;
        setBaseDao(this.optVariableDao);
    }


}

