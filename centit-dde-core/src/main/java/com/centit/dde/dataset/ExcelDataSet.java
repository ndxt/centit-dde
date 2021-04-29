package com.centit.dde.dataset;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.DataSet;
import com.centit.dde.core.SimpleDataSet;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.report.ExcelExportUtil;
import com.centit.support.report.ExcelImportUtil;
import com.centit.support.report.ExcelTypeEnum;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExcelDataSet extends FileDataSet {

    private InputStream inputStream;

    public  void  setInputStream(InputStream inputStream){
        this.inputStream=inputStream;
    }

    @Override
    public SimpleDataSet load(Map<String, Object> params) {
        try {
            SimpleDataSet dataSet = new SimpleDataSet();
            ExcelTypeEnum excelTypeEnum = ExcelTypeEnum.checkFileExcelType(inputStream);
            switch (excelTypeEnum){
                case HSSF:
                    dataSet.setData(excelStreamToArray("xls"));
                case XSSF:
                    dataSet.setData(excelStreamToArray("xlsx"));
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

    private   JSONArray excelStreamToArray(String fileType) throws Exception {
        // 根据文件名来创建Excel工作薄
        Workbook work = getWorkbook(inputStream, fileType);
        if (null == work) {
            throw new Exception("创建Excel工作薄为空！");
        }
        Sheet sheet = null;
        Row row = null;
        Cell cell = null;
        JSONArray jsonArray  = new JSONArray();
        // 遍历Excel中所有的sheet
        for (int i = 0; i < work.getNumberOfSheets(); i++) {
            sheet = work.getSheetAt(i);
            if (sheet == null) {
                continue;
            }
            // 取第一行标题
            row = sheet.getRow(0);
            String title[] = null;
            if (row != null) {
                title = new String[row.getLastCellNum()];
                for (int y = row.getFirstCellNum(); y < row.getLastCellNum(); y++) {
                    cell = row.getCell(y);
                    title[y] = String.valueOf(cell);
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
                    cell = row.getCell(y);
                    String key = title[y];
                    jsonObject.put(key, cell);
                }
                jsonArray.add(jsonObject);
            }
        }
        work.close();
        return jsonArray;
    }

    /**
     * 描述：根据文件后缀，自适应上传文件的版本
     *
     * @param inStr
     *            ,fileName
     * @return
     * @throws Exception
     */
    public static Workbook getWorkbook(InputStream inStr, String fileType) throws Exception {
        Workbook wb = null;
        if ("xls".equals(fileType)) {
            wb = new HSSFWorkbook(inStr); // 2003-
        } else if ("xlsx".equals(fileType)) {
            wb = new XSSFWorkbook(inStr); // 2007+
        } else {
            throw new Exception("解析的文件格式有误！");
        }
        return wb;
    }

    /**
     * 将集合生成Excel文件
     * @param objectList 数据
     * @return
     * @throws IOException
     */
    public static InputStream writeExcel(List<Map<String, Object>> objectList) throws IOException {
        //获取数据源的 key, 用于获取列数及设置标题
        Map<String, Object> map = objectList.get(0);
        Set<String> stringSet = map.keySet();
        ArrayList<String> headList = new ArrayList<>(stringSet);
        //定义一个新的工作簿
        XSSFWorkbook wb = new XSSFWorkbook();
        //创建一个Sheet页
        XSSFSheet sheet = wb.createSheet(System.currentTimeMillis()+"");
        //设置行高
        sheet.setDefaultRowHeight((short) (2 * 256));
        //为有数据的每列设置列宽
        for (int i = 0; i < headList.size(); i++) {
            sheet.setColumnWidth(i, 8000);
        }
        //设置单元格字体样式
        XSSFFont font = wb.createFont();
        font.setFontName("等线");
        font.setFontHeightInPoints((short) 16);
        //在sheet里创建第一行，并设置单元格内容为 title (标题)
        XSSFRow titleRow = sheet.createRow(0);
        XSSFCell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("Excel");
        //合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headList.size()));
        // 创建单元格文字居中样式并设置标题单元格居中
        XSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        titleCell.setCellStyle(cellStyle);
        //获得表格第二行
        XSSFRow row = sheet.createRow(1);
        //根据数据源信息给第二行每一列设置标题
        for (int i = 0; i < headList.size(); i++) {
            XSSFCell cell = row.createCell(i);
            cell.setCellValue(headList.get(i));
        }
        XSSFRow rows;
        XSSFCell cells;
        //循环拿到的数据给所有行每一列设置对应的值
        for (int i = 0; i < objectList.size(); i++) {
            //在这个sheet页里创建一行
            rows = sheet.createRow(i + 2);
            //给该行数据赋值
            for (int j = 0; j < headList.size(); j++) {
                String value = objectList.get(i).get(headList.get(j))==null?"":objectList.get(i).get(headList.get(j)).toString();
                cells = rows.createCell(j);
                cells.setCellValue(value);
            }
        }
        try( ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();) {
            wb.write(byteArrayOutputStream);
            return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        } finally {
            if (wb!=null){
                wb.close();
            }
        }
    }
}
