package com.centit.dde.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.util.WebUtils;

import com.centit.core.action.BaseEntityDwzAction;
import com.centit.core.utils.DwzResultParam;
import com.centit.dde.exception.SqlResolveException;
import com.centit.dde.po.DataOptStep;
import com.centit.dde.po.DatabaseInfo;
import com.centit.dde.po.ImportField;
import com.centit.dde.po.ImportOpt;
import com.centit.dde.po.OsInfo;
import com.centit.dde.service.DatabaseInfoManager;
import com.centit.dde.service.ImportOptManager;
import com.centit.dde.service.OsInfoManager;
import com.centit.sys.security.FUserDetail;
import com.centit.sys.util.SysParametersUtils;

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

    public String defDataSource() {

        List<DatabaseInfo> dbList = databaseInfoManager.listObjects();

        request.setAttribute("dbList", dbList);

        return "defDataSource";
    }

    /**
     * 数据导出内容字段
     *
     * @return
     */
    public String listField() {
        object = baseEntityManager.getObject(object);
        if (null == object) {
            //
        }

        List<DatabaseInfo> dbList = databaseInfoManager.listObjects();

        request.setAttribute("dbList", dbList);

        // 业务系统
        List<OsInfo> osinfoList = osInfoManager.listObjects();
        request.setAttribute("osinfoList", osinfoList);

        return "listField";
    }

    public String formField() {
        return "formField";
    }


    public String formTrigger() {
        return "formTrigger";
    }

    public String save() {
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

                return SUCCESS;
            } else {
                if (1 < listObjects.size() || !importDB.getImportId().equals(listObjects.get(0)
                        .getImportId())) {
                    dwzResultParam = new DwzResultParam(DwzResultParam.STATUS_CODE_300, message);

                    return SUCCESS;
                }
            }
        }











        try {
            importOptMag.validator(object);

            importOptMag.saveObject(object, (FUserDetail) getLoginUser());
        } catch (SqlResolveException e) {
            dwzResultParam.setStatusCode(DwzResultParam.STATUS_CODE_300);
            dwzResultParam.setMessage(SysParametersUtils.getValue(String.valueOf(e.getErrorcode())));

            return SUCCESS;
        }

        dwzResultParam.setForwardUrl("/dde/importOpt!list.do");
        return SUCCESS;
    }

    @Override
    public String edit() {
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

        request.setAttribute("dbList", dbList);

        // 业务系统
        List<OsInfo> osinfoList = osInfoManager.listObjects();
        request.setAttribute("osinfoList", osinfoList);

        //return EDIT;

        return "editTab";
    }

    private File uploadify;

    /**
     * 回写上传文件内容至页面
     *
     * @throws IOException
     */
    public void uploadify() throws IOException, FileUploadException {
        HttpServletResponse response = ServletActionContext.getResponse();


        StringWriter sw = new StringWriter();
        IOUtils.copy(new FileInputStream(uploadify), sw);


        String text = sw.toString();

        response.getWriter().print(text);
    }


    public String delete() {
        super.delete();

        return "delete";
    }


    public File getUploadify() {
        return uploadify;
    }

    public void setUploadify(File uploadify) {
        this.uploadify = uploadify;
    }
}
