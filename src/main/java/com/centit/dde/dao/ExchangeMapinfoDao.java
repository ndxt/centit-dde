package com.centit.dde.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.centit.dde.po.ExchangeMapinfo;
import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.core.dao.PageDesc;
import com.centit.framework.hibernate.dao.BaseDaoImpl;
import com.centit.framework.hibernate.dao.DatabaseOptUtils;
import com.centit.support.database.QueryUtils;

public class ExchangeMapinfoDao extends BaseDaoImpl<ExchangeMapinfo,Long> {
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

    @SuppressWarnings("unchecked")
    public List<String> listDatabaseName() {
        return (List<String>) DatabaseOptUtils.findObjectsByHql(this,
                "select t.databaseName from DatabaseInfo t");
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
        return DatabaseOptUtils.getNextLongSequence(this,"D_MAPINFOID");
    }


    public void flush() {
        DatabaseOptUtils.flush(this.getCurrentSession());
        //getHibernateTemplate().clear();
    }


    @SuppressWarnings("unchecked")
    public List<ExchangeMapinfo> listObjectExcludeUsed(final Map<String, Object> filterMap, final PageDesc pageDesc) {
        final String hql = "from ExchangeMapinfo ma where not exists (select m.mapinfoId from ExchangeMapinfo m, ExchangeTask t " +
                " join m.exchangeTaskdetails etd " +
                " where ma.mapinfoId = m.mapinfoId and etd.cid.mapinfoId = m.mapinfoId and etd.cid.taskId = t.taskId" +
                " and etd.cid.taskId = :taskId )";
       /* pageDesc.setTotalRows( Long.valueOf( DatabaseOptUtils.getSingleIntByHql(this,
                "select count(ma.mapinfoId) " + hql)).intValue()
                );*/
        return (List<ExchangeMapinfo>)DatabaseOptUtils.findObjectsByHql(this, "select ma " + hql,  
                QueryUtils.createSqlParamsMap(
                        "taskId", filterMap.get("taskId")) , pageDesc);
    }
}
