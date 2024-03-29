package com.centit.dde.services.impl;

import com.centit.dde.adapter.dao.TaskDetailLogDao;
import com.centit.dde.adapter.po.TaskDetailLog;
import com.centit.dde.services.TaskDetailLogManager;
import com.centit.dde.vo.DelTaskLogParameter;
import com.centit.support.algorithm.DatetimeOpt;
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
public class TaskDetailLogManagerImpl implements TaskDetailLogManager {

    @Autowired
    protected TaskDetailLogDao taskDetailLogDao;

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
        return taskDetailLogDao.delTaskDetailLog(delTaskLogParameter.getPacketId(),
            DatetimeOpt.smartPraseDate(delTaskLogParameter.getRunBeginTime()),
            delTaskLogParameter.getIsError());
    }
}

