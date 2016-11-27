package com.centit.dde.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.centit.dde.dao.TaskErrorDataDao;
import com.centit.dde.po.TaskErrorData;
import com.centit.dde.service.TaskErrorDataManager;
import com.centit.framework.hibernate.service.BaseEntityManagerImpl;

public class TaskErrorDataManagerImpl extends BaseEntityManagerImpl<TaskErrorData,Long,TaskErrorDataDao>
        implements TaskErrorDataManager {

    public static final Log log = LogFactory.getLog(TaskErrorDataManager.class);

    //private static final SysOptLog sysOptLog = SysOptLogFactoryImpl.getSysOptLog();

    private TaskErrorDataDao taskErrorDataDao;

    public void setTaskErrorDataDao(TaskErrorDataDao baseDao) {
        this.taskErrorDataDao = baseDao;
        setBaseDao(this.taskErrorDataDao);
    }

    public Long getTaskErrorId() {
        return this.taskErrorDataDao.getTaskErrorId();
    }

    public void saveTaskErrorData(TaskErrorData taskErrorData) {
        this.taskErrorDataDao.saveTaskErrorData(taskErrorData);
    }


    @Override
    public void flush() {
        taskErrorDataDao.flush();
    }
}

