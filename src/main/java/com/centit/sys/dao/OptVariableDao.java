package com.centit.sys.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.centit.core.dao.BaseDaoImpl;
import com.centit.core.dao.CodeBook;
import com.centit.sys.po.OptVariable;

public class OptVariableDao extends BaseDaoImpl<OptVariable> {
    private static final long serialVersionUID = 1L;

    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<String, String>();
            filterField.put("OPTID", CodeBook.EQUAL_HQL_ID);
            filterField.put("VARDESC", CodeBook.LIKE_HQL_ID);
            filterField.put("ISVALID", CodeBook.EQUAL_HQL_ID);

        }
        return filterField;
    }

    @SuppressWarnings("unchecked")
    public List<OptVariable> getVarByOptid(String optid) {
        return getHibernateTemplate().find("FROM OptVariable WHERE optid=?", optid);
    }

}
