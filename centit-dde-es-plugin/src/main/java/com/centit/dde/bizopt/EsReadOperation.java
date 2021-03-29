package com.centit.dde.bizopt;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.config.ElasticSearchConfig;
import com.centit.dde.utils.ElasticsearchUtil;
import com.centit.dde.factory.PooledRestClientFactory;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.SimpleDataSet;
import com.centit.dde.entity.EsSerachReadEntity;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.ResponseSingleData;
import com.centit.product.metadata.dao.SourceInfoDao;
import com.centit.product.metadata.po.SourceInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.elasticsearch.client.RestHighLevelClient;

import java.util.HashMap;
import java.util.Map;

public class EsReadOperation implements BizOperation {
    public static final Log log = LogFactory.getLog(EsReadOperation.class);


    private SourceInfoDao sourceInfoDao;

    public EsReadOperation( ) {
    }

    public EsReadOperation(SourceInfoDao sourceInfoDao) {
        this.sourceInfoDao = sourceInfoDao;
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson) throws Exception {
        EsSerachReadEntity esSerachReadEntity = JSONObject.parseObject(bizOptJson.toJSONString(), EsSerachReadEntity.class);
        return queryEs(bizModel, esSerachReadEntity);
    }

    //ES查询操作
    private ResponseData queryEs(BizModel bizModel, EsSerachReadEntity esSerachReadEntity) throws Exception {
        TimeInterval timer = DateUtil.timer();
        SourceInfo sourceInfo = sourceInfoDao.getDatabaseInfoById(esSerachReadEntity.getDataSourceId());
        log.debug("获取元数据信息耗时："+timer.intervalRestart()+"ms,获取元数据信息："+sourceInfo.toString());
        GenericObjectPool<RestHighLevelClient> restHighLevelClientGenericObjectPool = PooledRestClientFactory.obtainclientPool(new ElasticSearchConfig(), sourceInfo);
        RestHighLevelClient restHighLevelClient = null;
        try {
            restHighLevelClient = restHighLevelClientGenericObjectPool.borrowObject();
            log.debug("获restHighLevelClient耗时："+timer.intervalRestart()+"ms");
            JSONArray resultData;
            switch (esSerachReadEntity.getQueryType()){
                //精确查询
                case "1":
                    resultData = ElasticsearchUtil.accurateQuery(restHighLevelClient, esSerachReadEntity);
                    break;
                //条件查询
                case "2":
                    resultData = ElasticsearchUtil.matchQuery(restHighLevelClient, esSerachReadEntity);
                    break;
                //范围查询
                case "3":
                    resultData = ElasticsearchUtil.rangeQuery(restHighLevelClient, esSerachReadEntity);
                    break;
                //查询全部（分页查询） 默认分页查询
                default:
                    resultData = ElasticsearchUtil.matchAllQuery(restHighLevelClient, esSerachReadEntity);
            }
            log.info("查询类型:"+esSerachReadEntity.getQueryType()+",查询ES耗时："+timer.intervalRestart()+"ms");
            SimpleDataSet dataSet = new SimpleDataSet();
            dataSet.setData(resultData);
            bizModel.putDataSet(esSerachReadEntity.getId(),dataSet);
            return ResponseSingleData.makeResponseData(resultData);
        } finally {
            restHighLevelClientGenericObjectPool.returnObject(restHighLevelClient);
            log.debug("restHighLevelClient放回连接池中");
        }
    }

    //json串转map
    public static Map<String, Object> jsonStrToMap(String json) {
        JSONObject jsonObject = JSONObject.parseObject(json);
        Map<String, Object> valueMap = new HashMap<String, Object>();
        valueMap.putAll(jsonObject);
        return valueMap;
    }

}
