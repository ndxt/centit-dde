package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.framework.common.ResponseData;
import com.centit.support.json.JSONTransformer;
import org.apache.commons.lang3.StringUtils;

/**
 * 定义JSON 数据
 */
public class DefineJsonDataOperation implements BizOperation {

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        String targetDsName =bizOptJson.getString("id");
        String jsonValue=BuiltInOperation.getJsonFieldString(bizOptJson,"jsonValue",null);
        if (StringUtils.isNotEmpty(jsonValue)){
            //jsonValue = !jsonValue.startsWith("{") && jsonValue.startsWith("\"")? jsonValue.replace("\"",""): jsonValue;
            Object data =
                JSONTransformer.transformer( jsonValue.startsWith("{") ? JSON.parse(jsonValue) : jsonValue, new BizModelJSONTransform(bizModel));
            bizModel.putDataSet(targetDsName,new DataSet(data));
        }else {
            return BuiltInOperation.createResponseData(0, 1,ResponseData.ERROR_OPERATION, bizOptJson.getString("SetsName")+"：表达式不能为空！");
        }
        return BuiltInOperation.createResponseSuccessData(bizModel.getDataSet(targetDsName).getSize());
    }
}
