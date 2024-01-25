package com.centit.dde.adapter.po;

import com.alibaba.fastjson2.JSONObject;

import java.util.Date;
import java.util.Map;

/**
 * @author zhf
 */
public interface DataPacketInterface {
    /**
     * 设置运行时间
     *
     * @param lastRunTime 最后运行时间
     */
    void setLastRunTime(Date lastRunTime);

    Date getLastRunTime();

    void setNextRunTime(Date nextRunTime);

    Date getNextRunTime();

    Date getRecordDate();

    Date getUpdateDate();
    /**
     * 获取applicationId
     *
     * @return applicationId
     */
    String getOsId();

    String getOptId();

    String getPacketId();

    String getPacketName();

    String getTaskType();

    Boolean getIsValid();

    String getTaskCron();

    JSONObject getDataOptDescJson();

    Map<String, Object> getPacketParamsValue();

    String getNeedRollback();

    Integer getBufferFreshPeriod();

    Integer getBufferFreshPeriodType();

    void setLogLevel(Integer logLevel);

    Integer getLogLevel();

    Boolean getIsDisable();

    DataOptStep attainDataOptStep();
}
