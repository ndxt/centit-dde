package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.framework.common.ResponseData;
import com.centit.framework.core.dao.DictionaryMapUtils;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.database.utils.PageDesc;
import com.centit.support.json.JSONTransformer;
import com.centit.workflow.po.UserTask;
import com.centit.workflow.service.FlowEngine;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 获取工作流待办组件
 */
public class WorkFlowUserTaskBizOperation implements BizOperation {

    private FlowEngine flowEngine;

    public WorkFlowUserTaskBizOperation(FlowEngine flowEngine) {
        this.flowEngine = flowEngine;
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        String id = bizOptJson.getString("id");
        Map<String, Object> queryParam = new HashMap<>(10);
        JSONArray paramList = bizOptJson.getJSONArray("paramList");
        //构建查询参数
        for (Object fieldInfo : paramList) {
            Map<String, Object> fieldMap = CollectionsOpt.objectToMap(fieldInfo);
            String fieldValue = StringBaseOpt.castObjectToString(fieldMap.get("fieldValue"));
            if (StringUtils.isNotBlank(fieldValue)) {
                String formulaValue = StringBaseOpt.castObjectToString(JSONTransformer.transformer(fieldValue, new BizModelJSONTransform(bizModel)));
                queryParam.put(StringBaseOpt.castObjectToString(fieldMap.get("fieldName")), formulaValue);
            }
        }
        Object pageNo = queryParam.get("pageNo");
        Object pageSize = queryParam.get("pageSize");
        PageDesc pageDesc = new PageDesc();
        if (pageNo != null && pageSize != null) {
            pageDesc.setPageNo(NumberBaseOpt.castObjectToInteger(pageNo));
            pageDesc.setPageSize(NumberBaseOpt.castObjectToInteger(pageSize));
        }
        String userCode = StringBaseOpt.castObjectToString(queryParam.get("userCode"));
        ResponseData responseData;
        Integer taskType = bizOptJson.getInteger("taskType");
        switch (taskType) {
            //静态待办
            case 1:
                responseData = flowEngine.dubboUserStaticTask(queryParam, pageDesc);
                bizModel.putDataSet(id, new DataSet(responseData.getData()));
                break;
            //委托待办
            case 2:
                responseData =flowEngine.dubboUserGrantorTask(queryParam, pageDesc);
                bizModel.putDataSet(id, new DataSet(responseData.getData()));
                break;
            //岗位待办
            case 3:
                if (StringUtils.isBlank(userCode)) {
                    return ResponseData.makeErrorMessage("userCode不能为空！");
                }
                responseData = flowEngine.dubboUserDynamicTask(queryParam, pageDesc);
                bizModel.putDataSet(id, new DataSet(responseData.getData()));
                break;
            //自己和委托的待办
            case 4:
                responseData = flowEngine.dubboUserStaticAndGrantorTask(queryParam, pageDesc);
                bizModel.putDataSet(id, new DataSet(responseData.getData()));
                break;
            //所有待办
            case 5:
                if (StringUtils.isBlank(userCode)) {
                    return ResponseData.makeErrorMessage("userCode不能为空！");
                }
                responseData = flowEngine.dubboUserAllTask(queryParam, pageDesc);
                bizModel.putDataSet(id, new DataSet(responseData.getData()));
                break;
            //获取流程所有活动节点的任务列表
            case 6:
                String flowInstId = StringBaseOpt.castObjectToString(queryParam.get("flowInstId"));
                if (StringUtils.isBlank(flowInstId)) {
                    return ResponseData.makeErrorMessage("flowInstId不能为空！");
                }
                List<UserTask> userTask = flowEngine.listFlowActiveNodeOperators(flowInstId);
                bizModel.putDataSet(id, new DataSet(DictionaryMapUtils.objectsToJSONArray(userTask)));
                break;
            default:
                return ResponseData.makeErrorMessage("未知操作类型！");
        }
        return BuiltInOperation.createResponseSuccessData(1);
    }
}
