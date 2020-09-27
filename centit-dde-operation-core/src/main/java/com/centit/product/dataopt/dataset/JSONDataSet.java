package com.centit.product.dataopt.dataset;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.centit.product.dataopt.core.DataSet;
import com.centit.product.dataopt.core.SimpleDataSet;
import com.centit.support.algorithm.BooleanBaseOpt;
import com.centit.support.file.FileIOOpt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

public class JSONDataSet extends FileDataSet{

    private static final Logger logger = LoggerFactory.getLogger(JSONDataSet.class);
    /**
     * 读取 dataSet 数据集
     * @param params 模块的自定义参数
     * @return dataSet 数据集
     */
    @Override
    public SimpleDataSet load(Map<String, Object> params) {
        try {
            if(params !=null && BooleanBaseOpt.castObjectToBoolean(
                params.get("isJSONArray"),false)) {
                JSONArray json = JSON.parseArray(FileIOOpt.readStringFromFile(this.getFilePath()));
                return SimpleDataSet.fromJsonArray(json);
            }else {
                return JSON.parseObject(FileIOOpt.readStringFromFile(this.getFilePath()), SimpleDataSet.class);
            }
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(),e);
            return null;
        }
    }

    /**
     * 将 dataSet 数据集 持久化
     *
     * @param dataSet 数据集
     */
    @Override
    public void save(DataSet dataSet) {
        try {
            FileIOOpt.writeStringToFile(JSON.toJSONString(dataSet), this.getFilePath());
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(),e);
        }
    }
}
