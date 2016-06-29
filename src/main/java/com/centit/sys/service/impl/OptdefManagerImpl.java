package com.centit.sys.service.impl;

import com.centit.core.service.BaseEntityManagerImpl;
import com.centit.sys.dao.OptDefDao;
import com.centit.sys.po.FOptdef;
import com.centit.sys.service.OptdefManager;

import java.util.List;

public class OptdefManagerImpl extends BaseEntityManagerImpl<FOptdef>
        implements OptdefManager {
    private static final long serialVersionUID = 1L;
    private OptDefDao dao;

    public void setOptdefDao(OptDefDao pDao) {
        setBaseDao(pDao);
        this.dao = pDao;
    }

    public FOptdef getObject(FOptdef object) {

        if (object == null)
            return null;
        FOptdef newObj = dao.getObjectById(object.getOptcode());
        if (newObj == null) {
            newObj = object;
            newObj.setOptcode(dao.getNextOptCode());
        }
        return newObj;
    }

    public List<FOptdef> getOptDefByOptID(String sOptID) {
        return dao.getOptDefByOptID(sOptID);
    }

}
