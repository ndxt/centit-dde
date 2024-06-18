package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
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
import com.centit.support.common.ObjectException;
import com.centit.support.database.jsonmaptable.GeneralJsonObjectDao;
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
        String sortField = bizOptJson.getString(GeneralJsonObjectDao.TABLE_SORT_FIELD);
        if(StringUtils.isNotBlank(sortField)){
            queryParam.put(GeneralJsonObjectDao.TABLE_SORT_FIELD, sortField);
        }
        String orderField = bizOptJson.getString(GeneralJsonObjectDao.TABLE_SORT_ORDER);
        if(StringUtils.isNotBlank(orderField)){
            queryParam.put(GeneralJsonObjectDao.TABLE_SORT_ORDER, orderField);
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

        String topUnit = dataOptContext.getTopUnit();
        if(StringUtils.isBlank(topUnit)){
            topUnit = bizModel.fetchTopUnit();
        }
        queryParam.put("topUnit", topUnit);
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
                    return ResponseData.makeErrorMessage(
                        ResponseData.ERROR_FIELD_INPUT_NOT_VALID,
                        dataOptContext.getI18nMessage("error.701.field_is_blank", "userCode"));
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
                    return ResponseData.makeErrorMessage(
                        ResponseData.ERROR_FIELD_INPUT_NOT_VALID,
                        dataOptContext.getI18nMessage("error.701.field_is_blank", "userCode"));
                }
                responseData = flowEngine.dubboUserAllTask(queryParam, pageDesc);
                bizModel.putDataSet(id, new DataSet(responseData.getData()));
                break;
            //获取流程所有活动节点的任务列表
            case 6:
                String flowInstId = StringBaseOpt.castObjectToString(queryParam.get("flowInstId"));
                if (StringUtils.isBlank(flowInstId)) {
                    return ResponseData.makeErrorMessage(ResponseData.ERROR_FIELD_INPUT_NOT_VALID,
                        dataOptContext.getI18nMessage("error.701.field_is_blank", "flowInstId"));
                }
                List<UserTask> userTask = flowEngine.listFlowActiveNodeOperators(flowInstId);
                bizModel.putDataSet(id, new DataSet(DictionaryMapUtils.objectsToJSONArray(userTask)));
                break;
            default:
                return ResponseData.makeErrorMessage(ObjectException.PARAMETER_NOT_CORRECT,
                    dataOptContext.getI18nMessage("dde.614.parameter_not_correct", "taskType"));
        }
        return BuiltInOperation.createResponseSuccessData(1);
    }
}
