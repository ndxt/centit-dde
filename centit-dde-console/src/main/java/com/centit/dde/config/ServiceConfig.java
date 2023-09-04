package com.centit.dde.config;

import com.alibaba.nacos.api.annotation.NacosProperties;
import com.alibaba.nacos.spring.context.annotation.config.EnableNacosConfig;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySources;
import com.centit.framework.components.impl.NotificationCenterImpl;
import com.centit.framework.config.SpringSecurityDaoConfig;
import com.centit.framework.core.service.DataScopePowerManager;
import com.centit.framework.core.service.impl.DataScopePowerManagerImpl;
import com.centit.framework.jdbc.config.JdbcConfig;
import com.centit.framework.model.adapter.NotificationCenter;
import com.centit.framework.security.StandardPasswordEncoderImpl;
import com.centit.search.service.ESServerConfig;
import com.centit.search.utils.ImagePdfTextExtractor;
import com.centit.support.algorithm.NumberBaseOpt;
import io.lettuce.core.RedisClient;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

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

    @Autowired
    Environment env;

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
    public RedisClient redisClient() {
        return RedisClient.create(redisHost);
    }

    @Bean
    public NotificationCenter notificationCenter() {
        NotificationCenterImpl notificationCenter = new NotificationCenterImpl();
        notificationCenter.initDummyMsgSenders();
        ///notificationCenter.registerMessageSender("innerMsg",innerMessageManager);
        return notificationCenter;
    }

    @Bean
    public ImagePdfTextExtractor.OcrServerHost ocrServerHost() {
        ImagePdfTextExtractor.OcrServerHost ocrServer = ImagePdfTextExtractor.fetchDefaultOrrServer();
        String stemp = env.getProperty("ocr.server.author.url");
        if(StringUtils.isNotBlank(stemp)){
            ocrServer.setAuthorUrl(stemp);
        }
        stemp = env.getProperty("ocr.server.recognition.url");
        if(StringUtils.isNotBlank(stemp)){
            ocrServer.setOrcUrl(stemp);
        }
        stemp = env.getProperty("ocr.server.author.username");
        if(StringUtils.isNotBlank(stemp)){
            ocrServer.setUserName(stemp);
        }
        stemp = env.getProperty("ocr.server.author.password");
        if(StringUtils.isNotBlank(stemp)){
            ocrServer.setPassword(stemp);
        }
        return ocrServer;
    }
    /*es 配置信息*/
    @Bean
    public ESServerConfig esServerConfig() {
        ESServerConfig config = new ESServerConfig();
        config.setServerHostIp(env.getProperty("elasticsearch.server.ip"));
        config.setServerHostPort(env.getProperty("elasticsearch.server.port"));
        config.setClusterName(env.getProperty("elasticsearch.server.cluster"));
        config.setOsId(env.getProperty("elasticsearch.osId"));
        config.setUsername(env.getProperty("elasticsearch.server.username"));
        config.setPassword(env.getProperty("elasticsearch.server.password"));
        config.setMinScore(NumberBaseOpt.parseFloat(env.getProperty("elasticsearch.filter.minScore"), 0.5F));
        return config;
    }

    @Bean
    public DataScopePowerManager queryDataScopeFilter() {
        return new DataScopePowerManagerImpl();
    }

}
