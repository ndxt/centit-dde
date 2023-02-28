package com.centit.dde.test;

import com.alibaba.fastjson2.annotation.JSONField;
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
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date recordDate;

    private String hasDataOpt;

    private String dataOptDescJson;

    private String bufferFreshPeriod;

    /**
     * 1表示普通任务，2表示定时任务
     */
    private String taskType;

    private String taskCron;

    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date lastRunTime;
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date nextRunTime;

    private String isValid;

    private String interfaceName;

    private String isWhile;

    private String returnType;

    private String returnResult;

    private String ownGroup;
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date updateDate;

    /**
     * 发布时间
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date publishDate;

    private String needRollback;
}
