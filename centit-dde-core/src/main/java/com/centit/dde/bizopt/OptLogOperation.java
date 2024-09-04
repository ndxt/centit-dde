package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.framework.common.ResponseData;
import com.centit.framework.components.OperationLogCenter;
import com.centit.framework.model.basedata.OperationLog;
import com.centit.framework.model.security.CentitUserDetails;
import com.centit.support.algorithm.GeneralAlgorithm;
import com.centit.support.algorithm.StringBaseOpt;
import org.apache.commons.lang3.StringUtils;

public class OptLogOperation implements BizOperation {

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        String logType = bizOptJson.getString("logType");
        BizModelJSONTransform transform = new BizModelJSONTransform(bizModel);
        String newValue = bizOptJson.getString("newValue");
        Object newObj = StringUtils.isBlank(newValue)? null : transform.attainExpressionValue(newValue);
        String oldValue = bizOptJson.getString("oldValue");
        Object oldObj = StringUtils.isBlank(oldValue)? null : transform.attainExpressionValue(oldValue);
        CentitUserDetails userDetails = dataOptContext.getCurrentUserDetail();

        String logLevel = BuiltInOperation.getJsonFieldString(bizOptJson, "logLevel", "0");
        //logLevel 操作日志 0 错误提示 1 警告信息 2 调试信息 3 只能同一机构查看的日志 4 只能自己查看的日志 5
        // optMethod optTag optContent

        String optTag = bizOptJson.getString("optTag");
        if( StringUtils.isNotBlank(optTag))
            optTag = StringBaseOpt.castObjectToString(transform.attainExpressionValue(optTag), optTag);
        else
            optTag = dataOptContext.getOptId();
        String optContent = bizOptJson.getString("optContent");
        if( StringUtils.isNotBlank(optContent))
            optContent = StringBaseOpt.castObjectToString(transform.attainExpressionValue(optContent), optContent);
        String optMethod = bizOptJson.getString("optMethod");
        if(StringUtils.isBlank(optMethod)){
            optMethod = dataOptContext.getPacketId();
        }
        OperationLog optLog = OperationLog.create().application(dataOptContext.getOsId())
            .level(logLevel)
            .operation(dataOptContext.getOptId())
            .topUnit(dataOptContext.getTopUnit())
            .unit(userDetails.getCurrentUnitCode())
            .user(userDetails.getUserCode())
            .loginIp(userDetails.getLoginIp())
            .correlation(dataOptContext.getLogId())
            .tag(optTag)
            .method(optMethod)
            .content(optContent);
        if("compare".equals(logType)){
            optLog.makeDifference(oldObj, newObj);
        } else {
            optLog.newObject(newObj).oldObject(oldObj);
        }
        OperationLogCenter.log(optLog);
        return BuiltInOperation.createResponseSuccessData(1);
    }
}
