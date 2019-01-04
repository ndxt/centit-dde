package com.centit.dde.service.impl;

import com.centit.dde.service.SchedulerManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.util.Date;
import java.util.UUID;


/**
 * 描述：
 * jobName 任务名
 * triggerName 触发器名
 * <p/>
 * 定义时注意，任务名，同一组中不可重名，如重名，会获取已经定义的任务，且再进行执行时会发生异常
 * 且在同一组时任务名不可与触发器名同名，也会发生异常
 * <p/>
 * 删除任务时使用任务名及组名，
 * 暂停或重新启用任务时使用触发器名及组名
 * <p/>
 * 组名可根据各模块及各块功能进行自定义
 */
@Service
public class SchedulerManagerImpl implements SchedulerManager {

    public static final Log log = LogFactory.getLog(SchedulerManagerImpl.class);

    private Scheduler scheduler;

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public JobDetail getJobDetail(String jobName) {
        return getJobDetail(jobName, Scheduler.DEFAULT_GROUP);
    }

    @Override
    public JobDetail getJobDetail(String jobName, String jobGroup) {
        JobDetail jobDetail = null;
        try {
            //获取任务列表中存在的任务
            jobDetail = scheduler.getJobDetail(jobName, jobGroup);
        } catch (SchedulerException e) {
            log.error(e.toString());
        }
        if (null == jobDetail) {
            jobDetail = new JobDetail(jobName, jobGroup, SysQuartzJobBeanImpl.class);
        }

        return jobDetail;
    }

    @Override
    public void schedule(JobDetail jobDetail, String triggerName, String cronExpression) {
        schedule(jobDetail, triggerName, Scheduler.DEFAULT_GROUP, cronExpression);
    }

    @Override
    public void schedule(JobDetail jobDetail, String triggerName, String triggerGroup, String cronExpression) {
        try {
//            scheduler.addJob(job, true);
            CronTrigger cronTrigger = new CronTrigger(triggerName, Scheduler.DEFAULT_GROUP, jobDetail.getName(),
                    jobDetail.getGroup());

            cronTrigger.setCronExpression(new CronExpression(cronExpression));
            scheduler.scheduleJob(jobDetail, cronTrigger);
//            scheduler.scheduleJob(cronTrigger);
//            scheduler.rescheduleJob(name, Scheduler.DEFAULT_GROUP, cronTrigger);

        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void pauseTrigger(String triggerName) {
        pauseTrigger(triggerName, Scheduler.DEFAULT_GROUP);
    }

    @Override
    public void pauseTrigger(String triggerName, String triggerGroup) {
        try {
            scheduler.pauseTrigger(triggerName, triggerGroup);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void resumeTrigger(String triggerName) {
        resumeTrigger(triggerName, Scheduler.DEFAULT_GROUP);
    }

    @Override
    public void resumeTrigger(String triggerName, String triggerGroup) {
        try {
            scheduler.resumeTrigger(triggerName, triggerGroup);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteJob(String jobName) {
        return deleteJob(jobName, Scheduler.DEFAULT_GROUP);
    }

    @Override
    public boolean deleteJob(String jobName, String jobGroup) {
        try {
            return scheduler.deleteJob(jobName, jobGroup);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void schedule(JobDetail jobDetail, String triggerName, Date startTime) {
        schedule(jobDetail, triggerName, startTime, null, 0, 0L);
    }

    @Override
    public void schedule(JobDetail jobDetail, String triggerName, Date startTime, Date endTime, int repeatCount) {
        schedule(jobDetail, triggerName, startTime, endTime, repeatCount, 0L);
    }

    @Override
    public void schedule(JobDetail jobDetail, String triggerName, Date startTime, Date endTime, int repeatCount, long repeatInterval) {
        if (!StringUtils.hasText(triggerName)) {
            triggerName = UUID.randomUUID().toString();
        }
        try {
            SimpleTrigger cronTrigger = new SimpleTrigger(triggerName, Scheduler.DEFAULT_GROUP, jobDetail.getName(),
                    Scheduler.DEFAULT_GROUP, startTime, endTime, repeatCount, repeatInterval);
            scheduler.scheduleJob(jobDetail, cronTrigger);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }
}
