package com.centit.dde.dao;

import com.centit.dde.po.UserDataOptId;
import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.hibernate.dao.BaseDaoImpl;
import com.centit.framework.hibernate.dao.DatabaseOptUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UserDataOptIdDao extends BaseDaoImpl<UserDataOptId,Long> {
    public static final Log log = LogFactory.getLog(UserDataOptId.class);

    @Override
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
            o.setUdId(DatabaseOptUtils.getNextLongSequence(this,"S_USER_DATAOPTID"));
        }
        super.saveObject(o);
    }
}
