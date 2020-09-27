package com.centit.product.dataopt.core;

public interface DataSetWriter {
    /**
     * 将 dataSet 数据集 持久化
     * @param dataSet 数据集
     */
    void save(DataSet dataSet);

    /**
     * 默认和 save 等效;
     * 对于文件类持久化方案来说可以差别化处理，比如添加到文件末尾
     * @param dataSet 数据集
     */
    default void append(DataSet dataSet){
        save(dataSet);
    }

    /**
     * 默认和 save 等效
     * 对于数据库类型的持久化来说可以有差别，比如合并，以避免主键冲突
     * @param dataSet 数据集
     */
    default void merge(DataSet dataSet){
        save(dataSet);
    }
}
