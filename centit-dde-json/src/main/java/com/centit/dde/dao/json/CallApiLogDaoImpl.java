package com.centit.dde.dao.json;

import com.alibaba.fastjson2.JSON;
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

/**
 * @author codefan@sina.com
 */
@Repository(value = "callApiLogDao")
public class CallApiLogDaoImpl implements CallApiLogDao {

    private final static Logger logger = LoggerFactory.getLogger(CallApiLogDaoImpl.class);

    @Override
    public void saveLog(CallApiLog callApiLog) {
        logger.info(JSON.toJSONString(callApiLog));
    }

    @Override
    public void saveLogDetils(CallApiLog callApiLog) {
        logger.info(JSON.toJSONString(callApiLog.getDetailLogs()));
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
    public List<Map<String, Object>> listLogsByProperties(Map<String, Object> param, PageDesc pageDesc) {
        return Collections.emptyList();
    }

    @Override
    public Map<String, Object> getLogStatisticsInfo(Map<String, Object> queryparameter) {
        throw new ObjectException(ObjectException.FUNCTION_NOT_SUPPORT,
            "Runtime 运行时环境，无法统计运行日志!");
    }

    @Override
    public int deleteTaskLog(String packetId, Date runBeginTime, boolean isError) {
        return 1;
    }
}
