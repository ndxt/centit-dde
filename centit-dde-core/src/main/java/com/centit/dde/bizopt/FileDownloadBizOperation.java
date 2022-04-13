package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataSet;
import com.centit.dde.utils.BizOptUtils;
import com.centit.fileserver.common.FileStore;
import com.centit.framework.common.ResponseData;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.StringBaseOpt;
import org.apache.commons.lang3.StringUtils;

import java.io.FileInputStream;
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
        String fileIds=BuiltInOperation.getJsonFieldString(bizOptJson,"fileId",null);
        List<InputStream> inputStreams = new ArrayList<>();
        if (StringUtils.isNotBlank(fileIds)){//直接填写文件id的请求，直接下载文件
            String[] split = fileIds.split(",");
            for (String fileId : split) {
                InputStream inputStream = new FileInputStream(fileStore.getFile(fileId));
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
                    InputStream inputStream =new FileInputStream(fileStore.getFile(StringBaseOpt.castObjectToString(fileId)));
                    inputStreams.add(inputStream);
                }
            }
        }
        DataSet objectToDataSet = BizOptUtils.castObjectToDataSet(CollectionsOpt.createHashMap("fileName", "",
            "fileSize", inputStreams.get(0).available(), "fileContent", inputStreams.get(0)));
        bizModel.putDataSet(targetDsName,objectToDataSet);
        return BuiltInOperation.getResponseSuccessData(bizModel.getDataSet(targetDsName).getSize());
    }
}
