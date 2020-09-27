package com.centit.product.dataopt.core;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SimpleBizModel implements BizModel, Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 模型名称
     */
    private String modelName;
    /**
     * 模型的标识， 就是对应的主键
     * 或者对应关系数据库查询的参数（数据源参数）
     */
    private Map<String, Object> modelTag;
    /**
     * 模型数据
     */
    protected Map<String, DataSet> bizData;

    public SimpleBizModel() {
        modelName = BizModel.DEFAULT_MODEL_NAME;
    }

    public SimpleBizModel(String modelName) {
        this.modelName = modelName;
    }

    @Override
    public void putDataSet(String relationPath, DataSet dataSet) {
        if (this.bizData == null) {
            this.bizData = new HashMap<>(6);
        }
        getBizData().put(relationPath, dataSet);
    }

    /**
     * @param singleRowAsObject 如果为true DataSet中只有一行记录的就作为JSONObject；
     *                          否则为 JSONArray
     * @return JSONObject
     */
    @Override
    public JSONObject toJSONObject(boolean singleRowAsObject) {
        JSONObject dataObject = new JSONObject();
        if (bizData != null) {
            for (DataSet dataSet : bizData.values()) {
                if (dataSet.getRowCount() == 1 && singleRowAsObject) {
                    dataObject.put(dataSet.getDataSetName(), dataSet.getFirstRow());
                } else if (!dataSet.isEmpty()) {
                    dataObject.put(dataSet.getDataSetName(), dataSet.getData());
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
    public JSONObject toJSONObject(String[] singleRowDatasets) {
        JSONObject dataObject = new JSONObject();
        if (bizData != null) {
            for (DataSet dataSet : bizData.values()) {
                if (StringUtils.equalsAny(dataSet.getDataSetName(), singleRowDatasets)) {
                    dataObject.put(dataSet.getDataSetName(), dataSet.getFirstRow());
                } else if (!dataSet.isEmpty()) {
                    dataObject.put(dataSet.getDataSetName(), dataSet.getData());
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
