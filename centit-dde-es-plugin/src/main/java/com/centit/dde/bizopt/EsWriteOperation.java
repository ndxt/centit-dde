package com.centit.dde.bizopt;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.config.ElasticSearchConfig;
import com.centit.dde.config.ElasticsearchUtil;
import com.centit.dde.config.PooledRestClientFactory;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataSet;
import com.centit.dde.core.SimpleDataSet;
import com.centit.dde.entity.EsSearchWriteEntity;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.ResponseSingleData;
import com.centit.product.metadata.dao.SourceInfoDao;
import com.centit.product.metadata.po.SourceInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.elasticsearch.client.RestHighLevelClient;

public class EsWriteOperation implements BizOperation {
    public static final Log log = LogFactory.getLog(EsWriteOperation.class);


    private SourceInfoDao sourceInfoDao;

    public EsWriteOperation( ) {
    }

    public EsWriteOperation(SourceInfoDao sourceInfoDao) {
        this.sourceInfoDao = sourceInfoDao;
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson) throws Exception {
        EsSearchWriteEntity esSerachReadEntity = JSONObject.parseObject(bizOptJson.toJSONString(), EsSearchWriteEntity.class);
        return insertEs(bizModel, esSerachReadEntity);
    }

    //ES插入操作
    private ResponseData insertEs(BizModel bizModel, EsSearchWriteEntity esSearchWriteEntity) throws Exception {
        String id = esSearchWriteEntity.getSourceId();
        DataSet dataSet = bizModel.getDataSet(id);
        JSONArray jsonArray = JSONObject.parseArray(String.valueOf(dataSet.getData()));
        TimeInterval timer = DateUtil.timer();
        SourceInfo sourceInfo = sourceInfoDao.getDatabaseInfoById(esSearchWriteEntity.getDataSourceId());
        log.debug("获取元数据信息耗时："+timer.intervalRestart()+"ms,获取元数据信息："+sourceInfo.toString());
        GenericObjectPool<RestHighLevelClient> restHighLevelClientGenericObjectPool = PooledRestClientFactory.obtainclientPool(new ElasticSearchConfig(), sourceInfo);
        RestHighLevelClient restHighLevelClient=null;
        try {
            restHighLevelClient = restHighLevelClientGenericObjectPool.borrowObject();
            log.debug("获restHighLevelClient耗时："+timer.intervalRestart()+"ms");
            Boolean indexResponse = ElasticsearchUtil.saveDocuments(restHighLevelClient,jsonArray, esSearchWriteEntity);
            log.info("插入ES数据耗时："+timer.intervalRestart()+"ms");
            SimpleDataSet resultDataSet = new SimpleDataSet();
            resultDataSet.setData(indexResponse);
            bizModel.putDataSet(id,resultDataSet);
            return ResponseSingleData.makeResponseData(indexResponse);
        }finally {
            restHighLevelClientGenericObjectPool.returnObject(restHighLevelClient);
            log.debug("restHighLevelClient放回连接池中");
        }
    }


    //es合并

}