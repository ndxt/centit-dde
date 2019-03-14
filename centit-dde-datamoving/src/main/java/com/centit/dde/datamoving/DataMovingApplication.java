package com.centit.dde.datamoving;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DataMovingApplication {
    /**
     * 这个只做一个任务的一次交换
     * 由 agent 调度
     * @param args args[0] 为任务ID
     */
    public static void main(String[] args) {
        if(args==null || args.length<1){
            return;
        }
        String taskId = args[0];
        System.out.println(taskId);
        SpringApplication.run(DataMovingApplication.class, args);
    }
}
