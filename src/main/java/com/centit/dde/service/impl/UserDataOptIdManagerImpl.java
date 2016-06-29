package com.centit.dde.service.impl;

import com.centit.core.service.BaseEntityManagerImpl;
import com.centit.dde.dao.UserDataOptIdDao;
import com.centit.dde.po.UserDataOptId;
import com.centit.dde.service.UserDataOptIdManager;

public class UserDataOptIdManagerImpl extends BaseEntityManagerImpl<UserDataOptId>
        implements UserDataOptIdManager {
    private static final long serialVersionUID = 1L;

    private UserDataOptIdDao userDataOptIdDao;

    public void setUserDataOptIdDao(UserDataOptIdDao userDataOptIdDao) {
        this.userDataOptIdDao = userDataOptIdDao;
        setBaseDao(userDataOptIdDao);
    }
}

