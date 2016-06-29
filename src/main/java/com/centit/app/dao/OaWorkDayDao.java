package com.centit.app.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.centit.app.po.OaWorkDay;
import com.centit.core.dao.BaseDaoImpl;
import com.centit.core.dao.CodeBook;

public class OaWorkDayDao extends BaseDaoImpl<OaWorkDay> {
    private static final long serialVersionUID = 1L;

    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<String, String>();

            filterField.put("workday", CodeBook.EQUAL_HQL_ID);


            filterField.put("daytype", CodeBook.LIKE_HQL_ID);

            filterField.put("worktimetype", CodeBook.LIKE_HQL_ID);

            filterField.put("workdaydesc", CodeBook.LIKE_HQL_ID);

            filterField.put("hasschedule1", CodeBook.LIKE_HQL_ID);

            filterField.put("hasschedule2", CodeBook.LIKE_HQL_ID);

            filterField.put("hasschedule3", CodeBook.LIKE_HQL_ID);

            filterField.put("hasschedule4", CodeBook.LIKE_HQL_ID);

        }
        return filterField;
    }

    @SuppressWarnings("unchecked")
    public List<OaWorkDay> getListByDate(Date beginTime, Date endTime) {

        String hql = "FROM OaWorkDay WHERE workday BETWEEN ? AND ?";

        return (List<OaWorkDay>) getHibernateTemplate().find(hql, new Object[]{beginTime, endTime});

    }


}
