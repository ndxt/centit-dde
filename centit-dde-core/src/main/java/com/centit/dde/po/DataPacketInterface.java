package com.centit.dde.po;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.DataOptStep;

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

    Integer getLogLevel();

    Boolean getIsDisable();

    DataOptStep getDataOptStep();
}
