package com.centit.dde.service.impl;

import com.centit.dde.dao.UserDataOptIdDao;
import com.centit.dde.po.UserDataOptId;
import com.centit.dde.service.UserDataOptIdManager;
import com.centit.framework.hibernate.service.BaseEntityManagerImpl;

public class UserDataOptIdManagerImpl extends BaseEntityManagerImpl<UserDataOptId
    ,Long,UserDataOptIdDao>
        implements UserDataOptIdManager {

    private UserDataOptIdDao userDataOptIdDao;

    public void setUserDataOptIdDao(UserDataOptIdDao userDataOptIdDao) {
        this.userDataOptIdDao = userDataOptIdDao;
        setBaseDao(userDataOptIdDao);
    }
}

