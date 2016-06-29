package com.centit.app.dao;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.centit.app.po.OaReply;
import com.centit.core.dao.BaseDaoImpl;
import com.centit.core.dao.CodeBook;

public class OaReplyDao extends BaseDaoImpl<OaReply> {
    private static final long serialVersionUID = 1L;
    public static final Log log = LogFactory.getLog(OaReplyDao.class);

    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<String, String>();

            filterField.put("replyid", CodeBook.EQUAL_HQL_ID);


            filterField.put("threadid", CodeBook.LIKE_HQL_ID);

            filterField.put("reply", CodeBook.LIKE_HQL_ID);

            filterField.put("replytime", CodeBook.LIKE_HQL_ID);

            filterField.put("userid", CodeBook.LIKE_HQL_ID);

            filterField.put("username", CodeBook.LIKE_HQL_ID);

        }
        return filterField;
    }
}
