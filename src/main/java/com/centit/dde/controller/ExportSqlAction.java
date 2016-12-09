package com.centit.dde.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

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
import com.centit.core.dao.SQLUtils;
import com.centit.core.utils.DwzResultParam;
import com.centit.dde.exception.SqlResolveException;
import com.centit.dde.po.ExportField;
import com.centit.dde.po.ExportSql;
import com.centit.dde.service.ExportSqlManager;
import com.centit.framework.core.common.JsonResultUtils;
import com.centit.framework.staticsystem.po.DatabaseInfo;
import com.centit.framework.staticsystem.po.OsInfo;
import com.centit.sys.po.FDatadictionary;
import com.centit.sys.security.FUserDetail;
import com.centit.sys.service.CodeRepositoryUtil;
import com.centit.sys.util.SysParametersUtils;

@Controller
@RequestMapping("/exportsql")
public class ExportSqlAction extends BaseEntityDwzAction<ExportSql> {
    private static final Log log = LogFactory.getLog(ExportSqlAction.class);

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

    private ExportSqlManager exportSqlMag;

    private DatabaseInfoManager databaseInfoManager;

    private OsInfoManager osInfoManager;

    public void setOsInfoManager(OsInfoManager osInfoManager) {
        this.osInfoManager = osInfoManager;
    }

    public void setDatabaseInfoManager(DatabaseInfoManager databaseInfoManager) {
        this.databaseInfoManager = databaseInfoManager;
    }

    public void setExportSqlManager(ExportSqlManager basemgr) {
        exportSqlMag = basemgr;
        this.setBaseEntityManager(exportSqlMag);
    }

    @RequestMapping(value="/save",method = {RequestMethod.PUT})
    public void save( HttpServletRequest request,HttpServletResponse response) {
        dwzResultParam = new DwzResultParam();
        dwzResultParam.setNavTabId(tabid);


        //判断名称的唯一性
        object.setExportName(org.apache.commons.lang.StringUtils.trim(object.getExportName()));

        Map<String, Object> filterMap = new HashMap<String, Object>();
        filterMap.put("eqExportName", object.getExportName());

        List<ExportSql> listObjects = exportSqlMag.listObjects(filterMap);


        if (!CollectionUtils.isEmpty(listObjects)) {
            ExportSql exportDB = exportSqlMag.getObject(object);
            String message = "导出名称已存在";
            if (null == exportDB) {
                dwzResultParam = new DwzResultParam(DwzResultParam.STATUS_CODE_300, message);

//                return SUCCESS;
                JsonResultUtils.writeSuccessJson(response);
                return;
            } else {
                if (1 < listObjects.size() || !exportDB.getExportId().equals(listObjects.get(0)
                        .getExportId())) {
                    dwzResultParam = new DwzResultParam(DwzResultParam.STATUS_CODE_300, message);

//                    return SUCCESS;
                    JsonResultUtils.writeSuccessJson(response);
                  return;
                }
            }
        }


        try {
            exportSqlMag.validator(object);

            exportSqlMag.saveObject(object, (FUserDetail) getLoginUser());
        } catch (SqlResolveException e) {
            dwzResultParam.setStatusCode(DwzResultParam.STATUS_CODE_300);
            String message = null;
            if (0 == e.getErrorcode()) {
                message = e.getMessage();
            } else {
                message = SysParametersUtils.getValue(String.valueOf(e.getErrorcode()));
            }

            dwzResultParam.setMessage(message);

            JsonResultUtils.writeSuccessJson(response);
            return;
        }

        dwzResultParam.setForwardUrl("/dde/exportSql!list.do");
        JsonResultUtils.writeSuccessJson(response);
    }

    @RequestMapping(value="/delete" ,method = {RequestMethod.DELETE})
    public void delete(HttpServletRequest request,HttpServletResponse response) {
        super.delete();

//        return "delete";
        JsonResultUtils.writeSuccessJson(response);
    }

