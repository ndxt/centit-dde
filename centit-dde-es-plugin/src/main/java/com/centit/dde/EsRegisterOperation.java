package com.centit.dde;

import com.centit.dde.core.BizOptFlow;
import com.centit.dde.utils.ConstantValue;
import com.centit.product.metadata.dao.SourceInfoDao;
import com.centit.search.service.ESServerConfig;
import com.centit.support.algorithm.NumberBaseOpt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
public class EsRegisterOperation {
    @Resource
    BizOptFlow bizOptFlow;

    @Autowired
    Environment env;

    @Autowired
    private SourceInfoDao sourceInfoDao;

    @PostConstruct
    void registerOperation(){
        ESServerConfig esServerConfig = esServerConfig();
        //注册查询操作类
        bizOptFlow.registerOperation(ConstantValue.ELASTICSEARCH_QUERY,
            new EsQueryBizOperation(esServerConfig, sourceInfoDao));
        //注册插入操作类
        bizOptFlow.registerOperation(ConstantValue.ELASTICSEARCH_WRITE,
            new EsWriteBizOperation(esServerConfig, sourceInfoDao));
    }

    private ESServerConfig esServerConfig() {
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
}
