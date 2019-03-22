package com.centit.dde.service.impl;

import com.centit.dde.dao.TaskExchangeDao;
import com.centit.dde.po.TaskExchange;
import com.centit.dde.service.TaskExchangeManager;
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

/**
 * @ClassName TaskExchangeManagerImpl
 * @Date 2019/3/20 11:07
 * @Version 1.0
 */
@Service
public class TaskExchangeManagerImpl extends BaseEntityManagerImpl<TaskExchange,Long, TaskExchangeDao> implements TaskExchangeManager {

    public static final Log log = LogFactory.getLog(TaskExchangeManager.class);

    @Autowired
    private TaskExchangeDao taskExchangeDao;

    @Resource(name="taskExchangeDao")
    @NotNull
    public void setTaskExchangeDao(TaskExchangeDao taskExchangeDao) {
        this.taskExchangeDao = baseDao;
        setBaseDao(this.taskExchangeDao);
    }


    @Override
    public TaskExchange getTaskExchange(String taskId) {
        return this.taskExchangeDao.getObjectById(taskId);
    }

    @Override
    public List<TaskExchange> listTaskExchange(Map<String, Object> param, PageDesc pageDesc) {
        return this.taskExchangeDao.listObjectsByProperties(param,pageDesc);
    }

    @Override
    public void createTaskExchange(TaskExchange taskExchange) {
        this.taskExchangeDao.saveNewObject(taskExchange);
        this.taskExchangeDao.saveObjectReferences(taskExchange);
    }

    @Override
    public void updateTaskExchange(TaskExchange taskExchange) {
        this.taskExchangeDao.updateObject(taskExchange);
        this.taskExchangeDao.saveObjectReferences(taskExchange);
    }

    @Override
    public void delTaskExchangeById(String taskId) {
        this.taskExchangeDao.deleteObjectById(taskId);
    }
}
