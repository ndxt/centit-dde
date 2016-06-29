package com.centit.app.action;

import java.util.List;

import com.centit.app.po.OaReply;
import com.centit.app.po.OaThread;
import com.centit.app.service.OaThreadManager;
import com.centit.core.action.BaseEntityDwzAction;

public class OaThreadAction extends BaseEntityDwzAction<OaThread> {
    private static final long serialVersionUID = 1L;
    private OaThreadManager oaThreadMag;

    public void setOaThreadManager(OaThreadManager basemgr) {
        oaThreadMag = basemgr;
        this.setBaseEntityManager(oaThreadMag);
    }

    private List<OaReply> oaReplys;

    public List<OaReply> getNewOaReplys() {
        return this.oaReplys;
    }

    public void setNewOaReplys(List<OaReply> oaReplys) {
        this.oaReplys = oaReplys;
    }


    public String save() {
        object.replaceOaReplys(oaReplys);

        return super.save();
    }

}
