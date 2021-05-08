package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataSet;
import com.centit.dde.core.SimpleDataSet;
import com.centit.fileserver.common.FileStore;
import com.centit.framework.common.ResponseData;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 文件下载节点
 */
public class FileDownloadBizOperation implements BizOperation {

    FileStore fileStore;

    public FileDownloadBizOperation(FileStore fileStore) {
        this.fileStore = fileStore;
    }

    public FileDownloadBizOperation() {
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson) throws Exception {
        String sourDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        String targetDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "id", sourDsName);
        //支持多个逗号隔开
        String fileIds=BuiltInOperation.getJsonFieldString(bizOptJson,"flieId",null);
        List<InputStream> inputStreams = new ArrayList<>();
        if (StringUtils.isNotBlank(fileIds)){//直接填写文件id的请求，直接下载文件
            String[] split = fileIds.split(",");
            for (String fileId : split) {
                InputStream inputStream = fileStore.loadFileStream(fileId);
                inputStreams.add(inputStream);
            }
        }else {
            //文件id存放在数据集中的情况
            DataSet dataSet = bizModel.fetchDataSetByName(sourDsName);
            if (dataSet==null){
                return BuiltInOperation.getResponseData(0, 500,
                    bizOptJson.getString("SetsName")+"：文件下载失败，请选择数据集！");
            }
            List<Map<String, Object>> dataSetDataAsList = dataSet.getDataAsList();
            for (Map<String, Object> objectMap : dataSetDataAsList) {
                for (Object fileId : objectMap.values()) {
                    InputStream inputStream = fileStore.loadFileStream(String.valueOf(fileId));
                    inputStreams.add(inputStream);
                }
            }
        }
        bizModel.putDataSet(targetDsName,new SimpleDataSet(inputStreams));
        return BuiltInOperation.getResponseSuccessData(bizModel.getDataSet(targetDsName).getSize());
    }
}
