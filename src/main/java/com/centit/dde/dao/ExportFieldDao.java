package com.centit.dde.dao;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.centit.core.dao.BaseDaoImpl;
import com.centit.core.dao.CodeBook;
import com.centit.dde.po.ExportField;

public class ExportFieldDao extends BaseDaoImpl<ExportField> {
    private static final long serialVersionUID = 1L;
    public static final Log log = LogFactory.getLog(ExportFieldDao.class);

    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<String, String>();

            filterField.put("exportId", CodeBook.EQUAL_HQL_ID);

            filterField.put("columnNo", CodeBook.EQUAL_HQL_ID);


            filterField.put("fieldName", CodeBook.LIKE_HQL_ID);

            filterField.put("fieldSentence", CodeBook.LIKE_HQL_ID);

            filterField.put("fieldType", CodeBook.LIKE_HQL_ID);

            filterField.put("fieldFormat", CodeBook.LIKE_HQL_ID);

            filterField.put("fieldStoreType", CodeBook.LIKE_HQL_ID);

        }
        return filterField;
    }
}
