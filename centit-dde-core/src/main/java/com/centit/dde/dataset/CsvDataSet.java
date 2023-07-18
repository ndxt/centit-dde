package com.centit.dde.dataset;

import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.adapter.utils.ConstantValue;
import com.centit.dde.core.DataSet;
import com.centit.dde.core.DataSetReader;
import com.centit.dde.core.DataSetWriter;
import com.centit.framework.common.ResponseData;
import com.centit.support.algorithm.BooleanBaseOpt;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.common.ObjectException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
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
        List<Map<String, Object>> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,
            Charset.forName(getCharset(params))), 8192)) {
            // firstRowAsHeader 如果是true则以第一行为key生成一个map，如果第一行不够长，后面的key自动为 column+i
            // 如果firstRowAsHeader为false，则必须要指定每一列的key，不够长同样自动为column+i
            boolean firstRowAsHeader = params == null ||
                BooleanBaseOpt.castObjectToBoolean(params.get("firstRowAsHeader"), true);
            CSVFormat csvFormat = CSVFormat.EXCEL;
            CSVParser csvParser = csvFormat.parse(reader);
            List<CSVRecord> recordList = csvParser.getRecords();
            List<String> headers = null;
            if (firstRowAsHeader) {
                if (recordList.size()>0) {
                    CSVRecord record = recordList.get(0);
                    headers = new ArrayList<>(record.size());
                    Iterator<String> iterator = record.iterator();
                    while (iterator.hasNext()){
                        headers.add(iterator.next());
                    }
                }
            } else {
                headers = loadColumnNames(params);
            }
            csvFormat.withHeader(CollectionsOpt.listToArray(headers));
            int headLen = 0;
            if(headers!=null){
                headLen=headers.size();
            }
            for (int k = 1; k < recordList.size(); k++) {
                CSVRecord record = recordList.get(k);
                int splitResultLength = record.size();
                Map<String, Object> map = new HashMap<>(splitResultLength);
                for (int i = 0; i < splitResultLength; i++) {
                    String columnName = i < headLen ? headers.get(i) : "column" + i;
                    map.put(columnName, record.get(i));
                }
                list.add(map);
            }
            csvParser.close();
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
            CSVPrinter csvPrinter = CSVFormat.EXCEL.withHeader(CollectionsOpt.listToArray(columnNames)).print(writer);
            String[] values = new String[columnNames.size()];
            for (Map<String, Object> row : list) {
                for (int i = 0; i < columnNames.size(); i++) {
                    values[i] = StringBaseOpt.castObjectToString(row.get(columnNames.get(i)), "");
                }
                csvPrinter.printRecord(values);
            }
            csvPrinter.flush();
            csvPrinter.close();
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
