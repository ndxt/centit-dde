package com.centit.dde.action;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.centit.core.action.BaseEntityDwzAction;
import com.centit.dde.po.UserDataOptId;
import com.centit.dde.service.UserDataOptIdManager;

public class UserDataOptIdAction extends BaseEntityDwzAction<UserDataOptId> {
    private static final Log log = LogFactory.getLog(UserDataOptIdAction.class);

    private static final long serialVersionUID = 1L;

    private UserDataOptIdManager userDataOptIdManager;

    public void setUserDataOptIdManager(UserDataOptIdManager userDataOptIdManager) {
        this.userDataOptIdManager = userDataOptIdManager;
        setBaseEntityManager(userDataOptIdManager);
    }


    @Override
    public String save() {
        if (null == object.getUdId()) {
            object.setCreateDate(new Date());
        } else {
            object.setLastModifyDate(new Date());
        }

        return super.save();
    }

    @Override
    public String edit() {
        return super.edit();
    }

    @Override
    public String delete() {
        super.delete();

        return "delete";
    }
}
