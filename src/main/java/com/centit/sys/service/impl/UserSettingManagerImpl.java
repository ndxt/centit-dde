package com.centit.sys.service.impl;

import com.centit.core.service.BaseEntityManagerImpl;
import com.centit.sys.dao.UserSettingDao;
import com.centit.sys.po.Usersetting;
import com.centit.sys.service.UserSettingManager;


public class UserSettingManagerImpl extends BaseEntityManagerImpl<Usersetting>
        implements UserSettingManager {

    private static final long serialVersionUID = 1L;
    private UserSettingDao usersettingDao;

    public void setUsersettingDao(UserSettingDao baseDao) {
        this.usersettingDao = baseDao;
        setBaseDao(this.usersettingDao);
    }

}

