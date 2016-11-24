package com.centit.dde.controller;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.util.WebUtils;

import com.centit.core.action.BaseEntityDwzAction;
import com.centit.core.utils.DwzResultParam;
import com.centit.dde.exception.SqlResolveException;
import com.centit.dde.po.DataOptInfo;
import com.centit.dde.po.DataOptStep;
import com.centit.dde.po.ImportOpt;
import com.centit.dde.service.DataOptInfoManager;
import com.centit.dde.service.ImportOptManager;
import com.centit.sys.security.FUserDetail;
import com.centit.sys.util.SysParametersUtils;

public class DataOptInfoAction extends BaseEntityDwzAction<DataOptInfo> {
    private static final Log log = LogFactory.getLog(DataOptInfoAction.class);

    // private static final ISysOptLog sysOptLog =
    // SysOptLogFactoryImpl.getSysOptLog("optid");

    private static final long serialVersionUID = 1L;

    private String tabid;

    public String getTabid() {
        return tabid;
    }

    public void setTabid(String tabid) {
        this.tabid = tabid;
    }

    private DataOptInfoManager dataOptInfoMag;

    private OsInfoManager osInfoManager;

    private ImportOptManager importOptManager;

    public void setOsInfoManager(OsInfoManager osInfoManager) {
        this.osInfoManager = osInfoManager;
    }

    public void setImportOptManager(ImportOptManager importOptManager) {
        this.importOptManager = importOptManager;
    }

    public void setDataOptInfoManager(DataOptInfoManager basemgr) {
        dataOptInfoMag = basemgr;
        this.setBaseEntityManager(dataOptInfoMag);
    }

    private List<DataOptStep> dataOptSteps;

    public List<DataOptStep> getNewDataOptSteps() {
        return this.dataOptSteps;
    }

    public void setNewDataOptSteps(List<DataOptStep> dataOptSteps) {
        this.dataOptSteps = dataOptSteps;
    }

    public String save() {
        dwzResultParam = new DwzResultParam();
        dwzResultParam.setNavTabId(tabid);
        try {
            dataOptInfoMag.saveObject(object, (FUserDetail) getLoginUser());
        } catch (SqlResolveException e) {
            dwzResultParam.setStatusCode(DwzResultParam.STATUS_CODE_300);
            dwzResultParam.setMessage(SysParametersUtils.getValue(String.valueOf(e.getErrorcode())));

            return SUCCESS;
        }

        dwzResultParam.setForwardUrl("/dde/dataOptInfo!list.do");
        return SUCCESS;
    }

    @Override
    public String edit() {
        DataOptInfo dataOptInfo = dataOptInfoMag.getObject(object);
        if (null != dataOptInfo) {
            baseEntityManager.copyObject(object, dataOptInfo);
        }

        object.setDataOptSteps(dataOptInfoMag.listDataOptStepByDataOptInfo(object));
        // copy
        if (StringUtils.hasText(WebUtils.findParameterValue(request, "type"))) {
            object.setDataOptId(null);
            object.setOptName(null);
            object.setOptDesc(null);
        }

        // 业务系统
        List<OsInfo> osinfoList = osInfoManager.listObjects();
        request.setAttribute("osinfoList", osinfoList);

        return EDIT;
    }

    public String delete() {
        super.delete();

        return "delete";
    }

    public String formField() {
        List<ImportOpt> importOpts = importOptManager.listObjects();
        request.setAttribute("importOpts", importOpts);

        List<OsInfo> osInfos = osInfoManager.listObjects();
        request.setAttribute("osInfos", osInfos);

        return "formField";
    }

}
