package com.centit.dde.config;

import com.centit.framework.config.BaseSpringMvcConfig;
import com.centit.framework.config.WebConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by zou_wy on 2017/3/29.
 */
@Configuration
@ComponentScan(basePackages = {"com.centit.dde.controller"},
        includeFilters = {@ComponentScan.Filter(value= org.springframework.stereotype.Controller.class)},
        useDefaultFilters = false)
public class NormalSpringMvcConfig extends BaseSpringMvcConfig {

}
