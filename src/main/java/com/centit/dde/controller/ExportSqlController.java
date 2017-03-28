package com.centit.dde.controller;

import com.centit.dde.exception.SqlResolveException;
import com.centit.dde.po.ExportField;
import com.centit.dde.po.ExportSql;
import com.centit.dde.service.ExportSqlManager;
import com.centit.dde.util.SQLUtils;
import com.centit.framework.common.SysParametersUtils;
import com.centit.framework.components.CodeRepositoryUtil;
import com.centit.framework.core.common.JsonResultUtils;
import com.centit.framework.core.common.ResponseData;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.staticsystem.po.DataDictionary;
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
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/exportsql")
public class ExportSqlController extends BaseController {
    private static final Log log = LogFactory.getLog(ExportSqlController.class);

    // private static final ISysOptLog sysOptLog =
    // SysOptLogFactoryImpl.getSysOptLog("optid");

    private static final long serialVersionUID = 1L;

    @Resource
    private ExportSqlManager exportSqlMag;

    @Resource
    protected StaticEnvironmentManager platformEnvironment;


    @RequestMapping(value="/save/{tabid}/{exportId}",method = {RequestMethod.PUT})
    public void save(@PathVariable Long exportId,ExportSql object, HttpServletRequest request,HttpServletResponse response) {
//        dwzResultParam = new DwzResultParam();
//        dwzResultParam.setNavTabId(tabid);


        //判断名称的唯一性
        object.setExportName(object.getExportName().trim());

        Map<String, Object> filterMap = new HashMap<String, Object>();
        filterMap.put("eqExportName", object.getExportName());

        List<ExportSql> listObjects = exportSqlMag.listObjects(filterMap);


        if (!CollectionUtils.isEmpty(listObjects)) {
//            ExportSql exportDB = exportSqlMag.getObjectById(object.getExportId());
            ExportSql exportDB = exportSqlMag.getObjectById(exportId);
            String message = "导出名称已存在";
            if (null == exportDB) {
//                dwzResultParam = new DwzResultParam(DwzResultParam.STATUS_CODE_300, message);

//                return SUCCESS;
                JsonResultUtils.writeSuccessJson(response);
                return;
            } else {
                if (1 < listObjects.size() || !exportDB.getExportId().equals(listObjects.get(0)
                        .getExportId())) {
//                    dwzResultParam = new DwzResultParam(DwzResultParam.STATUS_CODE_300, message);

//                    return SUCCESS;
                    JsonResultUtils.writeSuccessJson(response);
                  return;
                }
            }
        }


        try {
            exportSqlMag.validator(object);

            exportSqlMag.saveObject(object, getLoginUser(request));
        } catch (SqlResolveException e) {
//            dwzResultParam.setStatusCode(DwzResultParam.STATUS_CODE_300);
            String message = null;
            if (0 == e.getErrorcode()) {
                message = e.getMessage();
            } else {
//                message = SysParametersUtils.getValue(String.valueOf(e.getErrorcode()));
            }

//            dwzResultParam.setMessage(message);

            JsonResultUtils.writeSuccessJson(response);
            return;
        }

//        dwzResultParam.setForwardUrl("/dde/exportSql!list.do");
        JsonResultUtils.writeSuccessJson(response);
    }

    @RequestMapping(value="/delete/{exportId}" ,method = {RequestMethod.DELETE})
    public void delete(@PathVariable Long exportId,HttpServletRequest request,HttpServletResponse response) {
        exportSqlMag.deleteObjectById(exportId);
//        return "delete";
        JsonResultUtils.writeSuccessJson(response);
    }

    @RequestMapping(value="/edit/{exportId}" ,method = {RequestMethod.PUT})
    public void edit(ExportSql object,@PathVariable Long exportId,HttpServletRequest request,HttpServletResponse response) {
//        ExportSql exportSql = exportSqlMag.getObjectById(object.getExportId());
        ExportSql exportSql = exportSqlMag.getObjectById(object.getExportId());
        if (null != exportSql) {
            exportSqlMag.copyObject(object, exportSql);
        }

        // copy
        if (StringUtils.hasText(WebUtils.findParameterValue(request, "type"))) {
            object.setExportId(null);
            object.setExportName(null);
            object.setSourceOsId(null);
        }

        // 业务系统
        List<OsInfo> osinfoList = platformEnvironment.listOsInfos();
//        request.setAttribute("osinfoList", osinfoList);

        JsonResultUtils.writeSingleDataJson(osinfoList, response);
//        return EDIT;
    }

    /**
     * 数据导出内容字段
     *
     * @return
     */
    @RequestMapping(value="/listField" ,method = {RequestMethod.GET})
    public void listField(ExportSql object,HttpServletRequest request,HttpServletResponse response) {
        object = exportSqlMag.getObjectById(object.getExportId());
        if (null == object) {
            //
        }

        List<DatabaseInfo> dbList = platformEnvironment.listDatabaseInfo();

//        request.setAttribute("dbList", dbList);

        // 业务系统
        List<OsInfo> osinfoList = platformEnvironment.listOsInfos();
//        request.setAttribute("osinfoList", osinfoList);
//
//        return "listField";
        ResponseData resData = new ResponseData();
        resData.addResponseData("dbList", dbList);
        resData.addResponseData("osinfoList", osinfoList);
        JsonResultUtils.writeResponseDataAsJson(resData, response);
    }
    
