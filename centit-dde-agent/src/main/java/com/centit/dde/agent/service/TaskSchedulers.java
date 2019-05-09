package com.centit.dde.agent.service;

import com.centit.dde.dao.TaskExchangeDao;

import com.centit.dde.po.TaskExchange;

import com.centit.framework.components.OperationLogCenter;
import com.centit.framework.components.impl.TextOperationLogWriterImpl;
import com.centit.support.algorithm.CollectionsOpt;

import com.centit.support.quartz.QuartzJobUtils;
import com.sun.xml.internal.bind.v2.TODO;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
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
        //TODO 对于没变cron表达式不用替换
        for (TaskExchange ll : list) {
            // System.out.println(ll.getTaskName());
//               QuartzJobUtils.createOrReplaceCronJob(scheduler,ll.getTaskId(),ll.getTaskName(),"exec",ll.getTaskCron(),
//                   CollectionsOpt.createHashMap("command", "java -jar centit-dde-datamoving.jar logid",
//                       "envp", CollectionsOpt.createHashMap(ll.getTaskId())));
            QuartzJobUtils.createOrReplaceCronJob(scheduler, ll.getTaskId(), ll.getTaskName(), "task", ll.getTaskCron(),
                CollectionsOpt.createHashMap("taskId", ll.getTaskId()));
        }
        for (TriggerKey tKey : triggerKeys) {
            int i = 0;
            for (TaskExchange ll : list) {
                TriggerKey triggerKey = TriggerKey.triggerKey(ll.getTaskId(), ll.getTaskName());
                if (tKey.equals(triggerKey))
                    break;
                i++;
            }
            if (i == list.size()) {
                QuartzJobUtils.deleteJob(scheduler, tKey.getName(), tKey.getGroup());
            }
        }
    }

    @PostConstruct
    public void init() throws SchedulerException {
        //TODO 设置一个5分钟执行一次 的定时任务调用 refreshTask
        // refreshTask();


//           QuartzJobUtils.registerJobType("beanName", JavaBeanJob.class);
//           QuartzJobUtils.createOrReplaceSimpleJob(scheduler, "testjob",
//               "test", "beanName", 10,
//               CollectionsOpt.createHashMap("beanName", "taskExchangeDao","methodName","listObjects"));

        TextOperationLogWriterImpl textOperationLogWriter
            = new TextOperationLogWriterImpl();
        textOperationLogWriter.setOptLogHomePath("D:/Projects/RunData/dde/logs");
        OperationLogCenter.initOperationLogWriter(textOperationLogWriter);
        QuartzJobUtils.registerJobType("task", RunTaskJob.class);
        refreshTask();
        scheduler.start();
    }

    @Scheduled(fixedDelay = 1000 * 10)
    public void work() throws SchedulerException {
        refreshTask();
    }
}
