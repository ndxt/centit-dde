package com.centit.dde.service.impl;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.centit.dde.dao.TaskDetailLogDao;
import com.centit.dde.po.TaskDetailLog;
import com.centit.dde.service.TaskDetailLogManager;
import com.centit.framework.hibernate.service.BaseEntityManagerImpl;
@Service
public class TaskDetailLogManagerImpl extends BaseEntityManagerImpl<TaskDetailLog,Long,TaskDetailLogDao>
        implements TaskDetailLogManager {

    public static final Log log = LogFactory.getLog(TaskDetailLogManager.class);

    //private static final SysOptLog sysOptLog = SysOptLogFactoryImpl.getSysOptLog();

   
    private TaskDetailLogDao taskDetailLogDao;

    @Resource(name="taskDetailLogDao")
    @NotNull
    public void setTaskDetailLogDao(TaskDetailLogDao baseDao) {
        this.taskDetailLogDao = baseDao;
        setBaseDao(this.taskDetailLogDao);
    }

    public Long getTaskDetailLogId() {
        return this.taskDetailLogDao.getTaskDetailLogId();
    }

    @Override
    public void flush() {
        taskDetailLogDao.flush();
    }
}

