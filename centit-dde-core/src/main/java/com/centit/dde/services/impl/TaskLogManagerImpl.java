package com.centit.dde.services.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.adapter.dao.CallApiLogDao;
import com.centit.dde.adapter.dao.DataPacketDao;
import com.centit.dde.adapter.po.CallApiLog;
import com.centit.dde.adapter.po.CallApiLogDetail;
import com.centit.dde.services.TaskLogManager;
import com.centit.dde.utils.ConstantValue;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.algorithm.UuidOpt;
import com.centit.support.database.utils.PageDesc;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author zhf
 * @author codefan@sina.com
 */
@Service
public class TaskLogManagerImpl implements TaskLogManager {
    private static final Logger logger = LoggerFactory.getLogger(TaskLogManagerImpl.class);
    private static CallApiLogDao backgroundTaskLogDao = null;
    private static final ConcurrentLinkedQueue<CallApiLog> waitingForWriteLogs = new ConcurrentLinkedQueue<>();
    private static final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
    //ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private static final int MAX_LOG_COUNT_ONE_TIME = 800;
    /*
     * 异步写入API调用日志
     */
    static {
        //executor.setMaximumPoolSize(1);
        //executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        // 5秒写入一次
        executor.scheduleWithFixedDelay(() -> {
            if(backgroundTaskLogDao == null){
                return;
            }
            // 循环写入日志，一个周期最多写入 nCount 条，避免阻塞
            int nCount = MAX_LOG_COUNT_ONE_TIME;
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
                if(MAX_LOG_COUNT_ONE_TIME > nCount) {
                    logger.info("成功写入{}条API调用信息。", MAX_LOG_COUNT_ONE_TIME - nCount);
                }
            } catch (Exception e) {
                logger.error("写入API调用信息报错：{}", e.getMessage());
            }
        }, 23, 3, TimeUnit.SECONDS);
        //默认执行时间间隔为7秒
    }

    @Autowired
    private DataPacketDao dataPacketDao;

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
        waitingForWriteLogs.offer(callApiLog);
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
    public JSONArray statApiCallSumByTask(String taskId, Date startDate, Date endDate) {
        return taskLogDao.statApiCallSum("task", taskId, startDate, endDate);
    }

    @Override
    public JSONArray statApiCallSumByApplication(String applicationId, Date startDate, Date endDate) {
        return taskLogDao.statApiCallSum("application", applicationId, startDate, endDate);
    }

    @Override
    public JSONArray statApiCallSumByTopUnit(String topUnit, Date startDate, Date endDate) {
        return taskLogDao.statApiCallSum("topUnit", topUnit, startDate, endDate);
    }

    @Override
    public JSONObject statApiEfficiency(String taskId, Date startDate, Date endDate) {
        return taskLogDao.statApiEfficiency(taskId, startDate, endDate);
    }

    @Override
    public JSONObject statApplicationInfo(String osId){
        JSONObject appInfo = new JSONObject();
        Date currentDate = DatetimeOpt.currentUtilDate();
        String today = DatetimeOpt.convertDateToString(currentDate);
        String yesterday = DatetimeOpt.convertDateToString(DatetimeOpt.addDays(currentDate,-1));
        String lastWeek = DatetimeOpt.convertDateToString(DatetimeOpt.addDays(currentDate,-7));
        int lastWeekCallSum = 0;
        int monthCallSum = 0;
        JSONArray jsonArray = taskLogDao.statCallSumByOs(osId,
            DatetimeOpt.truncateToDay(DatetimeOpt.addMonths(currentDate,-1)), currentDate);
        for(Object obj : jsonArray) {
            if(obj instanceof Map) {
                Map<String, Object> map = (Map<String, Object>) obj;
                String dateS = StringBaseOpt.castObjectToString(map.get("runBeginTime"));
                int callSum = NumberBaseOpt.castObjectToInteger(map.get("callSum"), 0);

                if (today.equals(dateS)){
                    appInfo.put("todaySum", callSum);
                } else if (yesterday.equals(dateS)){
                    appInfo.put("yesterdaySum", callSum);
                }
                if(lastWeek.compareTo(dateS) <= 0){
                    lastWeekCallSum += callSum;
                }
                monthCallSum += callSum;
            }
        }
        appInfo.put("weekSum", lastWeekCallSum);
        appInfo.put("monthSum", monthCallSum);
        appInfo.putAll(dataPacketDao.statApplicationInfo(osId));
        return appInfo;
    }

    public void appendPacketName(JSONArray jsonArr){
        if(jsonArr == null || jsonArr.isEmpty()) return;
        List<String> packetIds = new ArrayList<>(50);
        for(Object obj : jsonArr) {
            if(obj instanceof Map) {
                Map<String, Object> map = (Map<String, Object>) obj;
                String packetId = StringBaseOpt.castObjectToString(map.get("taskId"));
                if(StringUtils.isNotBlank(packetId)) {
                    packetIds.add(packetId);
                }
            }
        }
        if(packetIds.isEmpty()) return;
        Map<String, String> packetNameMap = dataPacketDao.mapDataPacketName(packetIds);
        for(Object obj : jsonArr) {
            if(obj instanceof Map) {
                Map<String, Object> map = (Map<String, Object>) obj;
                String packetId = StringBaseOpt.castObjectToString(map.get("taskId"));
                map.put("taskName", packetNameMap.get(packetId));
            }
        }
    }

    @Override
    public JSONArray statTopActive(String osId, int topSize, Date startDate, Date endDate){
        JSONArray jsonArr = taskLogDao.statTopTask(osId, "all", topSize,
            startDate, endDate);
        appendPacketName(jsonArr);
        return jsonArr;
    }


    @Override
    public JSONArray statTopFailed(String osId, int topSize, Date startDate, Date endDate){
        JSONArray jsonArr = taskLogDao.statTopTask(osId, "failed", topSize,
            startDate, endDate);
        appendPacketName(jsonArr);
        return jsonArr;
    }

}

