package com.centit.product.dataopt.dataset;

import com.alibaba.fastjson.JSONObject;
import com.centit.product.dataopt.core.DataSet;
import com.centit.product.dataopt.core.SimpleDataSet;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.algorithm.UuidOpt;
import com.centit.support.compiler.Pretreatment;
import com.centit.support.file.FileIOOpt;
import com.centit.support.report.ExcelImportUtil;
import com.centit.support.report.ExcelTypeEnum;
import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

public class CsvDataSet extends FileDataSet {

    /**
     * 读取 dataSet 数据集
     *
     * @param params 模块的自定义参数
     * @return dataSet 数据集
     */
    private Map<String, Object> params;
    @Override
    public SimpleDataSet load(Map<String, Object> params) {
        try {
            List<Map<String, Object>> list = new ArrayList<>();
            File dir = new File(getFilePath());
            if (dir.isFile()) {
                readCsvFile(list, getFilePath());
            } else {
                File[] files = dir.listFiles();
                if (null != files) {
                    for (File subFileNames : files) {
                        if (!subFileNames.isDirectory()) {
                            readCsvFile(list, subFileNames.getPath());
                        }
                    }
                }
            }
            SimpleDataSet dataSet = new SimpleDataSet();
            dataSet.setData(list);
            return dataSet;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void readCsvFile(List<Map<String, Object>> list, String filePath) throws IOException {
        InputStream inputStream = new FileInputStream(filePath);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,
            Charset.forName("gbk")), 8192);
        CsvReader csvReader = new CsvReader(reader);
        csvReader.setDelimiter(',');
        csvReader.setSafetySwitch(false);
        if (csvReader.readRecord()) {
            String[] splitedHead = csvReader.getValues();
            while (csvReader.readRecord()) {
                Map<String, Object> map = new HashMap<>();
                String[] splitedResult = csvReader.getValues();
                for (int i = 0; i < splitedHead.length; i++) {
                    map.put(splitedHead[i], splitedResult[i]);
                }
                list.add(map);
            }
        }
        csvReader.close();
    }

    private String[] readCsvHead(String filePath) throws IOException {
        InputStream inputStream = new FileInputStream(filePath);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,
            Charset.forName("gbk")), 8192);
        CsvReader csvReader = new CsvReader(reader);
        csvReader.setDelimiter(',');
        if (csvReader.readRecord()) {
            return csvReader.getValues();
        }
        return null;
    }

    public String[] getColumns() {
        try {
            File dir = new File(getFilePath());
            if (dir.isFile()) {
                return readCsvHead(getFilePath());
            } else {
                File[] files = dir.listFiles();
                if (null != files) {
                    return readCsvHead(files[0].getPath());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将 dataSet 数据集 持久化
     *
     * @param dataSet 数据集
     */
    @Override
    public void save(DataSet dataSet) {
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(new File(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(
            outputStream, Charset.forName("gbk")));
        CsvWriter csvWriter = new CsvWriter(writer, ',');
        csvWriter.setTextQualifier('"');
        csvWriter.setUseTextQualifier(true);
        csvWriter.setRecordDelimiter(IOUtils.LINE_SEPARATOR.charAt(0));
        List<Map<String, Object>> list=dataSet.getData();
        Collections.sort(list, (o1, o2) -> Integer.compare(o2.size(), o1.size()));
        int iHead = 0;
        for (Map<String, Object> row : list) {
            if (iHead == 0) {
                try {
                    csvWriter.writeRecord(row.keySet().toArray(new String[0]));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            iHead++;
            try {
                List<String> splitedRows = new ArrayList<String>();
                for (String key : list.get(0).keySet()) {
                    Object column = row.get(key);
                    if (null != column) {
                        splitedRows.add(column.toString());
                    } else {
                        splitedRows.add("");
                    }

                }
                csvWriter.writeRecord(splitedRows.toArray(new String[0]));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        csvWriter.close();
        IOUtils.closeQuietly(outputStream);
    }


    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
