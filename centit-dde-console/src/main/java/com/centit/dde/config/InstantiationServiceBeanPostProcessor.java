package com.centit.dde.config;

import com.centit.framework.components.OperationLogCenter;
import com.centit.framework.model.adapter.MessageSender;
import com.centit.framework.model.adapter.NotificationCenter;
import com.centit.framework.model.adapter.OperationLogWriter;
import com.centit.support.json.JSONOpt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.sqlite.JDBC;

import javax.annotation.Nullable;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author codefan
 * @date 17-7-6
 */
@Component
public class InstantiationServiceBeanPostProcessor implements ApplicationListener<ContextRefreshedEvent>
{
    protected static Logger logger = LoggerFactory.getLogger(InstantiationServiceBeanPostProcessor.class);
    private final NotificationCenter notificationCenter;

    private final OperationLogWriter optLogManager;

    private final MessageSender innerMessageManager;
    @Autowired
    public InstantiationServiceBeanPostProcessor(NotificationCenter notificationCenter, OperationLogWriter optLogManager, MessageSender innerMessageManager) {
        this.notificationCenter = notificationCenter;
        this.optLogManager = optLogManager;
        this.innerMessageManager = innerMessageManager;
    }

    @Override
    public void onApplicationEvent(@Nullable ContextRefreshedEvent event)
    {
        JSONOpt.fastjsonGlobalConfig();
        if(innerMessageManager!=null) {
            notificationCenter.registerMessageSender("innerMsg", innerMessageManager);
        }
        if(optLogManager!=null) {
            OperationLogCenter.registerOperationLogWriter(optLogManager);
        }

        try {
            DriverManager.registerDriver(new JDBC());
        }
        catch (SQLException e) {
            logger.error("注册sqlite驱动失败:" + e.getMessage());
        }
    }

}
