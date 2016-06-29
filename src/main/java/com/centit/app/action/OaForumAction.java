package com.centit.app.action;

import java.util.List;

import com.centit.app.po.OaForum;
import com.centit.app.po.OaThread;
import com.centit.app.service.OaForumManager;
import com.centit.core.action.BaseEntityDwzAction;

public class OaForumAction extends BaseEntityDwzAction<OaForum> {
    private static final long serialVersionUID = 1L;
    private OaForumManager oaForumMag;

    public void setOaForumManager(OaForumManager basemgr) {
        oaForumMag = basemgr;
        this.setBaseEntityManager(oaForumMag);
    }

    private List<OaThread> oaThreads;

    public List<OaThread> getNewOaThreads() {
        return this.oaThreads;
    }

    public void setNewOaThreads(List<OaThread> oaThreads) {
        this.oaThreads = oaThreads;
    }


    public String save() {
        //object.replaceOaThreads( oaThreads);
        object.setForumname("跌一个");
        return super.save();
    }

}
