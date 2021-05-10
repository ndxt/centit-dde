package com.centit.dde.dataset;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.bizopt.BuiltInOperation;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.DataSet;
import com.centit.dde.core.SimpleDataSet;
import com.centit.fileserver.utils.SystemTempFileUtils;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.file.FileSystemOpt;
import com.centit.support.report.ExcelExportUtil;
import com.centit.support.report.ExcelImportUtil;
import com.centit.support.report.ExcelTypeEnum;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExcelDataSet extends FileDataSet {

    private InputStream inputStream;

    public  void  setInputStream(InputStream inputStream){
        this.inputStream=inputStream;
    }

    @Override
    public SimpleDataSet load(Map<String, Object> params) {
        try {
            SimpleDataSet dataSet = new SimpleDataSet();
            //直接执行ExcelTypeEnum.checkFileExcelType(inputStream) 会导致流损坏，创建Workbook时报错，目前只能通过复制流对象来解决
            List<InputStream> inputStreamList  =cloneInputStream(inputStream) ;
            ExcelTypeEnum excelTypeEnum = ExcelTypeEnum.checkFileExcelType(inputStreamList.get(0));
            switch (excelTypeEnum){
                case HSSF:
                    dataSet.setData(excelStreamToArray(inputStreamList.get(1),ExcelTypeEnum.HSSF));
                    break;
                case XSSF:
                    dataSet.setData(excelStreamToArray(inputStreamList.get(1),ExcelTypeEnum.XSSF));
                    break;
                case NOTEXCEL:
                    dataSet.setData(null);
            }
            return dataSet;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取字段信息
     *
     * @return
     */
    public String[] getColumns() {
        try {
            if (ExcelTypeEnum.checkFileExcelType(getFilePath()) != ExcelTypeEnum.NOTEXCEL) {
                return ExcelImportUtil.loadColumnsFromExcel(getFilePath(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void save(DataSet dataSet) {
        List<Object[]> fields = new ArrayList<>();
        for (Map<String, Object> map : dataSet.getDataAsList()) {
            fields.add(map.values().toArray());
        }
        String path;
        File file = new File(this.getFilePath());
        if (file.isFile()) {
            path = this.getFilePath();
        } else {
            String fileDate = DatetimeOpt.convertDateToString(DatetimeOpt.currentUtilDate(), "YYYYMMddHHmmss");
            path = this.getFilePath() + File.separator + fileDate;
            File filepath = new File(path);
            if (!filepath.exists()) {
                filepath.mkdirs();
            }
            path = path + File.separator + "sys.xls";
        }
        try {
            ExcelExportUtil.appendDataToExcelSheet(path, 0, fields, dataSet.getDataAsList().get(0).keySet().toArray(new String[0]));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static JSONArray excelStreamToArray(InputStream inputStream, ExcelTypeEnum excelType) throws Exception {
        Workbook work = excelType == ExcelTypeEnum.HSSF ? new HSSFWorkbook(inputStream) : new XSSFWorkbook(inputStream);
        if (null == work) {
            throw new Exception("创建Excel工作薄为空！");
        }
        Sheet sheet;
        Row row;
        JSONArray jsonArray  = new JSONArray();
        // 遍历Excel中所有的sheet
        for (int i = 0; i < work.getNumberOfSheets(); i++) {
            sheet = work.getSheetAt(i);
            if (sheet == null) {
                continue;
            }
            // 取第一行标题
            row = sheet.getRow(0);
            Object title[];
            if (row != null) {
                title = new String[row.getLastCellNum()];
                for (int y = row.getFirstCellNum(); y < row.getLastCellNum(); y++) {
                    Object cellValue = getCellValue(row.getCell(y));
                    title[y] = cellValue;
                }
            } else {
                continue;
            }
            // 遍历当前sheet中的所有行
            for (int j = 1; j < sheet.getLastRowNum() + 1; j++) {
                row = sheet.getRow(j);
                JSONObject jsonObject = new JSONObject();
                // 遍历所有的列
                for (int y = row.getFirstCellNum(); y < row.getLastCellNum(); y++) {
                    Object cellValue = getCellValue(row.getCell(y));
                    Object key = title[y];
                    jsonObject.put((String) key, cellValue);
                }
                jsonArray.add(jsonObject);
            }
        }
        work.close();
        return jsonArray;
    }

    public static  InputStream writeExcel(BizModel bizModel,JSONObject bizOptJson) throws Exception{
        String path = BuiltInOperation.getJsonFieldString(bizOptJson, "source", "");
        File excel= new File(SystemTempFileUtils.getRandomTempFilePath());
        for(Map.Entry<String,DataSet> set:bizModel.getBizData().entrySet()) {
            if(set.getKey().equals(path) || StringBaseOpt.isNvl(path)) {
                String[] head= CollectionsOpt.listToArray(set.getValue().getFirstRow().keySet());
                ExcelExportUtil.appendDataToExcelSheet(excel.getPath(), set.getKey(), (List<Object>) set.getValue().getData(), head,head);
            }
        }
        FileInputStream  fileInputStream = new FileInputStream(excel);
        FileSystemOpt.deleteFile(excel);
        return  fileInputStream;
    }

    private static  List<InputStream> cloneInputStream(InputStream input) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = input.read(buffer)) > -1) {
                baos.write(buffer, 0, len);
            }
            baos.flush();
            List<InputStream> inputStreamList = new ArrayList<>();
            ByteArrayInputStream byteArrayInputStreamOne = new ByteArrayInputStream(baos.toByteArray());
            ByteArrayInputStream byteArrayInputStreamTwo = new ByteArrayInputStream(baos.toByteArray());
            inputStreamList.add(byteArrayInputStreamOne);
            inputStreamList.add(byteArrayInputStreamTwo);
            return inputStreamList;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object getCellValue(Cell cell) {
        Object value = null;
        DecimalFormat df = new DecimalFormat("0"); // 格式化number String字符
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd"); // 日期格式化
        DecimalFormat df2 = new DecimalFormat("0"); // 格式化数字
        switch (cell.getCellType()) {
            case STRING:
                value = cell.getRichStringCellValue().getString();
                break;
            case NUMERIC:
                if ("General".equals(cell.getCellStyle().getDataFormatString())) {
                    value = df.format(cell.getNumericCellValue());
                } else if ("m/d/yy".equals(cell.getCellStyle().getDataFormatString())) {
                    value = sdf.format(cell.getDateCellValue());
                } else {
                    value = df2.format(cell.getNumericCellValue());
                }
                break;
            case BOOLEAN:
                value = cell.getBooleanCellValue();
                break;
            case BLANK:
                value = "";
                break;
            default:
                break;
        }
        return value;
    }

}
