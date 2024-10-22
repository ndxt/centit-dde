package com.centit.dde.agent;

import com.centit.dde.agent.service.ContextUtils;
import com.centit.support.json.JSONOpt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.sqlite.JDBC;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author zhf
 */
@EnableScheduling
@SpringBootApplication
@ComponentScan(basePackages = {"com.centit"})
public class AgentApplication {
    protected static Logger logger = LoggerFactory.getLogger(AgentApplication.class);
    /**
     * 运行一个后台进程，最好作为服务启动
     *
     * @param args 为 任务分组号
     */
    public static void main(String[] args) {
        JSONOpt.fastjsonGlobalConfig();
        try {
            DriverManager.registerDriver(new JDBC());
        }
        catch (SQLException e) {
            logger.error("注册sqlite驱动失败:" + e.getMessage());
        }
        ConfigurableApplicationContext context = SpringApplication.run(AgentApplication.class, args);
        ContextUtils.setApplicationContext(context);
    }

}
