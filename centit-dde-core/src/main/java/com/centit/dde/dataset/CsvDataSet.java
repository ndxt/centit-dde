package com.centit.dde.dataset;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.DataSet;
import com.centit.dde.core.DataSetReader;
import com.centit.dde.core.DataSetWriter;
import com.centit.dde.utils.ConstantValue;
import com.centit.framework.common.ResponseData;
import com.centit.support.algorithm.BooleanBaseOpt;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.common.ObjectException;
import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

/**
 * @author zhf
 */
public class CsvDataSet implements DataSetReader, DataSetWriter {
    private static String DEFAULT_CHARSET = "gbk";
    private InputStream inputStream;

    protected String filePath;

    public void setFilePath(String filePath) throws FileNotFoundException {
        this.filePath = filePath;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public DataSet load(Map<String, Object> params) throws Exception {
        List<Map<String, Object>> list = readCsvFile(params);
        DataSet dataSet = new DataSet();
        dataSet.setData(list);
        return dataSet;
    }

    /**
     * 将 dataSet 数据集 持久化
     *
     * @param dataSet 数据集
     */
    @Override
    public void save(DataSet dataSet) {
        try (OutputStream outputStream = new FileOutputStream(filePath)) {
            saveCsv2OutStream(dataSet, outputStream, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String[] getColumns() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,
            Charset.forName(DEFAULT_CHARSET)), 8192);
        CsvReader csvReader = new CsvReader(reader);
        csvReader.setDelimiter(',');
        if (csvReader.readRecord()) {
            return csvReader.getValues();
        }
        return new String[0];
    }

    private List<Map<String, Object>> readCsvFile(Map<String, Object> params) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,
            Charset.forName(getCharset(params))), 8192)) {

            CsvReader csvReader = new CsvReader(reader);
            csvReader.setDelimiter(',');
            csvReader.setSafetySwitch(false);
            // firstRowAsHeader 如果是true则以第一行为key生成一个map，如果第一行不够长，后面的key自动为 column+i
            // 如果firstRowAsHeader为false，则必须要指定每一列的key，不够长同样自动为column+i
            List<String> headers = null;
            boolean firstRowAsHeader = params == null ||
                BooleanBaseOpt.castObjectToBoolean(params.get("firstRowAsHeader"), true);
            if (firstRowAsHeader) {
                if (csvReader.readRecord()) {
                    String[] splitHead = csvReader.getValues();
                    headers = CollectionsOpt.arrayToList(splitHead);
                }
            } else {
                headers = loadColumnNames(params);
            }

            if (headers == null) {
                headers = new ArrayList<>();
            }
            int headLen = headers.size();

            while (csvReader.readRecord()) {
                String[] splitResult = csvReader.getValues();
                int splitResultLength = splitResult.length;
                Map<String, Object> map = new HashMap<>(splitResultLength);
                for (int i = 0; i < splitResultLength; i++) {
                    String columnName = i < headLen ? headers.get(i) : "column" + i;
                    map.put(columnName, splitResult[i]);
                }
                list.add(map);
            }
            csvReader.close();
            return list;
        }
    }

    private static void saveCsv2OutStream(DataSet dataSet, OutputStream outs, Map<String, Object> params) throws IOException {
        // firstRowAsHeader 如果是true则将所有的key作为第一行，后面按照key的顺序对齐
        // 如果firstRowAsHeader为false，则必须要指定每一列的key，只保存对应的key列，多余的列不保存
        boolean firstRowAsHeader = params == null ||
            BooleanBaseOpt.castObjectToBoolean(params.get("firstRowAsHeader"), true);
        List<String> columnNames;
        List<Map<String, Object>> list = dataSet.getDataAsList();
        if (list.size() == 0) {
            return;
        }
        if (firstRowAsHeader) {
            Set<String> headers = new HashSet<>(20);
            for (Map<String, Object> row : list) {
                headers.addAll(row.keySet());
            }
            columnNames = CollectionsOpt.cloneList(headers);
        } else {
            columnNames = loadColumnNames(params);
        }

        if (columnNames == null || columnNames.size() == 0) {
            throw new ObjectException(ResponseData.ERROR_USER_CONFIG, "配置信息有错，或者数据为空！");
        }
        try (
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outs, Charset.forName(getCharset(params))))) {

            CsvWriter csvWriter = new CsvWriter(writer, ',');
            csvWriter.setTextQualifier('"');
            csvWriter.setUseTextQualifier(true);
            csvWriter.setRecordDelimiter(IOUtils.LINE_SEPARATOR.charAt(0));

            if (firstRowAsHeader) {
                csvWriter.writeRecord(CollectionsOpt.listToArray(columnNames));
            }

            String[] values = new String[columnNames.size()];
            for (Map<String, Object> row : list) {
                for (int i = 0; i < columnNames.size(); i++) {
                    values[i] = StringBaseOpt.castObjectToString(row.get(columnNames.get(i)), "");
                }
                csvWriter.writeRecord(values);
            }
            csvWriter.flush();
            csvWriter.close();
        }
    }

    private static List<String> loadColumnNames(Map<String, Object> params) {
        if (params.get(ConstantValue.HEADERS) == null) {
            return null;
        }
        List<String> columnNames = null;
        Object header = params.get(ConstantValue.HEADERS);
        if (header instanceof Collection) {
            columnNames = CollectionsOpt.mapCollectionToList((Collection<?>) header,
                (a) -> ((JSONObject) a).getString("header"));
        }
        return columnNames;
    }

    public static InputStream createCsvStream(DataSet dataSet, Map<String, Object> params) throws IOException {
        try (
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()
        ) {
            saveCsv2OutStream(dataSet, byteArrayOutputStream, params);
            return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        }
    }


    private static String getCharset(Map<String, Object> params) {
        if (params == null) {
            return DEFAULT_CHARSET;
        }
        String charSet = StringBaseOpt.castObjectToString(params.get("charsetType"));
        if (StringUtils.isBlank(charSet)) {
            return DEFAULT_CHARSET;
        }
        return charSet;
    }

}
