package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.SimpleDataSet;
import com.centit.dde.dataset.CsvDataSet;
import com.centit.dde.dataset.ExcelDataSet;
import com.centit.dde.dataset.HttpDataSet;
import com.centit.dde.dataset.JSONDataSet;
import com.centit.fileserver.common.FileStore;

import java.io.IOException;


/**
 * @author zhf
 */
public class HttpBizOperation implements BizOperation {

    public HttpBizOperation() {
    }

    @Override
    public JSONObject runOpt(BizModel bizModel, JSONObject bizOptJson) {
        String sourDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "nodeName", bizModel.getModelName());
        String http = BuiltInOperation.getJsonFieldString(bizOptJson, "sql", "");
        SimpleDataSet dataSet = SimpleDataSet.createSingleObjectSet(http);
        bizModel.putDataSet(sourDsName, dataSet);
        return BuiltInOperation.getJsonObject(dataSet.getRowCount());
    }
}
