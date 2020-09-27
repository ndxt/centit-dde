package com.centit.product.dataopt.bizopt;

import com.centit.product.dataopt.core.*;

import java.util.HashMap;
import java.util.Map;

public class DataLoadSupplier implements BizSupplier {
    private Map<String, DataSetReader> dataReaders;
    private String modelName;
    private boolean batchWise;
    /**
     * 模型的标识， 就是对应的主键
     */
    private Map<String, Object> modeTag;

    public DataLoadSupplier(){
        this.batchWise = false;
    }

    protected BizModel loadData(){
        if(dataReaders == null || dataReaders.isEmpty()){
            return null;
        }
        SimpleBizModel bizModel = new SimpleBizModel();
        for(Map.Entry<String, DataSetReader> ent : dataReaders.entrySet()) {
            DataSet ds = ent.getValue().load(modeTag);
            if(ds != null && !ds.isEmpty()) {
                bizModel.putDataSet(ds.getDataSetName(), ds);
            }
        }
        return  bizModel;
    }

    /**
     * 在后续的操作中需要改变业务环境(或者modeTag)避免死循环
     * 业务数据是否是 批量的
     * 如果是，处理器将反复调用 。知道 get() 返回 null 结束
     * @return 否是 批量的
     */
    public boolean isBatchWise(){
        return this.batchWise;
    }

    /**
     * Gets a result.
     *
     * @return a result
     */
    @Override
    public BizModel get() {
        return loadData();
    }

    public DataLoadSupplier dataReader(String dataSetName, DataSetReader reader){
        if(dataReaders == null){
            dataReaders = new HashMap<>(5);
        }
        dataReaders.put(dataSetName, reader);
        return this;
    }

    public Map<String, DataSetReader> getDataReaders() {
        return dataReaders;
    }

    public void setDataReaders(Map<String, DataSetReader> dataReaders) {
        this.dataReaders = dataReaders;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public Map<String, Object> getModeTag() {
        return modeTag;
    }

    public void setModeTag(Map<String, Object> modeTag) {
        this.modeTag = modeTag;
    }

    public void setBatchWise(boolean batchWise) {
        this.batchWise = batchWise;
    }
}
