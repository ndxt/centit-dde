package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataSet;
import com.centit.dde.core.SimpleDataSet;
import com.centit.dde.utils.DataSetOptUtil;
import com.centit.framework.common.ResponseData;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * @author zhf
 */
public class JsonBizOperation implements BizOperation {

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson) {
        String sourDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        String targetDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "id", sourDsName);
        String jsonexpression=BuiltInOperation.getJsonFieldString(bizOptJson,"jsonexpression",null);
        DataSet dataSet = bizModel.fetchDataSetByName(sourDsName);
        if (StringUtils.isNotBlank(jsonexpression)){
            List<InputStream> inputStreams = DataSetOptUtil.getInputStreamByFieldName(jsonexpression,dataSet);
            if (inputStreams.size()==0){
                return BuiltInOperation.getResponseData(0, 500, bizOptJson.getString("SetsName")+"：读取JSON文件异常，不支持的流类型转换！");
            }
            try {
                List<String> jsonData = toJson(inputStreams);
                bizModel.putDataSet(targetDsName, new SimpleDataSet(jsonData));
            } catch (IOException e) {
                return BuiltInOperation.getResponseData(0, 500, bizOptJson.getString("SetsName")+"：读取JSON文件异常，异常信息："+e.getMessage());
            }
        }else {
            return BuiltInOperation.getResponseData(0, 500, bizOptJson.getString("SetsName")+"：读取JSON文件异常，未指定参数");
        }
        return BuiltInOperation.getResponseSuccessData(1);
    }

    //将流转出json文件
    private List<String>  toJson( List<InputStream> inputStreams) throws IOException {
        List<String> jsonDatas = new ArrayList<>();
        for (InputStream inputStream : inputStreams) {
            StringBuilder stringBuilder = new StringBuilder();
            try(BufferedInputStream bis = new BufferedInputStream(inputStream);) {
                byte[] bys = new byte[1024];
                int len = 0;
                while ((len = bis.read(bys)) != -1) {
                    stringBuilder.append(new String(bys,0,len));
                }
            }
            jsonDatas.add(stringBuilder.toString());
        }
        return  jsonDatas;
    }
}
