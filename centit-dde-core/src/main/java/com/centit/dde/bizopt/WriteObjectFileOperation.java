package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.dataset.FileDataSet;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.framework.common.ResponseData;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.file.FileType;
import com.centit.support.json.JSONTransformer;
import com.centit.support.xml.XMLObject;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * 生成json文件节点信息
 */
public class WriteObjectFileOperation implements BizOperation {

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws IOException {
        String sourDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        String targetDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "id", sourDsName);

        String fileType = bizOptJson.getString("fileType");
        if(StringUtils.isBlank(fileType)){
            fileType = "json";
        }
        Object data = bizModel.getDataSet(sourDsName).getData();
        String object;
        if("xml".equalsIgnoreCase(fileType)) {
            object = JSON.toJSONString(data);
        }else {
            String rootName = bizOptJson.getString("rootName");
            if(StringUtils.isBlank(rootName)){
                rootName = "object";
            }
            object = XMLObject.objectToXMLString(rootName, data);
        }
        String fileName = null;
        if(StringUtils.isNotBlank(bizOptJson.getString("fileName"))) {
            fileName = StringBaseOpt.castObjectToString(JSONTransformer.transformer(
                bizOptJson.getString("fileName"), new BizModelJSONTransform(bizModel, data))) ;
        }
        if(StringUtils.isBlank(fileName)) {
            fileName = DatetimeOpt.currentTimeWithSecond();
        }
        if(!fileName.endsWith("."+fileType)){
            fileName = FileType.truncateFileExtName(fileName)+"."+fileType;
        }

        ByteArrayInputStream inputStream = new ByteArrayInputStream(object.getBytes());
        FileDataSet objectToDataSet =new FileDataSet(fileName,
            inputStream.available(), inputStream);

        bizModel.putDataSet(targetDsName,objectToDataSet);
        return BuiltInOperation.createResponseSuccessData(objectToDataSet.getSize());
    }
}
