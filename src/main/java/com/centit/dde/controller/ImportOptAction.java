package com.centit.dde.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.WebUtils;

import com.centit.core.action.BaseEntityDwzAction;
import com.centit.core.utils.DwzResultParam;
import com.centit.dde.exception.SqlResolveException;
import com.centit.dde.po.DataOptStep;
import com.centit.dde.po.ImportField;
import com.centit.dde.po.ImportOpt;
import com.centit.dde.service.ImportOptManager;
import com.centit.framework.core.common.JsonResultUtils;
import com.centit.sys.security.FUserDetail;
import com.centit.sys.util.SysParametersUtils;

@Controller
@RequestMapping("/importopt")
public class ImportOptAction extends BaseEntityDwzAction<ImportOpt> {
    private static final Log log = LogFactory.getLog(ImportOptAction.class);

    // private static final ISysOptLog sysOptLog =
    // SysOptLogFactoryImpl.getSysOptLog("optid");

    private static final long serialVersionUID = 1L;

    private ImportOptManager importOptMag;

    public void setImportOptManager(ImportOptManager basemgr) {
        importOptMag = basemgr;
        this.setBaseEntityManager(importOptMag);
    }

    private String tabid;

    public String getTabid() {
        return tabid;
    }

    private DatabaseInfoManager databaseInfoManager;

    private OsInfoManager osInfoManager;

    public void setOsInfoManager(OsInfoManager osInfoManager) {
        this.osInfoManager = osInfoManager;
    }

    public void setDatabaseInfoManager(DatabaseInfoManager databaseInfoManager) {
        this.databaseInfoManager = databaseInfoManager;
    }

    private List<DataOptStep> dataOptSteps;

    public List<DataOptStep> getNewDataOptSteps() {
        return this.dataOptSteps;
    }

    public void setNewDataOptSteps(List<DataOptStep> dataOptSteps) {
        this.dataOptSteps = dataOptSteps;
    }

    private List<ImportField> importFields;

    public List<ImportField> getNewImportFields() {
        return this.importFields;
    }

    public void setNewImportFields(List<ImportField> importFields) {
        this.importFields = importFields;
    }

    @RequestMapping(value="/defDataSource",method = {RequestMethod.GET})
    public String defDataSource(HttpServletRequest request,HttpServletResponse response) {

        List<DatabaseInfo> dbList = databaseInfoManager.listObjects();

//        request.setAttribute("dbList", dbList);
//
//        return "defDataSource";
        JsonResultUtils.writeSingleDataJson(dbList, response);
    }

    /**
     * 数据导出内容字段
     *
     * @return
     */
    @RequestMapping(value="/listField",method = {RequestMethod.GET})
    public void listField(HttpServletRequest request,HttpServletResponse response) {
        object = baseEntityManager.getObject(object);
        if (null == object) {
            //
        }

        List<DatabaseInfo> dbList = databaseInfoManager.listObjects();

        request.setAttribute("dbList", dbList);

        // 业务系统
        List<OsInfo> osinfoList = osInfoManager.listObjects();
//        request.setAttribute("osinfoList", osinfoList);
//
//        return "listField";
        JsonResultUtils.writeSingleDataJson(osinfoList,dbList, response);
    }

//    @RequestMapping(value="/formField")
//    public String formField() {
//        return "formField";
//    }

//    @RequestMapping(value="/formTrigger")
//    public String formTrigger() {
//        return "formTrigger";
//    }
    
    @RequestMapping(value="/save",method = {RequestMethod.PUT})
    public void save(HttpServletRequest request,HttpServletResponse response) {
        dwzResultParam = new DwzResultParam();
        dwzResultParam.setNavTabId(tabid);



        //判断名称的唯一性
        object.setImportName(org.apache.commons.lang.StringUtils.trim(object.getImportName()));

        Map<String, Object> filterMap = new HashMap<String, Object>();
        filterMap.put("importNameEq", object.getImportName());

        List<ImportOpt> listObjects = importOptMag.listObjects(filterMap);


        if (!CollectionUtils.isEmpty(listObjects)) {
            ImportOpt importDB = importOptMag.getObject(object);
            String message = "导入名称已存在";
            if (null == importDB) {
                dwzResultParam = new DwzResultParam(DwzResultParam.STATUS_CODE_300, message);

//                return SUCCESS;
                JsonResultUtils.writeSuccessJson(response);
                return;
            } else {
                if (1 < listObjects.size() || !importDB.getImportId().equals(listObjects.get(0)
                        .getImportId())) {
                    dwzResultParam = new DwzResultParam(DwzResultParam.STATUS_CODE_300, message);

//                    return SUCCESS;
                JsonResultUtils.writeSuccessJson(response);
                return;
                }
            }
        }

        try {
            importOptMag.validator(object);

            importOptMag.saveObject(object, (FUserDetail) getLoginUser());
        } catch (SqlResolveException e) {
            dwzResultParam.setStatusCode(DwzResultParam.STATUS_CODE_300);
            dwzResultParam.setMessage(SysParametersUtils.getValue(String.valueOf(e.getErrorcode())));

//            return SUCCESS;
            JsonResultUtils.writeSuccessJson(response);
            return;
        }

        dwzResultParam.setForwardUrl("/dde/importOpt!list.do");
//        return SUCCESS;
        JsonResultUtils.writeSuccessJson(response);
    }

    @RequestMapping(value="/edit",method = {RequestMethod.PUT})
    public void edit(HttpServletRequest request,HttpServletResponse response) {
        ImportOpt importOpt = importOptMag.getObject(object);
        if (null != importOpt) {
            baseEntityManager.copyObject(object, importOpt);
        }

        // copy
        if (StringUtils.hasText(WebUtils.findParameterValue(request, "type"))) {
            object.setImportId(null);
            object.setImportName(null);
            object.setSourceOsId(null);
        }


        List<DatabaseInfo> dbList = databaseInfoManager.listObjects();

//        request.setAttribute("dbList", dbList);

        // 业务系统
        List<OsInfo> osinfoList = osInfoManager.listObjects();
//        request.setAttribute("osinfoList", osinfoList);

        //return EDIT;

        JsonResultUtils.writeSingleDataJson(dbList,osinfoList, response);
//        return "editTab"; /page/dde/importFieldTabList.jsp
    }

    private File uploadify;

    /**
     * 回写上传文件内容至页面
     *
     * @throws IOException
     */
    @RequestMapping(value="/uploadify")
    public void uploadify() throws IOException, FileUploadException {
        HttpServletResponse response = ServletActionContext.getResponse();


        StringWriter sw = new StringWriter();
        IOUtils.copy(new FileInputStream(uploadify), sw);


        String text = sw.toString();

        response.getWriter().print(text);
    }


    @RequestMapping(value="/delete",method = {RequestMethod.DELETE})
    public void delete(HttpServletRequest request,HttpServletResponse response) {
        super.delete();

//        return "delete";
        JsonResultUtils.writeSuccessJson(response);
    }


    public File getUploadify() {
        return uploadify;
    }

    public void setUploadify(File uploadify) {
        this.uploadify = uploadify;
    }
}
