package com.centit.dde.agent.service;

import com.centit.fileserver.client.FileClientImpl;
import com.centit.fileserver.client.FileInfoOptClient;
import com.centit.fileserver.common.FileInfoOpt;
import com.centit.framework.components.impl.NotificationCenterImpl;
import com.centit.framework.components.impl.TextOperationLogWriterImpl;
import com.centit.framework.core.service.DataScopePowerManager;
import com.centit.framework.core.service.impl.DataScopePowerManagerImpl;
import com.centit.framework.model.adapter.NotificationCenter;
import com.centit.framework.model.adapter.OperationLogWriter;
import com.centit.framework.model.adapter.UserUnitFilterCalcContextFactory;
import com.centit.framework.security.StandardPasswordEncoderImpl;
import com.centit.search.service.ESServerConfig;
import com.centit.search.utils.ImagePdfTextExtractor;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.security.SecurityOptUtils;
import com.centit.workflow.service.impl.SystemUserUnitCalcContextFactoryImpl;
import io.lettuce.core.RedisClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * @author zhf
 */
@Configuration
public class ServiceConfig implements EnvironmentAware {

    private Environment env;
    @Override
    public void setEnvironment(Environment environment) {
        this.env = environment;
    }

    private final PathConfig pathConfig;


    @Autowired
    public ServiceConfig(PathConfig pathConfig) {
        this.pathConfig = pathConfig;
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        // schedulerFactoryBean.setQuartzProperties();
        return schedulerFactoryBean;
    }

    @Bean
    public NotificationCenter notificationCenter() {
        NotificationCenterImpl notificationCenter = new NotificationCenterImpl();
        notificationCenter.initDummyMsgSenders();
        return notificationCenter;
    }
    @Bean
    public UserUnitFilterCalcContextFactory userUnitFilterFactory() {
        return new SystemUserUnitCalcContextFactoryImpl();
    }
    @Bean
    public OperationLogWriter operationLogWriter() {
        TextOperationLogWriterImpl textOperationLogWriter
            = new TextOperationLogWriterImpl();
        textOperationLogWriter.setOptLogHomePath(pathConfig.getLogs());
        return textOperationLogWriter;
    }

    @Bean("passwordEncoder")
    public StandardPasswordEncoderImpl passwordEncoder() {
        return  new StandardPasswordEncoderImpl();
    }

    @Bean
    MessageSource messageSource() {
        ReloadableResourceBundleMessageSource ms = new ReloadableResourceBundleMessageSource();
        ms.setUseCodeAsDefaultMessage(true);
        //"classpath:org/springframework/security/messages"
        ms.setBasenames("classpath:i18n/messages", "classpath:i18n/workflow", "classpath:i18n/dde",
            "classpath:org/springframework/security/messages");
        ms.setDefaultEncoding("UTF-8");
        return ms;
    }

    @Bean
    public DataScopePowerManager queryDataScopeFilter() {
        return new DataScopePowerManagerImpl();
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
    public FileInfoOpt fileInfoOpt() {
        String fileServerUrl = env.getProperty("fileserver.url");
        FileClientImpl fileClient = new FileClientImpl();
        fileClient.init(fileServerUrl, fileServerUrl, "u0000000", "000000", fileServerUrl);
        FileInfoOptClient fileStoreBean = new FileInfoOptClient();
        fileStoreBean.setFileClient(fileClient);
        return fileStoreBean;
    }

    @Bean
    public RedisClient redisClient() {
        String host = env.getProperty("redis.default.host");
        Integer port = NumberBaseOpt.castObjectToInteger(env.getProperty("redis.default.port"), 6379);
        String password = env.getProperty("redis.default.password");
        Integer database =  NumberBaseOpt.castObjectToInteger(env.getProperty("redis.default.database"), 0);
        // redis:[password@]host[:port][/database]
        StringBuilder redisUri = new StringBuilder("redis://");
        if (StringUtils.isNotBlank(password)) {
            redisUri.append(SecurityOptUtils.decodeSecurityString(password)).append("@");
        }
        redisUri.append(host).append(":").append(port).append("/").append(database);
        return RedisClient.create(redisUri.toString());
    }
}
