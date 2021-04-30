package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataSet;
import com.centit.dde.core.SimpleDataSet;
import com.centit.dde.utils.DataSetOptUtil;
import com.centit.fileserver.client.FileClient;
import com.centit.fileserver.client.po.FileInfo;
import com.centit.fileserver.common.FileStore;
import com.centit.framework.common.ResponseData;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件下载节点
 */
@Component
public class FileDownloadBizOperation implements BizOperation {

    @Resource
    FileStore fileStore;

    @Resource
    FileClient fileClient;

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson) throws Exception {
        String sourDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        String targetDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "id", sourDsName);
        String fileIds=BuiltInOperation.getJsonFieldString(bizOptJson,"flieId",null);
        DataSet dataSet = bizModel.fetchDataSetByName(sourDsName);
        List<InputStream> inputStreams = new ArrayList<>();
        if (dataSet!=null){
            Map<String, String> mapInfo = new HashMap<>();
            mapInfo.put(fileIds, fileIds);
            DataSet destDs = DataSetOptUtil.mapDateSetByFormula(dataSet, mapInfo.entrySet());
            List<Map<String, Object>> dataAsList = destDs.getDataAsList();
            for (Map<String, Object> objectMap : dataAsList) {
                String fileId = String.valueOf(objectMap.get(fileIds));
                InputStream inputStream = fileStore.loadFileStream(fileId);
                inputStreams.add(inputStream);
            }
        }else {
            InputStream inputStream = fileStore.loadFileStream(fileIds);
            inputStreams.add(inputStream);
        }
        bizModel.putDataSet(targetDsName,new SimpleDataSet(inputStreams));
        return BuiltInOperation.getResponseSuccessData(dataSet.getSize());
    }
}
