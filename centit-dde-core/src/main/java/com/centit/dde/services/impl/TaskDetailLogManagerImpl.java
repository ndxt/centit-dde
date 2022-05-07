package com.centit.dde.services.impl;

import com.centit.dde.dao.TaskDetailLogDao;
import com.centit.dde.po.TaskDetailLog;
import com.centit.dde.services.TaskDetailLogManager;
import com.centit.dde.vo.DelTaskLogParameter;
import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import com.centit.framework.jdbc.service.BaseEntityManagerImpl;
import com.centit.support.database.utils.PageDesc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author zhf
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TaskDetailLogManagerImpl extends BaseEntityManagerImpl<TaskDetailLog, Long, TaskDetailLogDao> implements TaskDetailLogManager {

    private final TaskDetailLogDao taskDetailLogDao;

    @Autowired
    public TaskDetailLogManagerImpl(TaskDetailLogDao taskDetailLogDao) {
        this.taskDetailLogDao = taskDetailLogDao;
    }


    @Override
    public TaskDetailLog getTaskDetailLog(String logDetailId) {
        return this.taskDetailLogDao.getObjectById(logDetailId);
    }

    @Override
    public List<TaskDetailLog> listTaskDetailLog(Map<String, Object> param, PageDesc pageDesc) {
        return this.taskDetailLogDao.listObjectsByProperties(param, pageDesc);
    }

    @Override
    public void createTaskDetailLog(TaskDetailLog detailLog) {
        this.taskDetailLogDao.saveNewObject(detailLog);
        this.taskDetailLogDao.saveObjectReferences(detailLog);
    }

    @Override
    public void updateTaskDetailLog(TaskDetailLog detailLog) {
        this.taskDetailLogDao.updateObject(detailLog);
        this.taskDetailLogDao.saveObjectReferences(detailLog);
    }

    @Override
    public void delTaskDetailLog(String logDetailId) {
        this.taskDetailLogDao.deleteObjectById(logDetailId);
    }

    @Override
    public int delTaskDetailLog(DelTaskLogParameter delTaskLogParameter) {
        StringBuilder sqlDetail=new StringBuilder("delete from d_task_detail_log  where task_id=? AND DATE(run_begin_time) <= ? ");
        if (!delTaskLogParameter.getIsError()){
            sqlDetail.append(" AND log_info = 'ok'  ");
        }
        return DatabaseOptUtils.doExecuteSql(taskDetailLogDao,sqlDetail.toString(),new Object[]{delTaskLogParameter.getPacketId(),delTaskLogParameter.getRunBeginTime()});
    }
}

