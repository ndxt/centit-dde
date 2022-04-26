package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.config.ElasticSearchConfig;
import com.centit.dde.core.*;
import com.centit.dde.entity.EsWriteVo;
import com.centit.dde.factory.PooledRestClientFactory;
import com.centit.dde.write.ElasticsearchWriteUtils;
import com.centit.framework.common.ResponseData;
import com.centit.product.adapter.po.SourceInfo;
import com.centit.product.metadata.dao.SourceInfoDao;
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

    public EsWriteBizOperation(SourceInfoDao sourceInfoDao) {
        this.sourceInfoDao = sourceInfoDao;
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        EsWriteVo esWriteVo = JSONObject.parseObject(bizOptJson.toJSONString(), EsWriteVo.class);
        String id = esWriteVo.getId();
        String source = esWriteVo.getSource();
        //组成文档id的字段
        JSONArray docIds = bizOptJson.getJSONArray("config");
        if (docIds!=null){
            docIds.stream().forEach(docIdInfo->{
                JSONObject fieldInfo =  (JSONObject)docIdInfo;
                esWriteVo.getDocumentIds().add(fieldInfo.getString("columnName"));
            });
        }
        DataSet dataSet = bizModel.getDataSet(source);
        List<Map<String, Object>> data = dataSet.getDataAsList();
        List<String> addData = new ArrayList<>();
        data.stream().forEach(datum-> addData.add(JSONObject.toJSONString(datum)));
        SourceInfo sourceInfo = sourceInfoDao.getDatabaseInfoById(esWriteVo.getDatabaseId());
        GenericObjectPool<RestHighLevelClient> restHighLevelClientGenericObjectPool = PooledRestClientFactory.obtainclientPool(new ElasticSearchConfig(), sourceInfo);
        RestHighLevelClient restHighLevelClient=null;
        try {
            restHighLevelClient = restHighLevelClientGenericObjectPool.borrowObject();
            JSONObject jsonObject = ElasticsearchWriteUtils.batchSaveDocuments(restHighLevelClient,addData, esWriteVo);
            bizModel.putDataSet(id,new SimpleDataSet(jsonObject));
            return BuiltInOperation.getResponseSuccessData(bizModel.getDataSet(id).getSize());
        }finally {
            restHighLevelClientGenericObjectPool.returnObject(restHighLevelClient);
            log.debug("restHighLevelClient放回连接池中");
        }
    }
}
