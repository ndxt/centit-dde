package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.dde.utils.BizOptUtils;
import com.centit.dde.utils.ConstantValue;
import com.centit.fileserver.common.FileStore;
import com.centit.framework.common.ResponseData;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.compiler.Pretreatment;
import com.centit.support.report.ExcelExportUtil;
import com.centit.support.report.ExcelImportUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GenerateExcelFileBizeOperation implements BizOperation {

    FileStore fileStore;

    public GenerateExcelFileBizeOperation(FileStore fileStore) {
        this.fileStore = fileStore;
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        String id = bizOptJson.getString("id");
        String source = bizOptJson.getString("source");
        String fileName=StringUtils.isNotBlank(bizOptJson.getString("fileName"))?
            StringBaseOpt.castObjectToString(Pretreatment.mapTemplateStringAsFormula(
                bizOptJson.getString("fileName"), new BizModelJSONTransform(bizModel))):
            DatetimeOpt.currentTimeWithSecond();
        //模板文件id
        String templateFileId =bizOptJson.getString("templateFileId");
        DataSet dataSet = bizModel.fetchDataSetByName(source);
        //根据模板生成
        if (StringUtils.isNotBlank(templateFileId)){
            //从第几行开始插入
            Integer beginRow =bizOptJson.getInteger("beginRow")==null?0:bizOptJson.getInteger("beginRow");
            //指定写入第几个sheet中
            String sheetName = bizOptJson.getString("sheetName");
            InputStream inputStream = fileStore.loadFileStream(templateFileId);
            if (inputStream==null){
                return  BuiltInOperation.createResponseData(0, 1,ResponseData.ERROR_OPERATION, bizOptJson.getString("SetsName")+"：Excel模板不存在，请先上传模板！");
            }
            Object data = dataSet.getData();
            JSONArray jsonArray = JSON.parseArray(JSON.toJSONString(data));
            XSSFWorkbook xssfWorkbook =new XSSFWorkbook(inputStream);
            XSSFSheet sheet = xssfWorkbook.getSheet(sheetName);
            Map<String, String> mapInfoDesc = BuiltInOperation.jsonArrayToMap(bizOptJson.getJSONArray("config"), "columnName", "expression");
            Map<Integer, String> mapInfo = ExcelImportUtil.mapColumnIndex(mapInfoDesc);

            ExcelExportUtil.saveObjectsToExcelSheet(sheet,jsonArray,mapInfo,beginRow,true);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            xssfWorkbook.write(byteArrayOutputStream);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            DataSet objectToDataSet = BizOptUtils.castObjectToDataSet(CollectionsOpt.createHashMap(
                ConstantValue.FILE_NAME, fileName.endsWith(".xlsx")?fileName:fileName+".xlsx",
                ConstantValue.FILE_SIZE, inputStream.available(),
                ConstantValue.FILE_CONTENT ,byteArrayInputStream));
            bizModel.putDataSet(id,objectToDataSet);
            byteArrayOutputStream.close();
            xssfWorkbook.close();
            return BuiltInOperation.createResponseSuccessData(dataSet.getSize());
        }
        //获取表达式信息
        Map<String, String> mapInfo = BuiltInOperation.jsonArrayToMap(bizOptJson.getJSONArray("config"), "columnName", "expression");
        if (dataSet==null){
            return BuiltInOperation.createResponseData(0, 1,ResponseData.ERROR_OPERATION, bizOptJson.getString("SetsName")+"：生成EXCEL文件异常，请指定数据集！");
        }
        List<Map<String, Object>> dataAsList = dataSet.getDataAsList();
        List<String> headers = new ArrayList<>();
        List<String> values = new ArrayList<>();
        mapInfo.keySet().stream().forEach(header->headers.add(header));
        mapInfo.values().stream().forEach(value->values.add(value));
        String[] titles = headers.toArray(new String[mapInfo.size()]);
        String[] fields = values.toArray(new String[mapInfo.size()]);
        InputStream inputStream = ExcelExportUtil.generateExcelStream(dataAsList, titles, fields);
        DataSet objectToDataSet = BizOptUtils.castObjectToDataSet(CollectionsOpt.createHashMap(
            ConstantValue.FILE_NAME,fileName.endsWith(".xlsx")?fileName:fileName+".xlsx",
            ConstantValue.FILE_SIZE, inputStream.available(),
            ConstantValue.FILE_CONTENT,inputStream));
        bizModel.putDataSet(id,objectToDataSet);
        return BuiltInOperation.createResponseSuccessData(dataSet.getSize());
    }


}
