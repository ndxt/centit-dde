package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataSet;
import com.centit.dde.core.SimpleDataSet;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.dde.utils.CloneUtils;
import com.centit.framework.common.ResponseData;
import com.centit.support.json.JSONTransformer;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;

/**
 * 生成json文件节点信息
 */
public class GenerateJsonBizOperation implements BizOperation {

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson){
        String sourDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        String targetDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "id", sourDsName);
        String requestBody= (String)bizModel.getModelTag().get("requestBody");
        Object data;
        if (StringUtils.isNotBlank(requestBody)){
            data=requestBody;
        } else {
            data = bizModel.fetchDataSetByName(sourDsName).getData();
        }
        String object = JSON.toJSONString(data);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(object.getBytes());
        SimpleDataSet dataSet = new SimpleDataSet(byteArrayInputStream);
        bizModel.putDataSet(targetDsName,dataSet);
        return BuiltInOperation.getResponseSuccessData(dataSet.getSize());
    }

    
    //返回json
    public static ResponseData returnJson(BizModel bizModel, JSONObject bizOptJson){
        String sourDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        String targetDsName =bizOptJson.getString("id");
        String jsonValue=BuiltInOperation.getJsonFieldString(bizOptJson,"jsonValue",null);
        if (StringUtils.isNotBlank(jsonValue)){
            Object data = JSONTransformer.transformer(JSON.parse(jsonValue), new BizModelJSONTransform(bizModel));
            bizModel.putDataSet(targetDsName,new SimpleDataSet(data));
        }else {
            DataSet dataSet = bizModel.getDataSet(sourDsName);
            if (dataSet != null) {
                bizModel.putDataSet(targetDsName, CloneUtils.clone((SimpleDataSet) dataSet));
            }
        }
        return BuiltInOperation.getResponseSuccessData(bizModel.getDataSet(targetDsName).getSize());
    }
}
