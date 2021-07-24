package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.config.ElasticSearchConfig;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataSet;
import com.centit.dde.core.SimpleDataSet;
import com.centit.dde.entity.EsWriteVo;
import com.centit.dde.factory.PooledRestClientFactory;
import com.centit.dde.utils.ElasticsearchWriteUtils;
import com.centit.dde.utils.EsIndexNameExistsUtils;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.ResponseSingleData;
import com.centit.product.metadata.dao.SourceInfoDao;
import com.centit.product.metadata.po.SourceInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.elasticsearch.client.RestHighLevelClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EsWriteBizOperation implements BizOperation {
    public static final Log log = LogFactory.getLog(EsWriteBizOperation.class);

    private SourceInfoDao sourceInfoDao;

    public EsWriteBizOperation( ) {
    }

    public EsWriteBizOperation(SourceInfoDao sourceInfoDao) {
        this.sourceInfoDao = sourceInfoDao;
    }



    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson) throws Exception {
        return writeEs(bizModel, bizOptJson);
    }

    //ES插入操作
    private ResponseData writeEs(BizModel bizModel, JSONObject bizOptJson) throws Exception {
        EsWriteVo esSearchWriteEntity = JSONObject.parseObject(bizOptJson.toJSONString(), EsWriteVo.class);
        String source = esSearchWriteEntity.getSource();
        DataSet dataSet = bizModel.getDataSet(source);
        List<Map<String, Object>> data = dataSet.getDataAsList();
        List<String> addData = new ArrayList<>();
        for (Map<String, Object> datum : data) {
            String jsonData = JSONObject.toJSONString(datum);
            addData.add(jsonData);
        }
        TimeInterval timer = DateUtil.timer();
        SourceInfo sourceInfo = sourceInfoDao.getDatabaseInfoById(esSearchWriteEntity.getDataSourceId());
        log.debug("获取元数据信息耗时："+timer.intervalRestart()+"ms,获取元数据信息："+sourceInfo.toString());
        GenericObjectPool<RestHighLevelClient> restHighLevelClientGenericObjectPool = PooledRestClientFactory.obtainclientPool(new ElasticSearchConfig(), sourceInfo);
        RestHighLevelClient restHighLevelClient=null;
        try {
            restHighLevelClient = restHighLevelClientGenericObjectPool.borrowObject();
            log.debug("获restHighLevelClient耗时："+timer.intervalRestart()+"ms");
            String indexName = esSearchWriteEntity.getIndexName();
            if (!EsIndexNameExistsUtils.indexNameExists(restHighLevelClient,indexName)){
                return BuiltInOperation.getResponseData(0, 500, bizOptJson.getString("SetsName")+":"+indexName+"索引不存在！");
            }
            Boolean indexResponse = ElasticsearchWriteUtils.batchSaveDocuments(restHighLevelClient,addData, esSearchWriteEntity);
            log.info("插入ES数据耗时："+timer.intervalRestart()+"ms");
            bizModel.putDataSet(esSearchWriteEntity.getId(),new SimpleDataSet(indexResponse));
            return ResponseSingleData.makeResponseData(indexResponse);
        }finally {
            restHighLevelClientGenericObjectPool.returnObject(restHighLevelClient);
            log.debug("restHighLevelClient放回连接池中");
        }
    }

}
