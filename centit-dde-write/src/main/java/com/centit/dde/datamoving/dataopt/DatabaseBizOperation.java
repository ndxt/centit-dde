package com.centit.dde.datamoving.dataopt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.framework.ip.po.DatabaseInfo;
import com.centit.framework.ip.service.IntegrationEnvironment;
import com.centit.product.dataopt.bizopt.BuiltInOperation;
import com.centit.product.dataopt.bizopt.PersistenceOperation;
import com.centit.product.dataopt.core.BizModel;
import com.centit.product.dataopt.core.DataSet;
import com.centit.product.dataopt.core.DataSetWriter;
import com.centit.product.dataopt.dataset.CsvDataSet;
import com.centit.product.dataopt.dataset.ExcelDataSet;
import com.centit.product.dataopt.dataset.FileDataSet;
import com.centit.product.dataopt.dataset.SQLDataSetWriter;
import com.centit.product.dataopt.utils.DataSetOptUtil;
import com.centit.product.metadata.service.MetaDataService;
import com.centit.support.common.ObjectException;
import com.centit.support.database.metadata.TableInfo;
import com.centit.support.database.transaction.JdbcTransaction;
import com.centit.support.database.utils.DataSourceDescription;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhf
 */
@Component
public class DatabaseBizOperation extends BuiltInOperation {
    @Value("${os.file.base.dir:./file_home/export}")
    private String path;
    private IntegrationEnvironment integrationEnvironment;
    private MetaDataService metaDataService;

    public DatabaseBizOperation() {

    }

    public DatabaseBizOperation(JSONObject bizOptJson) {
        this.bizOptJson = bizOptJson;
    }
    @Override
    @JdbcTransaction
    public BizModel apply(BizModel bizModel) {
        JSONArray optSteps = bizOptJson.getJSONArray("steps");
        if(optSteps==null || optSteps.isEmpty()){
            return bizModel;
        }
        BizModel result = bizModel;
        for(Object step : optSteps){
            if(step instanceof JSONObject){
                runOneStep(result, (JSONObject)step);
            }
        }
        return result;
    }
    private BizModel runPersistence(BizModel bizModel, JSONObject bizOptJson) {
        String dataType = getJsonFieldString(bizOptJson, "dataType", "D");
        switch (dataType) {
            case "E":
                return writeExcelFile(bizModel, bizOptJson);
            case "C":
                return writeCsvFile(bizModel, bizOptJson);
            default:
                return writeDatabase(bizModel, bizOptJson);

        }

    }


    private BizModel writeDatabase(BizModel bizModel, JSONObject bizOptJson) {
        String sourDsName = getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        String databaseCode = getJsonFieldString(bizOptJson, "databaseCode", null);
        String tableId = getJsonFieldString(bizOptJson, "tableId", null);
        if (StringUtils.isBlank(tableId)) {
            tableId = getJsonFieldString(bizOptJson, "tableName", null);
        }
        String writerType = getJsonFieldString(bizOptJson, "writerType", "merge");
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
        runPersistenceMap(bizModel, bizOptJson);
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
        DataSetWriter dataSetWriter = new SQLDataSetWriter(
            DataSourceDescription.valueOf(databaseInfo), tableInfo);

        switch (writerType) {
            case PersistenceOperation.WRITER_INDICATE_APPEND:
                dataSetWriter.append(dataSet);
                break;
            case PersistenceOperation.WRITER_INDICATE_MERGE:
            case PersistenceOperation.WRITER_INDICATE_UPDATE:
                dataSetWriter.merge(dataSet);
                break;
            default:
                dataSetWriter.save(dataSet);
                break;
        }
        return bizModel;
    }

    private void runPersistenceMap(BizModel bizModel, JSONObject bizOptJson) {
        String sourDsName = getJsonFieldString(bizOptJson,"source", bizModel.getModelName());
        String targetDsName = getJsonFieldString(bizOptJson, "target", sourDsName);
        Object mapInfo = bizOptJson.get("fieldsMap");
        if(mapInfo instanceof Map){
            DataSet dataSet = bizModel.fetchDataSetByName(sourDsName);
            HashMap<Object, Object> map=new HashMap<>(((Map) mapInfo).size()+1);
            ((Map) mapInfo).forEach((key,value)->map.put(value,key));
            if(dataSet != null) {
                DataSet destDs = DataSetOptUtil.appendDeriveField(dataSet, ((Map) map).entrySet());
                //if(destDS != null){
                bizModel.putDataSet(targetDsName, destDs);
                //}
            }
        }
    }

    private BizModel writeCsvFile(BizModel bizModel, JSONObject bizOptJson) {
        String sourDsName = getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        String fileName = getJsonFieldString(bizOptJson, "fileName", null);
        if (path == null) {
            throw new ObjectException(bizOptJson,
                ObjectException.NULL_EXCEPTION, "配置文件没有设置保存文件路径");
        }
        CsvDataSet dataSetWriter = new CsvDataSet();
        dataSetWriter.setFilePath(path + File.separator + fileName);
        dataSetWriter.save(runAppend(bizModel, bizOptJson).getBizData().get(sourDsName));
        return bizModel;
    }

    private BizModel writeExcelFile(BizModel bizModel, JSONObject bizOptJson) {
        String sourDsName = getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        String fileName = getJsonFieldString(bizOptJson, "fileName", null);
        if (path == null) {
            throw new ObjectException(bizOptJson,
                ObjectException.NULL_EXCEPTION, "配置文件没有设置保存文件路径");
        }
        FileDataSet dataSetWriter = new ExcelDataSet();
        dataSetWriter.setFilePath(path + File.separator + fileName);
        dataSetWriter.save(bizModel.getBizData().get(sourDsName));
        return bizModel;
    }



    @Override
    public BizModel runOneStep(BizModel bizModel, JSONObject bizOptJson) {
        super.runOneStep(bizModel,bizOptJson);
        String sOptType = bizOptJson.getString("operation");
        if("persistence".equals(sOptType)){
                return runPersistence(bizModel, bizOptJson);
        }
        return bizModel;
    }

    public void setIntegrationEnvironment(IntegrationEnvironment integrationEnvironment) {
        this.integrationEnvironment = integrationEnvironment;
    }

    public void setMetaDataService(MetaDataService metaDataService) {
        this.metaDataService = metaDataService;
    }
}
