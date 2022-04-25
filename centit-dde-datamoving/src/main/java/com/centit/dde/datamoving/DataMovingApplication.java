package com.centit.dde.datamoving;

import com.centit.dde.po.DataPacket;
import com.centit.dde.services.DataPacketService;
import com.centit.dde.services.impl.TaskRun;
import com.centit.fileserver.common.FileStore;
import com.centit.fileserver.utils.OsFileStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author zhf
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.centit"},
    excludeFilters = @ComponentScan.Filter(value = org.springframework.stereotype.Controller.class))
public class DataMovingApplication {
    /**
     * 这个只做一个任务的一次交换
     * 由 agent 调度
     */

    @Value("${framework.app.home}")
    private String appHome;

    @Bean
    public FileStore fileStore() {
        return new OsFileStore(appHome + "/upload");
    }

    public static void main(String[] args) {
        if (args == null || args.length < 1) {
            return;
        }
        ConfigurableApplicationContext context = SpringApplication.run(DataMovingApplication.class, args);
        TaskRun taskRun = context.getBean(TaskRun.class);
        DataPacketService dataPacketService = context.getBean(DataPacketService.class);
        DataPacket dataPacket = dataPacketService.getDataPacket(args[0]);
        taskRun.runTask(dataPacket, null);
        int exitCode = SpringApplication.exit(context, () -> 0);
        System.exit(exitCode);
    }
}
