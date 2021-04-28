package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataSet;
import com.centit.dde.core.SimpleDataSet;
import com.centit.dde.utils.CloneUtils;
import com.centit.dde.utils.GetJsonFieldValueUtils;
import com.centit.framework.common.ResponseData;
import org.apache.commons.lang3.StringUtils;


/**
 * @author zhf
 */
public class JsonBizOperation implements BizOperation {

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson) {
        String sourDsName = bizOptJson.getString("id");
        String source = bizOptJson.getString("source");
        String jsonexpression = bizOptJson.getString("jsonexpression");
        DataSet dataSet = bizModel.getDataSet(source);
        if (StringUtils.isNotBlank(jsonexpression)){
            JSONArray jsonData = GetJsonFieldValueUtils.getJsonFieldValue(jsonexpression,dataSet);
            bizModel.putDataSet(sourDsName, new SimpleDataSet(jsonData));
        }else {
            bizModel.putDataSet(sourDsName, CloneUtils.clone(new SimpleDataSet(dataSet.getData())));
        }
        return BuiltInOperation.getResponseSuccessData(dataSet.getSize());
    }
}
