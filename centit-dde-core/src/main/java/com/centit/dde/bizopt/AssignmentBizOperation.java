package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.*;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.dde.utils.ConstantValue;
import com.centit.dde.vo.AssignmentVo;
import com.centit.framework.common.ResponseData;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.json.JSONTransformer;
import org.apache.commons.lang3.SerializationUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 赋值标签
 */
@Component
public class AssignmentBizOperation implements BizOperation {

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        AssignmentVo assignmentVo =bizOptJson.toJavaObject(AssignmentVo.class);
        DataSet dataSet = bizModel.getDataSet(assignmentVo.getSource());
        String assignType = assignmentVo.getAssignType();
        JSONObject dataSetData=null;
        String data = StringBaseOpt.castObjectToString(assignmentVo.getData());
        if (!StringBaseOpt.isNvl(data)){//根据表达式生成新的数据集
            dataSetData = (JSONObject)JSONTransformer.transformer(JSON.parse(data), new BizModelJSONTransform(bizModel));
        }
        DataSet newDataSet;
        switch (assignType){
            case "1"://复制数据集
                if (dataSet==null){
                    return BuiltInOperation.getResponseData(0, 500, bizOptJson.getString("SetsName")+"：未指定数据集，或指定数据集值为NULL！");
                }
                newDataSet=SerializationUtils.clone(new SimpleDataSet(dataSet.getData()));
                break;
            case "2"://手动赋值
                Object transformerdData =  JSONTransformer.transformer(JSON.parse(data), new BizModelJSONTransform(bizModel));
                Map<String, Object> modelTag = CollectionsOpt.objectToMap(bizModel.getStackData(ConstantValue.REQUEST_PARAMS_TAG));
                if (transformerdData instanceof  Map){
                    modelTag.putAll(CollectionsOpt.objectToMap(transformerdData));
                }else {
                 modelTag.put(assignmentVo.getId(),data);
                }
                newDataSet = new SimpleDataSet(data);
                break;
            default://引用数据集
                List<Map<String, Object>> dataAsList = dataSet.getDataAsList();
                Map<String,Object> map = CollectionsOpt.objectToMap(dataSetData);
                for (Map<String, Object> objectMap : dataAsList) {
                    map.forEach((key,value)-> objectMap.put(key,value));
                }
                newDataSet = new SimpleDataSet(dataSet);
                break;
        }
        bizModel.putDataSet(assignmentVo.getId(),newDataSet);
        return BuiltInOperation.getResponseSuccessData(bizModel.getDataSet(assignmentVo.getId()).getSize());
    }
}
