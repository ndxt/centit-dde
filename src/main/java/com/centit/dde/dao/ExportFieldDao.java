package com.centit.dde.dao;

import com.centit.dde.po.ExportField;
import com.centit.dde.po.ExportFieldId;
import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.hibernate.dao.BaseDaoImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class ExportFieldDao extends BaseDaoImpl<ExportField,ExportFieldId> {

    public static final Log log = LogFactory.getLog(ExportFieldDao.class);
    @Override
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
