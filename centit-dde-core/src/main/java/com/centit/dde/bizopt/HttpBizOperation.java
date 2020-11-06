package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.SimpleDataSet;


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
        SimpleDataSet dataSet = new SimpleDataSet(http);
        bizModel.putDataSet(sourDsName, dataSet);
        return BuiltInOperation.getJsonObject(dataSet.size());
    }
}
