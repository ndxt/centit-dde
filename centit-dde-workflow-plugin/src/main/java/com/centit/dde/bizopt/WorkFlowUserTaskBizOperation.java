package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.framework.common.ResponseData;
import com.centit.framework.core.dao.PageQueryResult;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.compiler.Pretreatment;
import com.centit.support.database.utils.PageDesc;
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
        Map<String ,Object> queryParam = new HashMap<>();
        JSONArray paramList = bizOptJson.getJSONArray("paramList");
        //构建查询参数
        for (Object fieldInfo : paramList) {
            Map<String, Object> fieldMap = CollectionsOpt.objectToMap(fieldInfo);
            String fieldValue = StringBaseOpt.castObjectToString(fieldMap.get("fieldValue"));
            if (StringUtils.isNotBlank(fieldValue)){
                String formulaValue = Pretreatment.mapTemplateStringAsFormula(fieldValue, new BizModelJSONTransform(bizModel));
                queryParam.put(StringBaseOpt.castObjectToString(fieldMap.get("fieldName")),formulaValue);
            }
        }
        Object pageNo = queryParam.get("pageNo");
        Object pageSize = queryParam.get("pageSize");
        PageDesc pageDesc = null;
        if (pageNo!=null && pageSize!=null){
            pageDesc = new PageDesc();
            pageDesc.setPageNo(NumberBaseOpt.castObjectToInteger(pageNo));
            pageDesc.setPageSize(NumberBaseOpt.castObjectToInteger(pageSize));
        }
        String userCode = StringBaseOpt.castObjectToString(queryParam.get("userCode"));
        List<UserTask> userTask;
        Integer taskType = bizOptJson.getInteger("taskType");
        switch (taskType){
            case 1://静态待办
                userTask = flowEngine.listUserStaticTask(queryParam, pageDesc);
                break;
            case 2://委托待办
                userTask= flowEngine.listUserGrantorTask(queryParam, pageDesc);
                break;
            case 3://岗位待办
                if (StringUtils.isBlank(userCode)){
                    return ResponseData.makeErrorMessage("userCode不能为空！");
                }
                userTask = flowEngine.listUserDynamicTask(queryParam, pageDesc);
                break;
            case 4://自己和委托的待办
                userTask = flowEngine.listUserStaticAndGrantorTask(queryParam, pageDesc);
                break;
            case 5://所有待办
                if (StringUtils.isBlank(userCode)){
                    return ResponseData.makeErrorMessage("userCode不能为空！");
                }
                userTask = flowEngine.listUserAllTask(queryParam, new PageDesc());
                break;
            default:
                return ResponseData.makeErrorMessage("未知操作类型！");
        }
        String id = bizOptJson.getString("id");
        if (pageDesc!=null){
            PageQueryResult<Object> result = PageQueryResult.createResult(CollectionsOpt.objectToList(userTask), pageDesc);
            bizModel.putDataSet(id, new DataSet(result));//返回带分页的数据
        }else {
            bizModel.putDataSet(id,new DataSet(userTask));
        }
        return BuiltInOperation.createResponseSuccessData(userTask.size());
    }
}
