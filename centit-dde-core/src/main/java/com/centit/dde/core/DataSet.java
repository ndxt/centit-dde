package com.centit.dde.core;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.annotation.JSONField;
import com.centit.support.algorithm.CollectionsOpt;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.*;

/**
 * 数据集 虚拟类
 */
public class DataSet implements DataSetReader, Serializable {

    private static final long serialVersionUID = 1L;

    public static String SINGLE_DATA_SET_DEFAULT_NAME = "default";
    public static String SINGLE_DATA_FIELD_NAME = "data";

    public static String DATA_SET_TYPE_TABLE = "T";

    /**
     * 返回 DataSet 的名称
     */
    protected String dataSetName;
    /**
     * 数据集中的数据
     * 是一个 对象（Map）列表；可以类比为JSONArray
     */
    protected Object data;

    public DataSet() {
        dataSetName = DataSet.SINGLE_DATA_SET_DEFAULT_NAME;
        this.data = null;// Collections.emptyList();
    }

    public DataSet(Object data) {
        this.dataSetName = DataSet.SINGLE_DATA_SET_DEFAULT_NAME;
        if(data instanceof DataSet){
            this.data = ((DataSet) data).getData();
        } else {
            this.data = data;
        }
    }

    public static DataSet toDataSet(Object data) {
        if(data instanceof DataSet){
            return (DataSet) data;
        }
        if(data instanceof Map){
            Map<String, Object> objectMap = (Map<String, Object>)data;
            if( objectMap.containsKey("dataSetName") && objectMap.containsKey("data")){
                JSONObject jobj = (JSONObject)JSON.toJSON(data);
                return JSON.toJavaObject(jobj, DataSet.class);
            }
        }
        return new DataSet(data);
    }

    public DataSet(String dataSetName, Object data) {
        if(data instanceof DataSet){
            this.data = ((DataSet) data).getData();
        } else {
            this.data = data;
        }
        this.dataSetName = dataSetName;
    }

    public String getDataSetName() {
        return dataSetName;
    }

    public void setDataSetName(String dataSetName) {
        this.dataSetName = dataSetName;
    }

    /**
     * 返回 DataSet 的类型
     * 二维表 T(table)、单个数据记录 R(row)、
     * 标量数据 S(scalar)、 E 空的(empty)
     *
     * @return DataSet 的类型
     */
    @JSONField(serialize = false)
    public String getDataSetType() {
        if (this.data == null) {
            return "E"; //empty
        }
        if (this.data instanceof Collection) {
            //Collection<?> objects = ((Collection<?>)this.data);
            return DATA_SET_TYPE_TABLE; //二维表 T(table)
        }
        if (this.data instanceof Map) {
            return "R"; // 单个数据记录 R(row)
        }

        Class<?> clazz = this.data.getClass();
        if (clazz.isArray() && !(this.data instanceof byte[])
            && !(this.data instanceof char[])) {
            return DATA_SET_TYPE_TABLE;
        }
        return "S"; //标量数据 S(scalar)
    }

    @JSONField(serialize = false)
    public List<Map<String, Object>> getDataAsList() {
        if (this.data == null) {
            return Collections.emptyList();
        }
        if (this.data instanceof List) {
            List<?> objects = (List<?>) this.data;
            if (objects.size() < 1) {
                return (List<Map<String, Object>>) objects;
            }
            //只检查第一个，应该检查多个
            Object firstRow = objects.get(0);
            if (firstRow instanceof Map) {
                return (List<Map<String, Object>>) objects;
            } /*else {
                List<Map<String, Object>> objList = new ArrayList<>(objects.size());
                for(Object obj : objects) {
                    objList.add(CollectionsOpt.objectToMap(obj));
                }
                return objList;
            }*/
        }

        if (this.data instanceof Collection) {
            Collection<?> objects = (Collection<?>) this.data;
            List<Map<String, Object>> objList = new ArrayList<>(objects.size());
            for (Object obj : objects) {
                objList.add(CollectionsOpt.objectToMap(obj));
            }
            return objList;
        }

        Class<?> clazz = this.data.getClass();
        if (clazz.isArray() && !(this.data instanceof byte[])
            && !(this.data instanceof char[])) {
            int len = Array.getLength(this.data);
            if (len > 0) {
                List<Map<String, Object>> objList = new ArrayList<>(len);
                for (int i = 0; i < len; i++) {
                    objList.add(CollectionsOpt.objectToMap(Array.get(this.data, i)));
                }
            }
        }

        return CollectionsOpt.createList(CollectionsOpt.objectToMap(this.data));
    }

    /**
     * @return 数据集大小
     */
    @JSONField(serialize = false)
    public int getSize() {
        if (this.data == null) {
            return 0;
        }
        if (this.data instanceof Collection) {
            return ((Collection<?>) this.data).size();
        }

        Class<?> clazz = this.data.getClass();
        if (clazz.isArray() && !(this.data instanceof byte[])
            && !(this.data instanceof char[])) {
            return Array.getLength(this.data);
        }

        return 1;
    }

    @JSONField(serialize = false)
    public Map<String, Object> getFirstRow() {
        if (this.data == null) {
            return Collections.emptyMap();
        }
        if (this.data instanceof Collection) {
            Collection<?> objects = (Collection<?>) this.data;
            if (objects.isEmpty()) {
                return Collections.emptyMap();
            }
            return CollectionsOpt.objectToMap(objects.iterator().next());
        }
        return CollectionsOpt.objectToMap(this.data);
    }


    public void setData(Object data) {
        this.data = data;
    }

    /**
     * 数据集中的数据
     *
     * @return 存储的原始对象
     */
    public Object getData() {
        return this.data;
    }

    /**
     * 读取 dataSet 数据集
     *
     * @param params 模块的自定义参数
     * @return dataSet 数据集
     */
    @Override
    public DataSet load(Map<String, Object> params, DataOptContext dataOptContext) {
        return this;
    }

    public String toJSONString(){
        return JSON.toJSONString(this.data);
    }

    public String toDebugString(){
        return JSON.toJSONString(this.data);
    }
}
