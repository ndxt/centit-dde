package com.centit.dde.util;

import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.centit.dde.po.TaskLog;
import com.centit.dde.service.TaskLogManager;

/**
 * Created by sx on 2015/1/20.
 */
public class TaskLogQueue implements IQuartzJobBean {
    private static final Log logger = LogFactory.getLog(TaskLogQueue.class);

    private TaskLogQueue() {
    }

    private static LinkedList<Object> blockingQueue = new LinkedList<Object>();

    private static TaskLogQueue taskLogQueue = new TaskLogQueue();

    static {
        taskLogQueue.initTimerTask();
    }


    public static void put(Object object) {
        blockingQueue.offer(object);
    }


    @Override
    public void initTimerTask() {
        JobDetail jobDetail = getSchedulerManager().getJobDetail("JOB_TASK_LOG",
                "JOB_TASK_LOG");
        // 设置回调
        jobDetail.getJobDataMap().put(IQuartzJobBean.QUARTZ_JOB_BEAN_KEY, this);


        getSchedulerManager().schedule(jobDetail, "Trigger_TASK_LOG",
                "JOB_GROUP_TASK_LOG", "*/10 * * * * ?");
    }

    @Override
    public void executeInternal(JobExecutionContext jobexecutioncontext) throws JobExecutionException {
        while (!blockingQueue.isEmpty()) {
            Object take = blockingQueue.poll();
            if (null == take) {
                return;
            }
            if (take instanceof TaskLog) {
                getTaskLogManager().saveObject((TaskLog) take);
            }
        }
    }


    private TaskLogManager getTaskLogManager() {
        return WebStartupLinstener.getCurrentWebApplicationContext().getBean(TaskLogManager.class);
    }

    private SchedulerManager getSchedulerManager() {
        return WebStartupLinstener.getCurrentWebApplicationContext().getBean(SchedulerManager.class);
    }
}
