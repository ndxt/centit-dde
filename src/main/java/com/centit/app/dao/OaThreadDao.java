package com.centit.app.dao;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.centit.app.po.OaThread;
import com.centit.core.dao.BaseDaoImpl;
import com.centit.core.dao.CodeBook;

public class OaThreadDao extends BaseDaoImpl<OaThread> {
    private static final long serialVersionUID = 1L;
    public static final Log log = LogFactory.getLog(OaThreadDao.class);

    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<String, String>();

            filterField.put("threadid", CodeBook.EQUAL_HQL_ID);


            filterField.put("forumid", CodeBook.LIKE_HQL_ID);

            filterField.put("titol", CodeBook.LIKE_HQL_ID);

            filterField.put("content", CodeBook.LIKE_HQL_ID);

            filterField.put("wirterid", CodeBook.LIKE_HQL_ID);

            filterField.put("wirter", CodeBook.LIKE_HQL_ID);

            filterField.put("posttime", CodeBook.LIKE_HQL_ID);

            filterField.put("viewnum", CodeBook.LIKE_HQL_ID);

            filterField.put("replnum", CodeBook.LIKE_HQL_ID);

        }
        return filterField;
    }
}
