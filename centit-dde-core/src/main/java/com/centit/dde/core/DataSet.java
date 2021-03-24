package com.centit.dde.core;

import java.util.List;
import java.util.Map;

/**
 * 数据集公共接口，
 * 一个真正的数据集 可能是但不限于下列数据：
 * 1. 文件。 xml、json、cvs、excel sheet
 * 2. 数据库中的一个查询
 * 3. 一个返回数据列表的程序接口
 * @author zhf
 */
public interface DataSet {
    String SINGLE_DATA_SET_DEFAULT_NAME = "default";
    String SINGLE_DATA_FIELD_NAME = "scalar";
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
     * 数据集中的数据
     * @return 存储的原始对象
     */
    Object getData();

    /**
     * 数据集中的数据
     * @return 是一个 对象（Map）列表；可以类比为JSONArray
     */
    List<Map<String, Object>> getDataAsList();


    /**
     * @return 数据集大小
     */
    int getSize();

    Map<String, Object> getFirstRow();
}
