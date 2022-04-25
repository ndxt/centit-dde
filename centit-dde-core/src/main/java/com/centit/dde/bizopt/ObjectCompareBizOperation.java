package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataSet;
import com.centit.dde.core.SimpleDataSet;
import com.centit.framework.common.ResponseData;
import com.centit.support.algorithm.GeneralAlgorithm;

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

        DataSet oldDataSet = bizModel.getDataSet(oldSource);
        DataSet newDataSet = bizModel.getDataSet(newSource);
        if(oldDataSet==null || newDataSet==null || oldDataSet.getSize() != 1 || newDataSet.getSize() !=1 ){
            return ResponseData.makeErrorMessage("数据对比组件只能对比单个记录的差异，源和目标数据集都只能有一条记录（DataSet.getSize()==1）。");
        }
        Map<String,Object> oldObject = oldDataSet.getFirstRow();
        Map<String,Object> newObject = newDataSet.getFirstRow();

        List<JSONObject> compareResult = new ArrayList<>();
        newObject.forEach((newKey,newValue)->{
            //新对象中的字段在旧对象中不存在
            Object oldValue = oldObject.get(newKey);
            if(!GeneralAlgorithm.equals(oldValue, newValue)){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("fieldName",newKey);
                jsonObject.put("oldValue",oldValue);
                jsonObject.put("newValue",newValue);
                compareResult.add(jsonObject);
            }
        });
        bizModel.putDataSet(id, new SimpleDataSet(compareResult));
        return BuiltInOperation.getResponseSuccessData(compareResult.size());
    }
}
