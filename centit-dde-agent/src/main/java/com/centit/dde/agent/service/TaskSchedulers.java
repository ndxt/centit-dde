package com.centit.dde.agent.service;

import com.centit.dde.dao.TaskExchangeDao;

import com.centit.dde.po.TaskExchange;

import com.centit.framework.components.OperationLogCenter;
import com.centit.framework.components.impl.TextOperationLogWriterImpl;
import com.centit.support.algorithm.CollectionsOpt;

import com.centit.support.quartz.QuartzJobUtils;
import com.sun.xml.internal.bind.v2.TODO;
import jdk.nashorn.internal.scripts.JO;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;

@Service
public class TaskSchedulers {
    @Autowired
    private TaskExchangeDao taskExchangeDao;

    @Autowired
    private Scheduler scheduler;

    private void refreshTask() throws SchedulerException {
        Set<TriggerKey> triggerKeys = scheduler.getTriggerKeys(GroupMatcher.anyTriggerGroup());
        List<TaskExchange> list = taskExchangeDao.listObjects();
        for (TaskExchange ll : list) {
            if (ll.getTaskCron()==null || ll.getExchangeDescJson()==null)
                break;
            int i = 0;
            for (TriggerKey tKey : triggerKeys) {
               if (tKey.getName().equals(ll.getTaskId())){
                   i++;
                   CronTrigger quatrzTrigger = (CronTrigger)scheduler.getTrigger(tKey);
//                   JobKey jobKey =quatrzTrigger.getJobKey();
//                   JobDetail jobDetail=scheduler.getJobDetail(jobKey);
//                   String json = (String) jobDetail.getJobDataMap().get("jsonExchange");
                   if (!(quatrzTrigger.getCronExpression().equals(ll.getTaskCron()))){
                       QuartzJobUtils.createOrReplaceCronJob(scheduler, ll.getTaskId(), ll.getTaskName(), "task", ll.getTaskCron(),
                           CollectionsOpt.createHashMap("taskId", ll.getTaskId()));
                       break;
                   }
                   break;
               }
            }
            if (i==0) {
                QuartzJobUtils.createOrReplaceCronJob(scheduler, ll.getTaskId(), ll.getTaskName(), "task", ll.getTaskCron(),
                    CollectionsOpt.createHashMap("taskId", ll.getTaskId()));
            }
        }
        for (TriggerKey tKey : triggerKeys) {
            int j = 0;
            for (TaskExchange ll : list) {
                TriggerKey triggerKey = TriggerKey.triggerKey(ll.getTaskId(), ll.getTaskName());
                if (tKey.equals(triggerKey))
                    break;
                j++;
            }
            if (j == list.size()) {
                QuartzJobUtils.deleteJob(scheduler, tKey.getName(), tKey.getGroup());
            }
        }
    }

    @PostConstruct
    public void init() throws SchedulerException {
        TextOperationLogWriterImpl textOperationLogWriter
            = new TextOperationLogWriterImpl();
        textOperationLogWriter.setOptLogHomePath("D:/Projects/RunData/dde/logs");
        OperationLogCenter.initOperationLogWriter(textOperationLogWriter);
        QuartzJobUtils.registerJobType("task", RunTaskJob.class);
        scheduler.start();
    }

    @Scheduled(fixedDelay = 1000 * 10)//5minute
    public void work() throws SchedulerException {
        refreshTask();
    }
}
