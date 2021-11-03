package com.centit.dde.consumer.config;

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

    public String getUseDataMoving() {
        return useDataMoving;
    }

    public void setUseDataMoving(String useDataMoving) {
        this.useDataMoving = useDataMoving;
    }

    private String useDataMoving;

    public String[] getOptId() {
        return optId;
    }

    public void setOptId(String[] ownGroups) {
        this.optId = ownGroups;
    }

    private String[] optId;
}
