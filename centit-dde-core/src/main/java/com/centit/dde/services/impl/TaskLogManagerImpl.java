package com.centit.dde.services.impl;

import com.alibaba.fastjson.JSONArray;
import com.centit.dde.dao.TaskLogDao;
import com.centit.dde.po.TaskLog;
import com.centit.dde.services.TaskLogManager;
import com.centit.dde.utils.ConstantValue;
import com.centit.dde.vo.DelTaskLogParameter;
import com.centit.dde.vo.StatisticsParameter;
import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import com.centit.framework.jdbc.service.BaseEntityManagerImpl;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.database.utils.PageDesc;
import com.centit.support.database.utils.QueryAndNamedParams;
import com.centit.support.database.utils.QueryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhf
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TaskLogManagerImpl extends BaseEntityManagerImpl<TaskLog, Long, TaskLogDao> implements TaskLogManager {

    private final TaskLogDao taskLogDao;

    @Autowired
    public TaskLogManagerImpl(TaskLogDao taskLogDao) {
        this.taskLogDao = taskLogDao;
    }

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
        this.taskLogDao.saveNewLog(taskLog);
        this.taskLogDao.saveObjectReferences(taskLog);
    }

    @Override
    public void saveTaskLog(TaskLog taskLog, int logLevel){
        int detailLogsCount = taskLog.getDetailLogs() == null ? 0 : taskLog.getDetailLogs().size();
        if(logLevel == ConstantValue.LOGLEVEL_CHECK_ERROR && detailLogsCount == 0){
            return;
        }
        this.taskLogDao.saveNewLog(taskLog);
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
        //折线图统计
        String sql="SELECT" +
            " DATE_FORMAT(run_begin_time, '%H') AS time, " +
            " count(*) AS num " +
            " FROM " +
            " d_task_log dtl " +
            " WHERE to_days(run_begin_time) = to_days(now()) " +
            " [:optId | AND OPT_ID=:optId] "+
            " [:packetId | AND task_id=:packetId] " +
            " GROUP BY time ";
        QueryAndNamedParams params = QueryUtils.translateQuery(sql, queryparameter);
        JSONArray dataList = DatabaseOptUtils.listObjectsByNamedSqlAsJson(taskLogDao,params.getQuery(), params.getParams());
        //饼图统计
        String  pieSql=
            " SELECT 'day' as time,other_message as message,count(*) as num FROM d_task_log " +
                " where to_days(run_begin_time) = to_days(now()) " +
                " and other_message is not NULL " +
                " [:packetId | AND task_id=:packetId ] " +
                " [:optId | AND OPT_ID=:optId] "+
                " GROUP BY other_message " +
                " UNION ALL" +
                " SELECT 'weeks' as time,other_message as message,count(*) as num FROM d_task_log " +
                " where DATE_SUB(CURDATE(), INTERVAL 7 DAY) <= date(run_begin_time) " +
                " and other_message is not NULL  " +
                " [:packetId | AND task_id=:packetId ] " +
                " [:optId | AND OPT_ID=:optId] "+
                " GROUP BY other_message" +
                " UNION ALL" +
                " SELECT 'month' as time,other_message as message,count(*) as num FROM d_task_log " +
                " where DATE_SUB(CURDATE(), INTERVAL 30 DAY) <= date(run_begin_time) " +
                " and other_message is not NULL " +
                " [:packetId | AND task_id=:packetId ] " +
                " [:optId | AND OPT_ID=:optId] "+
                " GROUP BY other_message";
        QueryAndNamedParams pieData = QueryUtils.translateQuery(pieSql, queryparameter);
        JSONArray pieDataList = DatabaseOptUtils.listObjectsByNamedSqlAsJson(taskLogDao,pieData.getQuery(), pieData.getParams());
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("brokenLineData",dataList);
        hashMap.put("pieData",pieDataList);
        return hashMap;
    }

    @Override
    public int deleteTaskLog(DelTaskLogParameter delTaskLogParameter) {
        StringBuilder sql=new StringBuilder("delete from d_task_log  where task_id =? AND DATE(run_begin_time) <=? ");
        if (!delTaskLogParameter.getIsError()){
            sql.append(" AND other_message !='error' ");
        }
        return DatabaseOptUtils.doExecuteSql(taskLogDao,sql.toString(),new Object[]{delTaskLogParameter.getPacketId(),delTaskLogParameter.getRunBeginTime()});
    }

}

