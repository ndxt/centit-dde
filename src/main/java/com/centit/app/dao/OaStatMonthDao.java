package com.centit.app.dao;

import java.util.HashMap;
import java.util.Map;

import com.centit.app.po.OaStatMonth;
import com.centit.core.dao.BaseDaoImpl;
import com.centit.core.dao.CodeBook;

public class OaStatMonthDao extends BaseDaoImpl<OaStatMonth> {
    private static final long serialVersionUID = 1L;

    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<String, String>();

            filterField.put("yearMonth", CodeBook.EQUAL_HQL_ID);


            filterField.put("beginDay", CodeBook.LIKE_HQL_ID);

            filterField.put("beginSchedule", CodeBook.LIKE_HQL_ID);

            filterField.put("eendDay", CodeBook.LIKE_HQL_ID);

            filterField.put("endSchedule", CodeBook.LIKE_HQL_ID);

        }
        return filterField;
    }
}
