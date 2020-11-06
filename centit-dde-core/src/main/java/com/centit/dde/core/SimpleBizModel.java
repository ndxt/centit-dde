package com.centit.dde.core;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhf
 */
public class SimpleBizModel implements BizModel, Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 模型名称
     */
    private String modelName;
    /**
     * 模型的标识， 就是对应的主键
     * 或者模型的参数
     * 或者对应关系数据库查询的参数（数据源参数）
     */
    private Map<String, Object> modelTag;
    /**
     * 模型数据
     */
    private Map<String, DataSet> bizData;

    public SimpleBizModel() {
        modelName = BizModel.DEFAULT_MODEL_NAME;
    }

    public SimpleBizModel(String modelName) {
        this.modelName = modelName;
    }

    @Override
    public int modelSize(){
        if(bizData == null || bizData.isEmpty()) {
            return 0;
        }
        DataSet mainData = fetchDataSetByName(modelName);
        if(mainData== null){
            return 0;
        }
        return mainData.size();
    }

    @Override
    public boolean isEmpty(){
        return bizData == null ||
            bizData.isEmpty();
    }

    @JSONField(deserialize = false, serialize = false)
    @Override
    public DataSet getDataSet(String relationPath){
        if(bizData!=null){
            return bizData.get(relationPath);
        }
        return null;
    }

    @Override
    public void putDataSet(String relationPath, DataSet dataSet) {
        if (this.bizData == null) {
            this.bizData = new HashMap<>(6);
        }
        getBizData().put(relationPath, dataSet);
    }

    @Override
    public JSONObject toJsonObject() {
        JSONObject dataObject = new JSONObject();
        if (bizData != null) {
            for (DataSet dataSet : bizData.values()) {
                dataObject.put(dataSet.getDataSetName(), dataSet.getData());
            }
        }
        if (modelTag != null && !modelTag.isEmpty()) {
            dataObject.put("modelTag", modelTag);
        }
        dataObject.put("modelName", this.getModelName());
        return dataObject;
    }
    /**
     * @param singleRowAsObject 如果为true DataSet中只有一行记录的就作为JSONObject；
     *                          否则为 JSONArray
     * @return JSONObject
     */
    @Override
    public JSONObject toJsonObject(boolean singleRowAsObject) {
        JSONObject dataObject = new JSONObject();
        if (bizData != null) {
            for (DataSet dataSet : bizData.values()) {
                if (dataSet.size() == 1 && singleRowAsObject) {
                    dataObject.put(dataSet.getDataSetName(), dataSet.getFirstRow());
                } else if (!dataSet.isEmpty()) {
                    dataObject.put(dataSet.getDataSetName(), dataSet.getDataAsList());
                }
            }
        }
        if (modelTag != null && !modelTag.isEmpty()) {
            dataObject.put("modelTag", modelTag);
        }
        dataObject.put("modelName", this.getModelName());
        return dataObject;
    }

    @Override
    public JSONObject toJsonObject(String[] singleRowDataSets) {
        JSONObject dataObject = new JSONObject();
        if (bizData != null) {
            for (DataSet dataSet : bizData.values()) {
                if (StringUtils.equalsAny(dataSet.getDataSetName(), singleRowDataSets)) {
                    dataObject.put(dataSet.getDataSetName(), dataSet.getFirstRow());
                } else if (!dataSet.isEmpty()) {
                    dataObject.put(dataSet.getDataSetName(), dataSet.getDataAsList());
                }
            }
        }
        dataObject.put("modelName", this.getModelName());
        if (modelTag != null && !modelTag.isEmpty()) {
            dataObject.put("modelTag", modelTag);
        }
        return dataObject;
    }

    @Override
    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    @Override
    public Map<String, Object> getModelTag() {
        return modelTag;
    }

    public void setModelTag(Map<String, Object> modelTag) {
        this.modelTag = modelTag;
    }

    @Override
    public Map<String, DataSet> getBizData() {
        return bizData;
    }

    public void setBizData(Map<String, DataSet> bizData) {
        this.bizData = bizData;
    }

    public static SimpleBizModel createSingleDataSetModel(String modelName, DataSet dataSet) {
        SimpleBizModel model = new SimpleBizModel(modelName);
        model.bizData = new HashMap<>(1);
        model.putDataSet(modelName, dataSet);
        return model;
    }

    public static SimpleBizModel createSingleDataSetModel(DataSet dataSet) {
        return createSingleDataSetModel(BizModel.DEFAULT_MODEL_NAME, dataSet);
    }
}
