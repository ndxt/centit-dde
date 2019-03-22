package com.centit.dde.service.impl;

import com.centit.dde.dao.TaskLogDao;
import com.centit.dde.po.TaskLog;
import com.centit.dde.service.TaskLogManager;
import com.centit.framework.jdbc.service.BaseEntityManagerImpl;
import com.centit.support.database.utils.PageDesc;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Service
public class TaskLogManagerImpl extends BaseEntityManagerImpl<TaskLog,Long,TaskLogDao> implements TaskLogManager {
    public static final Log log = LogFactory.getLog(TaskLogManager.class);

    @Autowired
    private TaskLogDao taskLogDao;

    @Resource(name="taskLogDao")
    @NotNull
    public void setTaskLogDao(TaskLogDao baseDao) {
        this.taskLogDao = baseDao;
        setBaseDao(this.taskLogDao);
    }


    @Override
    public TaskLog getLog(String logId) {
        return this.taskLogDao.getObjectById(logId);
    }

    @Override
    public List<TaskLog> listTaskLog(Map<String, Object> param, PageDesc pageDesc) {
        List<TaskLog> taskLogs = this.taskLogDao.listObjectsByProperties(param, pageDesc);
        return taskLogs;
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

