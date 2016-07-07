package com.centit.dde.dao;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.centit.dde.po.TaskDetailLog;
import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.hibernate.dao.BaseDaoImpl;
import com.centit.framework.hibernate.dao.DatabaseOptUtils;

public class TaskDetailLogDao extends BaseDaoImpl<TaskDetailLog,Long> {

    public static final Log logger = LogFactory.getLog(TaskDetailLogDao.class);

    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<String, String>();

            filterField.put("logDetailId", CodeBook.EQUAL_HQL_ID);


            filterField.put("logId", CodeBook.LIKE_HQL_ID);

            filterField.put("runBeginTime", CodeBook.LIKE_HQL_ID);

            filterField.put("runEndTime", CodeBook.LIKE_HQL_ID);

            filterField.put("mapinfoId", CodeBook.LIKE_HQL_ID);

            filterField.put("successPieces", CodeBook.LIKE_HQL_ID);

            filterField.put("errorPieces", CodeBook.LIKE_HQL_ID);

        }
        return filterField;
    }

    public Long getTaskDetailLogId() {
        return DatabaseOptUtils.getNextLongSequence(this,"D_TASKDETAILLOGID");
    }

}
