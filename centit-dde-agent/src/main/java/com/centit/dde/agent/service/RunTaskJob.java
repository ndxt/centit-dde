package com.centit.dde.agent.service;

import com.centit.dde.adapter.po.DataPacket;
import com.centit.dde.services.impl.TaskRun;
import com.centit.support.quartz.AbstractQuartzJob;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.TriggerUtils;
import org.quartz.impl.triggers.CronTriggerImpl;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhf
 */
//@DisallowConcurrentExecution
public class RunTaskJob extends AbstractQuartzJob {
    private static ConcurrentHashMap<String, Boolean> runningTask = new ConcurrentHashMap<>(128);

    public RunTaskJob() {
    }

    @Override
    protected void loadExecutionContext(JobExecutionContext context) {

    }

    @Override
    protected boolean runRealJob(JobExecutionContext context) {
        JobDataMap paramMap = context.getMergedJobDataMap();
        DataPacket dataPacket = (DataPacket) paramMap.get("taskExchange");

        PathConfig pathConfig = ContextUtils.getBean(PathConfig.class);
        boolean userMoving = "true".equalsIgnoreCase(pathConfig.getUseDataMoving());

        if (userMoving) {
            try {
                Runtime.getRuntime().exec("java -jar " + pathConfig.getDataMovingPath() + " " + dataPacket.getPacketId());
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        } else {
            Boolean isRunning = runningTask.put(dataPacket.getPacketId(), true);
            if (isRunning != null && isRunning) {
                logger.info("任务：" + dataPacket.getPacketId() + "仍在执行中忽略这个执行周期");
                return true;
            }
            try {
                TaskRun taskRun = ContextUtils.getBean(TaskRun.class);
                logger.info(String.format("execute scheduled tasks,taskName:%s,taskID：%s,cron expression：%s,execute time:%s",
                    dataPacket.getPacketName(),dataPacket.getPacketId(),dataPacket.getTaskCron(),getDate(dataPacket.getTaskCron())));
                taskRun.agentRunTask(dataPacket.getPacketId());
            } finally {
                runningTask.put(dataPacket.getPacketId(), false);
            }
        }
        return true;
    }

    private static String getDate(String cron){
        try {
            CronTriggerImpl cronTrigger = new CronTriggerImpl();
            cronTrigger.setCronExpression(cron);
            List<Date> dates = TriggerUtils.computeFireTimes(cronTrigger, null, 1);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.format(dates.get(0));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
