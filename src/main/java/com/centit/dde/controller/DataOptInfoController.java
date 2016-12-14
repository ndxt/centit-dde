package com.centit.dde.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.WebUtils;

import com.centit.dde.exception.SqlResolveException;
import com.centit.dde.po.DataOptInfo;
import com.centit.dde.po.DataOptStep;
import com.centit.dde.po.ImportOpt;
import com.centit.dde.service.DataOptInfoManager;
import com.centit.dde.service.ImportOptManager;
import com.centit.framework.common.SysParametersUtils;
import com.centit.framework.core.common.JsonResultUtils;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.staticsystem.po.OsInfo;
import com.sun.istack.Nullable;

@Controller
@RequestMapping("/dataoptinfo")
public class DataOptInfoController extends BaseController {
    private static final Log log = LogFactory.getLog(DataOptInfoController.class);

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
    @Resource
    @Nullable
    private DataOptInfoManager dataOptInfoMag;

/*    @Resource
    @NotNull
    private OsInfoManager osInfoManager;*/

    @Resource
    @NotNull
    private OsInfoManager osInfoManager;
    
    @Resource
    @Nullable
    private ImportOptManager importOptManager;

    private List<DataOptStep> dataOptSteps;

    public List<DataOptStep> getNewDataOptSteps() {
        return this.dataOptSteps;
    }

    public void setNewDataOptSteps(List<DataOptStep> dataOptSteps) {
        this.dataOptSteps = dataOptSteps;
    }
    @RequestMapping(value="/save/{{tabid}}",method = {RequestMethod.PUT})
    public void save(@PathVariable String tabid,DataOptInfo object,
            HttpServletRequest request,HttpServletResponse response) {
//        dwzResultParam = new DwzResultParam();
//        dwzResultParam.setNavTabId(tabid);
        try {
            dataOptInfoMag.saveObject(object, getLoginUser(request));
        } catch (SqlResolveException e) {
//            dwzResultParam.setStatusCode(DwzResultParam.STATUS_CODE_300);
//            dwzResultParam.setMessage(SysParametersUtils.getValue(String.valueOf(e.getErrorcode())));

//            return SUCCESS;
            JsonResultUtils.writeSuccessJson(response);
            return;
        }

//        dwzResultParam.setForwardUrl("/dde/dataOptInfo!list.do");
//        return SUCCESS;
        JsonResultUtils.writeSuccessJson(response);
    }

    
    @RequestMapping(value="/edit/{{dataOptId}}",method = {RequestMethod.GET})
    public void edit(@PathVariable String dataOptId,DataOptInfo object,HttpServletRequest request,HttpServletResponse response) {
        DataOptInfo dataOptInfo = dataOptInfoMag.getObjectById(dataOptId);
        if (null != dataOptInfo) {
            dataOptInfoMag.copyObjectNotNullProperty(object, dataOptInfo);
//            baseEntityManager.copyObject(object, dataOptInfo);
            dataOptInfoMag.copyObjectNotNullProperty(object, dataOptInfo);;
        }

        object.setDataOptSteps(dataOptInfoMag.listDataOptStepByDataOptInfo(object));
        // copy
        if (StringUtils.hasText(WebUtils.findParameterValue(request, "type"))) {
            object.setDataOptId(null);
            object.setOptName(null);
            object.setOptDesc(null);
            
        }

/*        // 业务系统
        List<OsInfo> osinfoList = osInfoManager.listObjects();
        request.setAttribute("osinfoList", osinfoList);*/

//        return EDIT;
//        JsonResultUtils.writeSingleDataJson(osinfoList, response);
        JsonResultUtils.writeSuccessJson(response);
    }

    
    @RequestMapping(value="/delete/{{dataOptId}}",method = {RequestMethod.DELETE})
    public void delete(@PathVariable String dataOptId, DataOptInfo object,HttpServletRequest request,HttpServletResponse response) {
        dataOptInfoMag.deleteObjectById(dataOptId);;
//        return "delete";
        JsonResultUtils.writeSuccessJson(response);
    }

    @RequestMapping(value="/formField",method = {RequestMethod.GET})
    public void formField(HttpServletRequest request,HttpServletResponse response) {
        List<ImportOpt> importOpts = importOptManager.listObjects();
        request.setAttribute("importOpts", importOpts);

//        List<OsInfo> osInfos = osInfoManager.listObjects();
//        request.setAttribute("osInfos", osInfos);
//        return "formField";
//        JsonResultUtils.writeSingleDataJson(osInfos, response);
        JsonResultUtils.writeSuccessJson(response);
    }

}
