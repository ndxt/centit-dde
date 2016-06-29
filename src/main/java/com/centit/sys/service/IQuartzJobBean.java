package com.centit.sys.service;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created with IntelliJ IDEA.
 * User: sx
 * Date: 13-7-4
 * Time: 上午10:45
 * 动态定时接口
 */
public interface IQuartzJobBean {
    public static final String QUARTZ_JOB_BEAN_KEY = "QUARTZ_JOB_BEAN_KEY";

    /**
     * 初始化定时任务
     */
    void initTimerTask();


    /**
     * 具体执行此定时任务方法
     *
     * @param jobexecutioncontext
     * @throws JobExecutionException
     */
    void executeInternal(JobExecutionContext jobexecutioncontext) throws JobExecutionException;
}
