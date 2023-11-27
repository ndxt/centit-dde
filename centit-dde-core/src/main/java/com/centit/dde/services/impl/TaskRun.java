package com.centit.dde.services.impl;

import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.adapter.dao.DataPacketDao;
import com.centit.dde.adapter.dao.DataPacketDraftDao;
import com.centit.dde.adapter.po.*;
import com.centit.dde.adapter.utils.ConstantValue;
import com.centit.dde.core.BizOptFlow;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataOptResult;
import com.centit.dde.services.TaskLogManager;
import com.centit.framework.common.ResponseData;
import com.centit.framework.model.adapter.PlatformEnvironment;
import com.centit.framework.model.basedata.OsInfo;
import com.centit.framework.model.basedata.UserInfo;
import com.centit.framework.model.security.CentitUserDetails;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.common.ObjectException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author zhf
 */
@Service
@Slf4j
public class TaskRun {

    private final TaskLogManager taskLogManager;

    private final DataPacketDraftDao dataPacketDraftDao;
    private final DataPacketDao dataPacketDao;
    private final BizOptFlow bizOptFlow;
    @Autowired
    private PlatformEnvironment platformEnvironment;

    @Autowired
    public TaskRun(TaskLogManager taskLogManager,
                   DataPacketDraftDao dataPacketCopyDao, DataPacketDao dataPacketDao,
                   BizOptFlow bizOptFlow) {
        this.taskLogManager = taskLogManager;
        this.dataPacketDraftDao = dataPacketCopyDao;
        this.dataPacketDao = dataPacketDao;
        this.bizOptFlow = bizOptFlow;
    }

    public void agentRunTask(String dataPacketId) {
        DataPacket dataPacket = dataPacketDao.getObjectById(dataPacketId);
//        CodeRepositoryCache.setPlatformEnvironment(platformEnvironment);
        DataOptContext optContext = new DataOptContext();
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
        TaskLog taskLog = buildLogInfo(optContext, dataPacketInterface);
        optContext.setTaskLog(taskLog);
        try {
            DataOptResult runResult = runOptModule(dataPacketInterface, optContext);
            //更新API信息
            updateApiData(optContext.getRunType(), dataPacketInterface);
            if(runResult.hasErrors()){
                taskLog.setOtherMessage(runResult.makeErrorResponse().getMessage());
            } else {
                taskLog.setOtherMessage("ok！");
            }
            taskLog.setRunEndTime(new Date());
            return runResult;
        } catch (Exception e) { // 未知异常，一般是泡不到这儿的
            dealException(taskLog, optContext, e);
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
                taskLogManager.saveTaskLog(taskLog, dataPacketInterface.getLogLevel());
        }
    }

    private DataOptResult runOptModule(DataPacketInterface dataPacketInterface, DataOptContext dataOptContext) throws Exception {
        JSONObject bizOptJson = dataPacketInterface.getDataOptDescJson();
        if (bizOptJson.isEmpty()) {
            throw new ObjectException("运行步骤为空");
        }
        return bizOptFlow.run(dataPacketInterface, dataOptContext);
    }

    private void dealException(TaskLog taskLog, DataOptContext optContext, Exception e) {
        taskLog.setOtherMessage(e.getMessage());
        taskLog.setRunEndTime(new Date());

        TaskDetailLog detailLog = new TaskDetailLog();
        detailLog.setRunBeginTime(new Date());
        detailLog.setTaskId(taskLog.getTaskId());
        detailLog.setLogId(taskLog.getLogId());
        detailLog.setOptNodeId("api");
        detailLog.setLogType("error");
        detailLog.setLogInfo(ObjectException.extortExceptionMessage(e));
        optContext.plusStepNo();
        detailLog.setStepNo(optContext.getStepNo());
        detailLog.setRunEndTime(new Date());
        taskLog.addDetailLog(detailLog);
    }

    private TaskLog buildLogInfo(DataOptContext context, DataPacketInterface dataPacketInterface) {
        TaskLog taskLog = new TaskLog();
        taskLog.setRunBeginTime(new Date());
        if(ConstantValue.RUN_TYPE_DEBUG.equals(context.getRunType())) {
            // 运行的是草稿
            taskLog.setApiType(0);
            // debug 状态将日志模式强制设置为 debug
            dataPacketInterface.setLogLevel(ConstantValue.LOGLEVEL_CHECK_DEBUG);
        } else {
            taskLog.setApiType(1);
        }

        if (dataPacketInterface != null) {
            if (!StringBaseOpt.isNvl(dataPacketInterface.getTaskType()) && ConstantValue.TASK_TYPE_AGENT.equals(dataPacketInterface.getTaskType())) {
                taskLog.setRunner("定时任务");
            } else {
                taskLog.setRunner(context.getCurrentUserCode());
            }
        }
        //taskLog.setLogId(UuidOpt.getUuidAsString32());
        taskLog.setOptId(dataPacketInterface.getOptId());
        taskLog.setApplicationId(dataPacketInterface.getOsId());
        taskLog.setRunType(dataPacketInterface.getPacketName());
        taskLog.setTaskId(dataPacketInterface.getPacketId());
        return taskLog;
    }

    private void updateApiData(String runType, DataPacketInterface dataPacketInterface) throws Exception {
        dataPacketInterface.setLastRunTime(new Date());
        if (ConstantValue.TASK_TYPE_AGENT.equals(dataPacketInterface.getTaskType())
            && dataPacketInterface.getIsValid()
            && !StringBaseOpt.isNvl(dataPacketInterface.getTaskCron())) {
            CronExpression cronExpression = new CronExpression(dataPacketInterface.getTaskCron());
            dataPacketInterface.setNextRunTime(cronExpression.getNextValidTimeAfter(dataPacketInterface.getLastRunTime()));
            dataPacketDao.updateObject(new String[]{"lastRunTime","nextRunTime"}, (DataPacket) dataPacketInterface);
        }else {
            dataPacketInterface.setNextRunTime(null);
        }

        /*if(ConstantValue.TASK_TYPE_AGENT.equals(dataPacketInterface.getTaskType()) && dataPacketInterface.getIsValid()){
            dataPacketDao.updateObject(new String[]{"lastRunTime","nextRunTime"}, (DataPacket) dataPacketInterface);
            //DataPacketDraft dataPacketDraft= new DataPacketDraft();
            //dataPacketDraft.setPacketId(dataPacketInterface.getPacketId());
            //dataPacketDraft.setLastRunTime(dataPacketInterface.getLastRunTime());
            //dataPacketDraft.setNextRunTime(dataPacketInterface.getNextRunTime());
            //dataPacketDraftDao.updateObject(new String[]{"lastRunTime","nextRunTime"}, dataPacketDraft);
        } else */
        if(ConstantValue.RUN_TYPE_DEBUG.equals(runType)) {
            dataPacketDraftDao.updateObject(new String[]{"lastRunTime"}, (DataPacketDraft) dataPacketInterface);
        }
    }
}
