package com.centit.dde.agent.service;

import com.centit.framework.components.impl.TextOperationLogWriterImpl;
import com.centit.framework.model.adapter.OperationLogWriter;
import com.centit.framework.security.model.CentitPasswordEncoder;
import com.centit.framework.security.model.StandardPasswordEncoderImpl;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * @author zhf
 */
@Configuration
public class QuartzConfig {
    private final PathConfig pathConfig;


    @Autowired
    public QuartzConfig(PathConfig pathConfig) {
        this.pathConfig = pathConfig;
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        return new SchedulerFactoryBean();
    }

    @Bean
    public Scheduler scheduler() {
        return schedulerFactoryBean().getScheduler();
    }

    @Bean
    public OperationLogWriter operationLogWriter() {
        TextOperationLogWriterImpl textOperationLogWriter
            = new TextOperationLogWriterImpl();
        textOperationLogWriter.setOptLogHomePath(pathConfig.getLogs());
        return textOperationLogWriter;
    }
    @Bean("passwordEncoder")
    public StandardPasswordEncoderImpl passwordEncoder() {
        return  new StandardPasswordEncoderImpl();
    }
}
