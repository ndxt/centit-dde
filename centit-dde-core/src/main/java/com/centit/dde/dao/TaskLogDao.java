package com.centit.dde.dao;

import com.centit.dde.po.TaskLog;
import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhf
 */
@Repository(value = "ddeTaskLogDao")
public class TaskLogDao extends BaseDaoImpl<TaskLog, Long> {

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

    public void saveNewLog(TaskLog log){
        this.saveNewObject(log);
        log.setHasSaved(true);
    }

}
