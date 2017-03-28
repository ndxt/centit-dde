package com.centit.dde.dao;

import com.centit.dde.po.ExchangeTaskdetail;
import com.centit.dde.po.ExchangeTaskdetailId;
import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.hibernate.dao.BaseDaoImpl;
import com.centit.framework.hibernate.dao.DatabaseOptUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
public class ExchangeTaskdetailDao extends BaseDaoImpl<ExchangeTaskdetail,ExchangeTaskdetailId> {

    public static final Log log = LogFactory.getLog(ExchangeTaskdetailDao.class);
    @Override
    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<String, String>();

            filterField.put("mapinfoId", CodeBook.EQUAL_HQL_ID);

            filterField.put("taskId", CodeBook.EQUAL_HQL_ID);


            filterField.put("mapinfoOrder", CodeBook.LIKE_HQL_ID);

            filterField.put(CodeBook.ORDER_BY_HQL_ID, "mapinfoOrder");

            filterField.put("task_Id", CodeBook.EQUAL_HQL_ID);


        }
        return filterField;
    }

    @SuppressWarnings("unchecked")
    public List<Long> getMapinfoIdUsed(Long taskId) {
        String hql = "select e.cid.mapinfoId from ExchangeTaskdetail e where e.cid.taskId=" + taskId + " order by e.mapinfoOrder";
        return (List<Long>) DatabaseOptUtils.findObjectsByHql(this,hql);
    }

    public List<ExchangeTaskdetail> getTaskDetails(Long taskId) {
        return listObjects(
                "from ExchangeTaskdetail where task_id=? order by mapinfoOrder",
                taskId);
    }

    public Long getMapinfoOrder(Long taskId) {

        String hql = "select nvl(max(e.mapinfoOrder),0) from ExchangeTaskdetail e where e.cid.taskId=" + taskId;
        return DatabaseOptUtils.getSingleIntByHql(this,hql);
    }

    public void deleteDetails(Long taskId, Long mapinfoId) {
        //String hql = "delete ExchangeTaskdetail e where e.cid.taskId="+taskId+" and e.cid.mapinfoId="+mapinfoId;
        this.deleteObjectById(new ExchangeTaskdetailId(mapinfoId, taskId));
    }

    public void deleteDetailsByMapinfoId(Long mapinfoId) {
        String hql = "delete ExchangeTaskdetail e where e.cid.mapinfoId=" + mapinfoId;
        DatabaseOptUtils.doExecuteHql(this,hql);
    }

    public void deleteDetailsByTaskId(Long taskId) {
        String hql = "delete ExchangeTaskdetail e where e.cid.taskId = ?";
        DatabaseOptUtils.doExecuteHql(this,hql, taskId);
    }

    public void updateDetailOrder(Long taskId, Long mapOrder) {
        String hql = "update ExchangeTaskdetail d set d.mapinfoOrder = d.mapinfoOrder - 1 where d.cid.taskId = ? and d.mapinfoOrder > ?";

        DatabaseOptUtils.doExecuteHql(this,hql, new Object[]{taskId, mapOrder});
    }
}
