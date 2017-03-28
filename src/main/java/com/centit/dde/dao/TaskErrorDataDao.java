package com.centit.dde.dao;

import com.centit.dde.po.TaskErrorData;
import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.hibernate.dao.BaseDaoImpl;
import com.centit.framework.hibernate.dao.DatabaseOptUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class TaskErrorDataDao extends BaseDaoImpl<TaskErrorData,Long> {

    public static final Log logger = LogFactory.getLog(TaskErrorDataDao.class);
    @Override
    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<String, String>();

            filterField.put("dataId", CodeBook.EQUAL_HQL_ID);


            filterField.put("logId", CodeBook.LIKE_HQL_ID);

            filterField.put("dataContent", CodeBook.LIKE_HQL_ID);

            filterField.put("errorMessage", CodeBook.LIKE_HQL_ID);

            filterField.put(CodeBook.ORDER_BY_HQL_ID, "dataId");

        }
        return filterField;
    }

    public Long getTaskErrorId() {
        return DatabaseOptUtils.getNextLongSequence(this,"D_TASKERRORID");
    }

    public void saveTaskErrorData(TaskErrorData taskErrorData) {
       /* String sql = "insert into D_TASK_ERROR_DATA(data_id,log_id,data_content,error_message) values ('"
	                 +taskErrorData.getDataId()+"','"+taskErrorData.getLogId()+"','"+taskErrorData.getDataContent()+"','"
	                 +taskErrorData.getErrorMessage()+
	    		     "')";*/

        saveObject(taskErrorData);
    }

}
