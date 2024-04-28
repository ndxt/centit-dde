package com.centit.dde.config;

import com.alibaba.nacos.api.annotation.NacosProperties;
import com.alibaba.nacos.spring.context.annotation.config.EnableNacosConfig;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySources;
import com.centit.fileserver.client.FileClientImpl;
import com.centit.fileserver.client.FileInfoOptClient;
import com.centit.fileserver.common.FileInfoOpt;
import com.centit.framework.components.impl.NotificationCenterImpl;
import com.centit.framework.config.SpringSecurityDaoConfig;
import com.centit.framework.core.service.DataScopePowerManager;
import com.centit.framework.core.service.impl.DataScopePowerManagerImpl;
import com.centit.framework.dubbo.config.DubboConfig;
import com.centit.framework.dubbo.config.IpServerDubboClientConfig;
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
import org.springframework.context.EnvironmentAware;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * @author zhf
 */
@EnableAsync
@Configuration
@EnableScheduling
@PropertySource("classpath:system.properties")
@Import({
    DubboConfig.class,
    IpServerDubboClientConfig.class,
    SpringSecurityDaoConfig.class,
    JdbcConfig.class})
@ComponentScan(basePackages = "com.centit",
    excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION,
        value = org.springframework.stereotype.Controller.class))

@EnableNacosConfig(globalProperties = @NacosProperties(serverAddr = "${nacos.server-addr}"))
@NacosPropertySources({@NacosPropertySource(dataId = "${nacos.system-dataid}", groupId = "CENTIT", autoRefreshed = true)})
public class ServiceConfig implements EnvironmentAware {
    Logger logger = LoggerFactory.getLogger(ServiceConfig.class);

    @Value("${app.home:./}")
    private String appHome;

    @Value("${fileserver.url}")
    private String fileserver;

    @Value("${redis.default.host}")
    private String redisHost;

    protected Environment env;

    @Override
    public void setEnvironment(@Autowired Environment environment) {
        if (environment != null) {
            this.env = environment;
        }
    }

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
    public CsrfTokenRepository csrfTokenRepository() {
        return new HttpSessionCsrfTokenRepository();
    }

    @Bean
    MessageSource messageSource() {
        ReloadableResourceBundleMessageSource ms = new ReloadableResourceBundleMessageSource();
        ms.setUseCodeAsDefaultMessage(true);
        ms.setBasenames("classpath:i18n/messages", "classpath:i18n/dde",
            "classpath:org/springframework/security/messages");
        ms.setDefaultEncoding("UTF-8");
        return ms;
    }

    @Bean
    public LocalValidatorFactoryBean validatorFactory() {
        return new LocalValidatorFactoryBean();
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
        String ocrServerHost = env.getProperty("ocr.server.url");
        if(StringUtils.isNotBlank(ocrServerHost)) {
            String stemp = env.getProperty("ocr.server.auth.api");
            if (StringUtils.isNotBlank(stemp)) {
                ocrServer.setAuthorUrl(ocrServerHost + stemp);
            }
            stemp = env.getProperty("ocr.server.ocr.api");
            if (StringUtils.isNotBlank(stemp)) {
                ocrServer.setOrcUrl(ocrServerHost + stemp);
            }
            stemp = env.getProperty("ocr.server.auth.username");
            if (StringUtils.isNotBlank(stemp)) {
                ocrServer.setUserName(stemp);
            }
            stemp = env.getProperty("ocr.server.auth.password");
            if (StringUtils.isNotBlank(stemp)) {
                ocrServer.setPassword(stemp);
            }
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

    @Bean
    public FileInfoOpt fileInfoOpt() {
        FileClientImpl fileClient = new FileClientImpl();
        fileClient.init(fileserver, fileserver, "u0000000", "000000", fileserver);
        FileInfoOptClient fileStoreBean = new FileInfoOptClient();
        fileStoreBean.setFileClient(fileClient);
        return fileStoreBean;
    }

}
