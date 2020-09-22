package com.centit.dde.services;

import com.alibaba.fastjson.JSONArray;
import com.centit.dde.po.DataSetDefine;
import com.centit.dde.vo.ColumnSchema;
import com.centit.support.database.utils.PageDesc;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author zhf
 */
public interface DataSetDefineService {

    /**
     * 新增数据库查询
     */
    void createDbQuery(DataSetDefine dataSetDefine);

    void updateDbQuery(DataSetDefine dataSetDefine);

    void deleteDbQuery(String queryId);

    List<DataSetDefine> listDbQuery(Map<String, Object> params, PageDesc pageDesc);

    DataSetDefine getDbQuery(String queryId);

    List<ColumnSchema> generateSqlFields(String databaseCode, String sql, Map<String, Object> params);
    List<ColumnSchema> generateExcelFields(Map<String, Object> params);
    List<ColumnSchema> generateCsvFields(Map<String, Object> params);
    List<ColumnSchema> generateJsonFields(Map<String, Object> params);
    List<ColumnSchema> generatePostFields(String jsonString);
    JSONArray queryViewSqlData(String databaseCode, String sql, Map<String, Object> params);

    Set<String> generateSqlParams(String sql);

}
