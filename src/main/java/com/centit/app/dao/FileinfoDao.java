package com.centit.app.dao;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.centit.app.po.Fileinfo;
import com.centit.core.dao.BaseDaoImpl;
import com.centit.core.dao.CodeBook;

public class FileinfoDao extends BaseDaoImpl<Fileinfo> {
    private static final long serialVersionUID = 1L;
    public static final Log log = LogFactory.getLog(FileinfoDao.class);

    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<String, String>();

            filterField.put("filecode", CodeBook.EQUAL_HQL_ID);


            filterField.put("filename", CodeBook.LIKE_HQL_ID);

            filterField.put("path", CodeBook.LIKE_HQL_ID);

        }
        return filterField;
    }
}
