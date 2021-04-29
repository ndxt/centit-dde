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
import java.io.InputStream;
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
        String flieIds=BuiltInOperation.getJsonFieldString(bizOptJson,"flieId",null);
        DataSet dataSet = bizModel.fetchDataSetByName(sourDsName);

        Map<String, String> mapInfo = new HashMap<>();
        mapInfo.put(flieIds, flieIds);

        List<InputStream> inputStreams = new ArrayList<>();
        if (dataSet!=null){
            DataSet destDs = DataSetOptUtil.mapDateSetByFormula(dataSet, mapInfo.entrySet());
            List<Map<String, Object>> dataAsList = destDs.getDataAsList();
            for (Map<String, Object> objectMap : dataAsList) {
                String fileId = String.valueOf(objectMap.get(flieIds));
                FileInfo fileInfo = fileClient.getFileInfo(fileId);
                if (fileInfo==null){
                    return BuiltInOperation.getResponseData(0, 0, "文件不存在！" );
                }
                InputStream inputStream = fileStore.loadFileStream(fileId);
                inputStreams.add(inputStream);
            }
        }else {
            FileInfo fileInfo = fileClient.getFileInfo(flieIds);
            if (fileInfo==null){
                return BuiltInOperation.getResponseData(0, 0, "文件不存在！" );
            }
            InputStream inputStream = fileStore.loadFileStream(flieIds);
            inputStreams.add(inputStream);
        }
        bizModel.putDataSet(targetDsName,new SimpleDataSet(inputStreams));
        return BuiltInOperation.getResponseSuccessData(dataSet.getSize());
    }
}
