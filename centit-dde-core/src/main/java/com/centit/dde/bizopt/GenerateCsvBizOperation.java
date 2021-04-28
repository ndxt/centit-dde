package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataSet;
import com.centit.dde.core.SimpleDataSet;
import com.centit.dde.utils.DataSetOptUtil;
import com.centit.framework.common.ResponseData;
import com.csvreader.CsvWriter;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 生成json文件节点信息
 */
@Service
public class GenerateCsvBizOperation implements BizOperation {

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson) throws Exception {
        String id = bizOptJson.getString("id");
        String source = bizOptJson.getString("source");
        //获取表达式信息
        Map<String, String> mapInfo = BuiltInOperation.jsonArrayToMap(bizOptJson.getJSONArray("config"), "columnName", "expression");
        if (mapInfo != null  && mapInfo.size()>0) {
            DataSet  dataSet = bizModel.fetchDataSetByName(source);
            if (dataSet != null) {
                DataSet destDs = DataSetOptUtil.mapDateSetByFormula(dataSet, mapInfo.entrySet());
                bizModel.putDataSet(id, destDs);
            }
        }
        DataSet thisDataSet = bizModel.getDataSet(id);
        DataSet dataSet;
        if (thisDataSet!=null){
            dataSet = bizModel.getDataSet(id);
        }else {
            dataSet = bizModel.getDataSet(source);
        }
        try(
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(byteArrayOutputStream, Charset.forName("gbk")));
        ) {
            CsvWriter csvWriter = new CsvWriter(writer, ',');
            csvWriter.setTextQualifier('"');
            csvWriter.setUseTextQualifier(true);
            csvWriter.setRecordDelimiter(IOUtils.LINE_SEPARATOR.charAt(0));
            List<Map<String, Object>> list=dataSet.getDataAsList();
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
            bizModel.putDataSet(id,new SimpleDataSet(byteArrayOutputStream));
        } catch (FileNotFoundException e) {
            BuiltInOperation.getResponseData(0, 500, bizOptJson.getString("")+"：创建csv文件异常，异常信息"+e.getMessage());
        }
        return BuiltInOperation.getResponseSuccessData(dataSet.getSize());
    }
}
