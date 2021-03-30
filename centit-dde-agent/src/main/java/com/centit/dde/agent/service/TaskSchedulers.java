package com.centit.dde.agent.service;

import com.centit.dde.dao.DataPacketCopyDao;
import com.centit.dde.po.DataPacketCopy;
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
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author zhf
 */
@Service
public class TaskSchedulers {
    private final DataPacketCopyDao dataPacketDao;
    private final Scheduler scheduler;
    private final OperationLogWriter operationLogWriter;
    private static String staticTaskMd5= "";
    private static ConcurrentHashMap<String, Object> queryParams = new ConcurrentHashMap<>(2);
    @Autowired
    public TaskSchedulers(DataPacketCopyDao dataPacketDao, Scheduler scheduler, OperationLogWriter operationLogWriter, PathConfig pathConfig) {
        this.dataPacketDao = dataPacketDao;
        this.scheduler = scheduler;
        this.operationLogWriter = operationLogWriter;
        queryParams.put("taskType", "2");
        queryParams.put("isValid", "T");
        if (pathConfig.getOwnGroups() != null && pathConfig.getOwnGroups().length > 0) {
            queryParams.put("ownGroup_in", pathConfig.getOwnGroups());
        }
    }

    private boolean isEqualMd5(List<DataPacketCopy> list) {
        boolean result = false;
        StringBuffer stringBuffer = new StringBuffer(100);
        for (DataPacketCopy i : list) {
            stringBuffer.append(i.getTaskCron());
        }
        String taskMd5 = "";
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[8192];
            int length;
            InputStream is = new ByteArrayInputStream(stringBuffer.toString().getBytes());
            while ((length = is.read(buffer)) != -1) {
                md5.update(buffer, 0, length);
            }
            taskMd5 = new String(Hex.encodeHex(md5.digest()));
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }
        if (taskMd5.equals(staticTaskMd5)) {
            result = true;
        } else {
            staticTaskMd5 = taskMd5;
        }
        return result;
    }

    private void refreshTask() throws SchedulerException {
        List<DataPacketCopy> list =  new CopyOnWriteArrayList<>(dataPacketDao.listObjectsByProperties(queryParams));
        if (isEqualMd5(list)) {
            return;
        }
        Set<TriggerKey> triggerKeys =  new CopyOnWriteArraySet<>(scheduler.getTriggerKeys(GroupMatcher.anyTriggerGroup()));
        for (DataPacketCopy ll : list) {
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
            for (DataPacketCopy ll : list) {
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
