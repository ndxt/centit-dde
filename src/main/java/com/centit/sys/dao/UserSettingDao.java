package com.centit.sys.dao;

import java.util.HashMap;
import java.util.Map;

import com.centit.core.dao.BaseDaoImpl;
import com.centit.core.dao.CodeBook;
import com.centit.sys.po.Usersetting;

public class UserSettingDao extends BaseDaoImpl<Usersetting> {
    private static final long serialVersionUID = 1L;

    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<String, String>();

            filterField.put("usercode", CodeBook.EQUAL_HQL_ID);


            filterField.put("framelayout", CodeBook.LIKE_HQL_ID);

            filterField.put("menustyle", CodeBook.LIKE_HQL_ID);

            filterField.put("pagestyle", CodeBook.LIKE_HQL_ID);

            filterField.put("mainpage", CodeBook.LIKE_HQL_ID);

            filterField.put("linesperpage", CodeBook.LIKE_HQL_ID);

            filterField.put("boardlayout", CodeBook.LIKE_HQL_ID);

            filterField.put("favorurl1", CodeBook.LIKE_HQL_ID);

            filterField.put("favorurl2", CodeBook.LIKE_HQL_ID);

            filterField.put("favorurl3", CodeBook.LIKE_HQL_ID);

            filterField.put("favorurl4", CodeBook.LIKE_HQL_ID);

            filterField.put("favorurl5", CodeBook.LIKE_HQL_ID);

            filterField.put("favorurl6", CodeBook.LIKE_HQL_ID);

            filterField.put("favorurl7", CodeBook.LIKE_HQL_ID);

            filterField.put("favorurl8", CodeBook.LIKE_HQL_ID);

            filterField.put("favorurl9", CodeBook.LIKE_HQL_ID);

        }
        return filterField;
    }
}
