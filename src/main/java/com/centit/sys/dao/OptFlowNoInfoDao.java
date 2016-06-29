package com.centit.sys.dao;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.centit.core.dao.BaseDaoImpl;
import com.centit.core.dao.CodeBook;
import com.centit.sys.po.OptFlowNoInfo;

public class OptFlowNoInfoDao extends BaseDaoImpl<OptFlowNoInfo> {
    private static final long serialVersionUID = 1L;
    public static final Log log = LogFactory.getLog(OptFlowNoInfoDao.class);

    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<String, String>();

            filterField.put("ownerCode", CodeBook.EQUAL_HQL_ID);

            filterField.put("codeDate", CodeBook.EQUAL_HQL_ID);

            filterField.put("codeCode", CodeBook.EQUAL_HQL_ID);


            filterField.put("curNo", CodeBook.LIKE_HQL_ID);

            filterField.put("lastCodeDate", CodeBook.LIKE_HQL_ID);

        }
        return filterField;
    }
}
