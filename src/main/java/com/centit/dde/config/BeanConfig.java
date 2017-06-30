package com.centit.dde.config;

import com.centit.framework.listener.InitialWebRuntimeEnvironment;
import com.centit.framework.hibernate.config.DataSourceConfig;
import com.centit.framework.staticsystem.config.SpringConfig;
import org.springframework.context.annotation.*;

@Configuration
@Import({SpringConfig.class, DataSourceConfig.class})
@ComponentScan(basePackages={"com.centit.dde"},
        excludeFilters={@ComponentScan.Filter(type= FilterType.ANNOTATION,
                value=org.springframework.stereotype.Controller.class)})
public class BeanConfig  {
    @Bean(initMethod = "initialEnvironment")
    @Lazy(value = false)
    public InitialWebRuntimeEnvironment initialEnvironment() {
        return new InitialWebRuntimeEnvironment();
    }
}
