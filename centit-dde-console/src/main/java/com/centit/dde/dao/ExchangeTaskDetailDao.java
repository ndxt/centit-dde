package com.centit.dde.dao;

import com.centit.dde.po.ExchangeTaskDetail;
import com.centit.dde.po.ExchangeTaskDetailId;
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
public class ExchangeTaskDetailDao extends BaseDaoImpl<ExchangeTaskDetail,ExchangeTaskDetailId> {

    public static final Log log = LogFactory.getLog(ExchangeTaskDetailDao.class);
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
        String hql = "select e.cid.mapInfoId from ExchangeTaskDetail e where e.cid.taskId=" + taskId + " order by e.mapinfoOrder";
        return (List<Long>) DatabaseOptUtils.findObjectsByHql(this,hql);
    }

    public List<ExchangeTaskDetail> getTaskDetails(Long taskId) {
        return listObjects(
                "from ExchangeTaskDetail where task_id=? order by mapinfoOrder",
                taskId);
    }

    public Long getMapinfoOrder(Long taskId) {

        String hql = "select nvl(max(e.mapinfoOrder),0) from ExchangeTaskDetail e where e.cid.taskId=" + taskId;
        return DatabaseOptUtils.getSingleIntByHql(this,hql);
    }

    public void deleteDetails(Long taskId, Long mapinfoId) {
        //String hql = "delete ExchangeTaskDetail e where e.cid.taskId="+taskId+" and e.cid.mapinfoId="+mapinfoId;
        this.deleteObjectById(new ExchangeTaskDetailId(mapinfoId, taskId));
    }

    public void deleteDetailsByMapinfoId(Long mapinfoId) {
        String hql = "delete ExchangeTaskDetail e where e.cid.mapinfoId=" + mapinfoId;
        DatabaseOptUtils.doExecuteHql(this,hql);
    }

    public void deleteDetailsByTaskId(Long taskId) {
        String hql = "delete ExchangeTaskDetail e where e.cid.taskId = ?";
        DatabaseOptUtils.doExecuteHql(this,hql, taskId);
    }

    public void updateDetailOrder(Long taskId, Long mapOrder) {
        String hql = "update ExchangeTaskDetail d set d.mapinfoOrder = d.mapinfoOrder - 1 where d.cid.taskId = ? and d.mapinfoOrder > ?";

        DatabaseOptUtils.doExecuteHql(this,hql, new Object[]{taskId, mapOrder});
    }
}
