package com.centit.dde.config;

import com.centit.framework.config.BaseSpringMvcConfig;
import org.springframework.context.annotation.ComponentScan;

/**
 *
 * @author zou_wy
 * @date 2017/3/29
 */
@ComponentScan(basePackages = {"com.centit.dde.controller"},
        includeFilters = {@ComponentScan.Filter(value= org.springframework.stereotype.Controller.class)},
        useDefaultFilters = false)
class DdeSpringMvcConfig extends BaseSpringMvcConfig {

}
