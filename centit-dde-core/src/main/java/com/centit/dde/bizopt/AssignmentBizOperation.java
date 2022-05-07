package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.utils.DatasetVariableTranslate;
import com.centit.framework.common.ResponseData;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.json.JSONOpt;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AssignmentBizOperation implements BizOperation {

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        //赋值目标
        String targetDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "target", null);
        //数据来源
        String sourceDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "source", null);
        //复制类型
        //assignType 复制类型， assign 直接赋值， property 对属性复制 ， append 追加元素, merge 合并属性
        String assignType = BuiltInOperation.getJsonFieldString(bizOptJson, "assignType", "assign");
        //取值表达式
        String formula = BuiltInOperation.getJsonFieldString(bizOptJson, "formula", ".");
        DataSet dataSet =bizModel.fetchDataSetByName(sourceDsName);

        DataSet targetDataSet =bizModel.fetchDataSetByName(targetDsName);
        if(dataSet == null  || targetDataSet==null){
            return BuiltInOperation.createResponseData(0,1, ResponseData.ERROR_USER_CONFIG,
                "配置信息出错，找不到对应的数据集："+targetDsName+"，"+sourceDsName+"。");
        }
        Object sourceData = new DatasetVariableTranslate(dataSet).getVarValue(formula);
        String property = null;
        if("property".equals(assignType)) {
            //复制属性
            property = BuiltInOperation.getJsonFieldString(bizOptJson, "property", ".");
        }
        // 对属性赋值
        if("property".equals(assignType) && StringUtils.isNotBlank(property) && !".".equals(property)){
            // property
            if( targetDataSet.getSize() < 1){
                JSONObject jsonObject = new JSONObject();
                JSONOpt.appendData(jsonObject, property, sourceData);
                targetDataSet.setData(jsonObject);
            } else if(targetDataSet.getSize() == 1){
                Map<String, Object> objMap = targetDataSet.getFirstRow();
                JSONObject jsonObject = (JSONObject)JSON.toJSON(objMap);
                JSONOpt.appendData(jsonObject, property, sourceData);
                targetDataSet.setData(jsonObject);
            } else {
                List<Map<String, Object>> objList = targetDataSet.getDataAsList();
                List<JSONObject> jsonList = new ArrayList<>(objList.size() +1);
                for(Map<String, Object> objMap : objList){
                    JSONObject jsonObject = (JSONObject)JSON.toJSON(objMap);
                    JSONOpt.appendData(jsonObject, property, sourceData);
                    jsonList.add(jsonObject);
                }
                targetDataSet.setData(jsonList);
            }
        } else if("append".equals(assignType)){ // 追加
            if( targetDataSet.getSize() < 1){
                targetDataSet.setData(CollectionsOpt.createList(sourceData));
            } else {
                List<Map<String, Object>> objList = targetDataSet.getDataAsList();
                objList.add(CollectionsOpt.objectToMap(sourceData));
                targetDataSet.setData(objList);
            }
        } else if("merge".equals(assignType)){ // 追加
            if( targetDataSet.getSize() < 1){
                targetDataSet.setData(sourceData);
            } else if( targetDataSet.getSize() == 1){
                targetDataSet.setData(CollectionsOpt.unionTwoMap(
                     CollectionsOpt.objectToMap(sourceData), targetDataSet.getFirstRow()));
            } else  {
                List<Map<String, Object>> objList = targetDataSet.getDataAsList();
                List<Map<String, Object>> jsonList = new ArrayList<>(objList.size() +1);
                for(Map<String, Object> objMap : objList){
                    jsonList.add(CollectionsOpt.unionTwoMap(
                        CollectionsOpt.objectToMap(sourceData), objMap));
                }
                targetDataSet.setData(jsonList);
            }
        } else { // 对目标对象赋值 assign || property == '.'
            targetDataSet.setData(sourceData);
        }
        return BuiltInOperation.createResponseSuccessData(targetDataSet.getSize());
    }
}
