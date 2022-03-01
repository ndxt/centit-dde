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
        if (StringUtils.isNotEmpty(jsonValue)&& jsonValue.startsWith("{")){
            //json格式表达式获取值
            Object data = JSONTransformer.transformer(JSON.parse(jsonValue), new BizModelJSONTransform(bizModel));
            bizModel.putDataSet(targetDsName,new SimpleDataSet(data));
        }else {
            //非json格式表达式获取值
            Object data = JSONTransformer.transformer(jsonValue, new BizModelJSONTransform(bizModel));
            bizModel.putDataSet(targetDsName, new SimpleDataSet(data));
        }
        return BuiltInOperation.getResponseSuccessData(bizModel.getDataSet(targetDsName).getSize());
    }
}
