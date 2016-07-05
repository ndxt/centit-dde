package com.centit.dde.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.centit.dde.dao.TaskDetailLogDao;
import com.centit.dde.po.TaskDetailLog;
import com.centit.dde.service.TaskDetailLogManager;
import com.centit.framework.hibernate.service.BaseEntityManagerImpl;

public class TaskDetailLogManagerImpl extends BaseEntityManagerImpl<TaskDetailLog>
        implements TaskDetailLogManager {
    private static final long serialVersionUID = 1L;
    public static final Log log = LogFactory.getLog(TaskDetailLogManager.class);

    //private static final SysOptLog sysOptLog = SysOptLogFactoryImpl.getSysOptLog();

    private TaskDetailLogDao taskDetailLogDao;

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

