package com.centit.dde.service.impl;

import com.centit.dde.dao.UserDataOptIdDao;
import com.centit.dde.po.UserDataOptId;
import com.centit.dde.service.UserDataOptIdManager;
import com.centit.framework.jdbc.service.BaseEntityManagerImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
@Service
public class UserDataOptIdManagerImpl extends BaseEntityManagerImpl<UserDataOptId
    ,Long,UserDataOptIdDao>
        implements UserDataOptIdManager {
    
    private UserDataOptIdDao userDataOptIdDao;

    @Resource(name="userDataOptIdDao")
    @NotNull
    public void setUserDataOptIdDao(UserDataOptIdDao userDataOptIdDao) {
        this.userDataOptIdDao = userDataOptIdDao;
        setBaseDao(userDataOptIdDao);
    }
}

