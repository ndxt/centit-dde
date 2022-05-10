package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.utils.DataSetOptUtil;
import com.centit.framework.common.ResponseData;
import com.centit.support.file.FileIOOpt;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;


/**
 * @author zhf
 */
public class JsonBizOperation implements BizOperation {

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws IOException {
        String sourDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        String targetDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "id", sourDsName);


        DataSet dataSet = bizModel.fetchDataSetByName(sourDsName);

        Map<String, Object> fileInfo = DataSetOptUtil.getFileFormDataset(dataSet, bizOptJson);

        InputStream inputStream = DataSetOptUtil.getInputStreamFormFile(fileInfo);
        if(inputStream !=null) {
            DataSet simpleDataSet =  new DataSet(JSON.parse(FileIOOpt.readStringFromInputStream(inputStream )));
            bizModel.putDataSet(targetDsName, simpleDataSet);
            return BuiltInOperation.createResponseSuccessData(simpleDataSet.getSize());
        }else {
            return BuiltInOperation.createResponseData(0,1,ResponseData.ERROR_OPERATION,bizOptJson.getString("SetsName")
                +"：读取JSON文件异常，不支持的流类型转换！");
        }
    }

}
