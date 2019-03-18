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
        String taskLogId = args[0];
        System.out.println(taskLogId);
        //通过 TaskLogId 获取到 TaskId
        // 通过TaskId 获取 DataPacketId
        // 通过 DataPacketId创建 DBPacketBizSupplier
        // 通过TaskInfo 创建 DatabaseBizOperation
        // 这行 DatabaseBizOperation 就完成工作了
        // 每一步需要编写日志
        SpringApplication.run(DataMovingApplication.class, args);
    }
}
