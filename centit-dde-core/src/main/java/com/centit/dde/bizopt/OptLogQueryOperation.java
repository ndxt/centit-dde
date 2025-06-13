package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.framework.common.ResponseData;
import com.centit.framework.core.dao.PageQueryResult;
import com.centit.framework.model.adapter.OperationLogWriter;
import com.centit.framework.model.basedata.OperationLog;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.database.utils.PageDesc;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 日志查询，这个按道理只能查当前租户的日志
 */
public class OptLogQueryOperation implements BizOperation {

    private OperationLogWriter operationLogWriter;

    public OptLogQueryOperation(OperationLogWriter operationLogWriter) {
        this.operationLogWriter = operationLogWriter;
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {

        BizModelJSONTransform transform = new BizModelJSONTransform(bizModel);

        String optId = StringBaseOpt.castObjectToString(transform.attainExpressionValue(bizOptJson.getString("optId")));

        Object optTag = transform.attainExpressionValue(bizOptJson.getString("optTag"));

        Object userCode = transform.attainExpressionValue(bizOptJson.getString("userCode"));

        Object optTimeGe = transform.attainExpressionValue(bizOptJson.getString("optTime_ge"));

        Object optTimeLe = transform.attainExpressionValue(bizOptJson.getString("optTime_le"));

        Object optContent = transform.attainExpressionValue(bizOptJson.getString("optContent"));

        Object optMethod = transform.attainExpressionValue(bizOptJson.getString("optMethod"));
        Object loginIp = transform.attainExpressionValue(bizOptJson.getString("loginIp"));
        Object newValue = transform.attainExpressionValue(bizOptJson.getString("newValue"));
        Object oldValue = transform.attainExpressionValue(bizOptJson.getString("oldValue"));

        int pageNo = NumberBaseOpt.castObjectToInteger(transform.attainExpressionValue(bizOptJson.getString("pageNo")), 1);

        int pageSize = NumberBaseOpt.castObjectToInteger(transform.attainExpressionValue(bizOptJson.getString("pageSize")), 20);

        Map<String, Object> filterMap = new HashMap<>();

        String osId = dataOptContext.getOsId();
        filterMap.put("osId", osId);

        String topUnit = bizModel.fetchTopUnit(); //  dataOptContext.getTopUnit();
        if (StringUtils.isBlank(topUnit)) {
            topUnit = bizModel.fetchTopUnit();
        }
        filterMap.put("topUnit", topUnit);

        if (optTag != null) filterMap.put("optTag", optTag);

        if (userCode != null) filterMap.put("userCode", userCode);

        if (optTimeGe != null) filterMap.put("optTime_ge", optTimeGe);

        if (optTimeLe != null) filterMap.put("optTime_le", optTimeLe);

        if (optContent != null) filterMap.put("optContent", optContent);
        if (optMethod != null) filterMap.put("optMethod", optMethod);
        if (loginIp != null) filterMap.put("loginIp_lk", loginIp);
        if (newValue != null) filterMap.put("newValue", newValue);
        if (oldValue != null) filterMap.put("oldValue", oldValue);

        List<? extends OperationLog> operationLogs = operationLogWriter.listOptLog(optId, filterMap, pageNo, pageSize);

        int count = operationLogWriter.countOptLog(optId, filterMap);

        PageDesc pageDesc = new PageDesc();
        pageDesc.setPageNo(pageNo);
        pageDesc.setPageSize(pageSize);
        pageDesc.setTotalRows(count);

        PageQueryResult<? extends OperationLog> result = PageQueryResult.createResultMapDict(operationLogs, pageDesc);

        bizModel.putDataSet(bizOptJson.getString("id"), new DataSet(result));

        return BuiltInOperation.createResponseSuccessData(count);
    }
}
