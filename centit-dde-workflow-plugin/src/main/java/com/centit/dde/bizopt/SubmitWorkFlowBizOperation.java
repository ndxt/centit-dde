package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataSet;
import com.centit.dde.core.SimpleDataSet;
import com.centit.dde.utils.DataSetOptUtil;
import com.centit.framework.common.ResponseData;
import com.centit.workflow.commons.SubmitOptOptions;
import com.centit.workflow.service.FlowEngine;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class SubmitWorkFlowBizOperation implements BizOperation {

    private FlowEngine flowEngine;

    public SubmitWorkFlowBizOperation() {
    }

    public SubmitWorkFlowBizOperation(FlowEngine flowEngine) {
        this.flowEngine = flowEngine;
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson) {
        String id = bizOptJson.getString("id");
        String source = bizOptJson.getString("source");
        DataSet dataSet = bizModel.getDataSet(source);
        if (dataSet==null){
            return BuiltInOperation.getResponseData(0, 500, bizOptJson.getString("SetsName")+"：未指定数据集！");
        }
        //获取表达式信息
        Map<String, String> mapInfo = BuiltInOperation.jsonArrayToMap(bizOptJson.getJSONArray("config"), "columnName", "expression");
        if (mapInfo!=null && mapInfo.size()>0){
            dataSet = DataSetOptUtil.mapDateSetByFormula(dataSet, mapInfo.entrySet());
        }
        Map<String, Object> objectMap = dataSet.getDataAsList().get(0);
        SubmitOptOptions submitOptOptions = mapToEntity(objectMap, SubmitOptOptions.class);
        List<String> list;
        try {
            list = flowEngine.submitOpt(submitOptOptions);
        } catch (Exception e) {
            bizModel.putDataSet(id,new SimpleDataSet(e.getMessage()));
            return BuiltInOperation.getResponseData(0, 500, e.getMessage());
        }
        if (list.size()==0){
            list.add(submitOptOptions.getNodeInstId());
        }
        bizModel.putDataSet(id,new SimpleDataSet(list));
        return BuiltInOperation.getResponseSuccessData(bizModel.getDataSet(id).getSize());
    }

    /**
     * Map转实体类(只能转SubmitOptOptions使用)
     */
    public static SubmitOptOptions mapToEntity(Map<String, Object> dataMap, Class<SubmitOptOptions> clzz) {
        SubmitOptOptions submitOptOptions=null;
        try {
            submitOptOptions = SubmitOptOptions.create();
            for(Field field : clzz.getDeclaredFields()) {
                if (dataMap.containsKey(field.getName())) {
                    boolean flag = field.isAccessible();
                    field.setAccessible(true);
                    Object object = dataMap.get(field.getName());
                    if (object!= null) {
                        if (field.getType()==Map.class){
                            Map map = JSON.parseObject(JSON.toJSONString(object), Map.class);
                            field.set(submitOptOptions, map);
                        }else if (field.getType()== List.class){
                            List list = JSON.parseObject(JSON.toJSONString(object), List.class);
                            field.set(submitOptOptions, list);
                        }else {
                            field.set(submitOptOptions, object);
                        }
                    }
                    field.setAccessible(flag);
                }
            }
            return  submitOptOptions;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return submitOptOptions;
    }

}
