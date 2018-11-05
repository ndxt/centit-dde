package com.centit.dde.dataio;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zou_wy on 2017/7/4.
 */
//@Component
public class TestSchedule implements SchedulingConfigurer {

    @Value("${cron}")
    private String cron;

//    public TestSchedule(){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(15 * 1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//                cron = "0/10 * * * * ?";
//                System.err.println("cron change to: " + cron);
//            }
//        }).start();
//    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(new Runnable() {
            @Override
            public void run() {
                // 任务逻辑
                System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date())+"============task1 start");
                try {
                    Thread.sleep(20000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date())+"============task1 end");
            }
        }, new Trigger() {
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext) {
                // 任务触发，可修改任务的执行周期
                CronTrigger trigger = new CronTrigger(cron);
                Date nextExec = trigger.nextExecutionTime(triggerContext);
                return nextExec;
            }
        });
    }
}
