package com.centit.dde.agent.service;

import com.centit.framework.components.impl.TextOperationLogWriterImpl;
import com.centit.framework.model.adapter.OperationLogWriter;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class QuartzConfig {

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();

        return schedulerFactoryBean;
    }

    @Bean
    public Scheduler scheduler() {
        return schedulerFactoryBean().getScheduler();
    }
    @Autowired
    public PathConfig pathConfig;
    @Bean
    public OperationLogWriter operationLogWriter() {
        TextOperationLogWriterImpl textOperationLogWriter
            = new TextOperationLogWriterImpl();
        textOperationLogWriter.setOptLogHomePath(pathConfig.getLogs());
        return textOperationLogWriter;
    }
}
