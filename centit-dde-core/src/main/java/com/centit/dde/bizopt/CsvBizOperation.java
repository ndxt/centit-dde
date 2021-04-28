package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.SimpleDataSet;
import com.centit.dde.utils.CloneUtils;
import com.centit.dde.utils.GetJsonFieldValueUtils;
import com.centit.framework.common.ResponseData;
import org.apache.commons.lang3.StringUtils;


/**
 * @author zhf
 */
public class CsvBizOperation implements BizOperation {

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson) {
        String sourDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "id", bizModel.getModelName());
        String source = bizOptJson.getString("source");
        String csvExpressions = bizOptJson.getString("csvexpressions");
        SimpleDataSet dataSet = (SimpleDataSet) bizModel.getDataSet(source);
        SimpleDataSet simpleDataSet;
        if (StringUtils.isNotBlank(csvExpressions)){
            JSONArray jsonData = GetJsonFieldValueUtils.getJsonFieldValue(csvExpressions,dataSet);
            simpleDataSet = new SimpleDataSet(jsonData);
        }else {
            simpleDataSet= CloneUtils.clone(dataSet);
        }
        bizModel.putDataSet(sourDsName,simpleDataSet);
        return BuiltInOperation.getResponseSuccessData(dataSet.getSize());
    }
}
