package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataSet;
import com.centit.dde.core.SimpleDataSet;
import com.centit.dde.utils.DataSetOptUtil;
import com.centit.framework.common.ResponseData;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class GenerateExcelBizeOperation implements BizOperation {
    private  static  final Logger logger = LoggerFactory.getLogger(GenerateExcelBizeOperation.class);
    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson) throws Exception {
        String id = bizOptJson.getString("id");
        String source = bizOptJson.getString("source");
        //获取表达式信息
        Map<String, String> mapInfo = BuiltInOperation.jsonArrayToMap(bizOptJson.getJSONArray("config"), "columnName", "expression");
        if (mapInfo != null && mapInfo.size() > 0) {
            DataSet dataSet = bizModel.fetchDataSetByName(source);
            if (dataSet != null) {
                DataSet destDs = DataSetOptUtil.mapDateSetByFormula(dataSet, mapInfo.entrySet());
                bizModel.putDataSet(id, destDs);
            }
        }
        DataSet thisDataSet = bizModel.getDataSet(id);
        DataSet dataSet;
        if (thisDataSet != null) {
            dataSet = bizModel.getDataSet(id);
        } else {
            dataSet = bizModel.getDataSet(source);
        }
        List<Map<String, Object>> dataAsList = dataSet.getDataAsList();
        ByteArrayOutputStream byteArrayOutputStream = writeExcel(dataAsList);
        bizModel.putDataSet(id,new SimpleDataSet(byteArrayOutputStream));
        //crateExcel(byteArrayOutputStream);
        return BuiltInOperation.getResponseSuccessData(dataSet.getSize());
    }

    //本地测试使用
    private void crateExcel(ByteArrayOutputStream byteArrayOutputStream) throws IOException {
        String fileName =System.currentTimeMillis()+".xlsx";
        FileOutputStream inputStream = new FileOutputStream(new File("D:\\filetest\\"+fileName));
        byteArrayOutputStream.writeTo(inputStream);
        inputStream.flush();
        inputStream.close();
        byteArrayOutputStream.close();
    }

    private ByteArrayOutputStream writeExcel(List<Map<String, Object>>  objectList){
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
        if (objectList.size()>1){//<=1的时候合并单元格会报错
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headList.size() - 1));
        }
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
            return byteArrayOutputStream;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (wb!=null){
                    wb.close();
                }
            }catch (Exception e){
                logger.error("流关闭异常，异常信息为："+e.getMessage());
            }
        }
        return null;
    }
}
