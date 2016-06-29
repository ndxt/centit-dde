package com.centit.app.service;


import java.util.Date;
import java.util.List;

import com.centit.app.po.OaWorkDay;
import com.centit.core.service.BaseEntityManager;

public interface OaWorkDayManager extends BaseEntityManager<OaWorkDay> {

    public List<OaWorkDay> getListByDay(Date begin, Date end);

}
