package com.centit.product.dataopt.core;

import com.alibaba.fastjson.JSONArray;
import com.centit.support.algorithm.CollectionsOpt;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 数据集 虚拟类
 */
public class SimpleDataSet implements DataSet, DataSetReader, Serializable {

    private static final long serialVersionUID = 1L;

    public SimpleDataSet(){
        dataSetName = DataSet.SINGLE_DATA_SET_DEFALUT_NAME;
        sorted = false;
    }

    public SimpleDataSet(String dataSetName){
        this.dataSetName = dataSetName;
        this.sorted = false;
    }

    public SimpleDataSet(List<Map<String, Object>> data) {
        this.data = data;
        this.dataSetName = DataSet.SINGLE_DATA_SET_DEFALUT_NAME;;
        this.sorted = false;
    }
    /**
     * 返回 DataSet 的名称
     */
    protected String dataSetName;


    protected boolean sorted;

    /**
     * 返回 所有数据维度，这个维度是有序的，这个属性不是必须的
     */
    protected List<String> dimensions;
    /**
     * 数据集中的数据
     * 是一个 对象（Map）列表；可以类比为JSONArray
     */
    protected List<Map<String, Object>> data;

    public Map<String, Object> getParms() {
        return parms;
    }

    public void setParms(Map<String, Object> parms) {
        this.parms = parms;
    }


    private Map<String, Object> parms;

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
        if(this.data ==null || this.data.size() == 0){
            return "E";//empty
        }
        if(this.data.size() > 1){
            return "T";//二维表 T(table)
        }
        if(this.data.get(0) != null && this.data.get(0).size() == 1){
            return "S"; // 标量数据 S(scalar)
        }
        return "R"; //单个数据记录 R(row)
    }

    @Override
    public List<Map<String, Object>> getData() {
        return data;
    }


    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }

    @Override
    public boolean isSorted() {
        return sorted;
    }

    public void setSorted(boolean sorted) {
        this.sorted = sorted;
    }

    @Override
    public List<String> getDimensions() {
        return dimensions;
    }

    public void setDimensions(List<String> dimensions) {
        this.dimensions = dimensions;
    }

    public static SimpleDataSet fromJsonArray(JSONArray ja){
        SimpleDataSet dataSet = new SimpleDataSet();
        dataSet.setData((List)ja);
        return dataSet;
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

    public static SimpleDataSet createSingleRowSet(Map<String, Object> rowData) {
        SimpleDataSet dataSet = new SimpleDataSet();
        dataSet.data = CollectionsOpt.createList(rowData);
        return dataSet;
    }

    public static SimpleDataSet createSingleObjectSet(Object data) {
        SimpleDataSet dataSet = new SimpleDataSet();
        dataSet.data = CollectionsOpt.createList(
            CollectionsOpt.createHashMap(DataSet.SINGLE_DATA_FIELD_NAME, data));
        return dataSet;
    }
}
