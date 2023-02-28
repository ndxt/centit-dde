package com.centit.dde.controller;

import com.alibaba.fastjson2.JSONArray;
import com.centit.dde.services.GenerateFieldsService;
import com.centit.dde.vo.ColumnSchema;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.support.common.ObjectException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

/**
 * @author zhf
 */
@Api(value = "获取列查询", tags = "列查询")
@RestController
@RequestMapping(value = "query")
public class GenerateFieldsController extends BaseController {

    private final GenerateFieldsService generateFieldsService;

    @Autowired
    public GenerateFieldsController(GenerateFieldsService generateFieldsService) {
        this.generateFieldsService = generateFieldsService;
    }

    @ApiOperation(value = "预览数据值返回前20行")
    @ApiImplicitParams(value = {
        @ApiImplicitParam(name = "databaseCode", value = "C:csv;J:json;其他为数据库", required = true),
        @ApiImplicitParam(name = "sql", value = "C,J:fileId;其他sql查询", required = true)
    })
    @RequestMapping(value = "/previewdata", method = {RequestMethod.POST})
    @WrapUpResponseBody
    public JSONArray queryViewSqlData(String databaseCode, String sql, HttpServletRequest request) {
        Map<String, Object> params = collectRequestParameters(request);
        try {
            switch (databaseCode) {
                case "C":
                    return generateFieldsService.queryViewCsvData(sql);
                case "J":
                    return generateFieldsService.queryViewJsonData(sql);
                default:
                    return generateFieldsService.queryViewSqlData(databaseCode, StringEscapeUtils.unescapeHtml4(sql), params);
            }
        } catch (Exception e) {
            throw new ObjectException(e);
        }
    }

    @ApiOperation(value = "生成查询字段列表")
    @ApiImplicitParams(value = {
        @ApiImplicitParam(name = "dataType", value = "C:csv;J:json;D:数据库", required = true),
        @ApiImplicitParam(name = "sql", value = "C,J:fileId;其他sql查询", required = true)
    })
    @RequestMapping(value = "/sqlcolumn", method = {RequestMethod.POST})
    @WrapUpResponseBody
    public List<ColumnSchema> generateSqlColumn(String databaseCode, String sql, String dataType, HttpServletRequest request) throws IOException {
        sql = StringEscapeUtils.unescapeHtml4(sql);
        Map<String, Object> params = collectRequestParameters(request);
        switch (dataType) {
            //case "E":
                //params.put("FileId", sql);
                //return generateFieldsService.generateExcelFields(params);
            case "C":
                params.put("FileId", sql);
                return generateFieldsService.generateCsvFields(params);
            case "J":
                return generateFieldsService.generateJsonFields(params);
            case "D":
                return generateFieldsService.generateSqlFields(databaseCode, sql, params);
            case "P":
                return generateFieldsService.generatePostFields(sql);
            default:
                return Collections.emptyList();
        }
    }

    @ApiOperation(value = "生成参数名称列表")
    @ApiImplicitParam(name = "sql", value = "查询SQL", required = true)
    @RequestMapping(value = "/param", method = {RequestMethod.POST})
    @WrapUpResponseBody
    public JSONArray generateParam(String sql) {
        Set<String> sets = generateFieldsService.generateSqlParams(sql);
        JSONArray jsonArray = new JSONArray();
        for (String key : sets) {
            Map<String, String> map = new HashMap<>(2);
            map.put("key", key);
            map.put("value", "");
            jsonArray.add(map);
        }
        return jsonArray;
    }

}
