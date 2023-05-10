package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.framework.common.ResponseData;
import com.centit.support.algorithm.CollectionsOpt;

import java.util.List;
import java.util.Map;

public class ClearDataOperation implements BizOperation {

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        //赋值目标
        String optType = BuiltInOperation.getJsonFieldString(bizOptJson, "optType", "dataSet");
        if("property".equals(optType)){
            //数据来源
            String sourceDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "source", null);
            List<String> propertyNames = CollectionsOpt.mapCollectionToList(bizOptJson.getJSONArray("config"),
                (a) -> ((JSONObject) a).getString("propertyName"), true);
            DataSet dataSet = bizModel.getDataSet(sourceDsName);
            int dataSize = dataSet.getSize();
            if (propertyNames != null && propertyNames.size() > 0) {
                if (dataSize == 1) {
                    Map<String, Object> objectMap = dataSet.getFirstRow();
                    for(String pn : propertyNames){
                        objectMap.remove(pn);
                    }
                } else if (dataSize > 1) {
                    for(Map<String, Object> objectMap  : dataSet.getDataAsList()){
                        for(String pn : propertyNames){
                            objectMap.remove(pn);
                        }
                    }
                }
            }
            return BuiltInOperation.createResponseSuccessData(dataSize);
        } else {
            List<String> sets = CollectionsOpt.mapCollectionToList(bizOptJson.getJSONArray("config"),
                (a) -> ((JSONObject) a).getString("dataSet"), true);

            if (sets == null || sets.size() == 0) {
                bizModel.clearBizData();
            } else {
                for (String s : sets) {
                    bizModel.putDataSet(s, null);
                }
            }
            return BuiltInOperation.createResponseSuccessData(0);
        }
    }
}
