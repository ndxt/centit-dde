package com.centit.dde.agent.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.dao.TaskLogDao;
import com.centit.dde.datamoving.service.TaskRun;
import com.centit.dde.po.TaskLog;
import com.centit.support.quartz.AbstractQuartzJob;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;


public class RunTaskJob extends AbstractQuartzJob {
    private String taskId;
    public RunTaskJob() {
    }

    protected void loadExecutionContext(JobExecutionContext context) {
        JobDataMap paramMap = context.getMergedJobDataMap();
        this.taskId = paramMap.getString("taskId");
    }

    protected boolean runRealJob(JobExecutionContext context) throws JobExecutionException {
        System.out.println(this.taskId);
//        JSONObject jsObject = new JSONObject();
//        jsObject.put("source","1");
//        jsObject.put("operation","persistence");
//        jsObject.put("databaseCode","0000000063");
//        jsObject.put("tableName","q_data_packet3");

//TODO 生成logid,使用command调用datamoving jar包
        TaskLogDao taskLogDao =ContextUtils.getBean(TaskLogDao.class);
        TaskLog taskLog = new TaskLog();
        taskLog.setTaskId(this.taskId);
        taskLog.setRunBeginTime(new Date());
        taskLog.setRunType("1");
        taskLogDao.saveNewObject(taskLog);
        try {
            Process proc= Runtime.getRuntime().exec("java -version");
            InputStream inputStream =proc.getInputStream();
            InputStream inputStream1 = proc.getErrorStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        TaskRun taskRun = ContextUtils.getBean(TaskRun.class);
//        taskRun.runTask(taskLog.getLogId(),null);
        return true;
    }
}
