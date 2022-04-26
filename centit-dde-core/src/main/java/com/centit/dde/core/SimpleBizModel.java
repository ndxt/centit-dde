package com.centit.dde.core;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.centit.dde.utils.ConstantValue;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.ResponseMapData;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.ReflectionOpt;
import com.centit.support.common.ObjectException;
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
    private Map<String, Object> stackData;

    /**
     * 模型数据
     */
    private Map<String, DataSet> bizData;

    private ResponseMapData responseMapData;

    public SimpleBizModel(String modelName) {
        this.modelName = modelName;
        this.stackData = new HashMap<>(8);
        this.responseMapData = new ResponseMapData(ResponseData.RESULT_OK,"");
    }
    @Override
    public ResponseMapData getResponseMapData(){
        return this.responseMapData;
    }

    @Override
    public void addResponseMapData(String sKey, ResponseData objValue) {
        this.responseMapData.setCode(objValue.getCode());
        this.responseMapData.setMessage(StringUtils.join(this.responseMapData.getMessage(),objValue.getMessage()));
        this.responseMapData.addResponseData(sKey,objValue);
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
        return mainData.getSize();
    }

    @JSONField(deserialize = false, serialize = false)
    @Override
    public DataSet getDataSet(String relationPath){
        if(bizData!=null){
            return bizData.get(relationPath);
        }
        return new SimpleDataSet();
    }

    @Override
    public void removeDataSet(String relationPath) {
        if (bizData != null){
            bizData.remove(relationPath);
        }
    }

    @Override
    public void putDataSet(String relationPath, DataSet dataSet) {
        if (this.bizData == null) {
            this.bizData = new HashMap<>(6);
        }
        ((SimpleDataSet)dataSet).setDataSetName(relationPath);
        getBizData().put(relationPath, dataSet);
    }

    @Override
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
    @Override
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

    @Override
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

    @Override
    public String getModelName() {
        return modelName;
    }

    @Override
    public Object getStackData(String key) {
        return stackData.get(key);
    }

    @Override
    public void setStackData(String key, Object value) {
        if(key == null || !key.startsWith("__")){
            throw new ObjectException("内部堆栈数据必须以'__'开头");
        }
        stackData.put(key, value);
    }

    // 调用栈 复制，避免覆盖，不过这个函数有瑕疵，不能做到深度复制
    @Override
    public Map<String, Object> dumpStackData() {
        return CollectionsOpt.cloneHashMap(stackData);
    }

    @Override
    public void restoreStackData(Map<String, Object> dumpData) {
        this.stackData = dumpData;
    }

    public void setCallStackData(Map<String, Object> callStack){
        if(callStack==null){
            return;
        }
        for(Map.Entry<String, Object> ent : callStack.entrySet()){
            if(ent.getKey().startsWith("__")){
                stackData.put(ent.getKey(), ent.getValue());
            }
        }
    }
    public void setModelName(String modelName) {
        this.modelName = modelName;
    }


    @Override
    public Map<String, DataSet> getBizData() {
        return bizData;
    }

    public void setBizData(Map<String, DataSet> bizData) {
        this.bizData = bizData;
    }

    private static SimpleBizModel createSingleDataSetModel(String modelName, DataSet dataSet) {
        SimpleBizModel model = new SimpleBizModel(modelName);
        model.bizData = new HashMap<>(1);
        model.putDataSet(modelName, dataSet);
        return model;
    }

    public static SimpleBizModel createSingleDataSetModel(DataSet dataSet) {
        return createSingleDataSetModel(BizModel.DEFAULT_MODEL_NAME, dataSet);
    }

    @Override
    public DataSet fetchDataSetByName(String relationPath){

        if(StringUtils.isBlank(relationPath)){
            return null;
        }
        String dateSetName = relationPath;
        //补丁 等全部修复代码后移除
        switch (relationPath){
            case "modelTag":
            case "pathData":
                dateSetName = ConstantValue.REQUEST_PARAMS_TAG;
                break;
            case "requestBody":
                dateSetName = ConstantValue.REQUEST_BODY_TAG;
                break;
            case "requestFile":
                dateSetName = ConstantValue.REQUEST_FILE_TAG;
                break;
        }

        if(dateSetName.startsWith("__")){
            Object obj = getStackData(dateSetName);
            if(obj != null){
                return new SimpleDataSet(obj);
            }
        }

        Map<String, DataSet> dss = getBizData();
        if(dss == null) {
            return null;
        }

        DataSet data = dss.get(dateSetName);
        if(data !=null ){
            return data;
        }

        return dss.values().stream().filter(
                ds -> ds.getDataSetName()
                    .equals(relationPath)).findFirst()
            .orElse(null);
    }
}
