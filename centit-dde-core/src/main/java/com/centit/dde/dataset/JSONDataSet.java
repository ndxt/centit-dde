package com.centit.dde.dataset;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.core.DataSetReader;
import com.centit.dde.core.DataSetWriter;
import com.centit.support.algorithm.BooleanBaseOpt;
import com.centit.support.file.FileIOOpt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

public class JSONDataSet implements DataSetReader, DataSetWriter {

    private static final Logger logger = LoggerFactory.getLogger(JSONDataSet.class);

    protected String filePath;

    public void setFilePath(String filePath) throws FileNotFoundException {
        this.filePath = filePath;
    }
    /**
     * 读取 dataSet 数据集
     *
     * @param params 模块的自定义参数
     * @return dataSet 数据集
     */
    @Override
    public DataSet load(Map<String, Object> params, DataOptContext dataOptContext) throws Exception{
        if (params != null && BooleanBaseOpt.castObjectToBoolean(
            params.get("isJSONArray"), false)) {
            JSONArray json = JSON.parseArray(FileIOOpt.readStringFromFile(filePath));
            return new DataSet(json);
        } else {
            return JSON.parseObject(FileIOOpt.readStringFromFile(filePath), DataSet.class);
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
            FileIOOpt.writeStringToFile(JSON.toJSONString(dataSet), filePath);
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }
}
