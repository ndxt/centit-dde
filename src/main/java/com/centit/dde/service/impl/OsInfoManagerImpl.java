package com.centit.dde.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.centit.dde.dao.OsInfoDao;
import com.centit.dde.po.OsInfo;
import com.centit.dde.service.OsInfoManager;
import com.centit.framework.hibernate.service.BaseEntityManagerImpl;
@Service
@Transactional
public class OsInfoManagerImpl extends BaseEntityManagerImpl<OsInfo,String,OsInfoDao>
        implements OsInfoManager {
 
    //private static final SysOptLog sysOptLog = SysOptLogFactoryImpl.getSysOptLog();

    @Override
    @Resource(name = "osInfoDao")
    public void setBaseDao(OsInfoDao baseDao) {
        super.baseDao = baseDao;
    }

}

