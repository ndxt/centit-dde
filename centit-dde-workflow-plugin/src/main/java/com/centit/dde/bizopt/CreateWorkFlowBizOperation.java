package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataSet;
import com.centit.dde.core.SimpleDataSet;
import com.centit.framework.common.ResponseData;
import com.centit.workflow.commons.CreateFlowOptions;
import com.centit.workflow.po.FlowInstance;
import com.centit.workflow.service.FlowEngine;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * 创建工作流节点
 */
public class CreateWorkFlowBizOperation implements BizOperation {

    private FlowEngine flowEngine;

    public CreateWorkFlowBizOperation(FlowEngine flowEngine) {
        this.flowEngine = flowEngine;
    }

    public CreateWorkFlowBizOperation() {}

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson) throws Exception {
        String id = bizOptJson.getString("id");
        String source = bizOptJson.getString("source");
        Map<String, Object> modelTag = bizModel.getModelTag();
        String requestBody= (String) modelTag.get("requestBody");
        DataSet dataSet = bizModel.getDataSet(source);
        CreateFlowOptions createFlowOptions;
        if (StringUtils.isNotBlank(requestBody)){
            createFlowOptions = JSONObject.parseObject(requestBody, CreateFlowOptions.class);
        }else if (modelTag.size()>1){
            createFlowOptions = JSONObject.parseObject(JSON.toJSONString(modelTag), CreateFlowOptions.class);
        }else {
            //获取表达式信息
            Map<String, String> mapInfo = BuiltInOperation.jsonArrayToMap(bizOptJson.getJSONArray("config"), "columnName", "expression");
            createFlowOptions = mapToEntity(mapInfo, CreateFlowOptions.class);
        }
        if (dataSet!=null){
            Object data = dataSet.getData();
           if (data instanceof Map){
               createFlowOptions.setVariables(JSON.parseObject(JSON.toJSONString(data),Map.class));
           }
        }
        FlowInstance instance = flowEngine.createInstance(createFlowOptions);
       if (instance!=null){
           bizModel.putDataSet(id,new SimpleDataSet(instance));
       }
        return BuiltInOperation.getResponseSuccessData(bizModel.getDataSet(id).getSize());
    }

    /**
     * Map转实体类(只能转CreateFlowOptions使用)
     */
    public static CreateFlowOptions mapToEntity(Map<String, String> dataMap, Class<CreateFlowOptions> clzz) {
        CreateFlowOptions createFlowOptions=null;
        try {
            createFlowOptions = CreateFlowOptions.create();
            for(Field field : clzz.getDeclaredFields()) {
                if (dataMap.containsKey(field.getName())) {
                    boolean flag = field.isAccessible();
                    field.setAccessible(true);
                    String object = dataMap.get(field.getName());
                    if (object!= null) {
                        if (field.getType()==Map.class){
                            Map map = JSON.parseObject(object, Map.class);
                            field.set(createFlowOptions, map);
                        }else if (field.getType()== List.class){
                            List list = JSON.parseObject(object, List.class);
                            field.set(createFlowOptions, list);
                        }else {
                            field.set(createFlowOptions, object);
                        }
                    }
                    field.setAccessible(flag);
                }
            }
            return  createFlowOptions;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return createFlowOptions;
    }
}
