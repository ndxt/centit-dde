package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataSet;
import com.centit.dde.core.SimpleDataSet;
import com.centit.dde.utils.DataSetOptUtil;
import com.centit.fileserver.client.po.FileInfo;
import com.centit.fileserver.common.FileStore;
import com.centit.framework.common.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        String sourDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        String targetDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "id", sourDsName);
        String fileNames=BuiltInOperation.getJsonFieldString(bizOptJson,"fileName",null);
        String fileupexpression=BuiltInOperation.getJsonFieldString(bizOptJson,"fileupexpression",null);

        DataSet dataSet = bizModel.fetchDataSetByName(sourDsName);

        Map<String, String> mapInfo = new HashMap<>();
        mapInfo.put(fileNames, fileNames);
        mapInfo.put(fileupexpression, fileupexpression);

        DataSet destDs = DataSetOptUtil.mapDateSetByFormula(dataSet, mapInfo.entrySet());
        List<String> fileIds = new ArrayList<>();
        for (Map<String, Object> objectMap : destDs.getDataAsList()) {
            String fileId="";
            FileInfo fileInfo = new FileInfo();
            String fileName = String.valueOf(objectMap.get(fileNames));
            fileInfo.setFileName(fileName);
            Object object = objectMap.get(fileupexpression);
            InputStream inputStream;
            if (object instanceof byte[]){
                inputStream = new ByteArrayInputStream((byte[])object);
            }else if (object instanceof InputStream){
                inputStream=(InputStream)object;
            }else {
                return  BuiltInOperation.getResponseData(0, 500, bizOptJson.getString("SetsName")+"：上传文件失败，不支持的流类型转换！");
            }
            fileId = fileStore.saveFile(inputStream, fileInfo, 0);
            fileIds.add(fileId);
        }
        bizModel.putDataSet(targetDsName,new SimpleDataSet(fileIds));
        return BuiltInOperation.getResponseSuccessData(dataSet.getSize());
    }
}
