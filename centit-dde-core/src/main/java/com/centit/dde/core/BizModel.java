package com.centit.dde.core;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.ResponseMapData;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @author codefan@sina.com
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
     * 堆栈变量，key必须以 __开头
     * @return  或者对应关系数据库查询的参数（数据源参数）
     * __api_info
     * __request_body
     * __request_file
     * __request_params
     * __log_level
     * __message_queue
     * __last_error
     * @see com.centit.dde.utils.ConstantValue
     */
    Object getStackData(String key);

    void setStackData(String key, Object value);

    Map<String, Object> dumpStackData();

    void restoreStackData(Map<String, Object> dumpData);
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

        if(StringUtils.isBlank(relationPath)){
            return null;
        }

        if(relationPath.startsWith("__")){
            Object obj = getStackData(relationPath);
            if(obj != null){
                return new SimpleDataSet(obj);
            }
        }
        Map<String, DataSet> dss = getBizData();
        if(dss == null) {
            return null;
        }
        DataSet data = dss.get(relationPath);
        if(data !=null ){
            return data;
        }
        return dss.values().stream().filter(
            ds -> ds.getDataSetName()
                .equals(relationPath)).findFirst()
                .orElse(null);
    }
}
