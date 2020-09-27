package com.centit.product.dataopt.core;

import java.util.List;
import java.util.Map;

/**
 * 数据集公共接口，
 * 一个真正的数据集 可能是但不限于下列数据：
 * 1. 文件。 xml、json、cvs、excel sheet
 * 2. 数据库中的一个查询
 * 3. 一个返回数据列表的程序接口
 */
public interface DataSet {
    String SINGLE_DATA_SET_DEFALUT_NAME = "default";
    String SINGLE_DATA_FIELD_NAME = "data";
    /**
     * 返回 DataSet 的名称
     * @return  DataSet 的名称
     */
    String getDataSetName();
    /**
     * 返回 DataSet 的类型
     * 二维表 T(table)、单个数据记录 R(row)、标量数据 S(scalar)
     * @return  DataSet 的类型
     */
    String getDataSetType();

    /**
     * @return 是否已按照维度属性排序
     */
    boolean isSorted();

    /**
     * 返回 所有数据维度，这个维度是有序的，这个属性不是必须的
     * @return 维度
     */
    List<String> getDimensions();

    /**
     * 数据集中的数据
     * @return 是一个 对象（Map）列表；可以类比为JSONArray
     */
    List<Map<String, Object>> getData();

    /**
     * @return 是否为空的数据集
     */
    default boolean isEmpty(){
        return getData()==null || getData().size()==0;
    }

    default int getRowCount(){
        return getData()==null? 0 : getData().size();
    }

    default Map<String, Object> getFirstRow() {
        if (!isEmpty()) {
            return getData().get(0);
        }
        return null;
    }
}
