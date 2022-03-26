package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataSet;
import com.centit.dde.core.SimpleDataSet;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.dde.vo.AssignmentVo;
import com.centit.framework.common.ResponseData;
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
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson) throws Exception {
        AssignmentVo assignmentVo =bizOptJson.toJavaObject(AssignmentVo.class);
        DataSet dataSet = bizModel.getDataSet(assignmentVo.getSource());
        String assignType = assignmentVo.getAssignType();
        JSONObject dataSetData=null;
        if (!StringBaseOpt.isNvl(String.valueOf(assignmentVo.getData()))){//根据表达式生成新的数据集
            dataSetData = (JSONObject)JSONTransformer.transformer(JSON.parse(String.valueOf(assignmentVo.getData())), new BizModelJSONTransform(bizModel));
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
                Object data =  JSONTransformer.transformer(JSON.parse(assignmentVo.getData()), new BizModelJSONTransform(bizModel));
                Map<String, Object> modelTag = bizModel.getModelTag();
                if (data instanceof  Map){
                    modelTag.putAll((Map)data);
                }else {
                 modelTag.put(assignmentVo.getId(),data);
                }
                newDataSet = new SimpleDataSet(data);
                break;
            default://引用数据集
                List<Map<String, Object>> dataAsList = dataSet.getDataAsList();
                Map map = JSONObject.parseObject(JSON.toJSONString(dataSetData), Map.class);
                for (Map<String, Object> objectMap : dataAsList) {
                    map.forEach((key,value)->{
                        objectMap.put((String) key,value);
                    });
                }
                newDataSet = new SimpleDataSet(dataSet);
                break;
        }
        bizModel.putDataSet(assignmentVo.getId(),newDataSet);
        return BuiltInOperation.getResponseSuccessData(bizModel.getDataSet(assignmentVo.getId()).getSize());
    }
}
