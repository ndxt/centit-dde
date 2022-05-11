package com.centit.dde.dataset;

import com.centit.dde.core.DataSet;
import com.centit.support.algorithm.BooleanBaseOpt;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import lombok.Data;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

/**
 * @author zhf
 */
@Data
public class CsvDataSet extends FileDataSet {
    private static String DEFAULT_CHARSET = "gbk";// "utf-8";
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

    private static String getCharset(Map<String, Object> params){
        if(params == null){
            return DEFAULT_CHARSET;
        }
        String charSet = StringBaseOpt.castObjectToString(params.get("charsetType"));
        if(StringUtils.isBlank(charSet)){
            return DEFAULT_CHARSET;
        }
        return charSet;
    }
    @Override
    public DataSet load(Map<String, Object> params) throws Exception {

        List<Map<String, Object>> list = readCsvFile(params);
        DataSet dataSet = new DataSet();
        dataSet.setData(list);
        return dataSet;
    }

    private List<Map<String, Object>> readCsvFile(Map<String, Object> params) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,
            Charset.forName(getCharset(params))), 8192)) {

            CsvReader csvReader = new CsvReader(reader);
            csvReader.setDelimiter(',');
            csvReader.setSafetySwitch(false);

            List<String> headers = null;// = new ArrayList<>()
            if (params == null || BooleanBaseOpt.castObjectToBoolean(params.get("firstRowAsHeader"), true)) {
                if (csvReader.readRecord()) {
                    String[] splitHead = csvReader.getValues();
                    headers = CollectionsOpt.arrayToList(splitHead);
                }
            } else {
                headers = StringBaseOpt.objectToStringList(params.get("headers"));
            }

            if (headers == null) {
                headers = new ArrayList<>();
            }
            int headLen = headers.size();

            while (csvReader.readRecord()) {
                Map<String, Object> map = new HashMap<>();
                String[] splitResult = csvReader.getValues();
                for (int i = 0; i < splitResult.length; i++) {
                    String columnName = i < headLen ? headers.get(i) : "column" + i;
                    map.put(columnName, splitResult[i]);
                }
                list.add(map);
            }
            csvReader.close();
            return list;
        }
    }

    /**
     * 将 dataSet 数据集 持久化
     *
     * @param dataSet 数据集
     */
    @Override
    public void save(DataSet dataSet) {
        try (OutputStream outputStream = new FileOutputStream(filePath)){
            saveCsv2OutStream(dataSet, outputStream, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveCsv2OutStream(DataSet dataSet, OutputStream outs, Map<String, Object> params) throws IOException {
        try (
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outs, Charset.forName(getCharset(params))))) {

            CsvWriter csvWriter = new CsvWriter(writer, ',');
            csvWriter.setTextQualifier('"');
            csvWriter.setUseTextQualifier(true);
            csvWriter.setRecordDelimiter(IOUtils.LINE_SEPARATOR.charAt(0));
            List<Map<String, Object>> list = dataSet.getDataAsList();

            boolean firstRowAsHeader = params == null ||
                BooleanBaseOpt.castObjectToBoolean(params.get("firstRowAsHeader"), true);
            Set<String> headers = new HashSet<>(20);
            for(Map<String, Object> row : list){
                headers.addAll(row.keySet());
            }
            List<String> columnNames = CollectionsOpt.cloneList(headers);
            if(firstRowAsHeader){
                csvWriter.writeRecord(CollectionsOpt.listToArray(columnNames));
            }

            String [] values = new String[columnNames.size()];
            for (Map<String, Object> row : list) {
                for(int i=0; i<columnNames.size(); i++){
                    values[i] = StringBaseOpt.castObjectToString(row.get(columnNames.get(i)),"");
                }
                csvWriter.writeRecord(values);
            }
            csvWriter.flush();
            csvWriter.close();
        }
    }

    public static InputStream createCsvStream(DataSet dataSet, Map<String, Object> params) throws IOException {
        try (
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()
        ) {
            saveCsv2OutStream(dataSet, byteArrayOutputStream, params);
            return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
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

}
