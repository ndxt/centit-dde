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
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class AssignmentOperation implements BizOperation {

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        //赋值目标
        String targetDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "target", null);

        //数据来源
        String sourceDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "source", null);

        DataSet targetDataSet = bizModel.getDataSet(targetDsName);
        DataSet dataSet = bizModel.getDataSet(sourceDsName);
        if(dataSet == null  || targetDataSet == null){
            return BuiltInOperation.createResponseData(0,1, ResponseData.ERROR_USER_CONFIG,
                "配置信息出错，找不到对应的数据集："+targetDsName+"，"+sourceDsName+"。");
        }

        //assignType 赋值类型， assign 直接赋值， property 对属性复制 ， append 追加元素, merge 合并属性
        String assignType = BuiltInOperation.getJsonFieldString(bizOptJson, "assignType", "assign");

        //取值表达式
        String formula = BuiltInOperation.getJsonFieldString(bizOptJson, "formula", ".");

        //计算赋值数据
        Object sourceData =(StringUtils.isBlank(formula) || ".".equals(formula) ) ?
            dataSet.getData() : new DatasetVariableTranslate(dataSet).getVarValue(formula);

        // 对属性赋值
        if("property".equals(assignType)){
            String property = BuiltInOperation.getJsonFieldString(bizOptJson, "attributeName", ".");
            if (".".equals(property)){
               return ResponseData.makeErrorMessage("属性名称不能为空！");
            }
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
            List<Map<String, Object>> objList = new ArrayList<>();
            if( targetDataSet.getSize() < 1){
                targetDataSet.setData(CollectionsOpt.createList(sourceData));
            } else {
                objList=new ArrayList<>(targetDataSet.getDataAsList());

            }
            if(sourceData instanceof Collection){
                for(Object obj : (Collection<?>) sourceData) {
                    objList.add(CollectionsOpt.objectToMap(obj));
                }
            }else {
                objList.add(CollectionsOpt.objectToMap(sourceData));
            }
            targetDataSet.setData(objList);
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
