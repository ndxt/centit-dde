package com.centit.dde.dataset;

import com.centit.dde.core.DataSet;
import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import lombok.Data;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

/**
 * @author zhf
 */
@Data
public class CsvDataSet extends FileDataSet {

    /**
     * 读取 dataSet 数据集
     *
     * @param params 模块的自定义参数
     * @return dataSet 数据集
     */
    private Map<String, Object> params;

    private InputStream inputStream;

    @Override
    public void setFilePath(String filePath) throws FileNotFoundException {
        super.filePath = filePath;
        if (new File(filePath).exists()) {
            inputStream = new FileInputStream(filePath);
        }
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public DataSet load(Map<String, Object> params) throws Exception {
        List<Map<String, Object>> list = new ArrayList<>();
        readCsvFile(list);
        DataSet dataSet = new DataSet();
        dataSet.setData(list);
        return dataSet;
    }

    private void readCsvFile(List<Map<String, Object>> list) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,
            Charset.forName("gbk")), 8192);
        CsvReader csvReader = new CsvReader(reader);
        csvReader.setDelimiter(',');
        csvReader.setSafetySwitch(false);
        if (csvReader.readRecord()) {
            String[] splitHead = csvReader.getValues();
            while (csvReader.readRecord()) {
                Map<String, Object> map = new HashMap<>();
                String[] splitResult = csvReader.getValues();
                for (int i = 0; i < splitHead.length; i++) {
                    map.put(splitHead[i], splitResult[i]);
                }
                list.add(map);
            }
        }
        csvReader.close();
    }

    public String[] getColumns() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,
            Charset.forName("gbk")), 8192);
        CsvReader csvReader = new CsvReader(reader);
        csvReader.setDelimiter(',');
        if (csvReader.readRecord()) {
            return csvReader.getValues();
        }
        return new String[0];
    }


    /**
     * 将 dataSet 数据集 持久化
     *
     * @param dataSet 数据集
     */
    @Override
    public void save(DataSet dataSet) {
        try (OutputStream outputStream = new FileOutputStream(filePath);
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                // 为什么是gbk， 这个是否可以作为一个参数从前端传入，
                // 默认应该是 utf-8
                outputStream, Charset.forName("gbk")))){

            CsvWriter csvWriter = new CsvWriter(writer, ',');
            csvWriter.setTextQualifier('"');
            csvWriter.setUseTextQualifier(true);
            csvWriter.setRecordDelimiter(IOUtils.LINE_SEPARATOR.charAt(0));
            List<Map<String, Object>> list = dataSet.getDataAsList();
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

                List<String> splitedRows = new ArrayList<>();
                for (String key : list.get(0).keySet()) {
                    Object column = row.get(key);
                    if (null != column) {
                        splitedRows.add(column.toString());
                    } else {
                        splitedRows.add("");
                    }

                }
                csvWriter.writeRecord(splitedRows.toArray(new String[0]));


            }
            csvWriter.close();
        } catch ( IOException e) {
            e.printStackTrace();
        }
        //IOUtils.closeQuietly(outputStream);
    }


    public static InputStream createCsvStream(DataSet dataSet) throws IOException {
        try (
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(byteArrayOutputStream, Charset.forName("gbk")));
        ) {
            CsvWriter csvWriter = new CsvWriter(writer, ',');
            csvWriter.setTextQualifier('"');
            csvWriter.setUseTextQualifier(true);
            csvWriter.setRecordDelimiter(IOUtils.LINE_SEPARATOR.charAt(0));
            List<Map<String, Object>> list = dataSet.getDataAsList();
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
            csvWriter.flush();
            if (csvWriter != null) {
                csvWriter.close();
            }
            return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        }
    }
}
