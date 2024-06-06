package com.centit.dde.utils;

import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.adapter.po.TaskDetailLog;
import com.centit.dde.core.DataOptContext;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

public abstract class BizOptUtils {
    public static TaskDetailLog createLogDetail(JSONObject bizOptJson, DataOptContext dataOptContext) {
        TaskDetailLog detailLog = new TaskDetailLog();

        String sOptType = bizOptJson.getString("type");
        String processName = bizOptJson.getString("processName");
        if (StringUtils.isBlank(processName)) {
            processName = bizOptJson.getString("nodeName");
        }
        String logType = sOptType + ":" + processName;
        detailLog.setRunBeginTime(new Date());
        detailLog.setOptNodeId(bizOptJson.getString("id"));
        detailLog.setLogType(logType);
        detailLog.setStepNo(dataOptContext.getStepNo());
        detailLog.setRunEndTime(new Date());
        detailLog.setTaskId(dataOptContext.getPacketId());
        dataOptContext.getTaskLog().addDetailLog(detailLog);
        return detailLog;
    }
}
