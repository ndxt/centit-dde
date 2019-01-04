package com.centit.dde.service.impl;

import com.centit.dde.service.IQuartzJobBean;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class SysQuartzJobBeanImpl extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext jobexecutioncontext) throws JobExecutionException {
        IQuartzJobBean quartzJobBean = (IQuartzJobBean) jobexecutioncontext.getJobDetail().getJobDataMap().get(IQuartzJobBean.QUARTZ_JOB_BEAN_KEY);
        quartzJobBean.executeInternal(jobexecutioncontext);
    }

}
