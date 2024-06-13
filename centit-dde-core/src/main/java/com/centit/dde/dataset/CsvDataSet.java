package com.centit.dde.dataset;

import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.core.DataSetReader;
import com.centit.dde.core.DataSetWriter;
import com.centit.dde.utils.ConstantValue;
import com.centit.support.algorithm.BooleanBaseOpt;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.file.CsvFileIO;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

/**
 * @author zhou_c@centit.com
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
    public DataSet load(Map<String, Object> params, DataOptContext dataOptContext) throws Exception {
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
        CSVParser csvParser = CSVFormat.EXCEL.parse(reader);
        List<CSVRecord> records = csvParser.getRecords();
        CSVRecord record  = records.get(0);
        List<String> headers = new ArrayList<>(record.size());
        Iterator<String> iterator = record.iterator();
        while (iterator.hasNext()){
            headers.add(iterator.next());
        }
        return CollectionsOpt.listToArray(headers);
    }

    private List<Map<String, Object>> readCsvFile(Map<String, Object> params) throws IOException {
        boolean firstRowAsHeader = params == null ||
            BooleanBaseOpt.castObjectToBoolean(params.get("firstRowAsHeader"), true);

        return CsvFileIO.readDataFromInputStream(inputStream, firstRowAsHeader, loadColumnNames(params),  getCharset(params));
    }

    private static void saveCsv2OutStream(DataSet dataSet, OutputStream outs, Map<String, Object> params) throws IOException {
        // firstRowAsHeader 如果是true则将所有的key作为第一行，后面按照key的顺序对齐
        // 如果firstRowAsHeader为false，则必须要指定每一列的key，只保存对应的key列，多余的列不保存
        boolean firstRowAsHeader = params == null ||
            BooleanBaseOpt.castObjectToBoolean(params.get("firstRowAsHeader"), true);
        CsvFileIO.saveData2OutputStream(dataSet.getDataAsList(), outs, firstRowAsHeader, loadColumnNames(params), getCharset(params));
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
