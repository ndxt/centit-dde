package com.centit.dde.services;

import com.centit.dde.po.DataPacket;
import com.centit.dde.po.DataSetDefine;
import com.centit.fileserver.common.FileStore;
import com.centit.framework.ip.service.IntegrationEnvironment;
import com.centit.product.dataopt.core.*;
import com.centit.product.dataopt.dataset.*;
import com.centit.support.database.utils.DataSourceDescription;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * @author zhf
 */
public class DBPacketBizSupplier implements BizSupplier {

    private DataPacket dbPacket;
    private IntegrationEnvironment integrationEnvironment;
    private Map<String, Object> queryParams;
    private boolean batchWise;
    private FileStore fileStore;

    public DBPacketBizSupplier(DataPacket dbPacket) {
        this.dbPacket = dbPacket;
    }

    /**
     * Gets a result.
     *
     * @return a result
     */
    @Override
    public BizModel get() {
        SimpleBizModel bizModel = new SimpleBizModel(this.dbPacket.getPacketName());
        Map<String, DataSet> dataSets = new HashMap<>(this.dbPacket.getDataSetDefines() != null ?
            this.dbPacket.getDataSetDefines().size() + 1 : 1);
        Map<String, Object> modelTag = this.dbPacket.getPacketParamsValue();
        if (queryParams != null && queryParams.size() > 0) {
            modelTag.putAll(queryParams);
        }
        if (this.dbPacket.getDataSetDefines() != null && this.dbPacket.getDataSetDefines().size() > 0) {
            for (DataSetDefine rdd : this.dbPacket.getDataSetDefines()) {
                switch (rdd.getSetType()) {
                    case "D": {
                        SQLDataSetReader sqlDsr = new SQLDataSetReader();
                        sqlDsr.setDataSource(DataSourceDescription.valueOf(
                            integrationEnvironment.getDatabaseInfo(rdd.getDatabaseCode())));
                        sqlDsr.setSqlSen(rdd.getQuerySQL());
                        SimpleDataSet dataset = sqlDsr.load(modelTag);
                        dataset.setDataSetName(rdd.getQueryName());
                        dataSets.put(rdd.getQueryId(), dataset);
                        break;
                    }
                    case "E": {
                        ExcelDataSet excelDataSet = new ExcelDataSet();
                        try {
                            excelDataSet.setFilePath(
                                fileStore.getFile(rdd.getQuerySQL()).getPath());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        SimpleDataSet dataset = excelDataSet.load(modelTag);
                        dataset.setDataSetName(rdd.getQueryName());
                        dataSets.put(rdd.getQueryId(), dataset);
                        break;
                    }
                    case "C": {
                        CsvDataSet csvDataSet = new CsvDataSet();
                        try {
                            csvDataSet.setFilePath(
                                fileStore.getFile(rdd.getQuerySQL()).getPath());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        SimpleDataSet dataset = csvDataSet.load(modelTag);
                        dataset.setDataSetName(rdd.getQueryName());
                        dataSets.put(rdd.getQueryId(), dataset);
                        break;
                    }
                    case "J":{
                        JSONDataSet jsonDataSet = new JSONDataSet();
                        try {
                            jsonDataSet.setFilePath(
                                fileStore.getFile(rdd.getQuerySQL()).getPath());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        SimpleDataSet dataset = jsonDataSet.load(modelTag);
                        dataset.setDataSetName(rdd.getQueryName());
                        dataSets.put(rdd.getQueryId(), dataset);
                        break;
                    }
                    case "H": {
                        HttpDataSet httpDataSet=new HttpDataSet(rdd.getQuerySQL());
                        SimpleDataSet dataset =httpDataSet.load(modelTag);
                        dataset.setDataSetName(rdd.getQueryName());
                        dataSets.put(rdd.getQueryId(), dataset);
                        break;
                    }
                    case "P":{
                        SimpleDataSet dataSet
                            =SimpleDataSet.createSingleObjectSet(rdd.getQuerySQL());
                        dataSets.put(rdd.getQueryId(), dataSet);
                    }
                    default:
                        break;
                }
            }
        }
        bizModel.setModelTag(modelTag);
        bizModel.setBizData(dataSets);
        return bizModel;
    }

    public void setDbPacket(DataPacket dbPacket) {
        this.dbPacket = dbPacket;
    }

    public void setIntegrationEnvironment(IntegrationEnvironment integrationEnvironment) {
        this.integrationEnvironment = integrationEnvironment;
    }

    public void setQueryParams(Map<String, Object> queryParams) {
        this.queryParams = queryParams;
    }


    public void setFileStore(FileStore fileStore) {
        this.fileStore = fileStore;
    }

    /**
     * 业务数据是否是 批量的
     * 如果是，处理器将反复调用 。知道 get() 返回 null 结束
     *
     * @return 否是 批量的
     */
    @Override
    public boolean isBatchWise() {
        return this.batchWise;
    }

    public void setBatchWise(boolean batchWise) {
        this.batchWise = batchWise;
    }
}
