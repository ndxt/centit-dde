package com.centit.sys.action;

import com.centit.core.action.BaseEntityDwzAction;
import com.centit.sys.po.OptLog;
import com.centit.sys.service.OptLogManager;
import com.opensymphony.xwork2.ActionContext;

public class OptLogAction extends BaseEntityDwzAction<OptLog> {

    private static final long serialVersionUID = 1L;
    private OptLogManager optLogMag;

    public void setOptLogManager(OptLogManager basemgr) {
        optLogMag = basemgr;
        this.setBaseEntityManager(optLogMag);
    }

    @Override
    public String list() {
        ActionContext.getContext().put("optIds", this.optLogMag.listOptIds());

        return super.list();
    }

    @Override
    public String view() {
        ActionContext.getContext().put("optIds", this.optLogMag.listOptIds());

        return super.view();
    }

}
