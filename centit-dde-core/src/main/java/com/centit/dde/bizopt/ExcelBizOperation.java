package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.*;
import com.centit.dde.dataset.ExcelDataSet;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.dde.utils.DataSetOptUtil;
import com.centit.framework.common.ResponseData;
import com.centit.support.compiler.Pretreatment;
import org.apache.commons.lang3.StringUtils;

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
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        String id =bizOptJson.getString("id");
        String source = bizOptJson.getString("source");
        Map<String, Object> params = new HashMap<>();
        String sheetName = StringUtils.isBlank(bizOptJson.getString("sheetName"))?"": bizOptJson.getString("sheetName");
        String headerRow =  StringUtils.isBlank(bizOptJson.getString("headerRow"))?"0":bizOptJson.getString("headerRow");
        String beginRow =  StringUtils.isBlank(bizOptJson.getString("beginRow"))?"0":bizOptJson.getString("beginRow");
        String endRow =  StringUtils.isBlank(bizOptJson.getString("endRow"))?"0":bizOptJson.getString("endRow");
        String startColumnNumber =  StringUtils.isBlank(bizOptJson.getString("startColumnNumber"))?"0":bizOptJson.getString("startColumnNumber");
        String endColumnNumber =  StringUtils.isBlank(bizOptJson.getString("endColumnNumber"))?"0":bizOptJson.getString("endColumnNumber");
        params.put("sheetName", Pretreatment.mapTemplateStringAsFormula(sheetName, new BizModelJSONTransform(bizModel)));
        params.put("headerRow",Pretreatment.mapTemplateStringAsFormula(headerRow, new BizModelJSONTransform(bizModel)));
        params.put("beginRow",Pretreatment.mapTemplateStringAsFormula(beginRow, new BizModelJSONTransform(bizModel)));
        params.put("endRow",Pretreatment.mapTemplateStringAsFormula(endRow, new BizModelJSONTransform(bizModel)));
        params.put("startColumnNumber",Pretreatment.mapTemplateStringAsFormula(startColumnNumber, new BizModelJSONTransform(bizModel)));
        params.put("endColumnNumber",Pretreatment.mapTemplateStringAsFormula(endColumnNumber, new BizModelJSONTransform(bizModel)));

        List<InputStream> requestFileInfo = DataSetOptUtil.getRequestFileInfo(bizModel);
        DataSet dataSet = bizModel.fetchDataSetByName(source);
        //挂载文件的字段
        String excelExpression=BuiltInOperation.getJsonFieldString(bizOptJson,"excelexpression",null);
        if (dataSet==null && requestFileInfo==null){
            return BuiltInOperation.createResponseData(0, 500,
                bizOptJson.getString("SetsName")+"：读取EXCEL文件异常，请指定数据集或者指定对应的流信息！");
        }
        List<InputStream> inputStreams;
        if (requestFileInfo != null){
            inputStreams = requestFileInfo;
        }else if (StringUtils.isNotBlank(excelExpression)){
            inputStreams  = DataSetOptUtil.getInputStreamByFieldName(excelExpression,dataSet);
        }else {
            inputStreams = DataSetOptUtil.getInputStreamByFieldName(dataSet);
        }
        if (inputStreams.size()==0){
            return BuiltInOperation.createResponseData(0, 500,
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
        return BuiltInOperation.createResponseSuccessData(objectList.size());
    }
}
