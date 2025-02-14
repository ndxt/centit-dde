package com.centit.dde.dao.impl;

import com.centit.dde.adapter.dao.CallApiLogDao;
import com.centit.dde.adapter.po.CallApiLog;
import com.centit.dde.adapter.po.CallApiLogDetail;
import com.centit.support.common.ObjectException;
import com.centit.support.database.utils.PageDesc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository(value = "callApiLogDao")
public class CallApiLogDaoImpl implements CallApiLogDao {

    private final static Logger logger = LoggerFactory.getLogger(CallApiLogDaoImpl.class);

    @Override
    public void saveLog(CallApiLog callApiLog) {

    }

    @Override
    public void saveLogDetils(CallApiLog callApiLog) {

    }

    @Override
    public CallApiLog getLog(String logId) {
        return null;
    }

    @Override
    public CallApiLog getLogWithDetail(String logId) {
        return null;
    }

    @Override
    public void deleteLogById(String logId) {

    }

    @Override
    public void deleteLogDetailById(String logId) {

    }

    @Override
    public List<CallApiLogDetail> listLogDetails(String logId) {
        return Collections.emptyList();
    }

    @Override
    public List<CallApiLog> listLogsByProperties(Map<String, Object> param, PageDesc pageDesc) {
        return Collections.emptyList();
    }

    @Override
    public Map<String, Object> getLogStatisticsInfo(Map<String, Object> queryparameter) {
        throw new ObjectException(ObjectException.FUNCTION_NOT_SUPPORT,
            "Runtime 运行时环境，不记录运行日志!");
    }

}
