package com.centit.dde.datamoving.dataopt;

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
import com.centit.product.metadata.service.MetaDataService;
import com.centit.support.common.ObjectException;
import com.centit.support.database.metadata.TableInfo;
import com.centit.support.database.transaction.JdbcTransaction;
import com.centit.support.database.utils.DataSourceDescription;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

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

    private BizModel runPersistence(BizModel bizModel, JSONObject bizOptJson) {
        String dataType = getJsonFieldString(bizOptJson, "dataType", "D");
        switch (dataType) {
            case "E":
                return writeExcelFile(bizModel, bizOptJson);
            case "C":
                return writeCsvFile(bizModel, bizOptJson);
            case "H":
                return runHttpPost(bizModel, bizOptJson);
            default:
                return writeDatabase(bizModel, bizOptJson);

        }

    }

    @JdbcTransaction
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
        runMap(bizModel, bizOptJson);
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

    private BizModel runHttpPost(BizModel bizModel, JSONObject bizOptJson) {

        return bizModel;
    }

    @Override
    public BizModel runOneStep(BizModel bizModel, JSONObject bizOptJson) {
        String sOptType = bizOptJson.getString("operation");
        if (StringUtils.isBlank(sOptType)) {
            return bizModel;
        }
        switch (sOptType) {
            case "map":
                return runMap(bizModel, bizOptJson);
            case "filter":
                return runFilter(bizModel, bizOptJson);
            case "append":
                return runAppend(bizModel, bizOptJson);
            case "stat":
                return runStat(bizModel, bizOptJson);
            case "analyse":
                return runAnalyse(bizModel, bizOptJson);
            case "cross":
                return runCross(bizModel, bizOptJson);
            case "compare":
                return runCompare(bizModel, bizOptJson);
            case "join":
                return runJoin(bizModel, bizOptJson);
            case "sort":
                return runSort(bizModel, bizOptJson);
            case "union":
                return runUnion(bizModel, bizOptJson);
            case "filterExt":
                return runFilterExt(bizModel, bizOptJson);
            case "check":
                return runCheckData(bizModel, bizOptJson);
            case "static":
                return runStaticData(bizModel, bizOptJson);
            case "persistence":
                return runPersistence(bizModel, bizOptJson);
            default:
                return bizModel;
        }
    }

    public void setIntegrationEnvironment(IntegrationEnvironment integrationEnvironment) {
        this.integrationEnvironment = integrationEnvironment;
    }

    public void setMetaDataService(MetaDataService metaDataService) {
        this.metaDataService = metaDataService;
    }
}
