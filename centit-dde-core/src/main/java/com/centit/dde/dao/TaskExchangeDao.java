package com.centit.dde.dao;

import com.centit.dde.po.TaskExchange;
import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName TaskExchangeDao
 * @Date 2019/3/20 10:51
 * @Version 1.0
 */
@Repository
public class TaskExchangeDao extends BaseDaoImpl<TaskExchange,Long> {

    public static final Log logger = LogFactory.getLog(TaskExchangeDao.class);

    @Override
    public Map<String, String> getFilterField() {
        Map<String, String> filterField = new HashMap<String, String>();
        filterField.put("taskId", CodeBook.EQUAL_HQL_ID);
        filterField.put("packetId", CodeBook.LIKE_HQL_ID);
        filterField.put("taskName", CodeBook.LIKE_HQL_ID);
        filterField.put("taskType", CodeBook.LIKE_HQL_ID);
        filterField.put("taskCron", CodeBook.LIKE_HQL_ID);
        filterField.put("taskDesc", CodeBook.LIKE_HQL_ID);
        filterField.put("lastRunTime", CodeBook.LIKE_HQL_ID);
        filterField.put("nextRunTime", CodeBook.LIKE_HQL_ID);
        filterField.put("isValid", "logId in (select a.logId " +
            "from TaskDetailLog a inner join a.taskErrorDatas b " +
            "where a.errorPieces>?)");

        filterField.put("createTime", CodeBook.LIKE_HQL_ID);
        filterField.put("created", CodeBook.LIKE_HQL_ID);
        filterField.put("lastUpdateTime", CodeBook.LIKE_HQL_ID);

        return filterField;
    }
}
