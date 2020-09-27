package com.centit.product.dataopt.core;

import java.util.Map;

public interface DataSetReader {
    /**
     * 读取 dataSet 数据集
     * @param params 模块的自定义参数
     * @return  dataSet 数据集
     */
    DataSet load(Map<String, Object> params);
}
