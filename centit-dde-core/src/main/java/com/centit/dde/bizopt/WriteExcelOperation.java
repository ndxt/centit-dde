package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.dataset.FileDataSet;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.fileserver.common.FileInfoOpt;
import com.centit.framework.common.ResponseData;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.compiler.Pretreatment;
import com.centit.support.report.ExcelExportUtil;
import com.centit.support.report.ExcelImportUtil;
import com.centit.support.report.ExcelReportUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class WriteExcelOperation implements BizOperation {

    private FileInfoOpt fileInfoOpt;

    public WriteExcelOperation(FileInfoOpt fileInfoOpt) {
        this.fileInfoOpt = fileInfoOpt;
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        String id = bizOptJson.getString("id");
        String source = bizOptJson.getString("source");
        DataSet dataSet = bizModel.getDataSet(source);
        if (dataSet == null) {
            return BuiltInOperation.createResponseData(0, 1, ResponseData.ERROR_OPERATION,
                bizOptJson.getString("SetsName") + "：生成EXCEL文件异常，请指定数据集！");
        }
        List<Map<String, Object>> dataAsList = dataSet.getDataAsList();
        Map<String, String> mapInfoDesc = BuiltInOperation.jsonArrayToMap(bizOptJson.getJSONArray("config"), "columnName", "expression");
        String sheetName = bizOptJson.getString("sheetName");
        if(StringUtils.isBlank(sheetName)){
            sheetName = "Sheet1";
        }
        //获取表达式信息
        String[] titles = mapInfoDesc.keySet().toArray(new String[0]);
        String[] fields = mapInfoDesc.values().toArray(new String[0]);
        /**fileType（生成方式） ： none, append, excel, jxls；*/
        String optType = bizOptJson.getString("fileType");
        int mergeColCell = NumberBaseOpt.castObjectToInteger(bizOptJson.getString("mergeColCell"), -1);
        if("append".equals(optType)){
            String fileDataSetName = bizOptJson.getString("fileDataSet");
            DataSet dataSet2 = bizModel.getDataSet(fileDataSetName);
            if(!(dataSet2 instanceof FileDataSet)){
                return BuiltInOperation.createResponseData(0, 1, ResponseData.ERROR_USER_CONFIG,
                    bizOptJson.getString("SetsName") + "：追加文件数据集配置错误！");
            }
            FileDataSet fileDataSet = (FileDataSet)dataSet2;
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fileDataSet.getFileInputStream());
            XSSFSheet sheet = xssfWorkbook.getSheet(sheetName);
            //Integer beginRow = bizOptJson.getInteger("beginRow") == null ? 0 : bizOptJson.getInteger("beginRow");
           // Map<Integer, String> mapInfo = ExcelImportUtil.mapColumnIndex(mapInfoDesc);
            ExcelExportUtil.generateExcelSheet(sheet, dataAsList, titles, fields);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            xssfWorkbook.write(byteArrayOutputStream);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            fileDataSet.setFileData(byteArrayInputStream);
            byteArrayOutputStream.close();
            xssfWorkbook.close();
            return BuiltInOperation.createResponseSuccessData(dataSet.getSize());
        }

        String fileName = StringUtils.isNotBlank(bizOptJson.getString("fileName")) ?
            StringBaseOpt.castObjectToString(Pretreatment.mapTemplateStringAsFormula(
                bizOptJson.getString("fileName"), new BizModelJSONTransform(bizModel))) :
            DatetimeOpt.currentTimeWithSecond();
        fileName = fileName.endsWith(".xlsx") ? fileName : fileName + ".xlsx";
        //模板文件id
        String templateFileId = bizOptJson.getString("templateFileId");
        //根据模板生成
        if (StringUtils.endsWithAny(optType,"jxls","excel") && StringUtils.isNotBlank(templateFileId)) {
            InputStream inputStream = fileInfoOpt.loadFileStream(templateFileId);
            if (inputStream == null) {
                return BuiltInOperation.createResponseData(0, 1, ResponseData.ERROR_OPERATION,
                    bizOptJson.getString("SetsName") + "：Excel模板不存在，请先上传模板！");
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            if("jxls".equals(optType)){
                ExcelReportUtil.exportExcel(inputStream, byteArrayOutputStream, dataSet.getFirstRow());
            } else {
                //从第几行开始插入
                Integer beginRow = bizOptJson.getInteger("beginRow") == null ? 0 : bizOptJson.getInteger("beginRow");
                //指定写入第几个sheet中
                XSSFWorkbook xssfWorkbook = new XSSFWorkbook(inputStream);
                XSSFSheet sheet = xssfWorkbook.getSheet(sheetName);

                Map<Integer, String> mapInfo = ExcelImportUtil.mapColumnIndex(mapInfoDesc);
                ExcelExportUtil.saveObjectsToExcelSheet(sheet, dataAsList, mapInfo, beginRow, true, mergeColCell);
                xssfWorkbook.write(byteArrayOutputStream);
                xssfWorkbook.close();
            }
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

            FileDataSet objectToDataSet = new FileDataSet(fileName, byteArrayOutputStream.size(), byteArrayInputStream);
            bizModel.putDataSet(id, objectToDataSet);
            byteArrayOutputStream.close();

            return BuiltInOperation.createResponseSuccessData(dataSet.getSize());
        }

        InputStream inputStream = ExcelExportUtil.generateExcelStream(sheetName, dataAsList, titles, fields);
        FileDataSet objectToDataSet = new FileDataSet(fileName, inputStream.available(), inputStream);
        bizModel.putDataSet(id, objectToDataSet);

        return BuiltInOperation.createResponseSuccessData(dataSet.getSize());
    }

}
