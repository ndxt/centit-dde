package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.SimpleDataSet;
import com.centit.dde.dataset.JSONDataSet;
import com.centit.fileserver.common.FileStore;

import java.io.IOException;


/**
 * @author zhf
 */
public class JsonBizOperation implements BizOperation {


    private FileStore fileStore;

    public JsonBizOperation(FileStore fileStore) {
        this.fileStore = fileStore;
    }

    @Override
    public JSONObject runOpt(BizModel bizModel, JSONObject bizOptJson) {
        String sourDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "nodeName", bizModel.getModelName());
        String filePath = BuiltInOperation.getJsonFieldString(bizOptJson, "sql", "");
        JSONDataSet jsonDataSet = new JSONDataSet();
        try {
            jsonDataSet.setFilePath(
                fileStore.getFile(filePath).getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        SimpleDataSet dataSet = jsonDataSet.load(null);
        bizModel.putDataSet(sourDsName, dataSet);
        return BuiltInOperation.getJsonObject(dataSet.size());
    }
}
