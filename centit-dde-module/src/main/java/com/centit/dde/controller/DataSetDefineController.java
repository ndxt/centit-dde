package com.centit.dde.controller;

import com.alibaba.fastjson.JSONArray;
import com.centit.dde.po.DataSetDefine;
import com.centit.dde.services.DataSetDefineService;
import com.centit.dde.vo.ColumnSchema;
import com.centit.framework.common.WebOptUtils;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.framework.core.dao.PageQueryResult;
import com.centit.support.database.utils.PageDesc;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author zhf
 */
@Api(value = "数据集查询", tags = "数据集查询")
@RestController
@RequestMapping(value = "query")
public class DataSetDefineController extends BaseController {

    private final DataSetDefineService dataSetDefineService;
    @Autowired
    public DataSetDefineController(DataSetDefineService dataSetDefineService) {
        this.dataSetDefineService = dataSetDefineService;
    }

    @ApiOperation(value = "新增数据集")
    @PostMapping
    @WrapUpResponseBody
    public void createDbQuery(DataSetDefine dataSetDefine, HttpServletRequest request){
        String userCode = WebOptUtils.getCurrentUserCode(request);
        dataSetDefine.setRecorder(userCode);
        dataSetDefineService.createDbQuery(dataSetDefine);
    }

    @ApiOperation(value = "编辑数据集")
    @PutMapping(value = "/{queryId}")
    @WrapUpResponseBody
    public void updateDbQuery(@PathVariable String queryId, DataSetDefine dataSetDefine){
        dataSetDefine.setQueryId(queryId);
        dataSetDefineService.updateDbQuery(dataSetDefine);
    }

    @ApiOperation(value = "删除数据集")
    @DeleteMapping(value = "/{queryId}")
    @WrapUpResponseBody
    public void deleteDbQuery(@PathVariable String queryId){
        dataSetDefineService.deleteDbQuery(queryId);
    }

    @ApiOperation(value = "查询数据集")
    @GetMapping
    @WrapUpResponseBody
    public PageQueryResult<DataSetDefine> listDbQuery(PageDesc pageDesc){
        List<DataSetDefine> list = dataSetDefineService.listDbQuery(null, pageDesc);
        return PageQueryResult.createResult(list, pageDesc);
    }

    @ApiOperation(value = "查询单个数据集")
    @GetMapping(value = "/{queryId}")
    @WrapUpResponseBody
    public DataSetDefine getDbQuery(@PathVariable String queryId){
        return dataSetDefineService.getDbQuery(queryId);
    }

    @ApiOperation(value = "预览数据值返回前20行")
    @ApiImplicitParams(value = {
        @ApiImplicitParam(name = "databaseCode", value = "数据库代码", required = true),
        @ApiImplicitParam(name = "sql", value = "查询SQL", required = true)
    })
    @RequestMapping(value = "/reviewdata", method = {RequestMethod.POST})
    @WrapUpResponseBody
    public JSONArray queryViewSqlData(String databaseCode, String sql, HttpServletRequest request){
        Map<String, Object> params = collectRequestParameters(request);
        return dataSetDefineService.queryViewSqlData(databaseCode, sql, params);
    }

    @ApiOperation(value = "生成查询字段列表")
    @ApiImplicitParam(name = "sql", value = "查询SQL", required = true)
    @RequestMapping(value = "/sqlcolumn", method = {RequestMethod.POST})
    @WrapUpResponseBody
    public List<ColumnSchema> generateSqlcolumn(String databaseCode, String sql, String dataType, HttpServletRequest request){
        sql= StringEscapeUtils.unescapeHtml4(sql);
        Map<String, Object> params = collectRequestParameters(request);
        switch (dataType) {
            case "E":
                params.put("FileId", sql);
                return dataSetDefineService.generateExcelFields(params);
            case "C":
                params.put("FileId", sql);
                return dataSetDefineService.generateCsvFields(params);
            case "J":
                return dataSetDefineService.generateJsonFields(params);
            case "D":
                return dataSetDefineService.generateSqlFields(databaseCode, sql, params);
            default:
                return null;
        }
    }


    @ApiOperation(value = "生成参数名称列表")
    @ApiImplicitParam(name = "sql", value = "查询SQL", required = true)
    @RequestMapping(value = "/param", method = {RequestMethod.POST})
    @WrapUpResponseBody
    public Set<String> generateParam(String sql ){
        return dataSetDefineService.generateSqlParams(sql);
    }

}
