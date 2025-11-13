package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.utils.ConstantValue;
import com.centit.dde.utils.DataSetOptUtil;
import com.centit.framework.common.ResponseData;

import java.util.ArrayList;
import java.util.Map;

public class JoinDataOperation implements BizOperation {

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {

        String sour1DsName = BuiltInOperation.getJsonFieldString(bizOptJson, "source1", null);
        String sour2DsName = BuiltInOperation.getJsonFieldString(bizOptJson, "source2", null);
        String join = BuiltInOperation.getJsonFieldString(bizOptJson, "operation", "join");
        Map<String, String> map = BuiltInOperation.jsonArrayToMap(bizOptJson.getJSONArray("configfield"), "primaryKey1", "primaryKey2");
        if (map != null) {
            DataSet dataSet = bizModel.getDataSet(sour1DsName);
            DataSet dataSet2 = bizModel.getDataSet(sour2DsName);
            DataSet destDs;
            if(ConstantValue.DATASET_JOIN_TYPE_APPEND.equalsIgnoreCase(join)){
                destDs = DataSetOptUtil.leftAppendTwoDataSet(dataSet, dataSet2, new ArrayList<>(map.entrySet()));
            } else {
                destDs = DataSetOptUtil.joinTwoDataSet(dataSet, dataSet2, new ArrayList<>(map.entrySet()), join);
            }
            if (destDs != null) {
                bizModel.putDataSet(BuiltInOperation.getJsonFieldString(bizOptJson, "id", bizModel.getModelName()), destDs);
                return BuiltInOperation.createResponseSuccessData(destDs.getSize());
            }
        }
        return BuiltInOperation.createResponseSuccessData(0);
    }
}