    @RequestMapping(value="/edit" ,method = {RequestMethod.PUT})
    public void edit(HttpServletRequest request,HttpServletResponse response) {
        ExportSql exportSql = exportSqlMag.getObject(object);
        if (null != exportSql) {
            baseEntityManager.copyObject(object, exportSql);
        }

        // copy
        if (StringUtils.hasText(WebUtils.findParameterValue(request, "type"))) {
            object.setExportId(null);
            object.setExportName(null);
            object.setSourceOsId(null);
        }

        // 业务系统
        List<OsInfo> osinfoList = osInfoManager.listObjects();
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
        JsonResultUtils.writeSingleDataJson(osinfoList, response);
    }
    
    @RequestMapping(value="/defDataSource" ,method = {RequestMethod.GET})
    public void defDataSource(HttpServletRequest request,HttpServletResponse response) {

        List<DatabaseInfo> dbList = databaseInfoManager.listObjects();

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
    public void resolveQuerySql() throws IOException {
        PrintWriter writer = ServletActionContext.getResponse().getWriter();
        String jsonResult = null;
        Map<String, Object> params = new HashMap<String, Object>();
        List<ExportField> efList = new ArrayList<ExportField>();

        try {
            efList = exportSqlMag.listExportFieldsByQuerysql(object);
        } catch (SqlResolveException e) {
//            params.put("message", SysParametersUtils.getValue(String.valueOf(e.getErrorcode())));
//            jsonResult = JSONObject.fromObject(params).toString();
//            writer.print(jsonResult);
//
//
//
//            return;

            //未通过sql验证，简单验证语法
            params.put("simple", 1);
            params.put("message", SysParametersUtils.getValue(String.valueOf(e.getErrorcode())));
        }

        params.put("splitsql", SQLUtils.splitSqlByFields(object.getQuerySql()));
        params.put("sqlfields", SQLUtils.getSqlFileds(object.getQuerySql()));
        params.put("efList", efList);

        jsonResult = JSONObject.fromObject(params).toString();
        writer.print(jsonResult);

    }

    /**
     * 分割Sql语句
     *
     * @throws IOException
     */
    @RequestMapping(value="/splitQuerySql")
    public void splitQuerySql() throws IOException {
        PrintWriter writer = ServletActionContext.getResponse().getWriter();
        String jsonResult = null;
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("splitsql", SQLUtils.splitSqlByFields(object.getQuerySql()));
        params.put("sqlfields", SQLUtils.getSqlFileds(object.getQuerySql()));

        jsonResult = JSONObject.fromObject(params).toString();
        writer.print(jsonResult);

    }

    /**
     * 解析数据字典中各数据库数据类型匹配
     *
     * @throws IOException
     */
    @RequestMapping(value="/analyissDataType")
    public void analyissDataType() throws IOException {
        PrintWriter writer = ServletActionContext.getResponse().getWriter();

        List<FDatadictionary> dataTypes = CodeRepositoryUtil.getDictionary("DATA_TYPE");
        Map<String, List<String>> dataTypeMap = new HashMap<String, List<String>>();

        for (FDatadictionary dataType : dataTypes) {
            if (!dataTypeMap.containsKey(dataType.getDatacode())) {
                dataTypeMap.put(dataType.getDatacode(), new ArrayList<String>());
            }

            if (StringUtils.hasText(dataType.getDatavalue())) {
                String[] dts = dataType.getDatavalue().split(",");
                for (String dt : dts) {
                    dataTypeMap.get(dataType.getDatacode()).add(dt);
                }
            }

        }

        writer.print(JSONObject.fromObject(dataTypeMap).toString());

    }


    /**
     * 导出源字段
     */
    @RequestMapping(value="/exportSourceField")
    public void exportSourceField() throws IOException {
        HttpServletResponse response = ServletActionContext.getResponse();
        ServletOutputStream output = response.getOutputStream();
        ExportSql exportSql = exportSqlMag.getObject(object);


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
