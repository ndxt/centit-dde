package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.SimpleDataSet;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.dde.utils.CloneUtils;
import com.centit.framework.common.ResponseData;
import com.centit.support.json.JSONTransformer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * 生成json文件节点信息
 */
@Component
public class GenerateJsonBizOperation implements BizOperation {
    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson){
        String id = bizOptJson.getString("id");
        String source = bizOptJson.getString("source");
        String jsonValue = bizOptJson.getString("jsonValue");
        SimpleDataSet dataSet;
        if (StringUtils.isNotBlank(jsonValue)){
            Object transformer = JSONTransformer.transformer(JSON.parse(jsonValue), new BizModelJSONTransform(bizModel));
            dataSet = new SimpleDataSet(transformer);
        }else {
            dataSet = CloneUtils.clone(new SimpleDataSet(bizModel.getDataSet(source).getData()));
        }
        bizModel.putDataSet(id,dataSet);
        return BuiltInOperation.getResponseSuccessData(dataSet.getSize());
    }
}
