package com.centit.dde.factory;

import com.centit.dde.config.ElasticSearchConfig;
import com.centit.product.adapter.po.SourceInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;

import java.util.concurrent.ConcurrentHashMap;

public class PooledRestClientFactory implements PooledObjectFactory<RestHighLevelClient> {
    public static final Log log = LogFactory.getLog(PooledRestClientFactory.class);

    ElasticSearchConfig elasticSearchConfig;

    SourceInfo sourceInfo;

    public PooledRestClientFactory(ElasticSearchConfig config, SourceInfo sourceInfo){
        this.elasticSearchConfig = config;
        this.sourceInfo=sourceInfo;
    }

    @Override
    public PooledObject<RestHighLevelClient> makeObject() throws Exception {
        return new DefaultPooledObject<>(elasticSearchConfig.restHighLevelClient(sourceInfo));
    }

    @Override
    public void destroyObject(PooledObject<RestHighLevelClient> p) throws Exception {
        RestHighLevelClient client = p.getObject();
        if( client!= null && client.ping(RequestOptions.DEFAULT)) {
            try {
                client.close();
            }catch (Exception e){
            }
        }
    }

    @Override
    public boolean validateObject(PooledObject<RestHighLevelClient> p) {
        RestHighLevelClient client = p.getObject();
        try {
            return client.ping(RequestOptions.DEFAULT);
        }catch(Exception e){
            return false;
        }
    }

    @Override
    public void activateObject(PooledObject<RestHighLevelClient> p) throws Exception {
        RestHighLevelClient restHighLevelClient = p.getObject();
        restHighLevelClient.ping(RequestOptions.DEFAULT);
    }

    @Override
    public void passivateObject(PooledObject<RestHighLevelClient> p) throws Exception {
    }

    public void setConifg(ElasticSearchConfig elasticSearchConfig) {
        this.elasticSearchConfig = elasticSearchConfig;
    }

    private static ConcurrentHashMap<String, GenericObjectPool<RestHighLevelClient>> clientPoolMap = new ConcurrentHashMap<>();

    public static GenericObjectPool<RestHighLevelClient> obtainclientPool(ElasticSearchConfig config,SourceInfo sourceInfo){
        GenericObjectPool<RestHighLevelClient> clientPool = clientPoolMap.get(sourceInfo.getDatabaseCode());
        if(clientPool==null) {
            GenericObjectPoolConfig<RestHighLevelClient> poolConfig = new GenericObjectPoolConfig<>();
            clientPool = new GenericObjectPool<>(new PooledRestClientFactory(config,sourceInfo), poolConfig);
            clientPoolMap.put(sourceInfo.getDatabaseCode(), clientPool);
        }
        return clientPool;
    }
}
