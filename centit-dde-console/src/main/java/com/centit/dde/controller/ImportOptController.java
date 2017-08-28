package com.centit.dde.controller;

import com.centit.dde.exception.SqlResolveException;
import com.centit.dde.po.ImportField;
import com.centit.dde.po.ImportOpt;
import com.centit.dde.service.ImportOptManager;
import com.centit.framework.common.JsonResultUtils;
import com.centit.framework.common.ResponseMapData;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.dao.PageDesc;
import com.centit.framework.security.model.CentitUserDetails;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/importopt")
public class ImportOptController extends BaseController {
    private static final Log log = LogFactory.getLog(ImportOptController.class);

    @Resource
    private ImportOptManager importOptManager;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public void list(PageDesc pageDesc, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> filterMap = convertSearchColumn(request);
        List<ImportOpt> listObjects = importOptManager.listObjects(filterMap, pageDesc);
        ResponseMapData resData = new ResponseMapData();
        resData.addResponseData(OBJLIST, listObjects);
        resData.addResponseData(PAGE_DESC, pageDesc);

        JsonResultUtils.writeResponseDataAsJson(resData, response);
    }

    @RequestMapping(value="/save",method = {RequestMethod.PUT})
    public void save(ImportOpt object,HttpServletRequest request,HttpServletResponse response) {

        CentitUserDetails user = getLoginUser(request);
        importOptManager.saveObject(object, user);

        JsonResultUtils.writeSuccessJson(response);
    }

    @RequestMapping(value="/edit/{importId}",method = {RequestMethod.GET})
    public void edit(@PathVariable Long importId ,HttpServletResponse response) {
        ImportOpt importOpt = importOptManager.getObjectById(importId);

        JsonResultUtils.writeSingleDataJson(importOpt, response);
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
        IOUtils.copy(new FileInputStream(uploadify), sw, "UTF-8");

        String text = sw.toString();

        response.getWriter().print(text);
    }


    @RequestMapping(value="/delete/{importId}",method = {RequestMethod.DELETE})
    public void delete(@PathVariable Long importId,HttpServletResponse response) {
        importOptManager.deleteObjectById(importId);
        JsonResultUtils.writeSuccessJson(response);
    }


    public File getUploadify() {
        return uploadify;
    }

    public void setUploadify(File uploadify) {
        this.uploadify = uploadify;
    }

    /**
     * 同步表格
     */
    @RequestMapping(value="/getFields/{databaseCode}/{tableName}")
    public void resolveQuerySql(@PathVariable String databaseCode,
                                @PathVariable String tableName,
                                HttpServletResponse response) throws SqlResolveException, IOException {

        List<ImportField> fields = importOptManager.listFields(databaseCode, tableName);

        JsonResultUtils.writeSingleDataJson(fields, response);
    }
}
