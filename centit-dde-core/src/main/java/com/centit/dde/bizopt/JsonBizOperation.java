package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataSet;
import com.centit.dde.core.SimpleDataSet;
import com.centit.dde.utils.DataSetOptUtil;
import com.centit.framework.common.ResponseData;
import com.centit.support.file.FileIOOpt;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * @author zhf
 */
public class JsonBizOperation implements BizOperation {

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson) throws IOException {
        String sourDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        String targetDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "id", sourDsName);
        String jsonexpression=BuiltInOperation.getJsonFieldString(bizOptJson,"jsonexpression",null);
        DataSet dataSet = bizModel.fetchDataSetByName(sourDsName);

        List<InputStream> requestFileInfo = DataSetOptUtil.getRequestFileInfo(bizModel);
        if (dataSet==null&& requestFileInfo==null){
            return BuiltInOperation.getResponseData(0, 500, bizOptJson.getString("SetsName")
                +"：读取JSON文件异常,请指定数据集或者指定对应的流信息！");
        }
        List<InputStream> inputStreams;
        if (StringUtils.isNotBlank(jsonexpression)){
            inputStreams = DataSetOptUtil.getInputStreamByFieldName(jsonexpression,dataSet);
        }else if(requestFileInfo != null){
            //
            inputStreams = requestFileInfo;
        }else  {
            // 这个是什么情况啊
            inputStreams = DataSetOptUtil.getInputStreamByFieldName(dataSet);
        }
        if (inputStreams.size()==0){
            return BuiltInOperation.getResponseData(0, 500, bizOptJson.getString("SetsName")
                +"：读取JSON文件异常，不支持的流类型转换！");
        }
        bizModel.putDataSet(targetDsName, new SimpleDataSet(toJson(inputStreams)));
        return BuiltInOperation.getResponseSuccessData(bizModel.getDataSet(targetDsName).getSize());
    }

    //将流转出json文件
    private List<Object> toJson( List<InputStream> inputStreams) throws IOException {
        List<Object> jsonDatas = new ArrayList<>();
        for (InputStream inputStream : inputStreams) {
            jsonDatas.add(JSON.parse(FileIOOpt.readStringFromInputStream(inputStream )));
        }
        return  jsonDatas;
    }
}
