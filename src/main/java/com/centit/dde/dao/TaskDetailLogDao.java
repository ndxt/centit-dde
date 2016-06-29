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
import com.centit.dde.po.TaskDetailLog;

public class TaskDetailLogDao extends BaseDaoImpl<TaskDetailLog> {
    private static final long serialVersionUID = 1L;
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
        return this.getNextLongSequence("D_TASKDETAILLOGID");
    }

    @Override
    public void saveObject(final TaskDetailLog o) {
        getHibernateTemplate().execute(new HibernateCallback<TaskDetailLog>() {
            @Override
            public TaskDetailLog doInHibernate(Session session) throws HibernateException, SQLException {
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
