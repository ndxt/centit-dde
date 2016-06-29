package com.centit.sys.dao;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.centit.core.dao.BaseDaoImpl;
import com.centit.core.dao.CodeBook;
import com.centit.sys.po.ScaffoldTest;

public class ScaffoldTestDao extends BaseDaoImpl<ScaffoldTest> {
    private static final long serialVersionUID = 1L;
    public static final Log log = LogFactory.getLog(ScaffoldTestDao.class);

    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<String, String>();

            filterField.put("logid", CodeBook.EQUAL_HQL_ID);


            filterField.put("loglevel", CodeBook.LIKE_HQL_ID);

            filterField.put("usercode", CodeBook.LIKE_HQL_ID);

            filterField.put("opttime", CodeBook.LIKE_HQL_ID);

            filterField.put("optid", CodeBook.LIKE_HQL_ID);

            filterField.put("optcode", CodeBook.LIKE_HQL_ID);

            filterField.put("optcontent", CodeBook.LIKE_HQL_ID);

            filterField.put("oldvalue", CodeBook.LIKE_HQL_ID);

        }
        return filterField;
    }
}
