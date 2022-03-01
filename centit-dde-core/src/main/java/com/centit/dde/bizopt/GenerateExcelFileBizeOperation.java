package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataSet;
import com.centit.dde.core.SimpleDataSet;
import com.centit.dde.dataset.ExcelDataSet;
import com.centit.dde.utils.BizOptUtils;
import com.centit.dde.utils.DataSetOptUtil;
import com.centit.fileserver.common.FileStore;
import com.centit.framework.common.ResponseData;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.report.ExcelExportUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GenerateExcelFileBizeOperation implements BizOperation {

    FileStore fileStore;

    public GenerateExcelFileBizeOperation(FileStore fileStore) {
        this.fileStore = fileStore;
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson) throws Exception {
        String sourDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        String targetDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "id", sourDsName);
        //模板文件id
        String templateFileId = BuiltInOperation.getJsonFieldString(bizOptJson, "templateFileId", sourDsName);
        //从第几行开始插入
        Integer beginRow = Integer.valueOf(BuiltInOperation.getJsonFieldString(bizOptJson, "beginRow", sourDsName))==null?0:
            Integer.valueOf(BuiltInOperation.getJsonFieldString(bizOptJson, "beginRow", sourDsName));
        //指定写入第几个sheet中
        String sheetName = BuiltInOperation.getJsonFieldString(bizOptJson, "sheetName", sourDsName);
        DataSet dataSet = bizModel.fetchDataSetByName(sourDsName);
        String requestBody= (String)bizModel.getInterimVariable().get("requestBody");
        if (StringUtils.isNotBlank(templateFileId)){//根据模板生成
            InputStream inputStream = fileStore.loadFileStream(templateFileId);
            if (inputStream==null){
                return  BuiltInOperation.getResponseData(0, 500, bizOptJson.getString("SetsName")+"：Excel模板不存在，请先上传模板！");
            }
            Object data = dataSet.getData();
            JSONArray jsonArray = JSON.parseArray(JSON.toJSONString(data));
            XSSFWorkbook xssfWorkbook =new XSSFWorkbook(inputStream);
            XSSFSheet sheet = xssfWorkbook.getSheet(sheetName);
            Map<Integer, String> mapInfo = jsonArrayToMap(bizOptJson.getJSONArray("config"), "columnName", "expression");
            ExcelExportUtil.saveObjectsToExcelSheet(sheet,jsonArray,mapInfo,beginRow,true);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            xssfWorkbook.write(byteArrayOutputStream);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            DataSet objectToDataSet = BizOptUtils.castObjectToDataSet(CollectionsOpt.createHashMap("fileName", System.currentTimeMillis()+".xlsx",
                "fileSize", inputStream.available(), "fileContent",byteArrayInputStream));
            bizModel.putDataSet(targetDsName,objectToDataSet);
            byteArrayOutputStream.close();
            xssfWorkbook.close();
            return BuiltInOperation.getResponseSuccessData(dataSet.getSize());
        }
        //获取表达式信息
        Map<String, String> mapInfo = BuiltInOperation.jsonArrayToMap(bizOptJson.getJSONArray("config"), "columnName", "expression");
        if (mapInfo != null && mapInfo.size() > 0) {
            if (dataSet != null) {
                dataSet = DataSetOptUtil.mapDateSetByFormula(dataSet, mapInfo.entrySet());
            }
        }else if (StringUtils.isNotBlank(requestBody)){
            dataSet= new SimpleDataSet(requestBody);
        }
        if (dataSet==null){
            return BuiltInOperation.getResponseData(0, 500, bizOptJson.getString("SetsName")+"：生成EXCEL文件异常，请指定数据集！");
        }
        List<Map<String, Object>> dataAsList = dataSet.getDataAsList();
        InputStream inputStream = ExcelDataSet.writeExcel(dataAsList,mapInfo);
        DataSet objectToDataSet = BizOptUtils.castObjectToDataSet(CollectionsOpt.createHashMap("fileName", System.currentTimeMillis()+".xlsx",
            "fileSize", inputStream.available(), "fileContent",inputStream));
        bizModel.putDataSet(targetDsName,objectToDataSet);
        return BuiltInOperation.getResponseSuccessData(dataSet.getSize());
    }


    private static Map<Integer, String> jsonArrayToMap(JSONArray json, String key, String... value) {
        if (json != null) {
            LinkedHashMap<Integer, String> map = new LinkedHashMap<>();
            for (Object o : json) {
                JSONObject temp = (JSONObject) o;
                if (!StringBaseOpt.isNvl(temp.getString(key))) {
                    StringBuilder values = new StringBuilder();
                    for (int i = 0; i < value.length; i++) {
                        values.append(temp.getString(value[i]));
                        if (i < value.length - 1) {
                            values.append(":");
                        }
                    }
                    map.put(temp.getInteger(key), values.toString());
                }
            }
            return map;
        }
        return Collections.EMPTY_MAP;
    }
}
