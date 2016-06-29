package com.centit.sys.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.centit.core.dao.BaseDaoImpl;
import com.centit.core.dao.CodeBook;
import com.centit.sys.po.FDatacatalog;

public class DataCatalogDao extends BaseDaoImpl<FDatacatalog> {
    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unchecked")
    public List<FDatacatalog> getSysCdctgs() {
        return getHibernateTemplate().find("FROM FDatacatalog fDatacatalog WHERE CATALOGSTYLE='S'");

    }

    @SuppressWarnings("unchecked")
    public List<FDatacatalog> getUserCdctgs() {
        return getHibernateTemplate().find("FROM FDatacatalog fDatacatalog WHERE CATALOGSTYLE='U'");
    }

    @SuppressWarnings("unchecked")
    public List<FDatacatalog> getGBCdctgs() {
        return getHibernateTemplate().find("FROM FDatacatalog fDatacatalog WHERE CATALOGSTYLE='G'");

    }

    //规定在List事件中只能通过这三个字段进行查询
    @Override
    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<String, String>();
            filterField.put("CATALOGCODE", CodeBook.LIKE_HQL_ID);
            filterField.put("CATALOGNAME", CodeBook.LIKE_HQL_ID);
            filterField.put("CATALOGSTYLE", CodeBook.EQUAL_HQL_ID);
            filterField.put(CodeBook.ORDER_BY_HQL_ID, "createDate desc");
        }
        return filterField;
    }
}
