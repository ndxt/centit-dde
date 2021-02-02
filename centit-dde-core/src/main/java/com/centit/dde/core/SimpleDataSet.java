package com.centit.dde.core;

import com.centit.support.algorithm.CollectionsOpt;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 数据集 虚拟类
 */
public class SimpleDataSet implements DataSet, DataSetReader, Serializable {

    private static final long serialVersionUID = 1L;

    public SimpleDataSet(){
        dataSetName = DataSet.SINGLE_DATA_SET_DEFAULT_NAME;
        //sorted = false;
    }

    public SimpleDataSet(Object data){
        this.dataSetName = DataSet.SINGLE_DATA_SET_DEFAULT_NAME;
        this.data = data;
    }

    public SimpleDataSet(String dataSetName, Object data) {
        this.data = data;
        this.dataSetName = dataSetName;
        //this.sorted = false;
    }
    /**
     * 返回 DataSet 的名称
     */
    protected String dataSetName;

    /**
     * 数据集中的数据
     * 是一个 对象（Map）列表；可以类比为JSONArray
     */
    protected Object data;

    @Override
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
     * @return  DataSet 的类型
     */
    @Override
    public String getDataSetType() {
        if(this.data ==null){
            return "E"; //empty
        }
        if(this.data instanceof Collection){
            //Collection<?> objects = ((Collection<?>)this.data);
            return "T"; //二维表 T(table)
        }
        if(this.data instanceof Map){
            return "R"; // 单个数据记录 R(row)
        }

        Class<?> clazz = this.data.getClass();
        if (clazz.isArray() && !(this.data instanceof byte[])
            && !(this.data instanceof char[])) {
            return "T";
        }
        return "S"; //标量数据 S(scalar)
    }

    @Override
    public List<Map<String, Object>> getDataAsList() {
        if(this.data ==null){
            return null;
        }
        if(this.data instanceof List){
            List<?> objects = (List<?>) this.data;
            if(objects.size()<1){
                return (List<Map<String, Object>>) objects;
            }
            //只检查第一个，应该检查多个
            Object firstRow = objects.get(0);
            if(firstRow instanceof Map){
                return (List<Map<String, Object>>) objects;
            } /*else {
                List<Map<String, Object>> objList = new ArrayList<>(objects.size());
                for(Object obj : objects) {
                    objList.add(CollectionsOpt.objectToMap(obj));
                }
                return objList;
            }*/
        }

        if(this.data instanceof Collection) {
            Collection<?> objects = (Collection<?>) this.data;
            List<Map<String, Object>> objList = new ArrayList<>(objects.size());
            for(Object obj : objects) {
                objList.add(CollectionsOpt.objectToMap(obj));
            }
            return objList;
        }

        Class<?> clazz = this.data.getClass();
        if (clazz.isArray() && !(this.data instanceof byte[])
                && !(this.data instanceof char[])) {
            int len = Array.getLength(this.data);
            if(len>0) {
                List<Map<String, Object>> objList = new ArrayList<>(len);
                for(int i=0;i<len;i++) {
                    objList.add(CollectionsOpt.objectToMap(Array.get(this.data, i)));
                }
            }
        }

        return CollectionsOpt.createList(CollectionsOpt.objectToMap(this.data));
    }

    /**
     * @return 是否为空的数据集
     */
    @Override
    public boolean isEmpty() {
        return false;
    }

    /**
     * @return 数据集大小
     */
    @Override
    public int size() {
        if(this.data ==null){
            return 0;
        }
        if(this.data instanceof Collection){
            return ((Collection<?>)this.data).size();
        }
        return 1;
    }

    @Override
    public Map<String, Object> getFirstRow() {
        if(this.data ==null){
            return null;
        }
        if(this.data instanceof Collection) {
            Collection<?> objects = (Collection<?>) this.data;
            if(objects.size()==0){
                return null;
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
    @Override
    public Object getData() {
        return this.data;
    }

    /**
     * 读取 dataSet 数据集
     * @param params 模块的自定义参数
     * @return dataSet 数据集
     */
    @Override
    public DataSet load(Map<String, Object> params) {
        return this;
    }


}
