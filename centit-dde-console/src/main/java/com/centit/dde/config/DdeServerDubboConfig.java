package com.centit.dde.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource({"classpath:dubbo-dde-server.xml"})
public class DdeServerDubboConfig {

}
