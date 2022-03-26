package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataSet;
import com.centit.dde.core.SimpleDataSet;
import com.centit.dde.dataset.ExcelDataSet;
import com.centit.dde.utils.DataSetOptUtil;
import com.centit.framework.common.ResponseData;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * @author zhf
 */
public class ExcelBizOperation implements BizOperation {

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson) throws Exception {
        String id =bizOptJson.getString("id");
        String source = bizOptJson.getString("source");
        //文件流  还是  字段（保存文件的字段）
        String sourceType = bizOptJson.getString("sourceType");
        DataSet dataSet = bizModel.fetchDataSetByName(source);
        List<InputStream> requestFileInfo = DataSetOptUtil.getRequestFileInfo(bizModel);
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
            SimpleDataSet  simpleDataSet= excelDataSet.load(null);
            objectList.add(simpleDataSet.getData());
        }
        bizModel.putDataSet(id,new SimpleDataSet(objectList));
        return BuiltInOperation.getResponseSuccessData(objectList.size());
    }
}
