package com.centit.dde.services.impl;

import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.adapter.dao.DataPacketDao;
import com.centit.dde.adapter.po.CallApiLog;
import com.centit.dde.adapter.po.CallApiLogDetail;
import com.centit.dde.adapter.po.DataPacket;
import com.centit.dde.adapter.po.DataPacketInterface;
import com.centit.dde.core.BizOptFlow;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataOptResult;
import com.centit.dde.services.TaskLogManager;
import com.centit.dde.utils.ConstantValue;
import com.centit.framework.common.ResponseData;
import com.centit.framework.model.adapter.PlatformEnvironment;
import com.centit.framework.model.basedata.OsInfo;
import com.centit.framework.model.basedata.UserInfo;
import com.centit.framework.model.security.CentitUserDetails;
import com.centit.support.common.ObjectException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Locale;

/**
 * @author zhf
 */
@Service
@Slf4j
public class TaskRun {

    private final TaskLogManager taskLogManager;
    private final DataPacketDao dataPacketDao;
    private final BizOptFlow bizOptFlow;

    @Autowired
    protected MessageSource messageSource;

    @Autowired
    private PlatformEnvironment platformEnvironment;

    @Autowired
    public TaskRun(TaskLogManager taskLogManager, DataPacketDao dataPacketDao,
                   BizOptFlow bizOptFlow) {
        this.taskLogManager = taskLogManager;
        this.dataPacketDao = dataPacketDao;
        this.bizOptFlow = bizOptFlow;
    }

    public void agentRunTask(String dataPacketId) {
        DataPacket dataPacket = dataPacketDao.getObjectById(dataPacketId);
//        CodeRepositoryCache.setPlatformEnvironment(platformEnvironment);
        DataOptContext optContext = new DataOptContext(messageSource, Locale.SIMPLIFIED_CHINESE);
        OsInfo osInfo = platformEnvironment.getOsInfo(dataPacket.getOsId());
        optContext.setStackData(ConstantValue.APPLICATION_INFO_TAG, osInfo);
        optContext.setTopUnit(osInfo.getTopUnit());
        CentitUserDetails userDetails = new CentitUserDetails();
        userDetails.setTopUnitCode(osInfo.getTopUnit());
        UserInfo userInfo = new UserInfo();
        userInfo.setUserCode("taskAgent");
        userInfo.setUserName("定时任务");
        userInfo.setTopUnit(osInfo.getTopUnit());
        userInfo.setPrimaryUnit(osInfo.getTopUnit());
        userDetails.setUserInfo(userInfo);
        optContext.setStackData(ConstantValue.SESSION_DATA_TAG, userDetails);
        runTask(dataPacket, optContext);
    }

    public DataOptResult runTask(DataPacketInterface dataPacketInterface, DataOptContext optContext) {
        CallApiLog callApiLog = buildLogInfo(optContext, dataPacketInterface);
        optContext.setCallApiLog(callApiLog);
        try {
            DataOptResult runResult = runOptModule(dataPacketInterface, optContext);
            //更新定时任务的API最后执行时间
            updateApiData(dataPacketInterface);//optContext.getRunType(),
            if(runResult.hasErrors()){
                callApiLog.setOtherMessage(runResult.makeErrorResponse().getMessage());
            } else {
                callApiLog.setOtherMessage("ok！");
            }
            callApiLog.setRunEndTime(new Date());
            return runResult;
        } catch (Exception e) { // 未知异常，一般是泡不到这儿的
            dealException(callApiLog, optContext, e);
            //创建一个错误的返回结果
            if(e instanceof ObjectException){
                ObjectException objex = (ObjectException) e;
                return DataOptResult.createExceptionResult(ResponseData.makeErrorMessageWithData(
                    objex.getObjectData(), objex.getExceptionCode(), objex.getMessage()));
            }
            return DataOptResult.createExceptionResult(ResponseData.makeErrorMessageWithData(
                null, ResponseData.ERROR_OPERATION, ObjectException.extortExceptionOriginMessage(e)));
        } finally { // 写入日志
            //如果是 debug 并且是断点（debugId不为空）状态不写入日志
            if(StringUtils.equals(optContext.getRunType(), ConstantValue.RUN_TYPE_NORMAL)
                || StringUtils.isBlank(optContext.getDebugId()))
                taskLogManager.saveTaskLog(callApiLog, dataPacketInterface.getLogLevel());
        }
    }

