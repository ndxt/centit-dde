package com.centit.dde.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.config.ElasticSearchConfig;
import com.centit.dde.factory.PooledRestClientFactory;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.ResponseSingleData;
import com.centit.framework.core.dao.PageQueryResult;
import com.centit.product.adapter.po.SourceInfo;
import com.centit.product.metadata.dao.SourceInfoDao;
import com.centit.product.metadata.service.MetaObjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.client.indices.GetMappingsRequest;
import org.elasticsearch.client.indices.GetMappingsResponse;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Api(tags = "ES索引信息查询")
@RestController
@RequestMapping("/es")
public class ElasticSearchController {
    @Resource
    SourceInfoDao sourceInfoDao;

    @Autowired
    private MetaObjectService metaObjectService;

    @GetMapping("/{dataSourceId}")
    @ApiOperation("获取索引列表")
    public ResponseData getIndexList(@PathVariable String dataSourceId){
        SourceInfo sourceInfo = sourceInfoDao.getDatabaseInfoById(dataSourceId);
        GenericObjectPool<RestHighLevelClient> restHighLevelClientGenericObjectPool = PooledRestClientFactory.obtainclientPool(new ElasticSearchConfig(), sourceInfo);
        RestHighLevelClient restHighLevelClient=null;
        try {
            restHighLevelClient = restHighLevelClientGenericObjectPool.borrowObject();
            GetIndexRequest indexRequest = new GetIndexRequest("*");
            GetIndexResponse getIndexResponse = restHighLevelClient.indices().get(indexRequest, RequestOptions.DEFAULT);
            String[] indices = getIndexResponse.getIndices();
            JSONArray indexArr = new JSONArray();
            Arrays.stream(indices).forEach(indexName->{
                if (!indexName.startsWith(".")){
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("name",indexName);
                    indexArr.add(jsonObject);
                }
            });
            PageQueryResult<Object> result = PageQueryResult.createResult(indexArr, null);
            return ResponseSingleData.makeResponseData(result);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            restHighLevelClientGenericObjectPool.returnObject(restHighLevelClient);
        }
        return null;
    }

    @GetMapping("/{dataSourceId}/{indexName}")
    @ApiOperation("获取索引字段列表")
    public ResponseData getFieldList(@PathVariable String dataSourceId,@PathVariable String indexName){
        SourceInfo sourceInfo = sourceInfoDao.getDatabaseInfoById(dataSourceId);
        GenericObjectPool<RestHighLevelClient> restHighLevelClientGenericObjectPool = PooledRestClientFactory.obtainclientPool(new ElasticSearchConfig(), sourceInfo);
        RestHighLevelClient restHighLevelClient=null;
        try {
            restHighLevelClient = restHighLevelClientGenericObjectPool.borrowObject();
            //指定索引
            GetMappingsRequest getMappings = new GetMappingsRequest().indices(indexName);
            //调用获取
            GetMappingsResponse getMappingResponse = restHighLevelClient.indices().getMapping(getMappings, RequestOptions.DEFAULT);
            //处理数据
            Map<String, MappingMetaData> allMappings = getMappingResponse.mappings();
            List<Object> fields = new ArrayList<>();
            for (Map.Entry<String, MappingMetaData> indexValue : allMappings.entrySet()) {
                Map<String, Object> mapping = indexValue.getValue().sourceAsMap();
                Iterator<Map.Entry<String, Object>> entries = mapping.entrySet().iterator();
                entries.forEachRemaining(stringObjectEntry -> {
                    if (stringObjectEntry.getKey().equals("properties")) {
                        Map<String, Object> value = (Map<String, Object>) stringObjectEntry.getValue();
                        for (Map.Entry<String, Object> ObjectEntry : value.entrySet()) {
                            String field = ObjectEntry.getKey();
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("name",field);
                            fields.add(jsonObject);
                            Map<String, Object> fieldInfo = (Map<String, Object>) ObjectEntry.getValue();
                            if (fieldInfo.get("fields")!=null){
                                Map fields1 = (Map)fieldInfo.get("fields");
                                fields1.forEach((key,val)->{
                                    JSONObject jsonObject1 = new JSONObject();
                                    jsonObject1.put("name",field+"."+key);
                                    fields.add(jsonObject1);
                                });
                            }
                        }
                    }
                });
            }
            PageQueryResult<Object> result = PageQueryResult.createResult(fields, null);
            return ResponseSingleData.makeResponseData(result);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            restHighLevelClientGenericObjectPool.returnObject(restHighLevelClient);
        }
        return null;
    }

