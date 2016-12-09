package com.centit.dde.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.centit.dde.dao.TaskLogDao;
import com.centit.dde.po.TaskLog;
import com.centit.dde.service.TaskLogManager;
import com.centit.framework.hibernate.service.BaseEntityManagerImpl;
@Service
public class TaskLogManagerImpl extends BaseEntityManagerImpl<TaskLog,Long,TaskLogDao>
        implements TaskLogManager {
    public static final Log log = LogFactory.getLog(TaskLogManager.class);

    //private static final SysOptLog sysOptLog = SysOptLogFactoryImpl.getSysOptLog();
    @Resource(name="taskLogDao")
    @NotNull
    private TaskLogDao taskLogDao;

    public void setTaskLogDao(TaskLogDao baseDao) {
        this.taskLogDao = baseDao;
        setBaseDao(this.taskLogDao);
    }

    public Long getTaskLogId() {
        return this.taskLogDao.getTaskLogId();
    }
    public List<String[]> taskLogStat(String sType,Object o){
        return this.taskLogDao.taskLogStat(sType, o);
    }

    @Override
    public void flush() {
        taskLogDao.flush();
    }
}

