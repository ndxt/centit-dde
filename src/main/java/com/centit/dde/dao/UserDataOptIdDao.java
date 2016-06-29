package com.centit.dde.dao;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.centit.core.dao.BaseDaoImpl;
import com.centit.core.dao.CodeBook;
import com.centit.dde.po.UserDataOptId;

public class UserDataOptIdDao extends BaseDaoImpl<UserDataOptId> {
    private static final long serialVersionUID = 1L;
    public static final Log log = LogFactory.getLog(UserDataOptId.class);

    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<String, String>();

            filterField.put("udId", CodeBook.EQUAL_HQL_ID);


            filterField.put("usercode", CodeBook.EQUAL_HQL_ID);

            filterField.put("dataoptType", CodeBook.EQUAL_HQL_ID);

            filterField.put("dataOptId", CodeBook.LIKE_HQL_ID);

            filterField.put("interfaceUrl", CodeBook.LIKE_HQL_ID);

            filterField.put("created", CodeBook.LIKE_HQL_ID);

            filterField.put("lastUpdateTime", CodeBook.LIKE_HQL_ID);

            filterField.put("createTime", CodeBook.SELF_ORDER_BY);

        }
        return filterField;
    }

    @Override
    public void saveObject(UserDataOptId o) {
        if (null == o.getUdId()) {
            o.setUdId(getNextLongSequence("S_USER_DATAOPTID"));
        }
        super.saveObject(o);
    }
}
