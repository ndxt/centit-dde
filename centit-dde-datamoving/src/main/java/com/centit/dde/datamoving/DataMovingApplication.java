package com.centit.dde.datamoving;

import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.adapter.po.DataPacket;
import com.centit.dde.adapter.utils.ConstantValue;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.services.DataPacketService;
import com.centit.dde.services.impl.TaskRun;
import com.centit.fileserver.common.FileStore;
import com.centit.fileserver.utils.OsFileStore;
import com.centit.framework.model.adapter.PlatformEnvironment;
import com.centit.framework.model.basedata.OsInfo;
import com.centit.framework.model.basedata.UserInfo;
import com.centit.framework.model.security.CentitUserDetails;
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

        PlatformEnvironment platformEnvironment = context.getBean(PlatformEnvironment.class);
        DataOptContext optContext = new DataOptContext();
        OsInfo osInfo =platformEnvironment.getOsInfo(dataPacket.getOsId());
        optContext.setStackData(ConstantValue.APPLICATION_INFO_TAG, osInfo);
        optContext.setTopUnit(osInfo.getTopUnit());
        CentitUserDetails userDetails = new CentitUserDetails();
        userDetails.setTopUnitCode(osInfo.getTopUnit());
        UserInfo userInfo = new UserInfo();
        userInfo.setUserCode("taskAgent");
        userInfo.setUserName("定时任务");
        userInfo.setTopUnit(osInfo.getTopUnit());
        userInfo.setPrimaryUnit(osInfo.getTopUnit());
        userDetails.setUserInfo(userInfo);
        optContext.setStackData(ConstantValue.SESSION_DATA_TAG, userDetails);
        taskRun.runTask(dataPacket, optContext);
        int exitCode = SpringApplication.exit(context, () -> 0);
        System.exit(exitCode);
    }
}
