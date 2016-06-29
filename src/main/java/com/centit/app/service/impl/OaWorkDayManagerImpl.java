package com.centit.app.service.impl;

import com.centit.app.dao.OaWorkDayDao;
import com.centit.app.po.OaWorkDay;
import com.centit.app.service.OaWorkDayManager;
import com.centit.core.service.BaseEntityManagerImpl;

import java.util.Date;
import java.util.List;

public class OaWorkDayManagerImpl extends BaseEntityManagerImpl<OaWorkDay> implements OaWorkDayManager {

    private static final long serialVersionUID = 1L;
    private OaWorkDayDao oaWorkDayDao;

    public OaWorkDayDao getOaWorkDayDao() {
        return oaWorkDayDao;
    }

    public void setOaWorkDayDao(OaWorkDayDao oaWorkDayDao) {
        this.oaWorkDayDao = oaWorkDayDao;
        super.setBaseDao(oaWorkDayDao);
    }

    @Override
    public List<OaWorkDay> getListByDay(Date begin, Date end) {
        try {
            List<OaWorkDay> list = oaWorkDayDao.getListByDate(begin, end);
            return list;
        } catch (Exception e) {
            return null;
        }

    }

}
