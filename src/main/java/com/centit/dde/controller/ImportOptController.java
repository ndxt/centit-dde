package com.centit.dde.controller;

import com.centit.dde.exception.SqlResolveException;
import com.centit.dde.po.ImportOpt;
import com.centit.dde.service.ImportOptManager;
import com.centit.framework.core.common.JsonResultUtils;
import com.centit.framework.core.common.ResponseData;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.staticsystem.po.DatabaseInfo;
import com.centit.framework.staticsystem.po.OsInfo;
import com.centit.framework.staticsystem.service.StaticEnvironmentManager;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.WebUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/importopt")
public class ImportOptController extends BaseController {
    private static final Log log = LogFactory.getLog(ImportOptController.class);

    // private static final ISysOptLog sysOptLog =
    // SysOptLogFactoryImpl.getSysOptLog("optid");

    private static final long serialVersionUID = 1L;

    @Resource

    private ImportOptManager importOptMag;


    @Resource
    protected StaticEnvironmentManager platformEnvironment;


    @RequestMapping(value="/defDataSource",method = {RequestMethod.GET})
    public void defDataSource(HttpServletResponse response) {

        List<DatabaseInfo> dbList = platformEnvironment.listDatabaseInfo();

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
    public void listField(ImportOpt object,HttpServletResponse response) {
        object = importOptMag.getObjectById(object.getImportId());
        if (null == object) {
            //
        }

        List<DatabaseInfo> dbList = platformEnvironment.listDatabaseInfo();
        // 业务系统
        List<OsInfo> osinfoList = platformEnvironment.listOsInfos();
        ResponseData resData = new ResponseData();
        resData.addResponseData("dbList", dbList);
        resData.addResponseData("osinfoList",osinfoList);

        JsonResultUtils.writeResponseDataAsJson(resData, response);
    }

//    @RequestMapping(value="/formField")
//    public String formField() {
//        return "formField";
//    }

//    @RequestMapping(value="/formTrigger")
//    public String formTrigger() {
//        return "formTrigger";
//    }
    
    @RequestMapping(value="/save/{{tabid}}",method = {RequestMethod.PUT})
    public void save(ImportOpt object,HttpServletRequest request,HttpServletResponse response) {
//        dwzResultParam = new DwzResultParam();
//        dwzResultParam.setNavTabId(tabid);



        //判断名称的唯一性
        object.setImportName(object.getImportName().trim());

        Map<String, Object> filterMap = new HashMap<String, Object>();
        filterMap.put("importNameEq", object.getImportName());

        List<ImportOpt> listObjects = importOptMag.listObjects(filterMap);


        if (!CollectionUtils.isEmpty(listObjects)) {
            ImportOpt importDB = importOptMag.getObjectById(object.getImportId());
            String message = "导入名称已存在";
            if (null == importDB) {
//                dwzResultParam = new DwzResultParam(DwzResultParam.STATUS_CODE_300, message);

//                return SUCCESS;
                JsonResultUtils.writeSuccessJson(response);
                return;
            } else {
                if (1 < listObjects.size() || !importDB.getImportId().equals(listObjects.get(0)
                        .getImportId())) {
//                    dwzResultParam = new DwzResultParam(DwzResultParam.STATUS_CODE_300, message);

//                    return SUCCESS;
                JsonResultUtils.writeSuccessJson(response);
                return;
                }
            }
        }

        try {
            importOptMag.validator(object);

            importOptMag.saveObject(object, getLoginUser(request));
        } catch (SqlResolveException e) {
//            dwzResultParam.setStatusCode(DwzResultParam.STATUS_CODE_300);
//            dwzResultParam.setMessage(SysParametersUtils.getValue(String.valueOf(e.getErrorcode())));

//            return SUCCESS;
            JsonResultUtils.writeSuccessJson(response);
            return;
        }

//        dwzResultParam.setForwardUrl("/dde/importOpt!list.do");
//        return SUCCESS;
        JsonResultUtils.writeSuccessJson(response);
    }

    @RequestMapping(value="/edit/{{importid}}",method = {RequestMethod.PUT})
    public void edit(@PathVariable Long importid ,ImportOpt object,HttpServletRequest request,HttpServletResponse response) {
        ImportOpt importOpt = importOptMag.getObjectById(importid);
        if (null != importOpt) {
            importOptMag.copyObject(object, importOpt);
        }

        // copy
        if (StringUtils.hasText(WebUtils.findParameterValue(request, "type"))) {
            object.setImportId(null);
            object.setImportName(null);
            object.setSourceOsId(null);
        }


        List<DatabaseInfo> dbList = platformEnvironment.listDatabaseInfo();

        // 业务系统
        List<OsInfo> osinfoList = platformEnvironment.listOsInfos();

        ResponseData resData = new ResponseData();
        resData.addResponseData("dbList", dbList);
        resData.addResponseData("osinfoList",osinfoList);

        JsonResultUtils.writeResponseDataAsJson(resData, response);
    }

    private File uploadify;

    /**
     * 回写上传文件内容至页面
     *
     * @throws IOException
     */
    @RequestMapping(value="/uploadify")
    public void uploadify(HttpServletResponse response) throws Exception {

        StringWriter sw = new StringWriter();
        IOUtils.copy(new FileInputStream(uploadify), sw);


        String text = sw.toString();

        response.getWriter().print(text);
    }


    @RequestMapping(value="/delete/{{importid}}",method = {RequestMethod.DELETE})
    public void delete(@PathVariable Long importid,HttpServletResponse response) {
        importOptMag.deleteObjectById(importid);
        JsonResultUtils.writeSuccessJson(response);
    }


    public File getUploadify() {
        return uploadify;
    }

    public void setUploadify(File uploadify) {
        this.uploadify = uploadify;
    }
}
