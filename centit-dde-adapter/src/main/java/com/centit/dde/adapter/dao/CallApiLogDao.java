package com.centit.dde.adapter.dao;

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

    void saveLogDetils(CallApiLog callApiLog);

    CallApiLog getLog(String logId);

    CallApiLog getLogWithDetail(String logId);

    void deleteLogById(String logId);

    void deleteLogDetailById(String logId);

    List<CallApiLogDetail> listLogDetails(String logId);

    List<CallApiLog> listLogsByProperties(Map<String, Object> param, PageDesc pageDesc);

    Map<String, Object> getLogStatisticsInfo(Map<String, Object> queryparameter);

    int deleteTaskLog(String packetId, Date runBeginTime, boolean isError);
}