    @RequestMapping(value="/defDataSource" ,method = {RequestMethod.GET})
    public void defDataSource(HttpServletRequest request,HttpServletResponse response) {

        List<DatabaseInfo> dbList = platformEnvironment.listDatabaseInfo();

//        request.setAttribute("dbList", dbList);
//
//        return "defDataSource";
        JsonResultUtils.writeSingleDataJson(dbList, response);
    }

//页面跳转/page/dde/importFieldNewForm.jsp
//    @RequestMapping(value="/formField")
//    public String formField() {
//        return "formField";
//    }

//    页面跳转/page/dde/exchangeMapinfoTrigger.jsp
//    @RequestMapping(value="/formTrigger")
//    public String formTrigger() {
//        return "formTrigger";
//    }


    /**
     * 解析Sql语句
     *
     * @throws IOException
     */
    @RequestMapping(value="/resolveQuerySql")
    public void resolveQuerySql(ExportSql object,HttpServletResponse response) throws IOException {
//        PrintWriter writer = response.getWriter();
//        String jsonResult = null;
//        Map<String, Object> params = new HashMap<String, Object>();
        List<ExportField> efList = new ArrayList<ExportField>();
        ResponseData resData = new ResponseData();
        try {
            efList = exportSqlMag.listExportFieldsByQuerysql(object);
        } catch (SqlResolveException e) {
//            params.put("message", SysParametersUtils.getValue(String.valueOf(e.getErrorcode())));
//            jsonResult = JSONObject.fromObject(params).toString();
//            writer.print(jsonResult);
//            return;
            //未通过sql验证，简单验证语法
            resData.addResponseData("simple", 1);
            resData.addResponseData("message", SysParametersUtils.getStringValue(String.valueOf(e.getErrorcode())));
//            params.put("simple", 1);
//            params.put("message", SysParametersUtils.getStringValue(String.valueOf(e.getErrorcode())));
        }

//        params.put("splitsql",SQLUtils.splitSqlByFields(object.getQuerySql()));
//        params.put("sqlfields", SQLUtils.getSqlFileds(object.getQuerySql()));
//        params.put("efList", efList);
        
        
        resData.addResponseData("splitsql",SQLUtils.splitSqlByFields(object.getQuerySql()));
        resData.addResponseData("sqlfields", SQLUtils.getSqlFileds(object.getQuerySql()));
        resData.addResponseData("efList", efList);
        JsonResultUtils.writeMapDataJson(resData, response);
//        jsonResult = JSONObject.fromObject(params).toString();
//        writer.print(jsonResult);
        

    }

    /**
     * 分割Sql语句
     *
     * @throws IOException
     */
    @RequestMapping(value="/splitQuerySql")
    public void splitQuerySql(ExportSql object,HttpServletResponse response) throws IOException {
        ResponseData resData = new ResponseData();
        resData.addResponseData("splitsql", SQLUtils.splitSqlByFields(object.getQuerySql()));
        resData.addResponseData("sqlfields", SQLUtils.getSqlFileds(object.getQuerySql()));
        JsonResultUtils.writeMapDataJson(resData, response);

    }

    /**
     * 解析数据字典中各数据库数据类型匹配
     *
     * @throws IOException
     */
    @RequestMapping(value="/analyissDataType")
    public void analyissDataType() throws IOException {
//        PrintWriter writer = ServletActionContext.getResponse().getWriter();

        List<DataDictionary> dataTypes = (List<DataDictionary>) CodeRepositoryUtil.getDictionary("DATA_TYPE");
//        Map<String, List<String>> dataTypeMap = new HashMap<String, List<String>>();
        
        ResponseData resData = new ResponseData();

        for (DataDictionary dataType : dataTypes) {
            if (resData.getResponseData(dataType.getDataCode())==null) {
                resData.addResponseData(dataType.getDataCode(), new ArrayList<String>());
            }

            if (StringUtils.hasText(dataType.getDataValue())) {
                String[] dts = dataType.getDataValue().split(",");
                for (String dt : dts) {
                    ArrayList<String> arr =  (ArrayList<String>) resData.getResponseData(dataType.getDataCode());
                    arr.add(dt);
                }
            }

        }

//        writer.print(JSONObject.fromObject(dataTypeMap).toString());

    }


    /**
     * 导出源字段
     */
    @RequestMapping(value="/exportSourceField")
    public void exportSourceField(ExportSql object,HttpServletResponse response) throws IOException {
//        HttpServletResponse response = ServletActionContext.getResponse();
        ServletOutputStream output = response.getOutputStream();
        ExportSql exportSql = exportSqlMag.getObjectById(object.getExportId());


        List<ExportField> exportFields = exportSql.getExportFields();

        Map<String, ExportField> values = new HashMap<String, ExportField>();
        for (int i = 0; i < exportFields.size(); i++) {
            values.put(String.valueOf(i), exportFields.get(i));
        }
        String text = com.alibaba.fastjson.JSONObject.toJSONString(values);

        response.setHeader("Content-disposition", "attachment;filename=" + exportSql.getDataOptId() + ".txt");
        response.setContentType("application/txt");
        response.setHeader("Content_Length", String.valueOf(text.length()));

        IOUtils.copy(new StringReader(text), output);
    }
}
