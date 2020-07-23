package com.centit.dde.services.impl;

import com.centit.dde.dao.TaskLogDao;
import com.centit.dde.po.TaskLog;
import com.centit.dde.services.TaskLogManager;
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
public class TaskLogManagerImpl extends BaseEntityManagerImpl<TaskLog, Long, TaskLogDao> implements TaskLogManager {

    private final TaskLogDao taskLogDao;

    @Autowired
    public TaskLogManagerImpl(TaskLogDao taskLogDao) {
        this.taskLogDao = taskLogDao;
    }

    @Override
    public TaskLog getLog(String logId) {
        return this.taskLogDao.getObjectById(logId);
    }

    @Override
    public List<TaskLog> listTaskLog(Map<String, Object> param, PageDesc pageDesc) {
        return this.taskLogDao.listObjectsByProperties(param, pageDesc);
    }

    @Override
    public void createTaskLog(TaskLog taskLog) {
        this.taskLogDao.saveNewObject(taskLog);
        this.taskLogDao.saveObjectReferences(taskLog);
    }

    @Override
    public void updateTaskLog(TaskLog taskLog) {
        this.taskLogDao.updateObject(taskLog);
        this.taskLogDao.saveObjectReferences(taskLog);
    }

    @Override
    public void deleteTaskLogById(String logId) {
        this.taskLogDao.deleteObjectForceById(logId);
    }

}

