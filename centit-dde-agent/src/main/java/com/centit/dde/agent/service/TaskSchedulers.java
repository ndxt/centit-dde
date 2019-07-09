package com.centit.dde.agent.service;

import com.centit.dde.dao.TaskExchangeDao;

import com.centit.dde.po.TaskExchange;

import com.centit.framework.components.OperationLogCenter;
import com.centit.framework.components.impl.TextOperationLogWriterImpl;
import com.centit.framework.model.adapter.OperationLogWriter;
import com.centit.support.algorithm.CollectionsOpt;

import com.centit.support.quartz.QuartzJobUtils;
import com.sun.tools.internal.ws.wsdl.parser.DOMForest;
import com.sun.xml.internal.bind.v2.TODO;
import jdk.nashorn.internal.scripts.JO;
import org.apache.commons.codec.binary.Hex;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Set;

@Service
public class TaskSchedulers {
    @Autowired
    private TaskExchangeDao taskExchangeDao;

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private OperationLogWriter operationLogWriter;
    private String taskMD5;
    private boolean isEqualMD5(List<TaskExchange> list){
        boolean result = false;
        String sList = "";
        for(TaskExchange i:list){
           sList += i.getTaskCron();
        }
        String taskMD5="";
        try {
            MessageDigest MD5 = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[8192];
            int length;
            InputStream is =new ByteArrayInputStream(sList.getBytes());
            while ((length = is.read(buffer)) != -1) {
                MD5.update(buffer, 0, length);
            }
            taskMD5=new String(Hex.encodeHex(MD5.digest()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (taskMD5.equals(this.taskMD5))
            result = true;
        else this.taskMD5 = taskMD5;
        return result;
    }
    private void refreshTask() throws SchedulerException {
        List<TaskExchange> list = taskExchangeDao.listObjects();
        if (isEqualMD5(list)) return;
        Set<TriggerKey> triggerKeys = scheduler.getTriggerKeys(GroupMatcher.anyTriggerGroup());

        for (TaskExchange ll : list) {
            if ("".equals(ll.getTaskCron())||ll.getTaskCron()==null ) {
                continue;
            }
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
            boolean found = false;
            for (TaskExchange ll : list) {
                TriggerKey triggerKey = TriggerKey.triggerKey(ll.getTaskId(), ll.getTaskName());
                if (tKey.equals(triggerKey)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                QuartzJobUtils.deleteJob(scheduler, tKey.getName(), tKey.getGroup());
            }
        }
    }

    @PostConstruct
    public void init() throws SchedulerException {
        OperationLogCenter.initOperationLogWriter(operationLogWriter);
        QuartzJobUtils.registerJobType("task", RunTaskJob.class);
        scheduler.start();
    }

    @Scheduled(fixedDelay = 1000 * 50)//5minute
    public void work() throws SchedulerException {
        refreshTask();
    }
}
