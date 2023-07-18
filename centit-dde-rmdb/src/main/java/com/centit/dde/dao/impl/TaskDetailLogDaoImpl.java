package com.centit.dde.dao.impl;

import com.centit.dde.adapter.dao.TaskDetailLogDao;
import com.centit.dde.adapter.po.TaskDetailLog;
import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author codefan@sina.com
 */
@Repository("taskDetailLogDao")
public class TaskDetailLogDaoImpl extends BaseDaoImpl<TaskDetailLog, Long> implements TaskDetailLogDao {

    @Override
    public Map<String, String> getFilterField() {
        Map<String, String> filterField = new HashMap<>(10);

        filterField.put("logDetailId", CodeBook.EQUAL_HQL_ID);
        filterField.put("taskId", CodeBook.LIKE_HQL_ID);
        filterField.put("logId", CodeBook.LIKE_HQL_ID);
        filterField.put("logType", CodeBook.LIKE_HQL_ID);
        filterField.put("runBeginTime", "runBeginTime>=?");
        filterField.put("runBeginTime2", "runBeginTime<=?");
        filterField.put("runEndTime", CodeBook.LIKE_HQL_ID);
        filterField.put("logInfo", CodeBook.LIKE_HQL_ID);
        filterField.put("successPieces", CodeBook.LIKE_HQL_ID);
        filterField.put("errorPieces", CodeBook.LIKE_HQL_ID);
        return filterField;
    }

    @Override
    public int delTaskDetailLog(String packetId, Date runBeginTime, boolean isError) {
        StringBuilder sqlDetail = new StringBuilder("delete from d_task_detail_log  where task_id=? AND run_begin_time <= ? ");
        if (!isError){
            sqlDetail.append(" AND log_info = 'ok' ");
        }
        return DatabaseOptUtils.doExecuteSql(this, sqlDetail.toString(),
            new Object[]{packetId, runBeginTime});
    }
}
