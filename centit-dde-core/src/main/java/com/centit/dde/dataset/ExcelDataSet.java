package com.centit.dde.dataset;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.DataSet;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.report.ExcelExportUtil;
import com.centit.support.report.ExcelImportUtil;
import com.centit.support.report.ExcelTypeEnum;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExcelDataSet extends FileDataSet {

    private InputStream inputStream;

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public DataSet load(Map<String, Object> params) throws Exception {
        return new DataSet(excelStreamToArray(inputStream,params));
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
        return new String[0];
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
            path = this.getFilePath() + File.separator + fileDate.substring(0,8);
            File filepath = new File(path);
            if (!filepath.exists()) {
                filepath.mkdirs();
            }
            path = path + File.separator + fileDate.substring(8)+ "sys.xls";
        }
        try {
            ExcelExportUtil.appendDataToExcelSheet(path, 0, fields, dataSet.getDataAsList().get(0).keySet().toArray(new String[0]));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<Map<String, Object>> excelStreamToArray(InputStream inputStream,Map<String, Object> params) throws Exception {
        JSONObject parseObject = JSON.parseObject(JSON.toJSONString(params));
        String sheetName = parseObject.getString("sheetName");
        int headerRow = parseObject.getInteger("headerRow");
        int beginRow = parseObject.getInteger("beginRow");
        int endRow = parseObject.getInteger("endRow");
        int beginColumn=parseObject.getInteger("startColumnNumber");
        int endColumn =parseObject.getInteger("endColumnNumber");
        return StringUtils.isNotBlank(sheetName)?
            ExcelImportUtil.loadMapFromExcelSheet(inputStream,sheetName,headerRow,beginRow,endRow,beginColumn,endColumn):
            ExcelImportUtil.loadMapFromExcelSheet(inputStream,0,headerRow,beginRow,endRow,beginColumn,endColumn);
    }
}
