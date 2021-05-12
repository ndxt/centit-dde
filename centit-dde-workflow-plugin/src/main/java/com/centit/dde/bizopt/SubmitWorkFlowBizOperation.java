package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.SimpleDataSet;
import com.centit.framework.common.ResponseData;
import com.centit.workflow.commons.SubmitOptOptions;
import com.centit.workflow.service.FlowEngine;
import org.apache.commons.lang3.StringUtils;

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
        Map<String, Object> modelTag = bizModel.getModelTag();
        String requestBody= (String) modelTag.get("requestBody");
        SubmitOptOptions submitOptOptions;
        if (StringUtils.isNotBlank(requestBody)){
            submitOptOptions = JSONObject.parseObject(requestBody, SubmitOptOptions.class);
        }else if (modelTag.size()>1){
            submitOptOptions = JSONObject.parseObject(JSON.toJSONString(modelTag), SubmitOptOptions.class);
        }else {
            //获取表达式信息
            Map<String, String> mapInfo = BuiltInOperation.jsonArrayToMap(bizOptJson.getJSONArray("config"), "columnName", "expression");
            submitOptOptions = mapToEntity(mapInfo,SubmitOptOptions.class);
        }
        List<String> list = flowEngine.submitOpt(submitOptOptions);
         if (list.size()>0){
             bizModel.putDataSet(id,new SimpleDataSet(list));
         }
        return BuiltInOperation.getResponseSuccessData(bizModel.getDataSet(id).getSize());
    }

    /**
     * Map转实体类(只能转SubmitOptOptions使用)
     */
    public static SubmitOptOptions mapToEntity(Map<String, String> dataMap, Class<SubmitOptOptions> clzz) {
        SubmitOptOptions submitOptOptions=null;
        try {
            submitOptOptions = SubmitOptOptions.create();
            for(Field field : clzz.getDeclaredFields()) {
                if (dataMap.containsKey(field.getName())) {
                    boolean flag = field.isAccessible();
                    field.setAccessible(true);
                    String object = dataMap.get(field.getName());
                    if (object!= null) {
                        if (field.getType()==Map.class){
                            Map map = JSON.parseObject(object, Map.class);
                            field.set(submitOptOptions, map);
                        }else if (field.getType()== List.class){
                            List list = JSON.parseObject(object, List.class);
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
