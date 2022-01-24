package com.centit.dde.core;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.ResponseMapData;

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
    ResponseMapData getResponseMapData();
    void addResponseMapData(String sKey, ResponseData objValue);
    /**
     * 模型的标识， 就是对应的主键
     * @return  或者对应关系数据库查询的参数（数据源参数）
     */
    Map<String, Object> getModelTag();

    /**
     * 存放内部逻辑使用的临时变量
     * @return
     */
    Map<String, Object> getInterimVariable();
    /**
     * @return  模型数据 key为关联关系链， value为dataSet
     */
    Map<String, DataSet> getBizData();

    int modelSize();


    void putDataSet(String relationPath, DataSet dataSet);

    DataSet getDataSet(String relationPath);

    //移除DataSet
    void removeDataSet(String relationPath);

    JSONObject toJsonObject();
    /**
     * @param singleRowAsObject 如果为true DataSet中只有一行记录的就作为JSONObject；
     *                          否则为 JSONArray
     * @return JSONObject
     */
    JSONObject toJsonObject(boolean singleRowAsObject);

    JSONObject toJsonObject(String [] singleRowDatasets);


    default void putMainDataSet(DataSet dataSet) {
        putDataSet(this.getModelName(), dataSet);
    }

    @JSONField(deserialize = false, serialize = false)
    default DataSet getMainDataSet(){
        return getDataSet(this.getModelName());
    }

    default DataSet fetchDataSetByName(String relationPath){
        Map<String, DataSet> dss = getBizData();
        if(dss == null) {
            return new SimpleDataSet();
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
