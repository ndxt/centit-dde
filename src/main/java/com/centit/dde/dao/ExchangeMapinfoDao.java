package com.centit.dde.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.centit.core.dao.BaseDaoImpl;
import com.centit.core.dao.CodeBook;
import com.centit.core.utils.PageDesc;
import com.centit.dde.po.ExchangeMapinfo;

public class ExchangeMapinfoDao extends BaseDaoImpl<ExchangeMapinfo> {
    private static final long serialVersionUID = 1L;
    public static final Log log = LogFactory.getLog(ExchangeMapinfoDao.class);

    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<String, String>();

            filterField.put("mapinfoId", CodeBook.EQUAL_HQL_ID);

            filterField.put("mapinfoIdNot", "mapinfoId not in (select e.cid.mapinfoId from ExchangeTaskdetail e where e.cid.taskId=? )");

            filterField.put("destDatabaseName", CodeBook.LIKE_HQL_ID);

            filterField.put("mapinfoName", CodeBook.LIKE_HQL_ID);
            filterField.put("mapinfoNameEq", "mapinfoName = ?");

            filterField.put("querySql", CodeBook.LIKE_HQL_ID);

            filterField.put("sourceDatabaseName", CodeBook.LIKE_HQL_ID);

            filterField.put("destTablename", CodeBook.LIKE_HQL_ID);

            filterField.put("isRepeat", CodeBook.LIKE_HQL_ID);

            filterField.put("mapinfoDesc", CodeBook.LIKE_HQL_ID);

            filterField.put(CodeBook.ORDER_BY_HQL_ID, "mapinfoId");

        }
        return filterField;
    }

    public List<String> listDatabaseName() {
        return (List<String>) this.findObjectsByHql("select t.databaseName from DatabaseInfo t");
        //return this.getHibernateTemplate().getSessionFactory().openSession().createSQLQuery("select t.databaseName from DatabaseInfo t").list();
    }

    public List<ExchangeMapinfo> listImportExchangeMapinfo(List<Long> mapinfoId) {
        if (mapinfoId.size() == 0) {
            mapinfoId.add(Long.valueOf(-1));
        }
        String hql = "from ExchangeMapinfo e where e.mapinfoId not in (?)";
        return this.listObjects(hql, (Object) mapinfoId);
    }

    public Long getNextLongSequence() {
        return getNextLongSequence("D_MAPINFOID");
    }


    public void flush() {
        getHibernateTemplate().flush();
        getHibernateTemplate().clear();
    }


    public List<ExchangeMapinfo> listObjectExcludeUsed(final Map<String, Object> filterMap, final PageDesc pageDesc) {
        final String hql = "from ExchangeMapinfo ma where not exists (select m.mapinfoId from ExchangeMapinfo m, ExchangeTask t " +
                " join m.exchangeTaskdetails etd " +
                " where ma.mapinfoId = m.mapinfoId and etd.cid.mapinfoId = m.mapinfoId and etd.cid.taskId = t.taskId" +
                " and etd.cid.taskId = :taskId )";


        return getHibernateTemplate().executeFind(new HibernateCallback<List<ExchangeMapinfo>>() {
            @Override
            public List<ExchangeMapinfo> doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createQuery("select count(ma.mapinfoId) " + hql);
                query.setParameter("taskId", filterMap.get("taskId"));
                pageDesc.setTotalRows(((Long) query.uniqueResult()).intValue());


                query = session.createQuery("select ma " + hql).setParameter("taskId", filterMap.get("taskId"))
                        .setFirstResult(pageDesc.getRowStart()).setMaxResults(pageDesc.getPageSize());

                return query.list();
            }
        });

    }
}
