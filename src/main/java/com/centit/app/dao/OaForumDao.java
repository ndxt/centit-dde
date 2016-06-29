package com.centit.app.dao;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.centit.app.po.OaForum;
import com.centit.core.dao.BaseDaoImpl;
import com.centit.core.dao.CodeBook;

public class OaForumDao extends BaseDaoImpl<OaForum> {
    private static final long serialVersionUID = 1L;
    public static final Log log = LogFactory.getLog(OaForumDao.class);

    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<String, String>();

            filterField.put("forumid", CodeBook.EQUAL_HQL_ID);


            filterField.put("boardid", CodeBook.LIKE_HQL_ID);

            filterField.put("forumname", CodeBook.LIKE_HQL_ID);

            filterField.put("forumpic", CodeBook.LIKE_HQL_ID);

            filterField.put("announcement", CodeBook.LIKE_HQL_ID);

            filterField.put("joinright", CodeBook.LIKE_HQL_ID);

            filterField.put("viewright", CodeBook.LIKE_HQL_ID);

            filterField.put("postright", CodeBook.LIKE_HQL_ID);

            filterField.put("replyright", CodeBook.LIKE_HQL_ID);

            filterField.put("isforumer", CodeBook.LIKE_HQL_ID);

            filterField.put("createtime", CodeBook.LIKE_HQL_ID);

            filterField.put("mebernum", CodeBook.LIKE_HQL_ID);

            filterField.put("threadnum", CodeBook.LIKE_HQL_ID);

            filterField.put("replynum", CodeBook.LIKE_HQL_ID);

        }
        return filterField;
    }


    /**
     * 生成内部消息编码
     *
     * @return long
     */
    public long getNextForumID() {
        String sForumID = getNextValueOfSequence("S_FORUMID");
        return Long.valueOf(sForumID);
    }

    public void saveObject(OaForum oaFornum) {
        if (oaFornum.getForumid() == null || oaFornum.getForumid() == 0) {
            oaFornum.setForumid(getNextForumID());
        }
        super.saveObject(oaFornum);
    }

}
