package com.centit.dde.agent.service;

import com.centit.dde.dao.DataPacketDao;
import com.centit.dde.po.DataPacket;
import com.centit.framework.components.OperationLogCenter;
import com.centit.framework.model.adapter.OperationLogWriter;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.quartz.QuartzJobUtils;
import org.apache.commons.codec.binary.Hex;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author zhf
 */
@Service
public class TaskSchedulers {
    private final DataPacketDao dataPacketDao;

    private final Scheduler scheduler;

    private final OperationLogWriter operationLogWriter;
    private String taskMd5;
    private final PathConfig pathConfig;

    @Autowired
    public TaskSchedulers(DataPacketDao dataPacketDao, Scheduler scheduler, OperationLogWriter operationLogWriter, PathConfig pathConfig) {
        this.dataPacketDao = dataPacketDao;
        this.scheduler = scheduler;
        this.operationLogWriter = operationLogWriter;
        this.pathConfig = pathConfig;
    }

    private boolean isEqualMd5(List<DataPacket> list) {
        boolean result = false;
        StringBuilder sList = new StringBuilder();
        for (DataPacket i : list) {
            sList.append(i.getTaskCron());
        }
        String taskMd5 = "";
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[8192];
            int length;
            InputStream is = new ByteArrayInputStream(sList.toString().getBytes());
            while ((length = is.read(buffer)) != -1) {
                md5.update(buffer, 0, length);
            }
            taskMd5 = new String(Hex.encodeHex(md5.digest()));
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }
        if (taskMd5.equals(this.taskMd5)) {
            result = true;
        } else {
            this.taskMd5 = taskMd5;
        }
        return result;
    }

    private void refreshTask() throws SchedulerException {
        Map<String, Object> map = CollectionsOpt.createHashMap("taskType", "2", "isValid", "T");
        if (pathConfig.getOwnGroups() != null && pathConfig.getOwnGroups().length > 0) {
            map.put("ownGroup_in", pathConfig.getOwnGroups());
        }
        List<DataPacket> list = dataPacketDao.listObjectsByProperties(map);
        if (isEqualMd5(list)) {
            return;
        }
        Set<TriggerKey> triggerKeys = scheduler.getTriggerKeys(GroupMatcher.anyTriggerGroup());

        for (DataPacket ll : list) {
            if ("".equals(ll.getTaskCron()) || ll.getTaskCron() == null) {
                continue;
            }
            int i = 0;
            for (TriggerKey tKey : triggerKeys) {
                if (tKey.getName().equals(ll.getPacketId())) {
                    i++;
                    CronTrigger quartzTrigger = (CronTrigger) scheduler.getTrigger(tKey);
                    if (!(quartzTrigger.getCronExpression().equals(ll.getTaskCron()))) {
                        QuartzJobUtils.createOrReplaceCronJob(scheduler, ll.getPacketId(), ll.getOwnGroup(), "task", ll.getTaskCron(),
                            CollectionsOpt.createHashMap("taskExchange", ll));
                        break;
                    }
                    break;
                }
            }
            if (i == 0) {
                QuartzJobUtils.createOrReplaceCronJob(scheduler, ll.getPacketId(), ll.getOwnGroup(), "task", ll.getTaskCron(),
                    CollectionsOpt.createHashMap("taskExchange", ll));
            }
        }
        for (TriggerKey tKey : triggerKeys) {
            boolean found = false;
            for (DataPacket ll : list) {
                TriggerKey triggerKey = TriggerKey.triggerKey(ll.getPacketId(), ll.getOwnGroup());
                if (tKey.equals(triggerKey)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                QuartzJobUtils.deleteJob(scheduler, tKey.getName(), tKey.getGroup());
            }
        }
    }

    @PostConstruct
    public void init() throws SchedulerException {
        OperationLogCenter.initOperationLogWriter(operationLogWriter);
        QuartzJobUtils.registerJobType("task", RunTaskJob.class);
        scheduler.start();
    }

    /**
     * 5minute
     */
    @Scheduled(fixedDelay = 1000 * 50)
    public void work() throws SchedulerException {
        refreshTask();
    }
}
