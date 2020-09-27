package com.centit.product.dataopt.dataset;

import com.centit.product.dataopt.core.DataSet;
import com.centit.product.dataopt.core.SimpleDataSet;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.report.ExcelExportUtil;
import com.centit.support.report.ExcelImportUtil;
import com.centit.support.report.ExcelTypeEnum;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExcelDataSet extends FileDataSet {

    @Override
    public SimpleDataSet load(Map<String, Object> params) {
        try {
            SimpleDataSet dataSet = new SimpleDataSet();
            if (ExcelTypeEnum.checkFileExcelType(getFilePath()) != ExcelTypeEnum.NOTEXCEL) {
                dataSet.setData(ExcelImportUtil.loadMapFromExcelSheet(getFilePath(), 0));
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
        for (Map<String, Object> map : dataSet.getData()) {
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
            ExcelExportUtil.appendDataToExcelSheet(path, 0, fields, dataSet.getData().get(0).keySet().toArray(new String[0]));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
