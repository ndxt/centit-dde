package com.centit.dde.dao;

import com.centit.dde.po.ExchangeTaskDetail;
import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
public class ExchangeTaskDetailDao extends BaseDaoImpl<ExchangeTaskDetail, Serializable> {

    public static final Log log = LogFactory.getLog(ExchangeTaskDetailDao.class);
    @Override
    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<String, String>();

            filterField.put("mapinfoId", CodeBook.EQUAL_HQL_ID);

            filterField.put("taskId", CodeBook.EQUAL_HQL_ID);


            filterField.put("mapInfoOrder", CodeBook.LIKE_HQL_ID);

            filterField.put(CodeBook.ORDER_BY_HQL_ID, "mapInfoOrder");

            //filterField.put("task_Id", CodeBook.EQUAL_HQL_ID);


        }
        return filterField;
    }

    @SuppressWarnings("unchecked")
    public List<Long> getMapinfoIdUsed(Long taskId) {
        String hql = "select e.cid.mapInfoId from ExchangeTaskDetail e where e.cid.taskId=" + taskId + " order by e.mapInfoOrder";
        return (List<Long>) DatabaseOptUtils.getObjectBySqlAsJson(this,hql);
    }

    public List<ExchangeTaskDetail> getTaskDetails(Long taskId) {
        Map<String, Object> filterMap = new HashMap<>();
        filterMap.put("task_Id",taskId);
        List<ExchangeTaskDetail> list = listObjectsBySql(
            "select task_id,mapinfo_id,mapinfo_order from D_EXCHANGE_TASKDETAIL where task_Id=:task_Id order by mapinfo_order",
            filterMap);
        return  list;
    }

    public Long getMapinfoOrder(Long taskId) {

        String hql = "select nvl(max(e.mapinfoOrder),0) from ExchangeTaskDetail e where e.cid.taskId=" + taskId;
        //return DatabaseOptUtils.getSingleIntByHql(this,hql);
        return null;
    }

    public void deleteDetails(Long taskId, Long mapinfoId) {
        //String hql = "delete ExchangeTaskDetail e where e.cid.taskId="+taskId+" and e.cid.mapinfoId="+mapinfoId;
        this.deleteObjectById(new ExchangeTaskDetail(mapinfoId, taskId));
    }

    public void deleteDetailsByMapinfoId(Long mapinfoId) {
        String hql = "delete ExchangeTaskDetail e where e.cid.mapinfoId=" + mapinfoId;
        DatabaseOptUtils.doExecuteSql(this,hql);
    }

    public void deleteDetailsByTaskId(Long taskId) {
        String hql = "delete ExchangeTaskDetail e where e.cid.taskId = "+taskId;
        DatabaseOptUtils.doExecuteSql(this,hql);
    }

    public void updateDetailOrder(Long taskId, Long mapOrder) {
        String hql = "update ExchangeTaskDetail d set d.mapinfoOrder = d.mapinfoOrder - 1 where d.cid.taskId = ? and d.mapinfoOrder > ?";

        DatabaseOptUtils.doExecuteSql(this,hql, new Object[]{taskId, mapOrder});
    }
}
