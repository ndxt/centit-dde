package com.centit.product.dataopt.bizopt;

import com.centit.product.dataopt.core.BizModel;
import com.centit.product.dataopt.core.BizOperation;
import com.centit.product.dataopt.core.DataSet;
import com.centit.product.dataopt.core.DataSetWriter;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据持久化操作
 */
public class PersistenceOperation implements BizOperation {

    public static final String WRITER_INDICATE_SAVE = "save";
    public static final String WRITER_INDICATE_APPEND = "append";
    public static final String WRITER_INDICATE_MERGE = "merge";
    public static final String WRITER_INDICATE_UPDATE = "update";

    /**
     * 给 dataWriters 边个名字， 一般这个名字和 dataSet名字对应
     */
    private Map<String, DataSetWriter> dataWriters;
    /**
     *
     */
    private Map<String, String> writerIndicates;



    @Override
    public BizModel apply(BizModel bizModel) {
        if(dataWriters==null || bizModel==null || bizModel.isEmpty()){
            return bizModel;
        }
        /**
         * 保证写入顺序和 bizModel 的 DataSet的顺序一致
         */
        for(Map.Entry<String, DataSet> ent : bizModel.getBizData().entrySet()){
            DataSetWriter dataSetWriter = dataWriters.get(ent.getKey());
            if(dataSetWriter!=null){
                switch (getWriterIndicate(ent.getKey())) {
                case WRITER_INDICATE_APPEND:
                    dataSetWriter.append(ent.getValue());
                    break;
                case WRITER_INDICATE_MERGE:
                case WRITER_INDICATE_UPDATE:
                    dataSetWriter.merge(ent.getValue());
                    break;
                default:
                    dataSetWriter.save(ent.getValue());
                    break;
                }
            }
        }
        return bizModel;
    }

    public PersistenceOperation writerIndicate(String dataSetName, String indicate){
        if(writerIndicates == null){
            writerIndicates = new HashMap<>(5);
        }
        writerIndicates.put(dataSetName, indicate);
        return this;
    }

    public PersistenceOperation dataWriter(String dataSetName, DataSetWriter writer){
        if(dataWriters == null){
            dataWriters = new HashMap<>(5);
        }
        dataWriters.put(dataSetName, writer);
        return this;
    }

    public PersistenceOperation dataWriter(String dataSetName, DataSetWriter writer, String indicate){
        dataWriter(dataSetName, writer);
        writerIndicate(dataSetName, indicate);
        return this;
    }

    public Map<String, DataSetWriter> getDataWriters() {
        return dataWriters;
    }

    public void setDataWriters(Map<String, DataSetWriter> dataWriters) {
        this.dataWriters = dataWriters;
    }

    private String getWriterIndicate(String dataSetName){
        if(writerIndicates == null){
            return WRITER_INDICATE_SAVE;
        }
        return writerIndicates.get(dataSetName);
    }
}
