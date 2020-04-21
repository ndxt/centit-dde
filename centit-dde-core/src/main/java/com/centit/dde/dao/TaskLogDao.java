package com.centit.dde.dao;

import com.centit.dde.po.TaskLog;
import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TaskLogDao extends BaseDaoImpl<TaskLog,Long> {

    public static final Log logger = LogFactory.getLog(TaskLogDao.class);

    @Override
    public Map<String, String> getFilterField() {
        Map<String, String> filterField = new HashMap<String, String>();

            filterField.put("logId", CodeBook.EQUAL_HQL_ID);

            filterField.put("taskId", CodeBook.EQUAL_HQL_ID);

            filterField.put("runBeginTime", "runBeginTime>=?");
            filterField.put("runBeginTime2", "runBeginTime<=?");

            filterField.put("runEndTime", CodeBook.LIKE_HQL_ID);

            filterField.put("runType", CodeBook.EQUAL_HQL_ID);

            filterField.put("runner", CodeBook.LIKE_HQL_ID);

            filterField.put("otherMessage", CodeBook.LIKE_HQL_ID);

            filterField.put("successPieces", CodeBook.LIKE_HQL_ID);

            filterField.put("errorPieces", CodeBook.LIKE_HQL_ID);

            /*filterField.put("isError", "logId in (select a.logId " +
            		"from TaskDetailLog a inner join a.taskErrorDatas b " +
            "where a.errorPieces>?)");
            filterField.put(CodeBook.ORDER_BY_HQL_ID, "runBeginTime desc");*/

        return filterField;
    }

    public Long getTaskLogId() {
        return DatabaseOptUtils.getSequenceNextValue(this, "D_TASKLOGID");
    }

  @SuppressWarnings("unchecked")
  public List<String[]> taskLogStat(String sType,Object o){
      String sql ="";
      if (sType.equals("year")){
          sql = "select to_char(trunc(a.run_begin_time,'mm'),'yyyy-mm') a,count(distinct b.task_id) b,"+
                  "count(distinct a.mapinfo_id) c,count(a.log_detail_id) d,"+
                  "sum(a.success_pieces) e,sum(a.error_pieces) f "+
                  "from d_task_detail_log a join d_task_log b on a.log_id=b.log_id "+
                  "where to_char(trunc(a.run_begin_time,'yyyy'),'yyyy')=? "+
                  "GROUP BY to_char(trunc(a.run_begin_time,'mm'),'yyyy-mm')";
      }
      else if (sType.equals("month")){
          sql = "select to_char(trunc(a.run_begin_time),'yyyy-mm-dd') a,count(distinct b.task_id) b,"+
                  "count(distinct a.mapinfo_id) c,count(a.log_detail_id) d,"+
                  "sum(a.success_pieces) e,sum(a.error_pieces) f "+
                  "from d_task_detail_log a join d_task_log b on a.log_id=b.log_id "+
                  "where to_char(trunc(a.run_begin_time,'mm'),'yyyymm')=? "+
                  "GROUP BY to_char(trunc(a.run_begin_time),'yyyy-mm-dd')";
      }

      //DatabaseOptUtils.listObjectsByNamedSql(this,sql,new Object[]{o});

      return null;
  }

}