    private DataOptResult runOptModule(DataPacketInterface dataPacketInterface, DataOptContext dataOptContext) throws Exception {
        JSONObject bizOptJson = dataPacketInterface.getDataOptDescJson();
        if (bizOptJson.isEmpty()) {
            throw new ObjectException("运行步骤为空");
        }
        return bizOptFlow.run(dataPacketInterface, dataOptContext);
    }

    private void dealException(CallApiLog callApiLog, DataOptContext optContext, Exception e) {
        callApiLog.setOtherMessage(ObjectException.extortExceptionOriginMessage(e));
        callApiLog.setRunEndTime(new Date());

        CallApiLogDetail detailLog = new CallApiLogDetail();
        detailLog.setRunBeginTime(new Date());
        detailLog.setOptNodeId("api");
        detailLog.setLogType("error");
        detailLog.setLogInfo(ObjectException.extortExceptionMessage(e));
        optContext.plusStepNo();
        detailLog.setStepNo(optContext.getStepNo());
        detailLog.setRunEndTime(new Date());
        callApiLog.addDetailLog(detailLog);
    }

    private CallApiLog buildLogInfo(DataOptContext context, DataPacketInterface dataPacketInterface) {
        CallApiLog callApiLog = new CallApiLog();
        callApiLog.setRunBeginTime(new Date());
        if(ConstantValue.RUN_TYPE_DEBUG.equals(context.getRunType())) {
            // 运行的是草稿
            callApiLog.setApiType(0);
            // debug 状态将日志模式强制设置为 debug
            dataPacketInterface.setLogLevel(ConstantValue.LOGLEVEL_CHECK_DEBUG);
        } else {
            callApiLog.setApiType(1);
        }

        if (dataPacketInterface != null) {
            if (ConstantValue.TASK_TYPE_AGENT.equals(dataPacketInterface.getTaskType())) {
                callApiLog.setRunner("定时任务");
            } else {
                callApiLog.setRunner(context.getCurrentUserCode());
            }
        }
        //taskLog.setLogId(UuidOpt.getUuidAsString32());
        callApiLog.setOptId(dataPacketInterface.getOptId());
        callApiLog.setTopUnit(context.getTopUnit());
        callApiLog.setApplicationId(dataPacketInterface.getOsId());
        callApiLog.setRunType(dataPacketInterface.getPacketName());
        callApiLog.setTaskId(dataPacketInterface.getPacketId());
        return callApiLog;
    }

    /**
     * 定时任务记录 下次运行时间
     * @param dataPacketInterface 任务信息
     * @throws Exception Cron 表达式异常
     */
    private void updateApiData(DataPacketInterface dataPacketInterface) throws Exception {
        if (ConstantValue.TASK_TYPE_AGENT.equals(dataPacketInterface.getTaskType())
           && dataPacketInterface instanceof DataPacket && dataPacketInterface.getIsValid()
            && StringUtils.isNotBlank(dataPacketInterface.getTaskCron())) {
            dataPacketInterface.setLastRunTime(new Date());
            CronExpression cronExpression = new CronExpression(dataPacketInterface.getTaskCron());
            dataPacketInterface.setNextRunTime(cronExpression.getNextValidTimeAfter(dataPacketInterface.getLastRunTime()));
            dataPacketDao.updateObject(new String[]{"lastRunTime","nextRunTime"}, (DataPacket) dataPacketInterface);
        }
    }
}
