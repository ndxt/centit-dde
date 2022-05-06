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
import com.centit.support.database.utils.PageDesc;
import com.centit.support.json.JSONTransformer;
import com.centit.workflow.po.NodeInstance;
import com.centit.workflow.service.FlowManager;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 获取流程实列组件
 */
public class WorkFlowInstNodesBizOperation  implements BizOperation {

    private FlowManager flowManager;

    public WorkFlowInstNodesBizOperation(FlowManager flowManager) {
        this.flowManager = flowManager;
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        String queryType = bizOptJson.getString("queryType");
        if (StringUtils.isBlank(queryType)){
            return ResponseData.makeErrorMessage("请选择获取类别！");
        }
        Map<String,Object>  queryParam = new HashMap<>();
        if (!"ALL".equals(queryType)){
            queryParam.put("nodeState",queryType);
        }
        JSONArray paramList = bizOptJson.getJSONArray("paramList");
        for (Object fieldInfo : paramList) {
            Map<String, Object> fieldMap = CollectionsOpt.objectToMap(fieldInfo);
            String fieldValue = StringBaseOpt.castObjectToString(fieldMap.get("fieldValue"));
            if (StringUtils.isNotBlank(fieldValue)){
                String formulaValue =  StringBaseOpt.castObjectToString(JSONTransformer.transformer(fieldValue, new BizModelJSONTransform(bizModel)));
                queryParam.put(StringBaseOpt.castObjectToString(fieldMap.get("fieldName")),formulaValue);
            }
        }
        PageDesc pageDesc =null;
        Object pageNo = queryParam.get("pageNo");
        Object pageSize = queryParam.get("pageSize");
        if (pageNo!=null && pageSize!=null){
            pageDesc = new PageDesc();
            pageDesc.setPageNo(NumberBaseOpt.castObjectToInteger(pageNo));
            pageDesc.setPageSize(NumberBaseOpt.castObjectToInteger(pageSize));
        }
        List<NodeInstance> nodeInstances = flowManager.listNodeInstance(queryParam,pageDesc);
        String id = bizOptJson.getString("id");
        if (pageDesc!=null){
            PageQueryResult<Object> result = PageQueryResult.createResult(CollectionsOpt.objectToList(nodeInstances), pageDesc);
            bizModel.putDataSet(id, new DataSet(result));//返回带分页的数据
        }else {
            bizModel.putDataSet(id,new DataSet(nodeInstances));
        }
        return BuiltInOperation.createResponseSuccessData(nodeInstances.size());
    }
}
