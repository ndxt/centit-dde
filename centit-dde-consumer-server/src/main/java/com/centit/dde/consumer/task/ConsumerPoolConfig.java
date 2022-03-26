package com.centit.dde.consumer.task;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author zhf
 */
@Component
@Data
@ConfigurationProperties(prefix="consumer")
public class ConsumerPoolConfig {
    private Integer corePoolSize;
    private Integer maximumPoolSize;
    private Integer keepAliveTime;
    private Integer queueSize;
}
