package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataSet;
import com.centit.dde.core.SimpleDataSet;
import com.centit.dde.dataset.ExcelDataSet;
import com.centit.dde.utils.DataSetOptUtil;
import com.centit.framework.common.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Component
public class GenerateExcelBizeOperation implements BizOperation {
    private  static  final Logger logger = LoggerFactory.getLogger(GenerateExcelBizeOperation.class);
    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson){
        String sourDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        String targetDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "id", sourDsName);
        DataSet dataSet = bizModel.fetchDataSetByName(sourDsName);
        //获取表达式信息
        Map<String, String> mapInfo = BuiltInOperation.jsonArrayToMap(bizOptJson.getJSONArray("config"), "columnName", "expression");
        if (mapInfo != null && mapInfo.size() > 0) {
            if (dataSet != null) {
                dataSet = DataSetOptUtil.mapDateSetByFormula(dataSet, mapInfo.entrySet());
            }
        }
        List<Map<String, Object>> dataAsList = dataSet.getDataAsList();
        try {
            InputStream inputStream = ExcelDataSet.writeExcel(dataAsList);
            bizModel.putDataSet(targetDsName,new SimpleDataSet(inputStream));
        } catch (IOException e) {
            return BuiltInOperation.getResponseData(0, 500, bizOptJson.getString("SetsName")+"：创建EXCEL文件异常，异常信息"+e.getMessage());
        }
        return BuiltInOperation.getResponseSuccessData(dataSet.getSize());
    }
}
