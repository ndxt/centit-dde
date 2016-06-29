package com.centit.app.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;

import com.centit.app.po.InnermsgRecipient;
import com.centit.core.dao.BaseDaoImpl;
import com.centit.core.dao.CodeBook;
import com.centit.core.utils.PageDesc;

public class InnermsgRecipientDao extends BaseDaoImpl<InnermsgRecipient> {
    private static final long serialVersionUID = 1L;
    public static final Log log = LogFactory.getLog(InnermsgRecipientDao.class);

    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<String, String>();

            filterField.put("msgtitle", "innermsg.msgtitle like ?");

            filterField.put("sender", "innermsg.sender like ?");

            filterField.put("begsenddate", "innermsg.senddate >= to_date(?,'yyyy-mm-dd')");

            filterField.put("endsenddate", "innermsg.senddate <= to_date(?,'yyyy-mm-dd')");

            filterField.put("receive", CodeBook.EQUAL_HQL_ID);

            filterField.put("replymsgcode", CodeBook.LIKE_HQL_ID);

            filterField.put("receivetype", CodeBook.LIKE_HQL_ID);

            filterField.put("mailtype", CodeBook.LIKE_HQL_ID);

            filterField.put("msgstate", CodeBook.LIKE_HQL_ID);

            filterField.put(CodeBook.ORDER_BY_HQL_ID, "innermsg.senddate desc");

        }
        return filterField;
    }

    @SuppressWarnings("unchecked")
    public List<InnermsgRecipient> listRecipient(InnermsgRecipient ir, PageDesc pageDesc) {
        DetachedCriteria criteria = DetachedCriteria.forClass(InnermsgRecipient.class);
        criteria.add(Example.create(ir));

        List<InnermsgRecipient> result = super.getHibernateTemplate().findByCriteria(criteria, pageDesc.getPageNo(),
                pageDesc.getPageSize());

        // pageDesc.setTotalRows((Integer) super.getHibernateTemplate()
        // .findByCriteria(criteria.setProjection(Projections.rowCount())).get(0));
        return result;
    }

    /**
     * 生成内部消息编码
     *
     * @return String
     */
    public String getNextMsgRecipiCode() {
        return String.valueOf(this.getNextValueOfSequence("S_MSGRECIPICODE"));

    }

    public void delete(InnermsgRecipient ir) {
        final String hql = "delete from InnermsgRecipient ir where ir.innermsg = ? and ir.receive = ?";

        this.doExecuteHql(hql, new Object[]{ir.getInnermsg(), ir.getReceive()});
    }

    public void saveBatch(List<InnermsgRecipient> irs) {
        for (int i = 0; i < irs.size(); i++) {
            this.save(irs.get(i));
            if (0 == i % 20) {
                this.getHibernateTemplate().flush();
                this.getHibernateTemplate().clear();
            }
        }

    }

    /**
     * 计算此消息持有人数
     *
     * @param ir
     * @return
     */
    public long countHoldUsers(InnermsgRecipient ir) {
        final String hql = "select count(*) from InnermsgRecipient ir where ir.innermsg.msgcode = ?";

        return (Long) this.findObjectsByHql(hql, new Object[]{ir.getInnermsg().getMsgcode()}).get(0);
    }

    /**
     * 删除指定MsgCode消息
     *
     * @param msgCode
     */
    public void deleteAllMsgByMsgCode(String msgCode) {
        final String hql = "delete from InnermsgRecipient ir where ir.cid.msgcode = ?";

        this.doExecuteHql(hql, new Object[]{msgCode});
    }
}
