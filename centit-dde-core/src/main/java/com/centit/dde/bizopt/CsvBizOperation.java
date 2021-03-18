package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.SimpleDataSet;
import com.centit.dde.dataset.CsvDataSet;
import com.centit.fileserver.common.FileStore;
import com.centit.framework.common.ResponseData;
import org.apache.commons.lang3.tuple.Pair;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;


/**
 * @author zhf
 */
public class CsvBizOperation implements BizOperation {
    private FileStore fileStore;

    public CsvBizOperation(FileStore fileStore) {
        this.fileStore = fileStore;
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson) {
        String sourDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "id", bizModel.getModelName());
        String filePath=null;
        if(bizOptJson.getJSONArray("upjson").size()>0) {
            filePath = bizOptJson.getJSONArray("upjson").getJSONObject(0).getString("fileId");
        }
        CsvDataSet csvDataSet = new CsvDataSet();
        try {
            if(filePath!=null) {
                csvDataSet.setFilePath(
                    fileStore.getFile(filePath).getPath());
            }else {
                csvDataSet.setInputStream(new ByteArrayInputStream(bizModel.getDataSet("requestFile").getData().toString().getBytes(Charset.forName("gbk"))));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        SimpleDataSet dataSet = csvDataSet.load(null);
        bizModel.putDataSet(sourDsName, dataSet);
        return BuiltInOperation.getResponseSuccessData(dataSet.size());
    }
}
