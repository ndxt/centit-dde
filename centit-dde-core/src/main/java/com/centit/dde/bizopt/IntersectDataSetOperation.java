package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.utils.DataSetOptUtil;
import com.centit.framework.common.ResponseData;
import com.centit.support.algorithm.BooleanBaseOpt;

import java.util.ArrayList;
import java.util.Map;

/**
 *求2个数据的交集
 */
public class IntersectDataSetOperation implements BizOperation {

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        String id = bizOptJson.getString("id");
        String mainDataSet = bizOptJson.getString("mainDataSet");
        String slaveDataSet = bizOptJson.getString("slaveDataSet");
        boolean unionData = BooleanBaseOpt.castObjectToBoolean(bizOptJson.get("unionData") ,false);

        Map<String, String> pks = BuiltInOperation.jsonArrayToMap(bizOptJson.getJSONArray("primaryFields"),
            "mainField", "slaveField");
        DataSet dataSet = DataSetOptUtil.intersectTwoDataSet(bizModel.getDataSet(mainDataSet),
            bizModel.getDataSet(slaveDataSet), new ArrayList<>(pks.entrySet()), unionData);
        bizModel.putDataSet(id,dataSet);
        return BuiltInOperation.createResponseSuccessData(dataSet==null?0:dataSet.getSize());
    }
}
