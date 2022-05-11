package com.centit.dde.es;

import com.centit.dde.core.BizOptFlow;
import com.centit.dde.utils.ConstantValue;
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
    Environment environment;

    @PostConstruct
    void registerOperation(){
        ESServerConfig esServerConfig = esServerConfig();
        //注册查询操作类
        bizOptFlow.registerOperation(ConstantValue.ELASTICSEARCH_QUERY,new EsQueryBizOperation(esServerConfig));
        //注册插入操作类
        bizOptFlow.registerOperation(ConstantValue.ELASTICSEARCH_WRITE,new EsWriteBizOperation(esServerConfig));
    }

    private ESServerConfig esServerConfig() {
        ESServerConfig config = new ESServerConfig();
        config.setServerHostIp(environment.getProperty("elasticsearch.server.ip"));
        config.setServerHostPort(environment.getProperty("elasticsearch.server.port"));
        config.setClusterName(environment.getProperty("elasticsearch.server.cluster"));
        config.setOsId(environment.getProperty("elasticsearch.osId"));
        config.setMinScore(NumberBaseOpt.parseFloat(environment.getProperty("elasticsearch.filter.minScore"), 0.5f));
        return config;
    }
}
