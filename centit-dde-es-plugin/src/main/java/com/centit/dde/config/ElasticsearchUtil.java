package com.centit.dde.config;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.entity.EsSearchWriteEntity;
import com.centit.dde.entity.EsSerachReadEntity;
import com.centit.dde.entity.QueryParameter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @className: EsUtil
 * @description: es 操作工具类;
 *      这里均采用同步调用的方式
 */
public class ElasticsearchUtil {

    public static final Log log = LogFactory.getLog(ElasticsearchUtil.class);

    //判断文档id是否已经存在
    public static boolean documentIdExist(RestHighLevelClient restHighLevelClient,String indexName,String documentId) throws IOException {
        GetRequest request = new GetRequest(indexName).id(documentId);
        return restHighLevelClient.exists(request, RequestOptions.DEFAULT);
    }

    //查询参数公有部分封装
    private  static  void publicPart(EsSerachReadEntity esSerachReadEntity, final SearchSourceBuilder searchSourceBuilder){
        // 设置源字段过虑,第一个参数结果集包括哪些字段，第二个参数表示结果集不包括哪些字段
        //if (esSerachEntity.getReturnField()!=null&&esSerachEntity.getReturnField().length>0){
        searchSourceBuilder.fetchSource(esSerachReadEntity.getReturnField(), esSerachReadEntity.getNotReturnField());
        //}
        //设置超时时间
        searchSourceBuilder.timeout(new TimeValue(120, TimeUnit.SECONDS));
        //排序
        if (esSerachReadEntity.getSortFieldMap()!=null && esSerachReadEntity.getSortFieldMap().size()>0){
            esSerachReadEntity.getSortFieldMap().forEach((key, value)->
                searchSourceBuilder.sort(key,value=="DESC"?SortOrder.DESC:SortOrder.ASC));
        }else {
            //设置是否按匹配度排序
            searchSourceBuilder.explain(true);
        }
        // 设置分页
        if (esSerachReadEntity.getPageNo()!=null && esSerachReadEntity.getPageSize()!=null){
            searchSourceBuilder.from(((esSerachReadEntity.getPageNo() - 1)) * esSerachReadEntity.getPageSize());
            searchSourceBuilder.size(esSerachReadEntity.getPageSize());
        }
    }

    //查询返回结果部分封装
    private static JSONArray resultPart(SearchResponse searchResponse){
        JSONArray resultArrays= new JSONArray();
        // 根据状态和数据条数验证是否返回了数据
        if (RestStatus.OK.equals(searchResponse.status()) && searchResponse.getHits().getTotalHits().value > 0) {
            SearchHits hits = searchResponse.getHits();
            for (SearchHit hit : hits) {
                JSONObject jsonObject = JSONObject.parseObject(hit.getSourceAsString());
                // 输出查询信息
                log.debug("获取es数据:"+jsonObject.toJSONString());
                resultArrays.add(jsonObject);
            }
        }
        return resultArrays;
    }

