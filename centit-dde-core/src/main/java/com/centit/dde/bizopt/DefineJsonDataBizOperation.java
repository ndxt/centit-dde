package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.SimpleDataSet;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.framework.common.ResponseData;
import com.centit.support.json.JSONTransformer;
import org.apache.commons.lang3.StringUtils;

/**
 * 定义JSON 数据
 */
public class DefineJsonDataBizOperation implements BizOperation {

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson) throws Exception {
        String targetDsName =bizOptJson.getString("id");
        String jsonValue=BuiltInOperation.getJsonFieldString(bizOptJson,"jsonValue",null);
        if (StringUtils.isNotEmpty(jsonValue)){
            //jsonValue = !jsonValue.startsWith("{") && jsonValue.startsWith("\"")? jsonValue.replace("\"",""): jsonValue;
            Object data =
                JSONTransformer.transformer( jsonValue.startsWith("{") ? JSON.parse(jsonValue) : jsonValue, new BizModelJSONTransform(bizModel));
            bizModel.putDataSet(targetDsName,new SimpleDataSet(data));
        }else {
            return BuiltInOperation.getResponseData(0, 500, bizOptJson.getString("SetsName")+"：表达式不能为空！");
        }
        return BuiltInOperation.getResponseSuccessData(bizModel.getDataSet(targetDsName).getSize());
    }
}
