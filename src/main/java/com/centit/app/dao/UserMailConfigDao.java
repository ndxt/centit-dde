package com.centit.app.dao;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import com.centit.app.po.UserMailConfig;
import com.centit.core.dao.BaseDaoImpl;
import com.centit.core.dao.CodeBook;

public class UserMailConfigDao extends BaseDaoImpl<UserMailConfig> {
    private static final long serialVersionUID = 1L;
    public static final Log log = LogFactory.getLog(UserMailConfigDao.class);

    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<String, String>();

            filterField.put("emailid", CodeBook.EQUAL_HQL_ID);

            filterField.put("usercode", CodeBook.EQUAL_HQL_ID);

            filterField.put("host", CodeBook.LIKE_HQL_ID);

            filterField.put("mailaccount", CodeBook.LIKE_HQL_ID);


            filterField.put("mailaccountEQ", "mailaccount = ?");

            filterField.put("mailpassword", CodeBook.LIKE_HQL_ID);

            filterField.put("mailtype", CodeBook.LIKE_HQL_ID);

            filterField.put("smtpurl", CodeBook.LIKE_HQL_ID);

            filterField.put("smtpport", CodeBook.LIKE_HQL_ID);

            filterField.put("pop3url", CodeBook.LIKE_HQL_ID);

            filterField.put("pop3port", CodeBook.LIKE_HQL_ID);

            filterField.put("intervaltime", CodeBook.LIKE_HQL_ID);

            filterField.put("retaindays", CodeBook.LIKE_HQL_ID);

        }
        return filterField;
    }

    @Override
    public void saveObject(UserMailConfig o) {
        if (!StringUtils.hasText(o.getEmailid())) {
            o.setEmailid(this.getNextMsgCode());
        }
        super.saveObject(o);
    }

    public String getNextMsgCode() {
        return String.valueOf(this.getNextValueOfSequence("S_MSGCODE"));

    }
}
