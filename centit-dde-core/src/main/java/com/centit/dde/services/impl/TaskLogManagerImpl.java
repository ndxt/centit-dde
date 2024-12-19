package com.centit.dde.services.impl;

import com.centit.dde.adapter.dao.TaskLogDao;
import com.centit.dde.adapter.po.TaskDetailLog;
import com.centit.dde.adapter.po.TaskLog;
import com.centit.dde.services.TaskLogManager;
import com.centit.dde.utils.ConstantValue;
import com.centit.dde.vo.DelTaskLogParameter;
import com.centit.dde.vo.StatisticsParameter;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.database.utils.PageDesc;
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
    protected TaskLogDao taskLogDao;

    @Override
    public TaskLog getLog(String logId) {
        return this.taskLogDao.getObjectById(logId);
    }

    @Override
    public List<TaskLog> listTaskLog(Map<String, Object> param, PageDesc pageDesc) {
        return this.taskLogDao.listObjectsByProperties(param, pageDesc);
    }

    @Override
    public void createTaskLog(TaskLog taskLog) {
        this.taskLogDao.saveNewObject(taskLog);
        this.taskLogDao.saveObjectReferences(taskLog);
    }

    @Override
    public void saveTaskLog(TaskLog taskLog, int logLevel){
        int detailLogsCount = taskLog.getDetailLogs() == null ? 0 : taskLog.getDetailLogs().size();
        if(logLevel == ConstantValue.LOGLEVEL_CHECK_ERROR && detailLogsCount == 0){
            return;
        }
        int maxE = 0, maxS = 0;
        if(taskLog.getDetailLogs()!=null){
            for(TaskDetailLog dl : taskLog.getDetailLogs()){
                if(maxE < dl.getErrorPieces()){
                    maxE = dl.getErrorPieces();
                }
                if(maxS < dl.getSuccessPieces()){
                    maxS = dl.getErrorPieces();
                }
            }
        }
        taskLog.setErrorPieces(maxE);
        taskLog.setSuccessPieces(maxS);
        this.taskLogDao.saveNewObject(taskLog);
        this.taskLogDao.saveObjectReferences(taskLog);
    }

    @Override
    public void updateTaskLog(TaskLog taskLog) {
        this.taskLogDao.updateObject(taskLog);
        this.taskLogDao.saveObjectReferences(taskLog);
    }

    @Override
    public void deleteTaskLogById(String logId) {
        this.taskLogDao.deleteObjectForceById(logId);
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
    }

}

