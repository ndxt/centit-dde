package com.centit.dde.config;


import com.centit.fileserver.common.FileStore;
import com.centit.fileserver.common.FileStoreContext;
import com.centit.framework.components.impl.NotificationCenterImpl;
import com.centit.framework.components.impl.TextOperationLogWriterImpl;
import com.centit.framework.config.SpringSecurityDaoConfig;
import com.centit.framework.jdbc.config.JdbcConfig;
import com.centit.framework.model.adapter.NotificationCenter;
import com.centit.framework.model.adapter.OperationLogWriter;
import com.centit.framework.security.model.StandardPasswordEncoderImpl;
import com.centit.support.algorithm.NumberBaseOpt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;


/**
 * @author zhf
 */
@EnableAsync
@EnableScheduling
@Import({//IPOrStaticAppSystemBeanConfig.class,
    SpringSecurityDaoConfig.class,
    JdbcConfig.class})
@ComponentScan(basePackages = "com.centit",
    excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION,
        value = org.springframework.stereotype.Controller.class))
@Configuration
public class ServiceConfig {
    Class clazz;
    Logger logger = LoggerFactory.getLogger(ServiceConfig.class);

    @Value("${app.home:./}")
    private String appHome;

    @Value("${fileserver.url}")
    private String fileserver;

    @Value("${redis.host}")
    private String redisHost;

    @Value("${redis.port}")
    private String redisPort;


    /**
     * 这个bean必须要有
     *
     * @return CentitPasswordEncoder 密码加密算法
     */
    @Bean("passwordEncoder")
    public StandardPasswordEncoderImpl passwordEncoder() {
        return new StandardPasswordEncoderImpl();
    }

    @Bean
    public JedisPool jedisPool() {
        logger.info("------------------------redisPort-----------------------:"+redisPort);
        JedisPool jedisPool= new JedisPool(redisHost, 6379);
        return jedisPool;
    }

    @Bean
    public NotificationCenter notificationCenter() {
        NotificationCenterImpl notificationCenter = new NotificationCenterImpl();
        notificationCenter.initDummyMsgSenders();
        ///notificationCenter.registerMessageSender("innerMsg",innerMessageManager);
        return notificationCenter;
    }

    @Bean
    @Lazy(value = false)
    public OperationLogWriter operationLogWriter() {
        TextOperationLogWriterImpl operationLog = new TextOperationLogWriterImpl();
        operationLog.setOptLogHomePath(appHome + "/logs");
        operationLog.init();
        return operationLog;
    }

  /*  @Bean
    public FileClientImpl fileClient() {
        FileClientImpl fileClient = new FileClientImpl();
        fileClient.init(fileserver, fileserver, "u0000000", "000000", fileserver);
        return fileClient;
    }*/

    @Resource
    FileStore fileStore;

    @Bean
    public FileStoreContext fileStoreContext(){
        return new  FileStoreContext(fileStore);
    }
}
