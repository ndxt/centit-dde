package com.centit.app.dao;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.centit.app.po.Publicinfolog;
import com.centit.core.dao.BaseDaoImpl;
import com.centit.core.dao.CodeBook;

public class PublicinfologDao extends BaseDaoImpl<Publicinfolog> {
    private static final long serialVersionUID = 1L;
    public static final Log log = LogFactory.getLog(PublicinfologDao.class);

    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<String, String>();

            filterField.put("usercode", CodeBook.EQUAL_HQL_ID);

            filterField.put("infocode", CodeBook.EQUAL_HQL_ID);


            filterField.put("operation", CodeBook.LIKE_HQL_ID);

            filterField.put("data1", CodeBook.LIKE_HQL_ID);

            filterField.put("data2", CodeBook.LIKE_HQL_ID);

        }
        return filterField;
    }
}
