package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.config.ElasticSearchConfig;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.SimpleDataSet;
import com.centit.dde.entity.EsReadVo;
import com.centit.dde.factory.PooledRestClientFactory;
import com.centit.dde.query.ElasticsearchReadUtils;
import com.centit.dde.utils.EsIndexNameExistsUtils;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.ResponseSingleData;
import com.centit.product.metadata.dao.SourceInfoDao;
import com.centit.product.metadata.po.SourceInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.elasticsearch.client.RestHighLevelClient;

public class EsReadBizOperation implements BizOperation {
    public static final Log log = LogFactory.getLog(EsReadBizOperation.class);
    private SourceInfoDao sourceInfoDao;

    public EsReadBizOperation( ) {
    }

    public EsReadBizOperation(SourceInfoDao sourceInfoDao) {
        this.sourceInfoDao=sourceInfoDao;
    }


    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson){
        return queryEs(bizModel, bizOptJson);
    }

    //ES查询操作
    private ResponseData queryEs(BizModel bizModel, JSONObject bizOptJson){
        EsReadVo esReadVo = JSONObject.parseObject(bizOptJson.toJSONString(), EsReadVo.class);
        SourceInfo sourceInfo = sourceInfoDao.getDatabaseInfoById(esReadVo.getDataSourceId());
        GenericObjectPool<RestHighLevelClient> restHighLevelClientGenericObjectPool =
            PooledRestClientFactory.obtainclientPool(new ElasticSearchConfig(), sourceInfo);
        RestHighLevelClient restHighLevelClient = null;
        try {
            restHighLevelClient = restHighLevelClientGenericObjectPool.borrowObject();
            if (!EsIndexNameExistsUtils.indexNameExists(restHighLevelClient,esReadVo.getQueryParameter().getIndexName())){
                return BuiltInOperation.getResponseData(0, 500, bizOptJson.getString("SetsName")+":"
                    +esReadVo.getQueryParameter().getIndexName()+"索引不存在！");
            }
            JSONObject result = ElasticsearchReadUtils.query(restHighLevelClient, esReadVo);
            bizModel.putDataSet(esReadVo.getId(),new SimpleDataSet(result));
        }catch (Exception e){
            return BuiltInOperation.getResponseData(0, 500,"查询es数据异常,异常信息："+e.getMessage());
        } finally {
            restHighLevelClientGenericObjectPool.returnObject(restHighLevelClient);
            log.debug("restHighLevelClient放回连接池中");
        }
        return ResponseSingleData.makeResponseData(bizModel.getDataSet(esReadVo.getId()).getSize());
    }
}
