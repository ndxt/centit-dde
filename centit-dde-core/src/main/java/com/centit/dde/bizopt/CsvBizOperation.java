package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.dataset.CsvDataSet;
import com.centit.dde.utils.DataSetOptUtil;
import com.centit.framework.common.ResponseData;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * @author zhf
 */
public class CsvBizOperation implements BizOperation {

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        String sourDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        String targetDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "id", sourDsName);
        String csvExpressions = BuiltInOperation.getJsonFieldString(bizOptJson,"csvexpressions",null);
        DataSet dataSet = bizModel.fetchDataSetByName(sourDsName);
        List<InputStream> requestFileInfo = DataSetOptUtil.getRequestFileInfo(bizModel);
        if(dataSet ==null && requestFileInfo==null){
            return BuiltInOperation.createResponseData(0, 1,
                ResponseData.ERROR_USER_CONFIG ,bizOptJson.getString("SetsName")+"：读取CSV文件异常，请指定数据集或者指定对应的流信息！");
        }
        List<Object> objectList = new ArrayList<>();
        List<InputStream> inputStreams;
        if (StringUtils.isNotBlank(csvExpressions)){
            inputStreams = DataSetOptUtil.getInputStreamByFieldName(csvExpressions,dataSet);
        }else  if (requestFileInfo!=null){
            inputStreams=requestFileInfo;
        }
        else {
            inputStreams = DataSetOptUtil.getInputStreamByFieldName(dataSet);
        }
        if (inputStreams.size()==0){
            return BuiltInOperation.createResponseData(0,1, ResponseData.ERROR_OPERATION, bizOptJson.getString("SetsName")+"：读取CSV文件异常，不支持的流类型转换！");
        }
        for (InputStream inputStream : inputStreams) {
            CsvDataSet csvDataSet = new CsvDataSet();
            csvDataSet.setInputStream(inputStream);
            DataSet simpleDataSet= csvDataSet.load(null);
            objectList.add(simpleDataSet.getData());
        }
        DataSet simpleDataSet = new DataSet(objectList);
        bizModel.putDataSet(targetDsName,simpleDataSet);
        return BuiltInOperation.createResponseSuccessData(simpleDataSet.getSize());
    }
}
