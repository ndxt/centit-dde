package com.centit.dde.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.centit.core.dao.BaseDaoImpl;
import com.centit.core.dao.CodeBook;
import com.centit.dde.po.TaskErrorData;

public class TaskErrorDataDao extends BaseDaoImpl<TaskErrorData> {
    private static final long serialVersionUID = 1L;
    public static final Log logger = LogFactory.getLog(TaskErrorDataDao.class);

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
        return this.getNextLongSequence("D_TASKERRORID");
    }

    public void saveTaskErrorData(TaskErrorData taskErrorData) {
       /* String sql = "insert into D_TASK_ERROR_DATA(data_id,log_id,data_content,error_message) values ('"
	                 +taskErrorData.getDataId()+"','"+taskErrorData.getLogId()+"','"+taskErrorData.getDataContent()+"','"
	                 +taskErrorData.getErrorMessage()+
	    		     "')";*/

        saveObject(taskErrorData);
    }

    @Override
    public void saveObject(final TaskErrorData o) {
        getHibernateTemplate().execute(new HibernateCallback<TaskErrorData>() {
            @Override
            public TaskErrorData doInHibernate(Session session) throws HibernateException, SQLException {
                Transaction tx = session.beginTransaction();
                try {
                    session.saveOrUpdate(o);
                    tx.commit();
                } catch (HibernateException e) {
                    logger.error(e);

                    tx.rollback();
                }
                return o;
            }
        });
    }

    public void flush() {
        getHibernateTemplate().flush();
        getHibernateTemplate().clear();
    }
}
