package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.config.ElasticSearchConfig;
import com.centit.dde.core.*;
import com.centit.dde.entity.EsQueryVo;
import com.centit.dde.entity.FieldAttributeInfo;
import com.centit.dde.factory.PooledRestClientFactory;
import com.centit.dde.query.ElasticsearchReadUtils;
import com.centit.dde.query.EsQueryType;
import com.centit.framework.common.ResponseData;
import com.centit.product.adapter.po.SourceInfo;
import com.centit.product.metadata.dao.SourceInfoDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EsQueryBizOperation implements BizOperation {
    public static final Log log = LogFactory.getLog(EsQueryBizOperation.class);

    private SourceInfoDao sourceInfoDao;

    public EsQueryBizOperation(SourceInfoDao sourceInfoDao) {
        this.sourceInfoDao=sourceInfoDao;
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext){
        EsQueryVo esReadVo = JSONObject.parseObject(bizOptJson.toJSONString(), EsQueryVo.class);
        //es服务地址
        String dataSourceId = esReadVo.getDatabaseId();
        SourceInfo sourceInfo = sourceInfoDao.getDatabaseInfoById(dataSourceId);
        GenericObjectPool<RestHighLevelClient> restHighLevelClientGenericObjectPool = PooledRestClientFactory.obtainclientPool(new ElasticSearchConfig(), sourceInfo);
        RestHighLevelClient restHighLevelClient = null;
        try {
            restHighLevelClient = restHighLevelClientGenericObjectPool.borrowObject();
            JSONObject result = ElasticsearchReadUtils.executeQuery(restHighLevelClient,bizModel,esReadVo);
            bizModel.putDataSet(esReadVo.getId(),new DataSet(result));
            return BuiltInOperation.createResponseSuccessData(bizModel.getDataSet(esReadVo.getId()).getSize());
        }catch (Exception e){
            return BuiltInOperation.createResponseData(0, 500,"查询es数据异常,异常信息："+e.getMessage());
        } finally {
            restHighLevelClientGenericObjectPool.returnObject(restHighLevelClient);
            log.debug("restHighLevelClient放回连接池中");
        }
    }

    public static void main(String[] args) throws Exception {
        String ips = "127.0.0.1:9200,127.0.0.1:9201,127.0.0.1:9202";
        RestClientBuilder restClientBuilder = new ElasticSearchConfig().restClientBuilder(ips);
        RestHighLevelClient restHighLevelClient=new RestHighLevelClient(restClientBuilder);
        BizModel simpleBizModel = new BizModel("test");
       /* List<String> ids = new ArrayList<>();
        ids.add("FILE_SVR");
        ids.add("zp_Qn5R5ROSo4sf-eovoWA");*/
        Map<String, Object> param = new HashMap<>();
        param.put("osId","zp_Qn5R5ROSo4sf-eovoWA");
        param.put("content","数据集");
        param.put("title","数据库");
        DataSet dataSet = new DataSet(param);
        Map<String, DataSet> map = new HashMap<>();
        map.put("es11",dataSet);
        simpleBizModel.setBizData(map);
        EsQueryVo esQueryVo = new EsQueryVo();
        esQueryVo.setExplain(false);
        esQueryVo.setQueryIndex(new String[]{"objects"});
        esQueryVo.setId("es11");
        esQueryVo.setPageNo(1);
        esQueryVo.setPageSize(5);
        esQueryVo.setTimeout(10);
        List<FieldAttributeInfo> fieldAttributeInfoList = new ArrayList<>();
        FieldAttributeInfo fieldAttributeInfo1 = new FieldAttributeInfo();
        fieldAttributeInfo1.setFieldName("osId");
        fieldAttributeInfo1.setExpression("es11.osId");
        fieldAttributeInfo1.setOperator(EsQueryType.FILTER);
        fieldAttributeInfo1.setIsHighligh(true);
        fieldAttributeInfo1.setAnalyze("ik_max_word");
        fieldAttributeInfo1.setQueryType(EsQueryType.TERM);
        fieldAttributeInfoList.add(fieldAttributeInfo1);

        FieldAttributeInfo fieldAttributeInfo2 = new FieldAttributeInfo();
        fieldAttributeInfo2.setFieldName("content");
        fieldAttributeInfo2.setExpression("es11.content");
        fieldAttributeInfo2.setOperator(EsQueryType.FILTER);
        fieldAttributeInfo2.setIsHighligh(true);
        fieldAttributeInfo2.setAnalyze("ik_max_word");
        fieldAttributeInfo2.setQueryType(EsQueryType.MATCH);
        fieldAttributeInfoList.add(fieldAttributeInfo2);

        FieldAttributeInfo fieldAttributeInfo3 = new FieldAttributeInfo();
        fieldAttributeInfo3.setFieldName("title");
        fieldAttributeInfo3.setExpression("es11.title");
        fieldAttributeInfo3.setOperator(EsQueryType.MUST);
        fieldAttributeInfo3.setIsHighligh(true);
        fieldAttributeInfo3.setAnalyze("ik_max_word");
        fieldAttributeInfo3.setQueryType(EsQueryType.MATCH);
        fieldAttributeInfoList.add(fieldAttributeInfo3);

        esQueryVo.setFieldAttributeInfos(fieldAttributeInfoList);
        JSONObject jsonObject = ElasticsearchReadUtils.executeQuery(restHighLevelClient, simpleBizModel, esQueryVo);
        System.out.println(jsonObject.toJSONString());
        restHighLevelClient.close();
    }
}
