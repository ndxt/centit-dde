package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.SimpleDataSet;
import com.centit.framework.common.ResponseData;
import com.centit.support.algorithm.CollectionsOpt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ObjectCompareBizOperation implements BizOperation {
    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson) throws Exception {
        String id = bizOptJson.getString("id");
        //原始数据
        String oldSource = bizOptJson.getString("oldSource");
        //新数据
        String newSource = bizOptJson.getString("newSource");

        Object oldDataSet = bizModel.getDataSet(oldSource).getData();

        Object newDataSet = bizModel.getDataSet(newSource).getData();

        Map<String,Object> oldObject = CollectionsOpt.objectToMap(oldDataSet);

        Map<String,Object> newObject = CollectionsOpt.objectToMap(newDataSet);

        List<JSONObject> compareResult = new ArrayList<>();
        newObject.forEach((newKey,newValue)->{
            JSONObject jsonObject = new JSONObject();
            //新对象中的字段在旧对象中不存在
            if (!oldObject.containsKey(newKey)){
                jsonObject.put("fieldName",newKey);
                jsonObject.put("oldValue","");
                jsonObject.put("newValue",newValue);
            }else {
                oldObject.forEach((oldKey,oldValue)->{
                    //字段相同并且值不相等
                    if (oldKey.equals(newKey) && !oldValue.equals(newValue)){
                        jsonObject.put("fieldName",newKey);
                        jsonObject.put("oldValue",oldValue);
                        jsonObject.put("newValue",newValue);
                    }
                });
            }
            if (!jsonObject.isEmpty()){
                compareResult.add(jsonObject);
            }
        });
        bizModel.putDataSet(id, new SimpleDataSet(compareResult));
        return BuiltInOperation.getResponseSuccessData(compareResult.size());
    }
}
