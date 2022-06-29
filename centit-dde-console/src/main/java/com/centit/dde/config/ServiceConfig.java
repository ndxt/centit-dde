package com.centit.dde.config;


import com.alibaba.nacos.api.annotation.NacosProperties;
import com.alibaba.nacos.spring.context.annotation.config.EnableNacosConfig;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySources;
import com.centit.fileserver.client.FileClient;
import com.centit.fileserver.client.FileClientImpl;
import com.centit.fileserver.client.FileInfoOptClient;
import com.centit.fileserver.common.FileInfoOpt;
import com.centit.framework.components.impl.NotificationCenterImpl;
import com.centit.framework.config.SpringSecurityDaoConfig;
import com.centit.framework.core.service.DataScopePowerManager;
import com.centit.framework.core.service.impl.DataScopePowerManagerImpl;
import com.centit.framework.jdbc.config.JdbcConfig;
import com.centit.framework.model.adapter.NotificationCenter;
import com.centit.framework.security.model.StandardPasswordEncoderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import redis.clients.jedis.JedisPool;


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
@EnableNacosConfig(globalProperties = @NacosProperties(serverAddr = "${nacos.server-addr}"))
@NacosPropertySources({@NacosPropertySource(dataId = "${nacos.system-dataid}", groupId = "CENTIT", autoRefreshed = true)}
)
public class ServiceConfig {
    Logger logger = LoggerFactory.getLogger(ServiceConfig.class);

    @Value("${app.home:./}")
    private String appHome;

    @Value("${fileserver.url}")
    private String fileserver;

    @Value("${redis.default.host}")
    private String redisHost;

    @Value("${redis.default.port}")
    private int redisPort;


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
        logger.info("------------------------redisPort-----------------------:" + redisPort);
        JedisPool jedisPool = new JedisPool(redisHost, redisPort);
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
    public DataScopePowerManager queryDataScopeFilter() {
        return new DataScopePowerManagerImpl();
    }


//    @Bean
//    @Lazy(value = false)
//    public OperationLogWriter operationLogWriter() {
//        TextOperationLogWriterImpl operationLog = new TextOperationLogWriterImpl();
//        operationLog.setOptLogHomePath(appHome + "/logs");
//        operationLog.init();
//        return operationLog;
//    }

    @Bean
    public FileClient fileClient() {
        FileClientImpl fileClient = new FileClientImpl();
        fileClient.init(fileserver, fileserver, "u0000000", "000000", fileserver);
        return fileClient;
    }

    @Bean
    public FileInfoOpt fileInfoOpt(@Autowired FileClient fileClient) {
        FileInfoOptClient fileStoreBean = new FileInfoOptClient();
        fileStoreBean.setFileClient(fileClient);
        return fileStoreBean;
    }
}
