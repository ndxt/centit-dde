package com.centit.dde.service.impl;

import com.centit.dde.dao.TaskDetailLogDao;
import com.centit.dde.po.TaskDetailLog;
import com.centit.dde.service.TaskDetailLogManager;
import com.centit.framework.jdbc.service.BaseEntityManagerImpl;
import com.centit.support.database.utils.PageDesc;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class TaskDetailLogManagerImpl extends BaseEntityManagerImpl<TaskDetailLog,Long,TaskDetailLogDao> implements TaskDetailLogManager {

    public static final Log log = LogFactory.getLog(TaskDetailLogManager.class);

    @Autowired
    private TaskDetailLogDao taskDetailLogDao;

    @Resource(name="taskDetailLogDao")
    @NotNull
    public void setTaskDetailLogDao(TaskDetailLogDao baseDao) {
        this.taskDetailLogDao = baseDao;
        setBaseDao(this.taskDetailLogDao);
    }


    @Override
    public TaskDetailLog getTaskDetailLog(String logDetailId) {
        return this.taskDetailLogDao.getObjectById(logDetailId);
    }

    @Override
    public List<TaskDetailLog> listTaskDetailLog(Map<String, Object> param, PageDesc pageDesc) {
        return this.taskDetailLogDao.listObjectsByProperties(param,pageDesc);
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
}

