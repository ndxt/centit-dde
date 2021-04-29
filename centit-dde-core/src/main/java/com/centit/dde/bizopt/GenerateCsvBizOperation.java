package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataSet;
import com.centit.dde.core.SimpleDataSet;
import com.centit.dde.dataset.CsvDataSet;
import com.centit.dde.utils.DataSetOptUtil;
import com.centit.framework.common.ResponseData;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * 生成json文件节点信息
 */
@Service
public class GenerateCsvBizOperation implements BizOperation {

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson){
        String sourDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        String targetDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "id", sourDsName);
        DataSet dataSet = bizModel.fetchDataSetByName(sourDsName);
        //获取表达式信息
        Map<String, String> mapInfo = BuiltInOperation.jsonArrayToMap(bizOptJson.getJSONArray("config"), "columnName", "expression");
        if (mapInfo != null  && mapInfo.size()>0) {
            if (dataSet != null) {
                dataSet = DataSetOptUtil.mapDateSetByFormula(dataSet, mapInfo.entrySet());
            }
        }
        try {
            InputStream inputStream = CsvDataSet.createCsvStream(dataSet);
            bizModel.putDataSet(targetDsName,new SimpleDataSet(inputStream));
        } catch (IOException e) {
           return BuiltInOperation.getResponseData(0, 500, bizOptJson.getString("SetsName")+"：创建csv文件异常，异常信息"+e.getMessage());
        }
        return BuiltInOperation.getResponseSuccessData(dataSet.getSize());
    }
}
