/**
 *
 */
package com.centit.app.action;

import java.util.List;

import com.centit.app.po.Innermsg;
import com.centit.app.service.InnermsgManager;
import com.centit.core.action.BaseAction;
import com.centit.sys.service.UserSettingManager;

public class DashboardAction extends BaseAction {

    private static final long serialVersionUID = 1L;
    private InnermsgManager innermsgManager;

    private UserSettingManager userSettingMgr;

    private List<Innermsg> msgList;

    public String show() throws Exception {

        return "dashboard";
    }

    public InnermsgManager getInnermsgManager() {
        return innermsgManager;
    }

    public void setInnermsgManager(InnermsgManager innermsgManager) {
        this.innermsgManager = innermsgManager;
    }

    public List<Innermsg> getMsgList() {
        return msgList;
    }

    public void setMsgList(List<Innermsg> msgList) {
        this.msgList = msgList;
    }

    public UserSettingManager getUserSettingMgr() {
        return userSettingMgr;
    }

    public void setUserSettingMgr(UserSettingManager userSettingMgr) {
        this.userSettingMgr = userSettingMgr;
    }

}
