package com.centit.app.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.centit.app.po.Innermsg;
import com.centit.core.dao.BaseDaoImpl;
import com.centit.core.dao.CodeBook;

public class InnermsgDao extends BaseDaoImpl<Innermsg> {
    private static final long serialVersionUID = 1L;

    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<String, String>();

            filterField.put("msgcode", CodeBook.EQUAL_HQL_ID);

            filterField.put("sender", CodeBook.EQUAL_HQL_ID);

            filterField.put("begsenddate", "senddate >= to_date(?,'yyyy-mm-dd')");

            filterField.put("endsenddate", "senddate <= to_date(?,'yyyy-mm-dd')");

            filterField.put("replymsgcode", CodeBook.LIKE_HQL_ID);

            filterField.put("msgtitle", CodeBook.LIKE_HQL_ID);

            filterField.put("receivetype", CodeBook.LIKE_HQL_ID);

            filterField.put("receivename", CodeBook.LIKE_HQL_ID);

            filterField.put("msgstate", CodeBook.EQUAL_HQL_ID);

            filterField.put("msgstateNoEq", "msgstate <> ?");

            filterField.put("msgtype", CodeBook.EQUAL_HQL_ID);
            filterField.put("mailtype", CodeBook.EQUAL_HQL_ID);

            filterField.put("msgcontent", CodeBook.LIKE_HQL_ID);

            filterField.put("emailid", "c.emailid = ?");

            filterField.put(CodeBook.ORDER_BY_HQL_ID, "senddate desc");

        }
        return filterField;
    }

    /**
     * 生成内部消息编码
     *
     * @return String
     */
    public String getNextMsgCode() {
        return String.valueOf(this.getNextValueOfSequence("S_MSGCODE"));

    }

    @Override
    public void saveObject(Innermsg o) {
        if (!StringUtils.hasText(o.getMsgcode())) {
            o.setMsgcode(this.getNextMsgCode());
        }
        super.saveObject(o);
    }

    // /**
    // * 查询某条消息的所有回复信息
    // *
    // * @param replymsgcode
    // * @return
    // */
    // @SuppressWarnings("unchecked")
    // public List<Innermsg> listMsgsByReplycode(Long replymsgcode) {
    // String baseHQL = "from Innermsg where replymsgcode = ? order by senddate asc";
    // return (List<Innermsg>) findObjectsByHql(baseHQL, replymsgcode);
    // }
    //
    // /**
    // * 根据接受人查询邮件
    // *
    // * @param replymsgcode
    // * @return
    // */
    // @SuppressWarnings("unchecked")
    // public List<Innermsg> listMsgsByReceive(String receive) {
    // String baseHQL = "from Innermsg where receive = ? order by senddate desc";
    // return (List<Innermsg>) findObjectsByHql(baseHQL, receive);
    // }
    //
    // @Override
    // public void saveObject(Innermsg innermsg) {
    // // if(innermsg.getMsgcode() == null || innermsg.getMsgcode() == 0){
    // // innermsg.setMsgcode(getNextMsgCode());
    // // }
    // super.saveObject(innermsg);
    // }

}
