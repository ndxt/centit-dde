package com.centit.dde.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.centit.dde.po.DatabaseInfo;
import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.hibernate.dao.BaseDaoImpl;

public class DatabaseInfoDao extends BaseDaoImpl<DatabaseInfo> {
    private static final long serialVersionUID = 1L;
    public static final Log log = LogFactory.getLog(DatabaseInfoDao.class);

    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<String, String>();

            filterField.put("databaseName", CodeBook.EQUAL_HQL_ID);

            filterField.put("databaseNames", CodeBook.EQUAL_HQL_ID);

            filterField.put("databaseType", CodeBook.LIKE_HQL_ID);

            filterField.put("hostPort", CodeBook.LIKE_HQL_ID);

            filterField.put("databaseUrl", CodeBook.LIKE_HQL_ID);

            filterField.put("username", CodeBook.LIKE_HQL_ID);

            filterField.put("password", CodeBook.LIKE_HQL_ID);

            filterField.put("dataDesc", CodeBook.LIKE_HQL_ID);

            filterField.put("createTime", CodeBook.LIKE_HQL_ID);

            filterField.put("created", CodeBook.LIKE_HQL_ID);

        }
        return filterField;
    }

    public boolean connectionTest(DatabaseInfo databaseInfo) {
        return DirectConn.testConn(
                new DirectConnDB(
                        databaseInfo.getDatabaseUrl(),
                        databaseInfo.getUsername(),
                        databaseInfo.getPassword()));
    }

    public List<Object> listDatabase() {
        return (List<Object>) this.findObjectsByHql("select t.databaseName from DatabaseInfo t");
    }
}
