package com.centit.dde.agent;

import com.centit.dde.agent.service.ContextUtils;
import com.centit.support.json.JSONOpt;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author zhf
 */
@EnableScheduling
@SpringBootApplication
@ComponentScan(basePackages = {"com.centit"},
    excludeFilters = @ComponentScan.Filter(value = org.springframework.stereotype.Controller.class))
public class AgentApplication {
    /**
     * 运行一个后台进程，最好作为服务启动
     *
     * @param args 为 任务分组号
     */
    public static void main(String[] args) {
        JSONOpt.fastjsonGlobalConfig();
        ConfigurableApplicationContext context = SpringApplication.run(AgentApplication.class, args);
        ContextUtils.setApplicationContext(context);
    }
}
