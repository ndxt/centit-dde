package com.centit.dde.services.impl;

import com.alibaba.fastjson2.JSONArray;
import com.centit.dde.adapter.dao.CallApiLogDao;
import com.centit.dde.adapter.po.CallApiLog;
import com.centit.dde.adapter.po.CallApiLogDetail;
import com.centit.dde.services.TaskLogManager;
import com.centit.dde.utils.ConstantValue;
import com.centit.support.algorithm.UuidOpt;
import com.centit.support.database.utils.PageDesc;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author zhf
 * @author codefan@sina.com
 */
@Service
public class TaskLogManagerImpl implements TaskLogManager {
    private static final Logger logger = LoggerFactory.getLogger(TaskLogManagerImpl.class);
    private static CallApiLogDao backgroundTaskLogDao = null;
    private static ConcurrentLinkedQueue<CallApiLog> waitingForWriteLogs = new ConcurrentLinkedQueue<>();
    private static ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(3);

    /*
     * 异步写入API调用日志
     */
    static {
        // 5秒写入一次
        executor.scheduleWithFixedDelay(() -> {
            if(backgroundTaskLogDao == null){
                return;
            }
            // 循环写入日志，一个周期最多写入 nCount 条，避免阻塞
            int nCount = 5000;
            try {
                while (nCount > 0) {
                    CallApiLog optLog = waitingForWriteLogs.poll();
                    if (optLog == null) {
                        break;
                    }
                    // 写入日志
                    backgroundTaskLogDao.saveLog(optLog);
                    if(optLog.getDetailLogs() != null && !optLog.getDetailLogs().isEmpty()) {
                        backgroundTaskLogDao.saveLogDetails(optLog);
                    }
                    nCount --;
                }
                if(5000 > nCount) {
                    logger.info("成功写入{}条API调用信息。", 5000 - nCount);
                }
            } catch (Exception e) {
                logger.error("写入API调用信息报错：{}", e.getMessage());
            }
        }, 37, 5, TimeUnit.SECONDS);
        //默认执行时间间隔为5秒
    }

    protected CallApiLogDao taskLogDao;
    public TaskLogManagerImpl(@Autowired CallApiLogDao taskLogDao) {
        this.taskLogDao = taskLogDao;
        backgroundTaskLogDao = this.taskLogDao;
    }

    @Override
    public CallApiLog getLog(String logId) {
        return this.taskLogDao.getLog(logId);
    }

    @Override
    public CallApiLog getLogWithDetail(String logId) {
        return this.taskLogDao.getLogWithDetail(logId);
    }

    @Override
    public List<CallApiLogDetail> listLogDetails(String logId){
        return this.taskLogDao.listLogDetails(logId);
    }

    @Override
    public List<Map<String, Object>> listTaskLog(Map<String, Object> param, PageDesc pageDesc) {
        return this.taskLogDao.listLogsByProperties(param, pageDesc);
    }

    @Override
    public void saveTaskLog(CallApiLog callApiLog, int logLevel){
        int detailLogsCount = callApiLog.getDetailLogs() == null ? 0 : callApiLog.getDetailLogs().size();
        if(logLevel == ConstantValue.LOGLEVEL_CHECK_ERROR && detailLogsCount == 0){
            return;
        }
        if(StringUtils.isBlank(callApiLog.getLogId())){
            callApiLog.setLogId(UuidOpt.getUuidAsString22());
        }
        waitingForWriteLogs.add(callApiLog);
        /* this.taskLogDao.saveLog(callApiLog);
        if(detailLogsCount > 0) {
            this.taskLogDao.saveLogDetails(callApiLog);
        }*/
    }

    @Override
    public void deleteTaskLogById(String logId) {
        this.taskLogDao.deleteLogById(logId);
    }

    @Override
    public JSONArray statApiCallSumByHour(String taskId, Date startDate, Date endDate) {
        return taskLogDao.statApiCallSumByHour(taskId, startDate, endDate);
    }

}

