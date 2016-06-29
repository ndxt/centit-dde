package com.centit.sys.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.centit.core.dao.BaseDaoImpl;
import com.centit.core.dao.CodeBook;
import com.centit.sys.po.OptLog;

public class OptLogDao extends BaseDaoImpl<OptLog> {
    private static final long serialVersionUID = 1L;
    public static final Log log = LogFactory.getLog(OptLogDao.class);

    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<String, String>();

            filterField.put("logid", CodeBook.EQUAL_HQL_ID);

            filterField.put("loglevel", CodeBook.LIKE_HQL_ID);

            filterField.put("usercode", CodeBook.LIKE_HQL_ID);

            filterField.put("begopttime", "opttime >= to_date(?,'yyyy-mm-dd')");

            filterField.put("endopttime", "opttime <= to_date(?,'yyyy-mm-dd')");

            filterField.put("optid", CodeBook.LIKE_HQL_ID);

            filterField.put("eqOptid", "optid = ?");

            filterField.put("optmethod", CodeBook.LIKE_HQL_ID);

            filterField.put("optcontent", CodeBook.LIKE_HQL_ID);

            filterField.put("oldvalue", CodeBook.LIKE_HQL_ID);

            filterField.put(CodeBook.ORDER_BY_HQL_ID, " opttime desc");

        }
        return filterField;
    }


    @SuppressWarnings("unchecked")
    public List<String> listOptIds() {
        final String hql = "select DISTINCT f.optid from OptLog f";


        return (List<String>) super.findObjectsByHql(hql);
    }
}
