package com.centit.dde.agent.service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author zhf
 */
@Component
@ConfigurationProperties(prefix="centit")
public class PathConfig {
    public String getDataMovingPath() {
        return dataMovingPath;
    }

    public void setDataMovingPath(String dataMovingPath) {
        this.dataMovingPath = dataMovingPath;
    }

    private String dataMovingPath;

    public String getLogs() {
        return logs;
    }

    public void setLogs(String logs) {
        this.logs = logs;
    }

    private String logs;
}
