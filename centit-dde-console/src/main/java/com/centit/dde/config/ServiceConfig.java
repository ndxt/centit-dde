package com.centit.dde.config;

import com.centit.framework.hibernate.config.DataSourceConfig;
import com.centit.framework.ip.app.config.IPAppSystemBeanConfig;
import com.centit.framework.listener.InitialWebRuntimeEnvironment;
import org.springframework.context.annotation.*;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableAsync
@EnableScheduling
@Import({IPAppSystemBeanConfig.class, DataSourceConfig.class})
@ComponentScan(basePackages={"com.centit.dde"},
        excludeFilters=@ComponentScan.Filter(value=org.springframework.stereotype.Controller.class))
public class ServiceConfig {
    @Bean(initMethod = "initialEnvironment")
    @Lazy(value = false)
    public InitialWebRuntimeEnvironment initialEnvironment() {
        return new InitialWebRuntimeEnvironment();
    }


}
