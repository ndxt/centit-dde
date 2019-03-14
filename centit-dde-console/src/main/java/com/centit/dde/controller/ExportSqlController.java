package com.centit.dde.controller;

import com.centit.dde.po.ExportField;
import com.centit.dde.po.ExportSql;
import com.centit.dde.service.ExportSqlManager;
import com.centit.framework.common.JsonResultUtils;
import com.centit.framework.common.ResponseMapData;
import com.centit.framework.components.CodeRepositoryUtil;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.security.model.CentitUserDetails;
import com.centit.framework.staticsystem.po.DataDictionary;
import com.centit.support.database.utils.PageDesc;
import com.centit.support.database.utils.QueryUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

    @Resource
    private ExportSqlManager exportSqlManager;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public void list(PageDesc pageDesc, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> filterMap = convertSearchColumn(request);
        List<ExportSql> listObjects = exportSqlManager.listObjects(filterMap);
        ResponseMapData resData = new ResponseMapData();
        resData.addResponseData(OBJLIST, listObjects);
        resData.addResponseData(PAGE_DESC, pageDesc);

        JsonResultUtils.writeResponseDataAsJson(resData, response);
    }

    @RequestMapping(value="/save",method = {RequestMethod.PUT})
    public void save(ExportSql object, HttpServletRequest request,HttpServletResponse response) {

        CentitUserDetails user = getLoginUser(request);
        exportSqlManager.saveObject(object, user);

        JsonResultUtils.writeSuccessJson(response);
    }

    @RequestMapping(value="/delete/{exportId}" ,method = {RequestMethod.DELETE})
    public void delete(@PathVariable Long exportId, HttpServletResponse response) {
        exportSqlManager.deleteObjectById(exportId);
        JsonResultUtils.writeSuccessJson(response);
    }

    @RequestMapping(value="/edit/{exportId}" ,method = {RequestMethod.GET})
    public void edit(@PathVariable Long exportId, HttpServletResponse response) {
        ExportSql exportSql = exportSqlManager.getObjectById(exportId);

        JsonResultUtils.writeSingleDataJson(exportSql,response);
    }

    /**
     * 解析Sql语句
     *
     * @throws IOException
     */
    @RequestMapping(value="/resolveQuerySql" ,method = {RequestMethod.POST})
    public void resolveQuerySql(ExportSql object,HttpServletResponse response) {
        List<ExportField> fields = new ArrayList();
        fields = exportSqlManager.listExportFieldsByQuerysql(object);
        JsonResultUtils.writeSingleDataJson(fields, response);
    }

    /**
     * 分割Sql语句
     *
     * @throws IOException
     */
    @RequestMapping(value="/splitQuerySql")
    public void splitQuerySql(ExportSql object,HttpServletResponse response) throws IOException {
        ResponseMapData resData = new ResponseMapData();
        resData.addResponseData("splitsql", QueryUtils.splitSqlByFields(object.getQuerySql()));
        resData.addResponseData("sqlfields", QueryUtils.getSqlFiledNames(object.getQuerySql()));
        JsonResultUtils.writeResponseDataAsJson(resData, response);

    }

    /**
     * 解析数据字典中各数据库数据类型匹配
     *
     * @throws IOException
     */
    @RequestMapping(value="/analyseDataType")
    public void analyseDataType() throws IOException {
//        PrintWriter writer = ServletActionContext.getResponse().getWriter();

        List<DataDictionary> dataTypes = (List<DataDictionary>) CodeRepositoryUtil.getDictionary("DATA_TYPE");
//        Map<String, List<String>> dataTypeMap = new HashMap<String, List<String>>();

        ResponseMapData resData = new ResponseMapData();

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
        ServletOutputStream output = response.getOutputStream();
        ExportSql exportSql = exportSqlManager.getObjectById(object.getExportId());


        List<ExportField> exportFields = exportSql.getExportFields();

        Map<String, ExportField> values = new HashMap<>();
        for (int i = 0; i < exportFields.size(); i++) {
            values.put(String.valueOf(i), exportFields.get(i));
        }
        String text = com.alibaba.fastjson.JSONObject.toJSONString(values);

        response.setHeader("Content-disposition", "attachment;filename=" + exportSql.getDataOptId() + ".txt");
        response.setContentType("application/txt");
        response.setHeader("Content_Length", String.valueOf(text.length()));

        IOUtils.copy(new StringReader(text), output, "UTF-8");
    }
}
