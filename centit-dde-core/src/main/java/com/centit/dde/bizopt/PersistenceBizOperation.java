package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataSet;
import com.centit.dde.dataset.CsvDataSet;
import com.centit.dde.dataset.ExcelDataSet;
import com.centit.dde.dataset.FileDataSet;
import com.centit.dde.dataset.SqlDataSetWriter;
import com.centit.dde.utils.ConstantValue;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.ResponseSingleData;
import com.centit.product.adapter.po.SourceInfo;
import com.centit.product.metadata.dao.SourceInfoDao;
import com.centit.product.metadata.service.MetaDataService;
import com.centit.support.common.ObjectException;
import com.centit.support.database.metadata.TableInfo;

import java.io.File;
import java.io.FileNotFoundException;


/**
 * @author zhf
 */
public class PersistenceBizOperation implements BizOperation {

    private String exportPath;
    private static final String WRITER_INDICATE_APPEND = "append";
    private static final String WRITER_INDICATE_MERGE = "merge";
    private static final String WRITER_INDICATE_UPDATE = "update";
    private SourceInfoDao sourceInfoDao;
    private MetaDataService metaDataService;

    public PersistenceBizOperation(String exportPath,
                                   SourceInfoDao sourceInfoDao,
                                   MetaDataService metaDataService) {
        this.exportPath = exportPath;
        this.sourceInfoDao = sourceInfoDao;
        this.metaDataService = metaDataService;
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson) throws FileNotFoundException {
        String dataType = BuiltInOperation.getJsonFieldString(bizOptJson, "dataType", "D");
        ResponseData jsonObject = new ResponseSingleData();
        switch (dataType) {
            case "E":
                writeExcelFile(bizModel, bizOptJson);
                break;
            case "C":
                writeCsvFile(bizModel, bizOptJson);
                break;
            default:
                jsonObject = writeDatabase(bizModel, bizOptJson);
                break;
        }
        return jsonObject;
    }

    private ResponseData writeDatabase(BizModel bizModel, JSONObject bizOptJson) {
        String isRun = BuiltInOperation.getJsonFieldString(bizOptJson, "isRun", "T");
        if (ConstantValue.FALSE.equalsIgnoreCase(isRun)) {
            return new ResponseSingleData();
        }
        String sourDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        String databaseCode = BuiltInOperation.getJsonFieldString(bizOptJson, "databaseName", null);
        String tableId = BuiltInOperation.getJsonFieldString(bizOptJson, "tableLabelName", null);
        String writerType = BuiltInOperation.getJsonFieldString(bizOptJson, "writerType", "merge");
        if (databaseCode == null || tableId == null) {
            return BuiltInOperation.getResponseData(0, 0,
                "对应的元数据信息找不到，数据库：" + databaseCode + " 表:" + tableId);
        }
        SourceInfo databaseInfo = sourceInfoDao.getDatabaseInfoById(databaseCode);
        if (databaseInfo == null) {
            return BuiltInOperation.getResponseData(0, 0,
                "数据库信息无效：" + databaseCode);
        }
        DataSet dataSet = bizModel.fetchDataSetByName(sourDsName);
        if (dataSet == null) {
            return BuiltInOperation.getResponseData(0, 0,
                "数据源信息无效：" + sourDsName);
        }
        TableInfo tableInfo = metaDataService.getMetaTableWithRelations(tableId);
        if (tableInfo == null) {
            return BuiltInOperation.getResponseData(0, 0,
                "对应的元数据信息找不到，数据库：" + databaseCode + " 表:" + tableId);
        }
        if (bizOptJson.get(ConstantValue.CONFIG) == null) {
            return BuiltInOperation.getResponseData(0, 0,
                "没有配置交换字段");
        }
        SqlDataSetWriter dataSetWriter = new SqlDataSetWriter(databaseInfo, tableInfo);
        dataSetWriter.setFieldsMap(BuiltInOperation.jsonArrayToMap(bizOptJson.getJSONArray("config"), "propertyName", "primaryKey1"));
        String saveAsWhole = BuiltInOperation.getJsonFieldString(bizOptJson, "saveAsWhole", "false");
        dataSetWriter.setSaveAsWhole("true".equalsIgnoreCase(saveAsWhole));
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
            return BuiltInOperation.getResponseData(dataSetWriter.getSuccessNums(), dataSetWriter.getErrorNums(), dataSetWriter.getInfo());
        }
        return BuiltInOperation.getResponseSuccessData(dataSetWriter.getSuccessNums());
    }

    private void writeCsvFile(BizModel bizModel, JSONObject bizOptJson) throws FileNotFoundException {
        String sourDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        String fileName = BuiltInOperation.getJsonFieldString(bizOptJson, "fileName", null);
        if (exportPath == null) {
            throw new ObjectException(bizOptJson,
                ObjectException.NULL_EXCEPTION, "配置文件没有设置保存文件路径");
        }
        CsvDataSet dataSetWriter = new CsvDataSet();
        dataSetWriter.setFilePath(exportPath + File.separator + fileName);
        BuiltInOperation.runAppend(bizModel, bizOptJson);
        dataSetWriter.save(bizModel.getBizData().get(sourDsName));
        //return bizModel;
    }

    private void writeExcelFile(BizModel bizModel, JSONObject bizOptJson) throws FileNotFoundException {
        String sourDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        String fileName = BuiltInOperation.getJsonFieldString(bizOptJson, "fileName", null);
        if (exportPath == null) {
            throw new ObjectException(bizOptJson,
                ObjectException.NULL_EXCEPTION, "配置文件没有设置保存文件路径");
        }
        FileDataSet dataSetWriter = new ExcelDataSet();
        dataSetWriter.setFilePath(exportPath + File.separator + fileName);
        dataSetWriter.save(bizModel.getBizData().get(sourDsName));
        //return bizModel;
    }

}
