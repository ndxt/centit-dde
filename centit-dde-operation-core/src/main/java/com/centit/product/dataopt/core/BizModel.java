package com.centit.product.dataopt.core;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import java.util.Map;

/**
 * @author zhf
 */
public interface BizModel {
    SimpleBizModel EMPTY_BIZ_MODEL
        = new SimpleBizModel("EMPTY_BIZ_MODEL");
    String DEFAULT_MODEL_NAME = "bizModel";
    String DATABASE_CODE_TAG_NAME = "DATABASE_CODE_TAG_NAME";
    /**
     * @return 模型名称; 可以作为主DataSet的名称
     */
    String getModelName();
    /**
     * 模型的标识， 就是对应的主键
     * @return  或者对应关系数据库查询的参数（数据源参数）
     */
    Map<String, Object> getModelTag();
    /**
     * @return  模型数据 key为关联关系链， value为dataSet
     */
    Map<String, DataSet> getBizData();

    /**
     * @param singleRowAsObject 如果为true DataSet中只有一行记录的就作为JSONObject；
     *                          否则为 JSONArray
     * @return JSONObject
     */
    JSONObject toJSONObject(boolean singleRowAsObject);

    JSONObject toJSONObject(String [] singleRowDatasets);

    void putDataSet(String relationPath, DataSet dataSet);

    default void putMainDataSet(DataSet dataSet) {
        putDataSet(this.getModelName(), dataSet);
    }

    default boolean isEmpty(){
        return getBizData() == null ||
            getBizData().isEmpty();
    }

    @JSONField(deserialize = false, serialize = false)
    default DataSet getMainDataSet(){
        if(!isEmpty()){
            return getBizData().get(getModelName());
        }
        return null;
    }

    default DataSet fetchDataSetByName(String relationPath){
        Map<String, DataSet> dss = getBizData();
        if(dss == null) {
            return null;
        }
        if (dss.containsKey(relationPath)) {
            return dss.get(relationPath);
        }
        return dss.values().stream().filter(
            ds -> ds.getDataSetName()
                .equals(relationPath)).findFirst()
                .orElse(null);
    }
}
