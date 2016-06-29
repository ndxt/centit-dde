package com.centit.dde.controller;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.centit.core.action.BaseEntityDwzAction;
import com.centit.dde.po.DataOptStep;
import com.centit.dde.po.OsInfo;
import com.centit.dde.service.OsInfoManager;
import com.centit.sys.security.FUserDetail;

public class OsInfoAction extends BaseEntityDwzAction<OsInfo> {
    private static final Log log = LogFactory.getLog(OsInfoAction.class);

    // private static final ISysOptLog sysOptLog =
    // SysOptLogFactoryImpl.getSysOptLog("optid");

    private static final long serialVersionUID = 1L;

    private OsInfoManager osInfoMag;

    public void setOsInfoManager(OsInfoManager basemgr) {
        osInfoMag = basemgr;
        this.setBaseEntityManager(osInfoMag);
    }

    private List<DataOptStep> dataOptSteps;

    public List<DataOptStep> getNewDataOptSteps() {
        return this.dataOptSteps;
    }

    public void setNewDataOptSteps(List<DataOptStep> dataOptSteps) {
        this.dataOptSteps = dataOptSteps;
    }

    public String save() {
        FUserDetail loginUser = (FUserDetail) super.getLoginUser();

        OsInfo dbObject = baseEntityManager.getObject(object);
        if (dbObject != null) {
            baseEntityManager.copyObjectNotNullProperty(dbObject, object);
            object = dbObject;
            object.setLastUpdateTime(new Date());
        } else {
            object.setCreateTime(new Date());
            object.setCreated(loginUser.getUsercode());
        }

        if (null == object.getHasInterface()) {
            object.setHasInterface("F");
        }

        baseEntityManager.saveObject(object);

        return SUCCESS;
    }

    public String delete() {
        super.delete();

        return "delete";
    }
}
