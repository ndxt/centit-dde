package com.centit.dde.dao.impl;

import com.alibaba.fastjson2.JSONArray;
import com.centit.dde.adapter.dao.TaskLogDao;
import com.centit.dde.adapter.po.TaskLog;
import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import com.centit.support.database.utils.QueryAndNamedParams;
import com.centit.support.database.utils.QueryUtils;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author codefan@sina.com
 */
@Repository(value = "ddeTaskLogDao")
public class TaskLogDaoImpl extends BaseDaoImpl<TaskLog, Long> implements TaskLogDao {

    @Override
    public Map<String, String> getFilterField() {
        Map<String, String> filterField = new HashMap<>(10);

        filterField.put("logId", CodeBook.EQUAL_HQL_ID);
        filterField.put("taskId", CodeBook.EQUAL_HQL_ID);
        filterField.put("runBeginTime", "runBeginTime>=?");
        filterField.put("runBeginTime2", "runBeginTime<=?");
        filterField.put("runEndTime", CodeBook.LIKE_HQL_ID);
        filterField.put("runType", CodeBook.EQUAL_HQL_ID);
        filterField.put("runner", CodeBook.LIKE_HQL_ID);
        filterField.put("otherMessage", CodeBook.LIKE_HQL_ID);
        filterField.put("successPieces", CodeBook.LIKE_HQL_ID);
        filterField.put("errorPieces", CodeBook.LIKE_HQL_ID);
        return filterField;
    }

    @Override
    public Map<String, Object> getLogStatisticsInfo(Map<String, Object> queryparameter) {
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
        JSONArray dataList = DatabaseOptUtils.listObjectsByNamedSqlAsJson(this, params.getQuery(), params.getParams());
        //饼图统计
        String  pieSql=
            " SELECT 'day' as time, other_message as message,count(*) as num FROM d_task_log " +
                " where to_days(run_begin_time) = to_days(now()) " +
                " and other_message is not NULL " +
                " [:packetId | AND task_id=:packetId ] " +
                " [:optId | AND OPT_ID=:optId] "+
                " GROUP BY other_message " +
                " UNION ALL" +
                " SELECT 'weeks' as time, other_message as message,count(*) as num FROM d_task_log " +
                " where DATE_SUB(CURDATE(), INTERVAL 7 DAY) <= date(run_begin_time) " +
                " and other_message is not NULL  " +
                " [:packetId | AND task_id=:packetId ] " +
                " [:optId | AND OPT_ID=:optId] "+
                " GROUP BY other_message" +
                " UNION ALL" +
                " SELECT 'month' as time, other_message as message, count(*) as num FROM d_task_log " +
                " where DATE_SUB(CURDATE(), INTERVAL 30 DAY) <= date(run_begin_time) " +
                " and other_message is not NULL " +
                " [:packetId | AND task_id=:packetId ] " +
                " [:optId | AND OPT_ID=:optId] "+
                " GROUP BY other_message";
        QueryAndNamedParams pieData = QueryUtils.translateQuery(pieSql, queryparameter);
        JSONArray pieDataList = DatabaseOptUtils.listObjectsByNamedSqlAsJson(this, pieData.getQuery(), pieData.getParams());
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("brokenLineData",dataList);
        hashMap.put("pieData",pieDataList);
        return hashMap;
    }

    @Override
    public int deleteTaskLog(String packetId, Date runBeginTime, boolean isError) {
        StringBuilder sql=new StringBuilder("delete from d_task_log  where task_id = ? AND run_begin_time <= ? ");
        if (! isError){
            sql.append(" AND (error_pieces is null or error_pieces = 0) ");
        }
        return DatabaseOptUtils.doExecuteSql(this, sql.toString(),
            new Object[]{packetId, runBeginTime});
    }

}
