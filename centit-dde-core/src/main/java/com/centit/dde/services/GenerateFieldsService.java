package com.centit.dde.services;

import com.alibaba.fastjson2.JSONArray;
import com.centit.dde.vo.ColumnSchema;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author zhf
 */
public interface GenerateFieldsService {

    /**
     * 新增数据库查询
     */
    List<ColumnSchema> generateSqlFields(String databaseCode,String sql, Map<String, Object> params);
    //List<ColumnSchema> generateExcelFields(Map<String, Object> params);
    List<ColumnSchema> generateCsvFields(Map<String, Object> params) throws IOException;
    List<ColumnSchema> generateJsonFields(Map<String, Object> params);
    List<ColumnSchema> generatePostFields(String jsonString);
    JSONArray queryViewSqlData(String databaseCode, String sql, Map<String, Object> params);
    JSONArray queryViewCsvData(String fileId) throws Exception;
    JSONArray queryViewJsonData(String fileId) throws Exception;
    Set<String> generateSqlParams(String sql);

}
