package com.centit.dde.core;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.centit.dde.utils.ConstantValue;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.common.ObjectException;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhf
 */
public class BizModel implements  Serializable {
    private static final long serialVersionUID = 1L;


    public static BizModel EMPTY_BIZ_MODEL
        = new BizModel("EMPTY_BIZ_MODEL");
    public static String DEFAULT_MODEL_NAME = "bizModel";

    @JSONField(deserialize = false, serialize = false)
    public DataSet getMainDataSet(){
        return getDataSet(this.getModelName());
    }
    /**
     * 模型名称
     */
    private String modelName;
    /**
     * 模型的标识， 就是对应的主键
     * 或者模型的参数
     * 或者对应关系数据库查询的参数（数据源参数）
     */
    private Map<String, Object> stackData;

    /**
     * 模型数据
     */
    private Map<String, DataSet> bizData;

    private DataOptResult optResult;

    public BizModel(String modelName) {
        this.modelName = modelName;
        this.stackData = new HashMap<>(8);
        this.optResult = new DataOptResult();
    }

    public DataOptResult getOptResult() {
        return optResult;
    }

    public int modelSize(){
        if(bizData == null || bizData.isEmpty()) {
            return 0;
        }
        DataSet mainData = fetchDataSetByName(modelName);
        if(mainData== null){
            return 0;
        }
        return mainData.getSize();
    }

    @JSONField(deserialize = false, serialize = false)
    public DataSet getDataSet(String relationPath){
        return fetchDataSetByName(relationPath);
    }

    public void removeDataSet(String relationPath) {
        if (bizData != null){
            bizData.remove(relationPath);
        }
    }

    public void putDataSet(String relationPath, DataSet dataSet) {
        if (this.bizData == null) {
            this.bizData = new HashMap<>(6);
        }
        ((DataSet)dataSet).setDataSetName(relationPath);
        getBizData().put(relationPath, dataSet);
    }

    public JSONObject toJsonObject() {
        JSONObject dataObject = new JSONObject();
        if (bizData != null) {
            for (Map.Entry<String, DataSet> map : bizData.entrySet()) {
                dataObject.put(map.getKey(),map.getValue()==null?null:map.getValue().getData());
            }
        }
        dataObject.put("stackData", stackData);
        dataObject.put("modelName", this.getModelName());
        return dataObject;
    }
    /**
     * @param singleRowAsObject 如果为true DataSet中只有一行记录的就作为JSONObject；
     *                          否则为 JSONArray
     * @return JSONObject
     */
    public JSONObject toJsonObject(boolean singleRowAsObject) {
        JSONObject dataObject = new JSONObject();
        if (bizData != null) {
            for (Map.Entry<String, DataSet> map : bizData.entrySet()) {
                DataSet dataSet=map.getValue();
                if (dataSet.getSize() == 1 && singleRowAsObject) {
                    dataObject.put(map.getKey(), dataSet.getFirstRow());
                } else  {
                    dataObject.put(map.getKey(), dataSet.getDataAsList());
                }
            }
        }
        if (stackData != null && !stackData.isEmpty()) {
            dataObject.put("stackData", stackData);
        }
        dataObject.put("modelName", this.getModelName());
        return dataObject;
    }

    public JSONObject toJsonObject(String[] singleRowDataSets) {
        JSONObject dataObject = new JSONObject();
        if (bizData != null) {
            for (DataSet dataSet : bizData.values()) {
                if (StringUtils.equalsAny(dataSet.getDataSetName(), singleRowDataSets)) {
                    dataObject.put(dataSet.getDataSetName(), dataSet.getFirstRow());
                } else  {
                    dataObject.put(dataSet.getDataSetName(), dataSet.getDataAsList());
                }
            }
        }
        dataObject.put("modelName", this.getModelName());
        if (stackData != null && !stackData.isEmpty()) {
            dataObject.put("stackData", stackData);
        }
        return dataObject;
    }

    public String getModelName() {
        return modelName;
    }

    public Object getStackData(String key) {
        Object object=stackData.get(key);
        if(object==null){
            return new HashMap<>(0);
        }else{
            return object;
        }
    }

    public void setStackData(String key, Object value) {
        if(key == null || !key.startsWith(ConstantValue.DOUBLE_UNDERLINE)){
            throw new ObjectException("内部堆栈数据必须以'__'开头");
        }
        stackData.put(key, value);
    }

    // 调用栈 复制，避免覆盖，不过这个函数有瑕疵，不能做到深度复制
    public Map<String, Object> dumpStackData() {
        return CollectionsOpt.cloneHashMap(stackData);
    }

    public void restoreStackData(Map<String, Object> dumpData) {
        this.stackData = dumpData;
    }

    public void setCallStackData(Map<String, Object> callStack){
        if(callStack==null){
            return;
        }
        for(Map.Entry<String, Object> ent : callStack.entrySet()){
            if(ent.getKey().startsWith(ConstantValue.DOUBLE_UNDERLINE)){
                stackData.put(ent.getKey(), ent.getValue());
            }
        }
    }
    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public Map<String, DataSet> getBizData() {
        return bizData;
    }

    public void setBizData(Map<String, DataSet> bizData) {
        this.bizData = bizData;
    }

    private static BizModel createSingleDataSetModel(String modelName, DataSet dataSet) {
        BizModel model = new BizModel(modelName);
        model.bizData = new HashMap<>(1);
        model.putDataSet(modelName, dataSet);
        return model;
    }

    public static BizModel createSingleDataSetModel(DataSet dataSet) {
        return createSingleDataSetModel(BizModel.DEFAULT_MODEL_NAME, dataSet);
    }

    public DataSet fetchDataSetByName(String relationPath){

        if(StringUtils.isBlank(relationPath)){
            return null;
        }
        String dataSetName = relationPath;
        //补丁 等全部修复代码后移除
        switch (relationPath){
            case "modelTag":
            case "pathData":
                dataSetName = ConstantValue.REQUEST_PARAMS_TAG;
                break;
            case "postBodyData":
                dataSetName = ConstantValue.REQUEST_BODY_TAG;
                break;
            case "postFileData":
                dataSetName = ConstantValue.REQUEST_FILE_TAG;
                break;
            case "responseDataCode":
                dataSetName = ConstantValue.LAST_ERROR_TAG;
                break;
        }

        if(dataSetName.startsWith(ConstantValue.DOUBLE_UNDERLINE)){
            if(ConstantValue.LAST_ERROR_TAG.equals(dataSetName)){
                return new DataSet(optResult.getLastError());
            }
            return new DataSet(getStackData(dataSetName));
        }

        Map<String, DataSet> dss = getBizData();
        if(dss == null) {
            return null;
        }

        DataSet data = dss.get(dataSetName);
        if(data !=null ){
            return data;
        }

        return dss.values().stream().filter(
                ds -> ds.getDataSetName()
                    .equals(relationPath)).findFirst()
            .orElse(null);
    }
}