    @GetMapping("/query_type")
    @ApiOperation("获取支持的查询类型")
    public ResponseData getQueryType(){
        String[] types = new String[]{"ids","sql","term","terms","fuzzy","range","match","exists","wildcard"
            ,"atch_all","multi_match","match_phrase","query_string","constant_score","match_phrase_prefix","simple_query_string"};
        PageQueryResult<Object> result = PageQueryResult.createResult(Arrays.asList(types), null);
        return ResponseSingleData.makeResponseData(result);
    }






/*

    @PostMapping("createIndex/{dataSourceId}/{indexName}")
    @ApiOperation("创建索引")
    public Object createIndex(@PathVariable String dataSourceId,@PathVariable String indexName, @RequestBody JSONObject  createIndexJson){
        SourceInfo sourceInfo = sourceInfoDao.getDatabaseInfoById(dataSourceId);
        GenericObjectPool<RestHighLevelClient> restHighLevelClientGenericObjectPool = PooledRestClientFactory.obtainclientPool(new ElasticSearchConfig(), sourceInfo);
        RestHighLevelClient restHighLevelClient=null;
        try {
            restHighLevelClient = restHighLevelClientGenericObjectPool.borrowObject();
            CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
            createIndexRequest.settings(createIndexJson.getJSONObject("settings").toJSONString(), XContentType.JSON);
            createIndexRequest.mapping(createIndexJson.getJSONObject("mappings").toJSONString(), XContentType.JSON);
            createIndexRequest.alias(new Alias(indexName+"_alias"));
            return restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            restHighLevelClientGenericObjectPool.returnObject(restHighLevelClient);
        }
        return null;
    }
*/

/*

    @PostMapping("matchall")
    @ApiOperation("全部查询测试")
    public Object matchAll(@RequestBody EsQueryVo esReadVo){
        SourceInfo sourceInfo = sourceInfoDao.getDatabaseInfoById(esReadVo.getDatabaseId());
        GenericObjectPool<RestHighLevelClient> restHighLevelClientGenericObjectPool = PooledRestClientFactory.obtainclientPool(new ElasticSearchConfig(), sourceInfo);
        RestHighLevelClient restHighLevelClient=null;
        try {
            restHighLevelClient = restHighLevelClientGenericObjectPool.borrowObject();
            JSONObject query = ElasticsearchReadUtils.executeQuery(restHighLevelClient, esReadVo);
            return query;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            restHighLevelClientGenericObjectPool.returnObject(restHighLevelClient);
        }
        return null;
    }


    @GetMapping("importData")
    @ApiOperation("导入数据")
    public Object importData(){
        String ips = "127.0.0.1:9200,127.0.0.1:9201,127.0.0.1:9202";
        RestClientBuilder restClientBuilder = new ElasticSearchConfig().restClientBuilder(ips);
        RestHighLevelClient restHighLevelClient=new RestHighLevelClient(restClientBuilder);
        try {
            JSONArray jsonArray = metaObjectService.listObjectsByProperties("dyJSkKMbQSye_-rMzDkTmA", new HashMap<>());
            List<String> list = new ArrayList<>();
            for (Object o : jsonArray) {
                JSONObject data = (JSONObject)o;
                ObjectDocument document = new ObjectDocument();
                document.setOsId(data.getString("osId"));
                document.setOptId(data.getString("optId"));
                document.setOptMethod(data.getString("optMethod"));
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("docId", data.getString("docId"));
                jsonObject.put("docPath",data.getString("docPath"));
                document.setOptUrl(jsonObject.toString());
                document.setTitle(data.getString("docTitle"));
                if (!StringUtils.isBlank(data.getString("docFile"))){
                    document.setContent(filterTag(data.getString("docFile")));
                }
                JSONObject jsonObject1 = JSON.parseObject(document.toJsonString(),JSONObject.class);
                jsonObject1.put("id",data.getString("docId"));
                list.add(JSON.toJSONString(jsonObject1));
            }
            EsWriteVo esWriteVo = new EsWriteVo();
            esWriteVo.setIndexName("objects");
            return ElasticsearchWriteUtils.batchSaveDocuments(restHighLevelClient,list,esWriteVo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
*/

    private String filterTag(String content){
        String regEx_style="<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式
        String regEx_html="<[^>]+>"; //定义HTML标签的正则表达式

        Pattern p_style=Pattern.compile(regEx_style,Pattern.CASE_INSENSITIVE);
        Matcher m_style=p_style.matcher(content);
        content=m_style.replaceAll(""); //过滤style标签

        Pattern p_html=Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE);
        Matcher m_html=p_html.matcher(content);
        content=m_html.replaceAll(""); //过滤html标签

        return content.trim(); //返回文本字符串

    }


    public static void main(String[] args) throws IOException {
        String ips = "127.0.0.1:9200,127.0.0.1:9201,127.0.0.1:9202";
        RestClientBuilder restClientBuilder = new ElasticSearchConfig().restClientBuilder(ips);
        RestHighLevelClient restHighLevelClient=new RestHighLevelClient(restClientBuilder);
        try {
         /*  GetIndexRequest indexRequest = new GetIndexRequest("*");
           GetIndexResponse getIndexResponse = restHighLevelClient.indices().get(indexRequest, RequestOptions.DEFAULT);
           String[] indices = getIndexResponse.getIndices();
           Arrays.stream(indices).forEach(System.out::println);*/

            //指定索引
            GetMappingsRequest getMappings = new GetMappingsRequest().indices("objects");
            //调用获取
            GetMappingsResponse getMappingResponse = restHighLevelClient.indices().getMapping(getMappings, RequestOptions.DEFAULT);
            //处理数据
            Map<String, MappingMetaData> allMappings = getMappingResponse.mappings();
            List<String> fields = new ArrayList<>();
            for (Map.Entry<String, MappingMetaData> indexValue : allMappings.entrySet()) {
                Map<String, Object> mapping = indexValue.getValue().sourceAsMap();
                Iterator<Map.Entry<String, Object>> entries = mapping.entrySet().iterator();
                entries.forEachRemaining(stringObjectEntry -> {
                    if (stringObjectEntry.getKey().equals("properties")) {
                        Map<String, Object> value = (Map<String, Object>) stringObjectEntry.getValue();
                        for (Map.Entry<String, Object> ObjectEntry : value.entrySet()) {
                            String field = ObjectEntry.getKey();
                            fields.add(field);
                            Map<String, Object> fieldInfo = (Map<String, Object>) ObjectEntry.getValue();
                            if (fieldInfo.get("fields")!=null){
                                Map fields1 = (Map)fieldInfo.get("fields");
                                fields1.forEach((key,val)->{
                                    fields.add(field+"."+key);
                                });
                            }
                        }
                    }
                });
            }
            fields.stream().forEach(System.out::println);
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            restHighLevelClient.close();
        }
    }
}
