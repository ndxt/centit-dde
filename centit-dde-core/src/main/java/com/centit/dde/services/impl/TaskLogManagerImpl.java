package com.centit.dde.services.impl;

import com.centit.dde.adapter.dao.CallApiLogDao;
import com.centit.dde.adapter.po.CallApiLog;
import com.centit.dde.adapter.po.CallApiLogDetail;
import com.centit.dde.services.TaskLogManager;
import com.centit.dde.utils.ConstantValue;
import com.centit.dde.vo.DelTaskLogParameter;
import com.centit.dde.vo.StatisticsParameter;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.algorithm.UuidOpt;
import com.centit.support.database.utils.PageDesc;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author zhf
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TaskLogManagerImpl implements TaskLogManager {

    @Autowired
    protected CallApiLogDao taskLogDao;

    @Override
    public CallApiLog getLog(String logId) {
        return this.taskLogDao.getLog(logId);
    }

    @Override
    public CallApiLog getLogWithDetail(String logId) {
        return this.taskLogDao.getLogWithDetail(logId);
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
        int maxE = 0, maxS = 0;
        if(callApiLog.getDetailLogs()!=null){
            for(CallApiLogDetail dl : callApiLog.getDetailLogs()){
                if(maxE < dl.getErrorPieces()){
                    maxE = dl.getErrorPieces();
                }
                if(maxS < dl.getSuccessPieces()){
                    maxS = dl.getSuccessPieces();
                }
            }
        }
        callApiLog.setErrorPieces(maxE);
        callApiLog.setSuccessPieces(maxS);
        if(StringUtils.isBlank(callApiLog.getLogId())){
            callApiLog.setLogId(UuidOpt.getUuidAsString22());
        }
        this.taskLogDao.saveLog(callApiLog);
        if(detailLogsCount > 0) {
            this.taskLogDao.saveLogDetils(callApiLog);
        }
    }

    @Override
    public void deleteTaskLogById(String logId) {
        this.taskLogDao.deleteLogById(logId);
    }

    @Override
    public Map<String, Object> getLogStatisticsInfo(StatisticsParameter parameter) {
        Map<String, Object> queryparameter = CollectionsOpt.objectToMap(parameter);
        return taskLogDao.getLogStatisticsInfo(queryparameter);
    }

    @Override
    public int deleteTaskLog(DelTaskLogParameter delTaskLogParameter) {
        return taskLogDao.deleteTaskLog(delTaskLogParameter.getPacketId(),
            DatetimeOpt.smartPraseDate(delTaskLogParameter.getRunBeginTime()),
            delTaskLogParameter.getIsError());
        // delete with detail
    }

}

