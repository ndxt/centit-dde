package com.centit.app.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.centit.app.po.Publicinfolog;
import com.centit.app.service.PublicinfologManager;
import com.centit.core.action.BaseEntityDwzAction;


public class PublicinfologAction extends BaseEntityDwzAction<Publicinfolog> {
    private static final Log log = LogFactory.getLog(PublicinfologAction.class);

    //private static final ISysOptLog sysOptLog = SysOptLogFactoryImpl.getSysOptLog("optid");

    private static final long serialVersionUID = 1L;
    private PublicinfologManager publicinfologMag;

    public void setPublicinfologManager(PublicinfologManager basemgr) {
        publicinfologMag = basemgr;
        this.setBaseEntityManager(publicinfologMag);
    }


    public String delete() {
        super.delete();

        return "delete";
    }
}
