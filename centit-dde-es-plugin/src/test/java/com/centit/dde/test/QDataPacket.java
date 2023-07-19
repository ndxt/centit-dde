package com.centit.dde.test;

import lombok.Data;

import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author zhou_c
 * @since 2021-07-28
 */
@Data
public class QDataPacket{

    private static final long serialVersionUID = 1L;

    private String packetId;

    private String applicationId;

    private String ownerType;

    private String ownerCode;

    private String packetName;

    private String packetType;

    private String packetDesc;

    private String Recorder;

    private Date recordDate;

    private String hasDataOpt;

    private String dataOptDescJson;

    private String bufferFreshPeriod;

    /**
     * 1表示普通任务，2表示定时任务
     */
    private String taskType;

    private String taskCron;

    private Date lastRunTime;

    private Date nextRunTime;

    private String isValid;

    private String interfaceName;

    private String isWhile;

    private String returnType;

    private String returnResult;

    private String ownGroup;

    private Date updateDate;

    /**
     * 发布时间
     */
    private Date publishDate;

    private String needRollback;
}
