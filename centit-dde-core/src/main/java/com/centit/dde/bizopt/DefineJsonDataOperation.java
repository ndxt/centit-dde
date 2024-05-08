package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.dataset.FileDataSet;
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
                JSONTransformer.transformer(jsonValue.startsWith("{") || jsonValue.startsWith("[") ?
                     JSON.parse(jsonValue) : jsonValue, new BizModelJSONTransform(bizModel));
            if (data instanceof FileDataSet){
                bizModel.putDataSet(targetDsName,(FileDataSet)data);
            }else {
                bizModel.putDataSet(targetDsName,new DataSet(data));
            }
        }else {
            return BuiltInOperation.createResponseData(0, 1,
                ResponseData.ERROR_FIELD_INPUT_NOT_VALID,
                dataOptContext.getI18nMessage("error.701.field_is_blank", "SetsName"));
        }
        return BuiltInOperation.createResponseSuccessData(bizModel.getDataSet(targetDsName).getSize());
    }
}
