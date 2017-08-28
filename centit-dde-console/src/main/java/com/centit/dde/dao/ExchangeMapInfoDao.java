package com.centit.dde.dao;

import com.centit.dde.po.ExchangeMapInfo;
import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.core.dao.PageDesc;
import com.centit.framework.hibernate.dao.BaseDaoImpl;
import com.centit.framework.hibernate.dao.DatabaseOptUtils;
import com.centit.support.database.utils.QueryUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ExchangeMapInfoDao extends BaseDaoImpl<ExchangeMapInfo,Long> {
    public static final Log log = LogFactory.getLog(ExchangeMapInfoDao.class);
    @Override
    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<>();

            filterField.put("mapInfoId", CodeBook.EQUAL_HQL_ID);

            filterField.put("mapInfoIdNot", "mapInfoId not in (select e.cid.mapInfoId from ExchangeTaskDetail e where e.cid.taskId=? )");

            filterField.put("destDatabaseName", CodeBook.LIKE_HQL_ID);

            filterField.put("mapInfoName", CodeBook.LIKE_HQL_ID);
            filterField.put("mapInfoNameEq", "mapInfoName = ?");

            filterField.put("querySql", CodeBook.LIKE_HQL_ID);

            filterField.put("sourceDatabaseName", CodeBook.LIKE_HQL_ID);

            filterField.put("destTablename", CodeBook.LIKE_HQL_ID);

            filterField.put("isRepeat", CodeBook.LIKE_HQL_ID);

            filterField.put("mapInfoDesc", CodeBook.LIKE_HQL_ID);

            filterField.put(CodeBook.ORDER_BY_HQL_ID, "mapInfoId");

        }
        return filterField;
    }

    @SuppressWarnings("unchecked")
    public List<String> listDatabaseName() {
        return (List<String>) DatabaseOptUtils.findObjectsByHql(this,
                "select t.databaseName from DatabaseInfo t");
        //return this.getHibernateTemplate().getSessionFactory().openSession().createSQLQuery("select t.databaseName from DatabaseInfo t").list();
    }

    public List<ExchangeMapInfo> listImportExchangeMapinfo(List<Long> mapInfoId) {
        if (mapInfoId.size() == 0) {
            mapInfoId.add(Long.valueOf(-1));
        }
        String hql = "from ExchangeMapInfo e where e.mapInfoId not in (?)";
        return this.listObjects(hql, (Object) mapInfoId);
    }

    public Long getNextLongSequence() {
        return DatabaseOptUtils.getNextLongSequence(this,"D_MAPINFOID");
    }


    public void flush() {
        DatabaseOptUtils.flush(this.getCurrentSession());
        //getHibernateTemplate().clear();
    }


    @SuppressWarnings("unchecked")
    public List<ExchangeMapInfo> listObjectExcludeUsed(final Map<String, Object> filterMap, final PageDesc pageDesc) {
        final String hql = "from ExchangeMapInfo ma where not exists (select m.mapInfoId from ExchangeMapInfo m, ExchangeTask t " +
                " join m.exchangeTaskdetails etd " +
                " where ma.mapInfoId = m.mapInfoId and etd.cid.mapInfoId = m.mapInfoId and etd.cid.taskId = t.taskId" +
                " and etd.cid.taskId = :taskId )";
       /* pageDesc.setTotalRows( Long.valueOf( DatabaseOptUtils.getSingleIntByHql(this,
                "select count(ma.mapInfoId) " + hql)).intValue()
                );*/
        return (List<ExchangeMapInfo>)DatabaseOptUtils.findObjectsByHql(this, "select ma " + hql,
                QueryUtils.createSqlParamsMap(
                        "taskId", filterMap.get("taskId")) , pageDesc);
    }
}
