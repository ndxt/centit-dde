package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataSet;
import com.centit.dde.core.SimpleDataSet;
import com.centit.dde.dataset.ExcelDataSet;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.dde.utils.DataSetOptUtil;
import com.centit.framework.common.ResponseData;
import com.centit.support.json.JSONTransformer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author zhf
 */
public class ExcelBizOperation implements BizOperation {

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson) throws Exception {
        String id =bizOptJson.getString("id");
        String source = bizOptJson.getString("source");
        //文件流  还是  字段（保存文件的字段）
        //String sourceType = bizOptJson.getString("sourceType");
        Map<String, Object> params = new HashMap<>();
        String sheetName = bizOptJson.getString("sheetName")==null?"": bizOptJson.getString("sheetName");
        String headerRow = bizOptJson.getString("headerRow")==null?"0":bizOptJson.getString("headerRow");
        String beginRow = bizOptJson.getString("beginRow")==null?"0":bizOptJson.getString("beginRow");
        String endRow = bizOptJson.getString("endRow")==null?"0":bizOptJson.getString("endRow");
        String startColumnNumber = bizOptJson.getString("startColumnNumber")==null?"0":bizOptJson.getString("startColumnNumber");
        String endColumnNumber = bizOptJson.getInteger("endColumnNumber")==null?"0":bizOptJson.getString("endColumnNumber");
        params.put("sheetName",JSONTransformer.transformer(sheetName, new BizModelJSONTransform(bizModel)));
        params.put("headerRow",JSONTransformer.transformer(headerRow, new BizModelJSONTransform(bizModel)));
        params.put("beginRow",JSONTransformer.transformer(beginRow, new BizModelJSONTransform(bizModel)));
        params.put("endRow",JSONTransformer.transformer(endRow, new BizModelJSONTransform(bizModel)));
        params.put("startColumnNumber",JSONTransformer.transformer(startColumnNumber, new BizModelJSONTransform(bizModel)));
        params.put("endColumnNumber",JSONTransformer.transformer(endColumnNumber, new BizModelJSONTransform(bizModel)));

        List<InputStream> requestFileInfo = DataSetOptUtil.getRequestFileInfo(bizModel);
        DataSet dataSet = bizModel.fetchDataSetByName(source);
        //挂载文件的字段
        String excelExpression=BuiltInOperation.getJsonFieldString(bizOptJson,"excelexpression",null);
        if (dataSet==null && requestFileInfo==null){
            return BuiltInOperation.getResponseData(0, 500,
                bizOptJson.getString("SetsName")+"：读取EXCEL文件异常，请指定数据集或者指定对应的流信息！");
        }
        List<InputStream> inputStreams;
        if (requestFileInfo !=null){
            inputStreams=requestFileInfo;
        }else if (StringUtils.isNotBlank(excelExpression)){
            inputStreams  = DataSetOptUtil.getInputStreamByFieldName(excelExpression,dataSet);
        }else {
            inputStreams = DataSetOptUtil.getInputStreamByFieldName(dataSet);
        }
        if (inputStreams.size()==0){
            return BuiltInOperation.getResponseData(0, 500,
                bizOptJson.getString("SetsName")+"：读取EXCEL文件异常，不支持的流类型转换！");
        }
        List<Object> objectList = new ArrayList<>();
        for (InputStream inputStream : inputStreams) {
            ExcelDataSet excelDataSet = new ExcelDataSet();
            excelDataSet.setInputStream(inputStream);
            SimpleDataSet  simpleDataSet= excelDataSet.load(params);
            objectList.add(simpleDataSet.getData());
        }
        bizModel.putDataSet(id,new SimpleDataSet(objectList));
        return BuiltInOperation.getResponseSuccessData(objectList.size());
    }
}
