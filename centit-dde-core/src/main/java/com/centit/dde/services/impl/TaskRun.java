package com.centit.dde.services.impl;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizOptFlow;
import com.centit.dde.dao.DataPacketDao;
import com.centit.dde.dao.TaskDetailLogDao;
import com.centit.dde.dao.TaskLogDao;
import com.centit.dde.po.DataPacket;
import com.centit.dde.po.TaskDetailLog;
import com.centit.dde.po.TaskLog;
import com.centit.dde.utils.Constant;
import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.common.ObjectException;
import com.centit.support.database.utils.DatabaseAccess;
import org.apache.commons.mail.MultiPartEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhf
 */
@Service
public class TaskRun {
    private final TaskLogDao taskLogDao;
    private final TaskDetailLogDao taskDetailLogDao;
    private final DataPacketDao dataPacketDao;
    private final BizOptFlow bizOptFlow;
    @Value("${email.hostName:}")
    private String hostName;

    @Value("${email.smtpPort:25}")
    private int smtpPort;

    @Value("${email.userName:}")
    private String userName;

    @Value("${email.userPassword:}")
    private String userPassword;

    @Value("${email.serverEmail:}")
    private String serverEmail;
    @Value("${email.emailTo:}")
    private String mailTo;

    @Autowired
    public TaskRun(TaskLogDao taskLogDao,
                   TaskDetailLogDao taskDetailLogDao,
                   DataPacketDao dataPacketDao,
                   BizOptFlow bizOptFlow) {
        this.taskLogDao = taskLogDao;
        this.taskDetailLogDao = taskDetailLogDao;
        this.dataPacketDao = dataPacketDao;
        this.bizOptFlow = bizOptFlow;
    }

    private Object runStep(DataPacket dataPacket, String logId, Map<String, Object> queryParams) throws Exception {
        JSONObject bizOptJson = dataPacket.getDataOptDescJson();
        if (bizOptJson.isEmpty()) {
            return null;
        }
        bizOptFlow.initStep(0);
        Map<String, Object> mapObject = new HashMap<>(dataPacket.getPacketParamsValue());
        if(queryParams!=null) {
            mapObject.putAll(queryParams);
        }
        return bizOptFlow.run(bizOptJson, logId, mapObject);
    }


    private void saveDetail(String info, TaskLog taskLog) {
        TaskDetailLog detailLog = new TaskDetailLog();
        detailLog.setRunBeginTime(new Date());
        detailLog.setTaskId(taskLog.getTaskId());
        detailLog.setLogId(taskLog.getLogId());
        detailLog.setLogType("error");
        detailLog.setLogInfo(info);
        detailLog.setRunEndTime(new Date());
        taskDetailLogDao.saveNewObject(detailLog);
    }

    public Object runTask(String packetId, Map<String, Object> queryParams) {
        TaskLog taskLog = new TaskLog();
        Date beginTime = new Date();
        DataPacket dataPacket = dataPacketDao.getObjectWithReferences(packetId);
        try {
            dataPacket.setLastRunTime(new Date());
            taskLog.setTaskId(packetId);
            taskLog.setApplicationId(dataPacket.getApplicationId());
            taskLog.setRunBeginTime(beginTime);
            taskLog.setRunType("V3"+dataPacket.getPacketName());
            taskLogDao.saveNewObject(taskLog);
            Object bizModel = runStep(dataPacket, taskLog.getLogId(), queryParams);
            taskLog.setRunEndTime(new Date());
            dataPacket.setNextRunTime(new Date());
            String two = "2";
            if (two.equals(dataPacket.getTaskType())
                && dataPacket.getIsValid()
                && !StringBaseOpt.isNvl(dataPacket.getTaskCron())) {
                CronSequenceGenerator cronSequenceGenerator = new CronSequenceGenerator(dataPacket.getTaskCron());
                dataPacket.setNextRunTime(cronSequenceGenerator.next(dataPacket.getLastRunTime()));
            }
            DatabaseOptUtils.doExecuteSql(dataPacketDao,"update q_data_packet set next_run_time=? where packet_id=?",
                new Object[]{dataPacket.getNextRunTime(),dataPacket.getPacketId()});
            TaskDetailLog taskDetailLog = taskDetailLogDao.getObjectByProperties(
                CollectionsOpt.createHashMap("logId", taskLog.getLogId(), "logInfo_ne", "ok"));
            taskLog.setOtherMessage(taskDetailLog == null ? "ok" : "error");
            taskLogDao.updateObject(taskLog);
            return bizModel;
        } catch (Exception e) {
            saveDetail(ObjectException.extortExceptionMessage(e, 4),
                taskLog);
            taskLog.setOtherMessage("error");
            taskLog.setRunEndTime(new Date());
            taskLogDao.mergeObject(taskLog);
            sendEmailMessage("任务执行异常", dataPacket.getPacketId() + dataPacket.getPacketName());
        }
        return null;
    }

    private void sendEmailMessage(String msgSubject, String msgContent) {
        if (StringBaseOpt.isNvl(mailTo)) {
            return;
        }
        try {
            MultiPartEmail multMail = new MultiPartEmail();
            multMail.setHostName(hostName);
            multMail.setSmtpPort(smtpPort);
            multMail.setAuthentication(userName, userPassword);
            multMail.setFrom(serverEmail);
            multMail.addTo(mailTo);
            multMail.setCharset("utf-8");
            multMail.setSubject(msgSubject);
            if (msgContent.endsWith("</html>") || msgContent.endsWith("</HTML>")) {
                multMail.addPart(msgContent, "text/html;charset=utf-8");
            } else {
                multMail.setContent(msgContent, "text/plain;charset=gb2312");
            }
            multMail.send();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
