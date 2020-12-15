package com.centit.dde.controller;

import com.alibaba.fastjson.JSONArray;
import com.centit.dde.services.GenerateFieldsService;
import com.centit.dde.vo.ColumnSchema;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpResponseBody;
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
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        @ApiImplicitParam(name = "databaseCode", value = "数据库代码", required = true),
        @ApiImplicitParam(name = "sql", value = "查询SQL", required = true)
    })
    @RequestMapping(value = "/previewdata", method = {RequestMethod.POST})
    @WrapUpResponseBody
    public JSONArray queryViewSqlData(String databaseCode, String sql, HttpServletRequest request) {
        Map<String, Object> params = collectRequestParameters(request);
        return generateFieldsService.queryViewSqlData(databaseCode, StringEscapeUtils.unescapeHtml4(sql), params);
    }

    @ApiOperation(value = "生成查询字段列表")
    @ApiImplicitParam(name = "sql", value = "查询SQL", required = true)
    @RequestMapping(value = "/sqlcolumn", method = {RequestMethod.POST})
    @WrapUpResponseBody
    public List<ColumnSchema> generateSqlcolumn(String databaseCode,String sql, String dataType, HttpServletRequest request) {
        sql = StringEscapeUtils.unescapeHtml4(sql);
        Map<String, Object> params = collectRequestParameters(request);
        switch (dataType) {
            case "E":
                params.put("FileId", sql);
                return generateFieldsService.generateExcelFields(params);
            case "C":
                params.put("FileId", sql);
                return generateFieldsService.generateCsvFields(params);
            case "J":
                return generateFieldsService.generateJsonFields(params);
            case "D":
                return generateFieldsService.generateSqlFields(databaseCode,sql, params);
            case "P":
                return generateFieldsService.generatePostFields(sql);
            default:
                return null;
        }
    }

    @ApiOperation(value = "生成参数名称列表")
    @ApiImplicitParam(name = "sql", value = "查询SQL", required = true)
    @RequestMapping(value = "/param", method = {RequestMethod.POST})
    @WrapUpResponseBody
    public Set<String> generateParam(String sql) {
        return generateFieldsService.generateSqlParams(sql);
    }

}
