package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataSet;
import com.centit.dde.dataset.CsvDataSet;
import com.centit.dde.dataset.ExcelDataSet;
import com.centit.dde.dataset.FileDataSet;
import com.centit.dde.dataset.SQLDataSetWriter;
import com.centit.framework.ip.po.DatabaseInfo;
import com.centit.framework.ip.service.IntegrationEnvironment;
import com.centit.product.metadata.service.MetaDataService;
import com.centit.support.common.ObjectException;
import com.centit.support.database.metadata.TableInfo;
import com.centit.support.database.utils.DataSourceDescription;

import java.io.File;
import java.util.Map;


/**
 * @author zhf
 */
public class PersistenceBizOperation implements BizOperation {

    private String exportPath;
    private static final String WRITER_INDICATE_APPEND = "append";
    private static final String WRITER_INDICATE_MERGE = "merge";
    private static final String WRITER_INDICATE_UPDATE = "update";
    private IntegrationEnvironment integrationEnvironment;
    private MetaDataService metaDataService;

    public PersistenceBizOperation(String exportPath,
                                   IntegrationEnvironment integrationEnvironment,
                                   MetaDataService metaDataService) {
        this.exportPath = exportPath;
        this.integrationEnvironment = integrationEnvironment;
        this.metaDataService = metaDataService;
    }

    @Override
    public JSONObject runOpt(BizModel bizModel, JSONObject bizOptJson) {
        String dataType = BuiltInOperation.getJsonFieldString(bizOptJson, "dataType", "D");
        JSONObject jsonObject = new JSONObject();
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

    public JSONObject writeDatabase(BizModel bizModel, JSONObject bizOptJson) {
        String isRun = BuiltInOperation.getJsonFieldString(bizOptJson, "isRun", "T");
        if ("F".equalsIgnoreCase(isRun)) {
            return new JSONObject();
        }
        String sourDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        String databaseCode = BuiltInOperation.getJsonFieldString(bizOptJson, "databaseName", null);
        String tableId = BuiltInOperation.getJsonFieldString(bizOptJson, "tableLabelName", null);
        String writerType = BuiltInOperation.getJsonFieldString(bizOptJson, "writerType", "merge");
        if (databaseCode == null || tableId == null) {
            throw new ObjectException(bizOptJson,
                ObjectException.NULL_EXCEPTION,
                "对应的元数据信息找不到，数据库：" + databaseCode + " 表:" + tableId);
        }
        DatabaseInfo databaseInfo = integrationEnvironment.getDatabaseInfo(databaseCode);
        if (databaseInfo == null) {
            throw new ObjectException(bizOptJson,
                ObjectException.NULL_EXCEPTION,
                "数据库信息无效：" + databaseCode);
        }
        DataSet dataSet = bizModel.fetchDataSetByName(sourDsName);
        if (dataSet == null) {
            throw new ObjectException(bizOptJson,
                ObjectException.NULL_EXCEPTION,
                "数据源信息无效：" + sourDsName);
        }
        TableInfo tableInfo = metaDataService.getMetaTableWithRelations(tableId);
        if (tableInfo == null) {
            throw new ObjectException(bizOptJson,
                ObjectException.NULL_EXCEPTION,
                "对应的元数据信息找不到，数据库：" + databaseCode + " 表:" + tableId);
        }
        if (bizOptJson.get("config") == null) {
            throw new ObjectException(bizOptJson,
                ObjectException.NULL_EXCEPTION, "没有配置交换字段");
        }
        SQLDataSetWriter dataSetWriter = new SQLDataSetWriter(
            DataSourceDescription.valueOf(databaseInfo), tableInfo);
        dataSetWriter.setFieldsMap((Map) BuiltInOperation.jsonArrayToMap(bizOptJson.getJSONArray("config"), "propertyName", "primaryKey1"));
        String saveAsWhole = BuiltInOperation.getJsonFieldString(bizOptJson, "saveAsWhole", "T");
        dataSetWriter.setSaveAsWhole("T".equalsIgnoreCase(saveAsWhole));
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

        JSONObject map = new JSONObject();
        if (dataSet.size() > 0) {
            map.put("info", dataSetWriter.getInfo());
        } else {
            map.put("info", "ok");
        }
        map.put("success", dataSetWriter.getSuccessNums());
        map.put("error", dataSetWriter.getErrorNums());
        return map;
    }

    public void writeCsvFile(BizModel bizModel, JSONObject bizOptJson) {
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

    public void writeExcelFile(BizModel bizModel, JSONObject bizOptJson) {
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
