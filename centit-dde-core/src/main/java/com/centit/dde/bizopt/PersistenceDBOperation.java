package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.dataset.SqlDataSetWriter;
import com.centit.dde.utils.ConstantValue;
import com.centit.framework.common.ResponseData;
import com.centit.product.adapter.po.MetaTable;
import com.centit.product.adapter.po.SourceInfo;
import com.centit.product.metadata.dao.SourceInfoDao;
import com.centit.product.metadata.service.MetaDataService;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.database.metadata.TableInfo;
import org.apache.commons.lang3.StringUtils;

import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * @author zhf
 */
public class PersistenceDBOperation implements BizOperation {
    private static String WRITER_ERROR_TAG = "__rmdb_writer_result";
    private static String WRITER_ERROR_MSG = "__rmdb_writer_result_msg";
    private static final String WRITER_INDICATE_APPEND = "append";
    private static final String WRITER_INDICATE_MERGE = "merge";
    private static final String WRITER_INDICATE_UPDATE = "update";

    private SourceInfoDao sourceInfoDao;
    private MetaDataService metaDataService;
    //private String exportPath;

    public PersistenceDBOperation(//String exportPath,
                                  SourceInfoDao sourceInfoDao,
                                  MetaDataService metaDataService) {
        //this.exportPath = exportPath;
        this.sourceInfoDao = sourceInfoDao;
        this.metaDataService = metaDataService;
    }

    private static Map<String, String> fetchFieldMap(JSONArray json) {
        if (json != null) {
            LinkedHashMap<String, String> map = new LinkedHashMap<>();
            for (Object o : json) {
                JSONObject temp = (JSONObject) o;
                String columnName = temp.getString("columnName");
                String propertyName = temp.getString("propertyName");
                if (StringUtils.isNotBlank(columnName) && StringUtils.isNotBlank(propertyName)) {
                    map.put(columnName, propertyName);
                } else {
                    String primaryKey1 = temp.getString("primaryKey1");
                    if (StringUtils.isNotBlank(primaryKey1) && StringUtils.isNotBlank(propertyName)) {
                        map.put(propertyName, primaryKey1);
                    }
                }
            }
            return map;
        }
        return Collections.EMPTY_MAP;
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws FileNotFoundException {
        String sourDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        String databaseCode = BuiltInOperation.getJsonFieldString(bizOptJson, "databaseName", null);
        String tableId = BuiltInOperation.getJsonFieldString(bizOptJson, "tableLabelName", null);
        String writerType = BuiltInOperation.getJsonFieldString(bizOptJson, "writerType", "merge");
        String resultMsg =BuiltInOperation.getJsonFieldString(bizOptJson,"resultMsg",WRITER_ERROR_MSG);
        String result =BuiltInOperation.getJsonFieldString(bizOptJson,"result",WRITER_ERROR_TAG);
        if (databaseCode == null || tableId == null) {
            return BuiltInOperation.createResponseData(0, 1,ResponseData.ERROR_OPERATION,
                "对应的元数据信息找不到，数据库：" + databaseCode + " 表:" + tableId);
        }
        SourceInfo databaseInfo = sourceInfoDao.getDatabaseInfoById(databaseCode);
        if (databaseInfo == null) {
            return BuiltInOperation.createResponseData(0, 1,ResponseData.ERROR_OPERATION,
                "数据库信息无效：" + databaseCode);
        }
        DataSet dataSet = bizModel.fetchDataSetByName(sourDsName);
        if (dataSet == null) {
            return BuiltInOperation.createResponseData(0, 1,ResponseData.ERROR_OPERATION,
                "数据源信息无效：" + sourDsName);
        }
        MetaTable tableInfo = metaDataService.getMetaTableWithRelations(tableId);
        if (tableInfo == null) {
            return BuiltInOperation.createResponseData(0, 1,ResponseData.ERROR_OPERATION,
                "对应的元数据信息找不到，数据库：" + databaseCode + " 表:" + tableId);
        }
        if (bizOptJson.get(ConstantValue.CONFIG) == null) {
            return BuiltInOperation.createResponseData(0, 1,ResponseData.ERROR_OPERATION,
                "没有配置交换字段");
        }
        SqlDataSetWriter dataSetWriter = new SqlDataSetWriter(databaseInfo, tableInfo,result,resultMsg);

        dataSetWriter.setFieldsMap(PersistenceDBOperation.fetchFieldMap(bizOptJson.getJSONArray("config")));
        // BuiltInOperation.jsonArrayToMap(bizOptJson.getJSONArray("config"), "propertyName", "primaryKey1"));
        String saveAsWhole = BuiltInOperation.getJsonFieldString(bizOptJson, "saveAsWhole", "false");
        //设置为true  作为整体提交
        dataSetWriter.setSaveAsWhole("false".equalsIgnoreCase(saveAsWhole));
        switch (writerType) {
            case WRITER_INDICATE_APPEND:
                dataSetWriter.append(dataSet);
                break;
            case WRITER_INDICATE_MERGE:
            case WRITER_INDICATE_UPDATE:
                dataSetWriter.merge(dataSet);
                break;
            default:
                dataSetWriter.save(dataSet);
                break;
        }
        if (dataSetWriter.getErrorNums() > 0) {
            return BuiltInOperation.createResponseData(dataSetWriter.getSuccessNums(), dataSetWriter.getErrorNums(),ResponseData.ERROR_PROCESS_ERROR, dataSetWriter.getInfo());
        }
        return BuiltInOperation.createResponseSuccessData(dataSetWriter.getSuccessNums());
    }

}
