package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataSet;
import com.centit.dde.core.SimpleDataSet;
import com.centit.dde.dataset.CsvDataSet;
import com.centit.dde.utils.BizOptUtils;
import com.centit.dde.utils.DataSetOptUtil;
import com.centit.framework.common.ResponseData;
import com.centit.support.algorithm.CollectionsOpt;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * 生成json文件节点信息
 */
public class GenerateCsvBizOperation implements BizOperation {

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson){
        String sourDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        String targetDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "id", sourDsName);
        DataSet dataSet = bizModel.fetchDataSetByName(sourDsName);
        String requestBody= (String)bizModel.getInterimVariable().get("requestBody");
        //获取表达式信息
        Map<String, String> mapInfo = BuiltInOperation.jsonArrayToMap(bizOptJson.getJSONArray("config"), "columnName", "expression");
        if (mapInfo != null  && mapInfo.size()>0) {
            if (dataSet != null) {
                dataSet = DataSetOptUtil.mapDateSetByFormula(dataSet, mapInfo.entrySet());
            }
        }else if (StringUtils.isNotBlank(requestBody)){
            dataSet = new SimpleDataSet(requestBody);
        }
        if (dataSet==null){
            return BuiltInOperation.getResponseData(0, 500, bizOptJson.getString("SetsName")+"：生成CSV文件异常，请指定数据集！");
        }
        try {
            InputStream inputStream = CsvDataSet.createCsvStream(dataSet);
            DataSet objectToDataSet = BizOptUtils.castObjectToDataSet(CollectionsOpt.createHashMap("fileName", System.currentTimeMillis()+".csv",
                "fileSize", inputStream.available(), "fileContent",inputStream));
            bizModel.putDataSet(targetDsName,objectToDataSet);
        } catch (IOException e) {
           return BuiltInOperation.getResponseData(0, 500, bizOptJson.getString("SetsName")+"：生成CSV文件异常，异常信息"+e.getMessage());
        }
        return BuiltInOperation.getResponseSuccessData(dataSet.getSize());
    }
}