    /**
     * 添加文档
     */
    public static IndexResponse saveDocument(RestHighLevelClient restHighLevelClient,String jsonData,String indexName){
        IndexRequest request = new IndexRequest(indexName);
        request =request.id(UUID.randomUUID().toString().replaceAll("-",""));
        request.source(jsonData, XContentType.JSON);
        IndexResponse indexResponse = null;
        try {
            indexResponse = restHighLevelClient.index(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return indexResponse;
    }

    /**
     * 批量插入或者更新   es存在就更新，不存在就插入
     */
    public static Boolean saveDocuments(RestHighLevelClient restHighLevelClient, JSONArray jsonDatas, EsSearchWriteEntity esSearchWriteEntity) throws IOException {
        BulkRequest requestBulk = new BulkRequest(esSearchWriteEntity.getIndexName());
        for (Object jsonData : jsonDatas) {
            String json = JSONObject.toJSONString(jsonData);
            StringBuilder doucmentId = new StringBuilder();
            String documentIds = esSearchWriteEntity.getDocumentIds();
            String[] fields = documentIds.split(",");
            JSONObject jsonObject = JSONObject.parseObject(String.valueOf(jsonData));
            for (int i = 0; i < fields.length; i++) {
                doucmentId.append(jsonObject.get(fields[i]));
            }
            //判断文档id是否已经存在，如果存在就做更新操作 反之
            if (documentIdExist(restHighLevelClient, esSearchWriteEntity.getIndexName(),doucmentId.toString())){
                UpdateRequest updateRequest= new UpdateRequest(esSearchWriteEntity.getIndexName(),doucmentId.toString());
                updateRequest.doc(json,XContentType.JSON);
                requestBulk.add(updateRequest);
            }else {
                IndexRequest indexReq = new IndexRequest().source(json, XContentType.JSON);
                indexReq.id(doucmentId.toString());
                requestBulk.add(indexReq);
            }
        }
        BulkResponse bulkResponse = restHighLevelClient.bulk(requestBulk, RequestOptions.DEFAULT);
        for(BulkItemResponse bulkItemResponse : bulkResponse){
            DocWriteResponse itemResponse = bulkItemResponse.getResponse();
            IndexResponse indexResponse = (IndexResponse) itemResponse;
            log.info("单条返回结果："+indexResponse.toString());
            if(bulkItemResponse.isFailed()){
                log.error("es 返回错误:"+bulkItemResponse.getFailureMessage());
                return  false;
            }
        }
        return true;
    }

    /**
     * 精确查询（查询条件不会进行分词，但是查询内容可能会分词，导致查询不到）
     */
    public static JSONArray accurateQuery(RestHighLevelClient restHighLevelClient, EsSerachReadEntity esSerachReadEntity) {
        try {
            // 构建查询条件（注意：termQuery 支持多种格式查询，如 boolean、int、double、string 等，这里使用的是 string 的查询）
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            publicPart(esSerachReadEntity,searchSourceBuilder);
            for (QueryParameter queryParameter : esSerachReadEntity.getQueryFieldMap()) {
                searchSourceBuilder.query(QueryBuilders.termQuery(queryParameter.getField(),queryParameter.getValue())
                    .boost(queryParameter.getBoots()==null?1.0f:queryParameter.getBoots()));
            }
            // 创建查询请求对象，将查询对象配置到其中
            SearchRequest searchRequest = new SearchRequest(esSerachReadEntity.getIndexName());
            searchRequest.source(searchSourceBuilder);
            // 执行查询，然后处理响应结果
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            return resultPart(searchResponse);
        } catch (IOException e) {
            log.error("", e);
        }
        return null;
    }

    /**
     * 匹配查询数据
     */
    public static JSONArray matchQuery(RestHighLevelClient restHighLevelClient, EsSerachReadEntity esSerachReadEntity) {
        try {
            // 构建查询条件
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            //多条件查询   and  查询
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            for (QueryParameter queryParameter : esSerachReadEntity.getQueryFieldMap()) {
                searchSourceBuilder.query(QueryBuilders
                    .matchQuery(queryParameter.getField(),queryParameter.getValue())
                    .analyzer(queryParameter.getAnalyzer()==null?null:queryParameter.getAnalyzer())
                    .boost(queryParameter.getBoots()==null?1.0f:queryParameter.getBoots())
                    //查询最小匹配度   可以是百分比   也可以是整型  这里设置的是百分比 直观
                    .minimumShouldMatch(queryParameter.getQueryMinimumProportion()==null?null:queryParameter.getQueryMinimumProportion()));
            }
            publicPart(esSerachReadEntity,searchSourceBuilder);
            searchSourceBuilder.query(boolQueryBuilder);
            // 创建查询请求对象，将查询对象配置到其中
            SearchRequest searchRequest = new SearchRequest(esSerachReadEntity.getIndexName());
            searchRequest.source(searchSourceBuilder);
            // 执行查询，然后处理响应结果
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            return resultPart(searchResponse);
        } catch (IOException e) {
            log.error("", e);
        }
        return null;
    }

    /**
     * 全部查询
     */
    public static JSONArray  matchAllQuery(RestHighLevelClient restHighLevelClient, EsSerachReadEntity esSerachReadEntity) {
        try {
            // 构建查询条件
            MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
            // 创建查询源构造器
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            publicPart(esSerachReadEntity,searchSourceBuilder);
            searchSourceBuilder.query(matchAllQueryBuilder);
            // 创建查询请求对象，将查询对象配置到其中
            SearchRequest searchRequest = new SearchRequest(esSerachReadEntity.getIndexName());
            searchRequest.source(searchSourceBuilder);
            // 执行查询，然后处理响应结果
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            return resultPart(searchResponse);
        } catch (IOException e) {
            log.error("", e);
        }
        return null;
    }

    /**
     *范围查询
     */
    public static JSONArray rangeQuery(RestHighLevelClient restHighLevelClient, EsSerachReadEntity esSerachReadEntity) {
        try {
            // 构建查询条件
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            // includeLower（是否包含下边界）、includeUpper（是否包含上边界）
            QueryBuilders.rangeQuery(esSerachReadEntity.getRangeField())
                .from(esSerachReadEntity.getRangeStartValue()).to(esSerachReadEntity.getRangeEndValue());
            publicPart(esSerachReadEntity,searchSourceBuilder);
            // 创建查询请求对象，将查询对象配置到其中
            SearchRequest searchRequest = new SearchRequest(esSerachReadEntity.getIndexName());
            searchRequest.source(searchSourceBuilder);
            // 执行查询，然后处理响应结果
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            // 根据状态和数据条数验证是否返回了数据
            return resultPart(searchResponse);
        } catch (IOException e) {
            log.error("", e);
        }
        return null;
    }

}









