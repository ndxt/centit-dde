package com.centit.dde.agent.service;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author zhf
 */
@Component
@ConfigurationProperties(prefix="centit")
@Data
public class PathConfig {
    private String dataMovingPath;

    private String logs;

    private String useDataMoving;

    private String[] optId;
}
