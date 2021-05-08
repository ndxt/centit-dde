package com.centit.dde.dataset;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.DataSet;
import com.centit.dde.core.SimpleDataSet;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.file.FileIOOpt;
import com.centit.support.report.ExcelExportUtil;
import com.centit.support.report.ExcelImportUtil;
import com.centit.support.report.ExcelTypeEnum;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.util.StreamUtils;

import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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


    /**
     * 描述：对表格中数值进行格式化
     *
     * @param cell
     * @return
     */
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
