package com.centit.dde.dao;

import com.centit.dde.po.ImportField;
import com.centit.dde.po.ImportFieldId;
import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.hibernate.dao.BaseDaoImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class ImportFieldDao extends BaseDaoImpl<ImportField,ImportFieldId> {
    public static final Log log = LogFactory.getLog(ImportFieldDao.class);
    @Override
    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<String, String>();

            filterField.put("columnNo", CodeBook.EQUAL_HQL_ID);

            filterField.put("importId", CodeBook.EQUAL_HQL_ID);


            filterField.put("sourceFieldName", CodeBook.LIKE_HQL_ID);

            filterField.put("destFieldName", CodeBook.LIKE_HQL_ID);

            filterField.put("destFieldType", CodeBook.LIKE_HQL_ID);

            filterField.put("isPk", CodeBook.LIKE_HQL_ID);

            filterField.put("destFieldDefault", CodeBook.LIKE_HQL_ID);

            filterField.put("isNull", CodeBook.LIKE_HQL_ID);

        }
        return filterField;
    }
}
