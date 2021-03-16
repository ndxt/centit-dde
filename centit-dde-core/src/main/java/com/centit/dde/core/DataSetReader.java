package com.centit.dde.core;

import java.sql.SQLException;
import java.util.Map;

/**
 * @author zhf
 */
public interface DataSetReader {
    /**
     * 读取 dataSet 数据集
     * @param params 模块的自定义参数
     * @return  dataSet 数据集
     */
    DataSet load(Map<String, Object> params) throws Exception;
}
