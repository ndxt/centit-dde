package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataSet;
import com.centit.dde.core.SimpleDataSet;
import com.centit.dde.core.impl.BizOptFlowImpl;
import com.centit.dde.utils.Constant;
import com.centit.dde.vo.CycleVo;
import com.centit.framework.common.ResponseData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.sun.xml.internal.ws.developer.Serialization;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 循环标签
 */
@Component
public class CycleBizOperation implements BizOperation {

    @Resource
    BizOptFlowImpl bizOptFlow;

    //注册服务
    @PostConstruct
    private void init(){
        bizOptFlow.registerOperation(Constant.CYCLE,new CycleBizOperation());
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson) throws Exception {
        CycleVo cycleVo =bizOptJson.toJavaObject(CycleVo.class);
        //下次循环时用来获取数据的下标
        int nextDataIndex = cycleVo.getNextDataIndex()==null?0:cycleVo.getNextDataIndex();
        int parentNextDataIndex = cycleVo.getParentNextDataIndex()==null?0:cycleVo.getParentNextDataIndex();
        //区间循环使用
        Integer increasingValue = cycleVo.getIncreasingValue()==null?1:cycleVo.getIncreasingValue();
        SimpleDataSet simpleDataSet = (SimpleDataSet) publicPortion(bizModel, cycleVo);
        List<Map<String, Object>> dataList = simpleDataSet.getDataAsList();
        if(dataList==null){
            return ResponseData.makeErrorMessage("未指定数据集，请指定数据集！");
        }
        String sourceId ="";
        if ("1".equals(cycleVo.getAssignType())){
            sourceId = cycleVo.getSource()+"_temp";
        }else {
            sourceId = cycleVo.getSource();
        }
        if (StringUtils.isNotBlank(cycleVo.getSubsetFieldName())&& parentNextDataIndex<dataList.size()){
            Map<String, Object> dataMap = dataList.get(parentNextDataIndex);
            Object object = dataMap.get(cycleVo.getSubsetFieldName());
            if (object instanceof JSONObject){
                bizModel.putDataSet(sourceId,new SimpleDataSet(object));
                bizOptJson.put("flag","isObject");
            }else if (object instanceof JSONArray){//嵌套循环，这个是子集中包含list
                JSONArray jsonArray = (JSONArray)object;
                bizOptJson.put("flag","isArray");
                bizModel.putDataSet(sourceId,new SimpleDataSet(jsonArray.get(nextDataIndex)));
                bizOptJson.put("nextDataIndex",(nextDataIndex+increasingValue));
                if ((nextDataIndex+increasingValue)>=jsonArray.size()){//当子集遍历完时，外层循环+1，外层循环读取下一个数据
                    bizOptJson.put("isEnd",true);
                    bizOptJson.put("parentNextDataIndex",(parentNextDataIndex+increasingValue));
                }
            }else {
                return ResponseData.makeErrorMessage("未知类型，转换失败！");
            }
        }
        return  BuiltInOperation.getResponseSuccessData(bizModel.getDataSet(cycleVo.getId()).getSize());
    }

    //获取数据集
    private   DataSet publicPortion(BizModel bizModel, CycleVo cycleVo) throws IOException {
        String assignType = cycleVo.getAssignType();
        SimpleDataSet dataSet = (SimpleDataSet)bizModel.getDataSet(cycleVo.getSource());
        if (dataSet==null){
            return null;
        }
        if("1".equals(assignType)) {
            SimpleDataSet simpleDataSet = (SimpleDataSet)bizModel.getDataSet(cycleVo.getId());
            if (simpleDataSet==null){
                //复制对象
                simpleDataSet = SerializationUtils.clone(dataSet);
                bizModel.putDataSet(cycleVo.getId(),simpleDataSet);
            }
            return simpleDataSet;
        }
        return dataSet;
    }
}
