package com.centit.app.action;

import com.centit.app.po.OaReply;
import com.centit.app.service.OaReplyManager;
import com.centit.core.action.BaseEntityDwzAction;


public class OaReplyAction extends BaseEntityDwzAction<OaReply> {
    private static final long serialVersionUID = 1L;
    private OaReplyManager oaReplyMag;

    public void setOaReplyManager(OaReplyManager basemgr) {
        oaReplyMag = basemgr;
        this.setBaseEntityManager(oaReplyMag);
    }


}
