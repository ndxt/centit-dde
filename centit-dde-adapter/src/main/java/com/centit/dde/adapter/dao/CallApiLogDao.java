package com.centit.dde.adapter.dao;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.adapter.po.CallApiLog;
import com.centit.dde.adapter.po.CallApiLogDetail;
import com.centit.support.database.utils.PageDesc;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author codefan@sina.com
 */

public interface CallApiLogDao {

    void saveLog(CallApiLog callApiLog);

    void saveLogDetails(CallApiLog callApiLog);

    CallApiLog getLog(String logId);

    CallApiLog getLogWithDetail(String logId);

    void deleteLogById(String logId);

    List<CallApiLogDetail> listLogDetails(String logId);

    List<Map<String, Object>> listLogsByProperties(Map<String, Object> param, PageDesc pageDesc);

    JSONArray statApiCallSum(String statType, String typeId, Date startDate, Date endDate);

    JSONArray statCallSumByOs(String osId, Date startDate, Date endDate);

    JSONArray statTopTask(String osId, String countType, int topSize, Date startDate, Date endDate);

    JSONObject statApiEfficiency(String taskId, Date startDate, Date endDate);
}
