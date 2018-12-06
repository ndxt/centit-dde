package com.centit.dde.dao;

import com.centit.dde.po.DataOptStep;
import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class DataOptStepDao extends BaseDaoImpl<DataOptStep,Long> {
    public static final Log log = LogFactory.getLog(DataOptStepDao.class);
    @Override
    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<String, String>();

            filterField.put("optStepId", CodeBook.EQUAL_HQL_ID);


            filterField.put("importId", CodeBook.LIKE_HQL_ID);

            filterField.put("optType", CodeBook.LIKE_HQL_ID);

            filterField.put("dataOptId", CodeBook.LIKE_HQL_ID);

            filterField.put("osId", CodeBook.LIKE_HQL_ID);

            filterField.put("mapinfoOrder", CodeBook.LIKE_HQL_ID);

        }
        return filterField;
    }
}
