package com.centit.sys.service;

import java.util.Date;

import org.quartz.JobDetail;


public interface SchedulerManager {
    /**
     * 通过jobName获取JobDetail，jobGroup为默认组
     *
     * @param jobName
     * @return
     */
    JobDetail getJobDetail(String jobName);

    /**
     * 通过jobName, jobGroup获取JobDetail
     *
     * @param jobName
     * @param jobGroup
     * @return
     */
    JobDetail getJobDetail(String jobName, String jobGroup);

    /**
     * 根据 Quartz Cron Expression 调试任务
     *
     * @param jobDetail      JobDetail
     * @param triggerName    定时触发器名
     * @param cronExpression Quartz Cron 表达式，如 "0/10 * * ? * * *"等
     */
    void schedule(JobDetail jobDetail, String triggerName, String cronExpression);


    /**
     * 根据 Quartz Cron Expression 调试任务
     *
     * @param jobDetail      JobDetail
     * @param triggerName    定时触发器名
     * @param triggerGroup   定时触发器组
     * @param cronExpression Quartz Cron 表达式，如 "0/10 * * ? * * *"等
     */
    void schedule(JobDetail jobDetail, String triggerName, String triggerGroup, String cronExpression);

    /**
     * 暂停任务
     *
     * @param triggerName
     */
    void pauseTrigger(String triggerName);

    /**
     * 暂停任务
     *
     * @param triggerName
     * @param triggerGroup
     */
    void pauseTrigger(String triggerName, String triggerGroup);


    /**
     * 恢复任务
     *
     * @param triggerName
     */
    void resumeTrigger(String triggerName);

    /**
     * 恢复任务
     *
     * @param triggerName
     * @param triggerGroup
     */
    void resumeTrigger(String triggerName, String triggerGroup);

    /**
     * 删除任务
     *
     * @param jobName
     * @return
     */
    boolean deleteJob(String jobName);

    /**
     * 删除任务
     *
     * @param jobName
     * @param jobGroup
     * @return
     */
    boolean deleteJob(String jobName, String jobGroup);

    /**
     * 在startTime时执行调试一次
     *
     * @param startTime 调度开始时间
     */
    void schedule(JobDetail jobDetail, String triggerName, Date startTime);


    /**
     * 在startTime时执行调试，endTime结束执行调度，重复执行repeatCount次
     *
     * @param startTime   调度开始时间
     * @param endTime     调度结束时间
     * @param repeatCount 重复执行次数
     */
    void schedule(JobDetail jobDetail, String triggerName, Date startTime, Date endTime, int repeatCount);


    /**
     * 在startTime时执行调试，endTime结束执行调度，重复执行repeatCount次，每隔repeatInterval秒执行一次
     *
     * @param startTime      调度开始时间
     * @param endTime        调度结束时间
     * @param repeatCount    重复执行次数
     * @param repeatInterval 执行时间隔间
     */
    void schedule(JobDetail jobDetail, String triggerName, Date startTime, Date endTime, int repeatCount, long repeatInterval);

}
