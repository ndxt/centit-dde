package com.centit.dde.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.centit.core.dao.BaseDaoImpl;
import com.centit.core.dao.CodeBook;
import com.centit.dde.po.ExchangeTaskdetail;
import com.centit.dde.po.ExchangeTaskdetailId;

public class ExchangeTaskdetailDao extends BaseDaoImpl<ExchangeTaskdetail> {
    private static final long serialVersionUID = 1L;
    public static final Log log = LogFactory.getLog(ExchangeTaskdetailDao.class);

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

    public List<Long> getMapinfoIdUsed(Long taskId) {
        String hql = "select e.cid.mapinfoId from ExchangeTaskdetail e where e.cid.taskId=" + taskId + " order by e.mapinfoOrder";
        return (List<Long>) this.findObjectsByHql(hql);
    }

    public List<ExchangeTaskdetail> getTaskDetails(Long taskId) {
        return listObjects(
                "from ExchangeTaskdetail where task_id=? order by mapinfoOrder",
                taskId);
    }

    public Long getMapinfoOrder(Long taskId) {

        String hql = "select nvl(max(e.mapinfoOrder),0) from ExchangeTaskdetail e where e.cid.taskId=" + taskId;
        return this.getSingleIntByHql(hql);
    }

    public void deleteDetails(Long taskId, Long mapinfoId) {
        //String hql = "delete ExchangeTaskdetail e where e.cid.taskId="+taskId+" and e.cid.mapinfoId="+mapinfoId;
        this.deleteObjectById(new ExchangeTaskdetailId(mapinfoId, taskId));
    }

    public void deleteDetailsByMapinfoId(Long mapinfoId) {
        String hql = "delete ExchangeTaskdetail e where e.cid.mapinfoId=" + mapinfoId;
        this.doExecuteHql(hql);
    }

    public void deleteDetailsByTaskId(Long taskId) {
        String hql = "delete ExchangeTaskdetail e where e.cid.taskId = ?";
        this.doExecuteHql(hql, taskId);
    }

    public void updateDetailOrder(Long taskId, Long mapOrder) {
        String hql = "update ExchangeTaskdetail d set d.mapinfoOrder = d.mapinfoOrder - 1 where d.cid.taskId = ? and d.mapinfoOrder > ?";

        super.doExecuteHql(hql, new Object[]{taskId, mapOrder});
    }
}
