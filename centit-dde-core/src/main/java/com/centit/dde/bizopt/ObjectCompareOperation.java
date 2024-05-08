package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.framework.common.ResponseData;
import com.centit.support.algorithm.GeneralAlgorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ObjectCompareOperation implements BizOperation {
    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        String id = bizOptJson.getString("id");
        //原始数据
        String oldSource = bizOptJson.getString("oldSource");
        //新数据
        String newSource = bizOptJson.getString("newSource");

        DataSet oldDataSet = bizModel.getDataSet(oldSource);
        DataSet newDataSet = bizModel.getDataSet(newSource);
        if(oldDataSet==null || newDataSet==null || oldDataSet.getSize() != 1 || newDataSet.getSize() !=1 ){
            return ResponseData.makeErrorMessage(ResponseData.ERROR_POSTCONDITION_FAILED,
                dataOptContext.getI18nMessage("dde.705.bad_data_for_compare"));
        }
        Map<String,Object> oldObject = oldDataSet.getFirstRow();
        boolean oldIsEmpty = oldObject.isEmpty();
        Map<String,Object> newObject = newDataSet.getFirstRow();

        List<JSONObject> compareResult = new ArrayList<>();
        newObject.forEach((newKey, newValue)->{
            //新对象中的字段在旧对象中不存在
            if(oldIsEmpty) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("fieldName",newKey);
                jsonObject.put("newValue",newValue);
            } else {
                Object oldValue = oldObject.get(newKey);
                if(GeneralAlgorithm.compareTwoObject(oldValue, newValue)!=0){
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("fieldName",newKey);
                    jsonObject.put("oldValue",oldValue);
                    jsonObject.put("newValue",newValue);
                    compareResult.add(jsonObject);
                }
            }
        });

        if(!oldIsEmpty) {
            oldObject.forEach((oldKey, oldValue) -> {
                if (!newObject.containsKey(oldKey)) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("fieldName", oldKey);
                    jsonObject.put("oldValue", oldValue);
                    compareResult.add(jsonObject);
                }
            });
        }

        bizModel.putDataSet(id, new DataSet(compareResult));
        return BuiltInOperation.createResponseSuccessData(compareResult.size());
    }
}
