package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataSet;
import com.centit.dde.core.SimpleDataSet;
import com.centit.dde.utils.GetJsonFieldValueUtils;
import com.centit.fileserver.client.po.FileInfo;
import com.centit.fileserver.common.FileStore;
import com.centit.framework.common.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

/**
 * 文件上传节点
 *
 */
@Component
public class FileUploadBizOperation implements BizOperation {

    @Autowired(required = false)
    private FileStore fileStore;

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson) throws Exception {
        String id =bizOptJson.getString("id");
        String source = bizOptJson.getString("source");
        String fileName = bizOptJson.getString("flieName");
        String fileupexpression = bizOptJson.getString("fileupexpression");
        DataSet dataSet = bizModel.getDataSet(source);
        Object data = dataSet.getData();
        if (StringUtils.isNotBlank(fileupexpression)){
            data = GetJsonFieldValueUtils.getJsonFieldValue(fileupexpression,dataSet);
        }
        String fileId;
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFileName(fileName);
        if (data instanceof OutputStream){//如果是outputstream 就是从生成csv excel文件节点过来的
            ByteArrayOutputStream byteArrayOutputStream =(ByteArrayOutputStream)data;
            ByteArrayInputStream input = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            fileId = fileStore.saveFile(input, fileInfo, byteArrayOutputStream.size());
        }else {//直接将数据生成流，把流直接上传到文件服务器
            String dataStr = JSONObject.toJSONString(data);
            ByteArrayInputStream input = new ByteArrayInputStream(dataStr.getBytes());
            fileId = fileStore.saveFile(input, fileInfo, dataStr.getBytes().length);
        }
        bizModel.putDataSet(id,new SimpleDataSet(fileId));
        return BuiltInOperation.getResponseSuccessData(dataSet.getSize());
    }
}
