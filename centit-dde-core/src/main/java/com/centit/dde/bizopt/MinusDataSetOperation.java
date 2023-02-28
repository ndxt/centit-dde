package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.utils.DataSetOptUtil;
import com.centit.framework.common.ResponseData;

import java.util.ArrayList;
import java.util.Map;

/**
 * 差集
 */
public class MinusDataSetOperation implements BizOperation {

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        String id = bizOptJson.getString("id");
        String mainDataSet = bizOptJson.getString("mainDataSet");
        String slaveDataSet = bizOptJson.getString("slaveDataSet");
        Map<String, String> pks = BuiltInOperation.jsonArrayToMap(bizOptJson.getJSONArray("primaryFields"), "mainField", "slaveField");
        DataSet dataSet = DataSetOptUtil.minusTwoDataSet(bizModel.getDataSet(mainDataSet), bizModel.getDataSet(slaveDataSet), new ArrayList<>(pks.entrySet()));
        bizModel.putDataSet(id,dataSet);
        return BuiltInOperation.createResponseSuccessData(dataSet==null?0:dataSet.getSize());
    }
}
