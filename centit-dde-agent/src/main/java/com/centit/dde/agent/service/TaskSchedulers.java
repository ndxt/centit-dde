package com.centit.dde.agent.service;

import com.centit.dde.adapter.dao.DataPacketDao;
import com.centit.dde.adapter.po.DataPacket;
import com.centit.dde.adapter.utils.ConstantValue;
import com.centit.framework.components.CodeRepositoryCache;
import com.centit.framework.components.OperationLogCenter;
import com.centit.framework.model.adapter.OperationLogWriter;
import com.centit.framework.model.adapter.PlatformEnvironment;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.quartz.QuartzJobUtils;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
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

    private final DataPacketDao dataPacketDao;
    private final Scheduler scheduler;
    private final OperationLogWriter operationLogWriter;
    private static String staticTaskMd5= "";
    private static ConcurrentHashMap<String, Object> queryParams = new ConcurrentHashMap<>(2);

    //@Autowired
    //private DBPlatformEnvironment dbPlatformEnvironment;

    @Autowired
    public TaskSchedulers(@Autowired DataPacketDao dataPacketDao,@Autowired  PlatformEnvironment dbPlatformEnvironment,
                          @Autowired Scheduler scheduler, @Autowired OperationLogWriter operationLogWriter,
                          @Autowired PathConfig pathConfig) {
        this.dataPacketDao = dataPacketDao;
        this.scheduler = scheduler;
        this.operationLogWriter = operationLogWriter;
        CodeRepositoryCache.setPlatformEnvironment(dbPlatformEnvironment);
        queryParams.put("taskType", ConstantValue.TASK_TYPE_AGENT);
        queryParams.put("isValid", true);
        queryParams.put("isDisable", false);
        if (pathConfig.getOptId() != null && pathConfig.getOptId().length > 0) {
            queryParams.put("optId_in", pathConfig.getOptId());
        }
    }

    private void refreshTask() throws SchedulerException {
        List<DataPacket> list =  new CopyOnWriteArrayList<>(dataPacketDao.listObjectsByProperties(queryParams));
        if (isEqualMd5(list)) {
            return;
        }
        Set<TriggerKey> triggerKeys =  new CopyOnWriteArraySet<>(scheduler.getTriggerKeys(GroupMatcher.anyTriggerGroup()));
        for (DataPacket dataPacket : list) {
            if (StringUtils.isBlank(dataPacket.getTaskCron())) {
                continue;
            }
            int i = 0;
            for (TriggerKey tKey : triggerKeys) {
                if (tKey.getName().equals(dataPacket.getPacketId())) {
                    i++;
                    CronTrigger quartzTrigger = (CronTrigger) scheduler.getTrigger(tKey);
                    if (!(quartzTrigger.getCronExpression().equals(dataPacket.getTaskCron()))) {
                        QuartzJobUtils.createOrReplaceCronJob(scheduler, dataPacket.getPacketId(), dataPacket.getOptId(), "task", dataPacket.getTaskCron(),
                            CollectionsOpt.createHashMap("taskExchange", dataPacket));
                        break;
                    }
                    break;
                }
            }
            if (i == 0) {
                QuartzJobUtils.createOrReplaceCronJob(scheduler, dataPacket.getPacketId(), dataPacket.getOptId(), "task", dataPacket.getTaskCron(),
                    CollectionsOpt.createHashMap("taskExchange", dataPacket));
            }
        }
        for (TriggerKey tKey : triggerKeys) {
            boolean found = false;
            for (DataPacket ll : list) {
                TriggerKey triggerKey = TriggerKey.triggerKey(ll.getPacketId(), ll.getOptId());
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

    private boolean isEqualMd5(List<DataPacket> list) {
        boolean result = false;
        StringBuffer stringBuffer = new StringBuffer(100);
        for (DataPacket i : list) {
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

}
