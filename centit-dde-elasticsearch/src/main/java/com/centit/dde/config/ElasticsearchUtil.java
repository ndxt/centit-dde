package com.centit.dde.config;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.range.Range;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @className: EsUtil
 * @description: es 操作工具类;
 *      这里均采用同步调用的方式
 */
public class ElasticsearchUtil {

    public static final Log log = LogFactory.getLog(ElasticsearchUtil.class);

    /**
     * 批量插入
     */
    public static Boolean saveDocuments(RestHighLevelClient restHighLevelClient,JSONArray jsonDatas,String indexName) throws IOException {
        BulkRequest request = new BulkRequest(indexName);
        for (Object jsonData : jsonDatas) {
            String json = JSONObject.toJSONString(jsonData);
            IndexRequest indexReq = new IndexRequest().source(json, XContentType.JSON);
            indexReq.id(UUID.randomUUID().toString().replaceAll("-",""));
            request.add(indexReq);
        }
        BulkResponse bulkResponse = restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
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
     * 查询文档
     */
    public  static JSONObject searchDocument(Map<String,String> param){
        return new JSONObject();
    }

    /**
     * 精确查询（查询条件不会进行分词，但是查询内容可能会分词，导致查询不到）
     */
    public static JSONArray  accurateQuery(RestHighLevelClient restHighLevelClient,String field,String fieldValue,String indexName) {
        JSONArray resultArrays= new JSONArray();
        try {
            // 构建查询条件（注意：termQuery 支持多种格式查询，如 boolean、int、double、string 等，这里使用的是 string 的查询）
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.termQuery(field, fieldValue));
            // 创建查询请求对象，将查询对象配置到其中
            SearchRequest searchRequest = new SearchRequest(indexName);
            searchRequest.source(searchSourceBuilder);
            // 执行查询，然后处理响应结果
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            // 根据状态和数据条数验证是否返回了数据
            if (RestStatus.OK.equals(searchResponse.status()) && searchResponse.getHits().getTotalHits().value > 0) {
                SearchHits hits = searchResponse.getHits();
                for (SearchHit hit : hits) {
                    JSONObject jsonObject = JSONObject.parseObject(hit.getSourceAsString());
                    // 输出查询信息
                    log.info("精确查询-查询出数据:"+jsonObject.toJSONString());
                    resultArrays.add(jsonObject);
                }
            }
        } catch (IOException e) {
            log.error("", e);
        }
        return resultArrays;
    }

    /**
     * 匹配查询符合条件的所有数据，并设置分页
     */
    public static JSONArray  matchAllQuery(RestHighLevelClient restHighLevelClient, Integer pageNo, Integer pageSize, String indexName) {
        JSONArray resultArrays= new JSONArray();
        try {
            // 构建查询条件
            MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
            // 创建查询源构造器
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(matchAllQueryBuilder);
            // 设置分页
            searchSourceBuilder.from(pageNo-1);
            searchSourceBuilder.size(pageSize);
            // 设置排序
            //searchSourceBuilder.sort("salary", SortOrder.ASC);
            // 创建查询请求对象，将查询对象配置到其中
            SearchRequest searchRequest = new SearchRequest(indexName);
            searchRequest.source(searchSourceBuilder);
            // 执行查询，然后处理响应结果
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            // 根据状态和数据条数验证是否返回了数据
            if (RestStatus.OK.equals(searchResponse.status()) && searchResponse.getHits().getTotalHits().value > 0) {
                SearchHits hits = searchResponse.getHits();
                for (SearchHit hit : hits) {
                    JSONObject jsonObject = JSONObject.parseObject(hit.getSourceAsString());
                    // 输出查询信息
                    resultArrays.add(jsonObject);
                    // 输出查询信息
                    log.info("全部查询-查询出数据："+jsonObject.toJSONString());
                }
            }
        } catch (IOException e) {
            log.error("", e);
        }
        return resultArrays;
    }

    /**
     * 匹配查询数据
     */
    public static JSONArray matchQuery(RestHighLevelClient restHighLevelClient, String field, String fieldValue, String indexName) {
        JSONArray resultArrays= new JSONArray();
        try {
            // 构建查询条件
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.matchQuery(field, fieldValue));
            // 创建查询请求对象，将查询对象配置到其中
            SearchRequest searchRequest = new SearchRequest(indexName);
            searchRequest.source(searchSourceBuilder);
            // 执行查询，然后处理响应结果
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            // 根据状态和数据条数验证是否返回了数据
            if (RestStatus.OK.equals(searchResponse.status()) && searchResponse.getHits().getTotalHits().value > 0) {
                SearchHits hits = searchResponse.getHits();
                for (SearchHit hit : hits) {
                    JSONObject jsonObject = JSONObject.parseObject(hit.getSourceAsString());
                    // 输出查询信息
                    resultArrays.add(jsonObject);
                    // 输出查询信息
                    log.info("匹配查询-查询出数据："+jsonObject.toJSONString());
                }
            }
        } catch (IOException e) {
            log.error("", e);
        }
        return resultArrays;
    }




























    /**
     * 多个内容在一个字段中进行查询
     */
    public void termsQuery(RestHighLevelClient restHighLevelClient,String fields,String[] fieldValues,String indexName) {
        List<String> resultArrays= new ArrayList<>();
        try {
            // 构建查询条件（注意：termsQuery 支持多种格式查询，如 boolean、int、double、string 等，这里使用的是 string 的查询）
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.termsQuery("address.keyword", "北京市丰台区", "北京市昌平区", "北京市大兴区"));
            // 创建查询请求对象，将查询对象配置到其中
            SearchRequest searchRequest = new SearchRequest("mydlq-user");
            searchRequest.source(searchSourceBuilder);
            // 执行查询，然后处理响应结果
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            // 根据状态和数据条数验证是否返回了数据
            if (RestStatus.OK.equals(searchResponse.status()) && searchResponse.getHits().getTotalHits().value > 0) {
                SearchHits hits = searchResponse.getHits();
                for (SearchHit hit : hits) {
                    String jsondata = hit.getSourceAsString();
                    // 输出查询信息
                    log.info("输出查询信息:"+jsondata);
                    resultArrays.add(jsondata);
                }
            }
        } catch (IOException e) {
            log.error("", e);
        }
    }

    /**
     * 词语匹配查询
     */
   /* public void matchPhraseQuery() {
        try {
            // 构建查询条件
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.matchPhraseQuery("address", "北京市通州区"));
            // 创建查询请求对象，将查询对象配置到其中
            SearchRequest searchRequest = new SearchRequest("mydlq-user");
            searchRequest.source(searchSourceBuilder);
            // 执行查询，然后处理响应结果
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            // 根据状态和数据条数验证是否返回了数据
            if (RestStatus.OK.equals(searchResponse.status()) && searchResponse.getHits().getTotalHits().value > 0) {
                SearchHits hits = searchResponse.getHits();
                for (SearchHit hit : hits) {
                    String jsondata = hit.getSourceAsString();
                    // 输出查询信息
                    log.info(jsondata.toString());
                }
            }
        } catch (IOException e) {
            log.error("", e);
        }
    }*/

    /**
     * 内容在多字段中进行查询
     */
   /* public void matchMultiQuery() {
        try {
            // 构建查询条件
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.multiMatchQuery("北京市", "address", "remark"));
            // 创建查询请求对象，将查询对象配置到其中
            SearchRequest searchRequest = new SearchRequest("mydlq-user");
            searchRequest.source(searchSourceBuilder);
            // 执行查询，然后处理响应结果
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            // 根据状态和数据条数验证是否返回了数据
            if (RestStatus.OK.equals(searchResponse.status()) && searchResponse.getHits().getTotalHits().value > 0) {
                SearchHits hits = searchResponse.getHits();
                for (SearchHit hit : hits) {
                    String jsondata = hit.getSourceAsString();
                    // 输出查询信息
                    log.info(jsondata.toString());
                }
            }
        } catch (IOException e) {
            log.error("", e);
        }
    }*/


    /**
     * 模糊查询所有以 “三” 结尾的姓名
     */
   /* public void fuzzyQuery() {
        try {
            // 构建查询条件
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.fuzzyQuery("name", "三").fuzziness(Fuzziness.AUTO));
            // 创建查询请求对象，将查询对象配置到其中
            SearchRequest searchRequest = new SearchRequest("mydlq-user");
            searchRequest.source(searchSourceBuilder);
            // 执行查询，然后处理响应结果
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            // 根据状态和数据条数验证是否返回了数据
            if (RestStatus.OK.equals(searchResponse.status()) && searchResponse.getHits().getTotalHits().value > 0) {
                SearchHits hits = searchResponse.getHits();
                for (SearchHit hit : hits) {
                    String jsondata = hit.getSourceAsString();
                    // 输出查询信息
                    log.info(jsondata.toString());
                }
            }
        } catch (IOException e) {
            log.error("", e);
        }
    }*/

    /**
     * 查询岁数 ≥ 30 岁的员工数据
     */
   /* public void rangeQuery() {
        try {
            // 构建查询条件
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.rangeQuery("age").gte(30));
            // 创建查询请求对象，将查询对象配置到其中
            SearchRequest searchRequest = new SearchRequest("mydlq-user");
            searchRequest.source(searchSourceBuilder);
            // 执行查询，然后处理响应结果
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            // 根据状态和数据条数验证是否返回了数据
            if (RestStatus.OK.equals(searchResponse.status()) && searchResponse.getHits().getTotalHits().value > 0) {
                SearchHits hits = searchResponse.getHits();
                for (SearchHit hit : hits) {
                    String data = hit.getSourceAsString();
                    // 输出查询信息
                    log.info(data);
                }
            }
        } catch (IOException e) {
            log.error("", e);
        }
    }*/

    /**
     * 查询距离现在 30 年间的员工数据
     * [年(y)、月(M)、星期(w)、天(d)、小时(h)、分钟(m)、秒(s)]
     * 例如：
     * now-1h 查询一小时内范围
     * now-1d 查询一天内时间范围
     * now-1y 查询最近一年内的时间范围
     */
   /* public void dateRangeQuery() {
        try {
            // 构建查询条件
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            // includeLower（是否包含下边界）、includeUpper（是否包含上边界）
            searchSourceBuilder.query(QueryBuilders.rangeQuery("birthDate")
                .gte("now-30y").includeLower(true).includeUpper(true));
            // 创建查询请求对象，将查询对象配置到其中
            SearchRequest searchRequest = new SearchRequest("mydlq-user");
            searchRequest.source(searchSourceBuilder);
            // 执行查询，然后处理响应结果
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            // 根据状态和数据条数验证是否返回了数据
            if (RestStatus.OK.equals(searchResponse.status()) && searchResponse.getHits().getTotalHits().value  > 0) {
                SearchHits hits = searchResponse.getHits();
                for (SearchHit hit : hits) {
                    String data = hit.getSourceAsString();
                    // 输出查询信息
                    log.info(data);
                }
            }
        } catch (IOException e) {
            log.error("", e);
        }
    }*/

    /**
     * 查询所有以 “三” 结尾的姓名
     *
     * *：表示多个字符（0个或多个字符）
     * ?：表示单个字符
     */
    /*public Object wildcardQuery() {
        try {
            // 构建查询条件
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.wildcardQuery("name.keyword", "*三"));
            // 创建查询请求对象，将查询对象配置到其中
            SearchRequest searchRequest = new SearchRequest("mydlq-user");
            searchRequest.source(searchSourceBuilder);
            // 执行查询，然后处理响应结果
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            // 根据状态和数据条数验证是否返回了数据
            if (RestStatus.OK.equals(searchResponse.status()) && searchResponse.getHits().getTotalHits().value > 0) {
                SearchHits hits = searchResponse.getHits();
                for (SearchHit hit : hits) {
                    String data = hit.getSourceAsString();
                    // 输出查询信息
                    log.info(data);
                }
            }
        } catch (IOException e) {
            log.error("", e);
        }
        return "";
    }*/

    /*public Object boolQuery() {
        try {
            // 创建 Bool 查询构建器
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            // 构建查询条件
            boolQueryBuilder.must(QueryBuilders.termsQuery("address.keyword", "北京市昌平区", "北京市大兴区", "北京市房山区"))
                .filter().add(QueryBuilders.rangeQuery("birthDate").format("yyyy").gte("1990").lte("1995"));
            // 构建查询源构建器
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(boolQueryBuilder);
            // 创建查询请求对象，将查询对象配置到其中
            SearchRequest searchRequest = new SearchRequest("mydlq-user");
            searchRequest.source(searchSourceBuilder);
            // 执行查询，然后处理响应结果
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            // 根据状态和数据条数验证是否返回了数据
            if (RestStatus.OK.equals(searchResponse.status()) && searchResponse.getHits().getTotalHits().value > 0) {
                SearchHits hits = searchResponse.getHits();
                for (SearchHit hit : hits) {
                    String data = hit.getSourceAsString();
                    // 输出查询信息
                    log.info(data);
                }
            }
        }catch (IOException e){
            log.error("",e);
        }
        return "";
    }*/

    /**
     * stats 统计员工总数、员工工资最高值、员工工资最低值、员工平均工资、员工工资总和
     */
  /*  public Object aggregationStats() {
        String responseResult = "";
        try {
            // 设置聚合条件
            AggregationBuilder aggr = AggregationBuilders.stats("salary_stats").field("salary");
            // 查询源构建器
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.aggregation(aggr);
            // 设置查询结果不返回，只返回聚合结果
            searchSourceBuilder.size(0);
            // 创建查询请求对象，将查询条件配置到其中
            SearchRequest request = new SearchRequest("mydlq-user");
            request.source(searchSourceBuilder);
            // 执行请求
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            // 获取响应中的聚合信息
            Aggregations aggregations = response.getAggregations();
            // 输出内容
            if (RestStatus.OK.equals(response.status()) || aggregations != null) {
                // 转换为 Stats 对象
                ParsedStats aggregation = aggregations.get("salary_stats");
                log.info("-------------------------------------------");
                log.info("聚合信息:");
                log.info("count："+ aggregation.getCount());
                log.info("avg:"+ aggregation.getAvg());
                log.info("max："+ aggregation.getMax());
                log.info("min："+ aggregation.getMin());
                log.info("sum："+ aggregation.getSum());
                log.info("-------------------------------------------");
            }
            // 根据具体业务逻辑返回不同结果，这里为了方便直接将返回响应对象Json串
            responseResult = response.toString();
        } catch (IOException e) {
            log.error("", e);
        }
        return responseResult;
    }*/

    /**
     * min 统计员工工资最低值
     */
/*    public Object aggregationMin() {
        String responseResult = "";
        try {
            // 设置聚合条件
            AggregationBuilder aggr = AggregationBuilders.min("salary_min").field("salary");
            // 查询源构建器
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.aggregation(aggr);
            searchSourceBuilder.size(0);
            // 创建查询请求对象，将查询条件配置到其中
            SearchRequest request = new SearchRequest("mydlq-user");
            request.source(searchSourceBuilder);
            // 执行请求
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            // 获取响应中的聚合信息
            Aggregations aggregations = response.getAggregations();
            // 输出内容
            if (RestStatus.OK.equals(response.status()) || aggregations != null) {
                // 转换为 Min 对象
                ParsedMin aggregation = aggregations.get("salary_min");
                log.info("-------------------------------------------");
                log.info("聚合信息:");
                log.info("min："+ aggregation.getValue());
                log.info("-------------------------------------------");
            }
            // 根据具体业务逻辑返回不同结果，这里为了方便直接将返回响应对象Json串
            responseResult = response.toString();
        } catch (IOException e) {
            log.error("", e);
        }
        return responseResult;
    }*/

    /**
     * max 统计员工工资最高值
     */
   /* public Object aggregationMax() {
        String responseResult = "";
        try {
            // 设置聚合条件
            AggregationBuilder aggr = AggregationBuilders.max("salary_max").field("salary");
            // 查询源构建器
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.aggregation(aggr);
            searchSourceBuilder.size(0);
            // 创建查询请求对象，将查询条件配置到其中
            SearchRequest request = new SearchRequest("mydlq-user");
            request.source(searchSourceBuilder);
            // 执行请求
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            // 获取响应中的聚合信息
            Aggregations aggregations = response.getAggregations();
            // 输出内容
            if (RestStatus.OK.equals(response.status()) || aggregations != null) {
                // 转换为 Max 对象
                ParsedMax aggregation = aggregations.get("salary_max");
                log.info("-------------------------------------------");
                log.info("聚合信息:");
                log.info("max："+ aggregation.getValue());
                log.info("-------------------------------------------");
            }
            // 根据具体业务逻辑返回不同结果，这里为了方便直接将返回响应对象Json串
            responseResult = response.toString();
        } catch (IOException e) {
            log.error("", e);
        }
        return responseResult;
    }*/

    /**
     * avg 统计员工工资平均值
     */
    /*public Object aggregationAvg() {
        String responseResult = "";
        try {
            // 设置聚合条件
            AggregationBuilder aggr = AggregationBuilders.avg("salary_avg").field("salary");
            // 查询源构建器
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.aggregation(aggr);
            searchSourceBuilder.size(0);
            // 创建查询请求对象，将查询条件配置到其中
            SearchRequest request = new SearchRequest("mydlq-user");
            request.source(searchSourceBuilder);
            // 执行请求
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            // 获取响应中的聚合信息
            Aggregations aggregations = response.getAggregations();
            // 输出内容
            if (RestStatus.OK.equals(response.status()) || aggregations != null) {
                // 转换为 Avg 对象
                ParsedAvg aggregation = aggregations.get("salary_avg");
                log.info("-------------------------------------------");
                log.info("聚合信息:");
                log.info("avg："+aggregation.getValue());
                log.info("-------------------------------------------");
            }
            // 根据具体业务逻辑返回不同结果，这里为了方便直接将返回响应对象Json串
            responseResult = response.toString();
        } catch (IOException e) {
            log.error("", e);
        }
        return responseResult;
    }*/

    /**
     * sum 统计员工工资总值
     */
  /*  public Object aggregationSum() {
        String responseResult = "";
        try {
            // 设置聚合条件
            SumAggregationBuilder aggr = AggregationBuilders.sum("salary_sum").field("salary");
            // 查询源构建器
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.aggregation(aggr);
            searchSourceBuilder.size(0);
            // 创建查询请求对象，将查询条件配置到其中
            SearchRequest request = new SearchRequest("mydlq-user");
            request.source(searchSourceBuilder);
            // 执行请求
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            // 获取响应中的聚合信息
            Aggregations aggregations = response.getAggregations();
            // 输出内容
            if (RestStatus.OK.equals(response.status()) || aggregations != null) {
                // 转换为 Sum 对象
                ParsedSum aggregation = aggregations.get("salary_sum");
                log.info("-------------------------------------------");
                log.info("聚合信息:");
                log.info("sum："+ aggregation.getValue());
                log.info("-------------------------------------------");
            }
            // 根据具体业务逻辑返回不同结果，这里为了方便直接将返回响应对象Json串
            responseResult = response.toString();
        } catch (IOException e) {
            log.error("", e);
        }
        return responseResult;
    }*/

    /**
     * count 统计员工总数
     */
  /*  public Object aggregationCount() {
        String responseResult = "";
        try {
            // 设置聚合条件
            AggregationBuilder aggr = AggregationBuilders.count("employee_count").field("salary");
            // 查询源构建器
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.aggregation(aggr);
            searchSourceBuilder.size(0);
            // 创建查询请求对象，将查询条件配置到其中
            SearchRequest request = new SearchRequest("mydlq-user");
            request.source(searchSourceBuilder);
            // 执行请求
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            // 获取响应中的聚合信息
            Aggregations aggregations = response.getAggregations();
            // 输出内容
            if (RestStatus.OK.equals(response.status()) || aggregations != null) {
                // 转换为 ValueCount 对象
                ParsedValueCount aggregation = aggregations.get("employee_count");
                log.info("-------------------------------------------");
                log.info("聚合信息:");
                log.info("count："+ aggregation.getValue());
                log.info("-------------------------------------------");
            }
            // 根据具体业务逻辑返回不同结果，这里为了方便直接将返回响应对象Json串
            responseResult = response.toString();
        } catch (IOException e) {
            log.error("", e);
        }
        return responseResult;
    }*/

    /**
     * percentiles 统计员工工资百分位
     */
  /*  public Object aggregationPercentiles() {
        String responseResult = "";
        try {
            // 设置聚合条件
            AggregationBuilder aggr = AggregationBuilders.percentiles("salary_percentiles").field("salary");
            // 查询源构建器
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.aggregation(aggr);
            searchSourceBuilder.size(0);
            // 创建查询请求对象，将查询条件配置到其中
            SearchRequest request = new SearchRequest("mydlq-user");
            request.source(searchSourceBuilder);
            // 执行请求
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            // 获取响应中的聚合信息
            Aggregations aggregations = response.getAggregations();
            // 输出内容
            if (RestStatus.OK.equals(response.status()) || aggregations != null) {
                // 转换为 Percentiles 对象
                ParsedPercentiles aggregation = aggregations.get("salary_percentiles");
                log.info("-------------------------------------------");
                log.info("聚合信息:");
                for (Percentile percentile : aggregation) {
                    log.info("百分位："+ percentile.getPercent()+":"+ percentile.getValue());
                }
                log.info("-------------------------------------------");
            }
            // 根据具体业务逻辑返回不同结果，这里为了方便直接将返回响应对象Json串
            responseResult = response.toString();
        } catch (IOException e) {
            log.error("", e);
        }
        return responseResult;
    }*/

    /**
     * 按岁数进行聚合分桶
     */
   /* public Object aggrBucketTerms() {
        try {
            AggregationBuilder aggr = AggregationBuilders.terms("age_bucket").field("age");
            // 查询源构建器
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.size(10);
            searchSourceBuilder.aggregation(aggr);
            // 创建查询请求对象，将查询条件配置到其中
            SearchRequest request = new SearchRequest("mydlq-user");
            request.source(searchSourceBuilder);
            // 执行请求
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            // 获取响应中的聚合信息
            Aggregations aggregations = response.getAggregations();
            // 输出内容
            if (RestStatus.OK.equals(response.status())) {
                // 分桶
                Terms byCompanyAggregation = aggregations.get("age_bucket");
                List<? extends Terms.Bucket> buckets = byCompanyAggregation.getBuckets();
                // 输出各个桶的内容
                log.info("-------------------------------------------");
                log.info("聚合信息:");
                for (Terms.Bucket bucket : buckets) {
                    log.info("桶名："+bucket.getKeyAsString()+"| 总数:"+ bucket.getDocCount());
                }
                log.info("-------------------------------------------");
            }
        } catch (IOException e) {
            log.error("", e);
        }
        return  "";
    }*/

    /**
     * 按工资范围进行聚合分桶
     */
  /*  public Object aggrBucketRange() {
        try {
            AggregationBuilder aggr = AggregationBuilders.range("salary_range_bucket")
                .field("salary")
                .addUnboundedTo("低级员工", 3000)
                .addRange("中级员工", 5000, 9000)
                .addUnboundedFrom("高级员工", 9000);
            // 查询源构建器
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.size(0);
            searchSourceBuilder.aggregation(aggr);
            // 创建查询请求对象，将查询条件配置到其中
            SearchRequest request = new SearchRequest("mydlq-user");
            request.source(searchSourceBuilder);
            // 执行请求
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            // 获取响应中的聚合信息
            Aggregations aggregations = response.getAggregations();
            // 输出内容
            if (RestStatus.OK.equals(response.status())) {
                // 分桶
                Range byCompanyAggregation = aggregations.get("salary_range_bucket");
                List<? extends Range.Bucket> buckets = byCompanyAggregation.getBuckets();
                // 输出各个桶的内容
                log.info("-------------------------------------------");
                log.info("聚合信息:");
                for (Range.Bucket bucket : buckets) {
                    log.info("桶名："+bucket.getKeyAsString()+" | 总数："+ bucket.getDocCount());
                }
                log.info("-------------------------------------------");
            }
        } catch (IOException e) {
            log.error("", e);
        }
        return  "";
    }*/

    /**
     * 按照时间范围进行分桶
     */
    /*public Object aggrBucketDateRange() {
        try {
            AggregationBuilder aggr = AggregationBuilders.dateRange("date_range_bucket")
                .field("birthDate")
                .format("yyyy")
                .addRange("1985-1990", "1985", "1990")
                .addRange("1990-1995", "1990", "1995");
            // 查询源构建器
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.size(0);
            searchSourceBuilder.aggregation(aggr);
            // 创建查询请求对象，将查询条件配置到其中
            SearchRequest request = new SearchRequest("mydlq-user");
            request.source(searchSourceBuilder);
            // 执行请求
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            // 获取响应中的聚合信息
            Aggregations aggregations = response.getAggregations();
            // 输出内容
            if (RestStatus.OK.equals(response.status())) {
                // 分桶
                Range byCompanyAggregation = aggregations.get("date_range_bucket");
                List<? extends Range.Bucket> buckets = byCompanyAggregation.getBuckets();
                // 输出各个桶的内容
                log.info("-------------------------------------------");
                log.info("聚合信息:");
                for (Range.Bucket bucket : buckets) {
                    log.info("桶名："+ bucket.getKeyAsString()+" | 总数："+ bucket.getDocCount());
                }
                log.info("-------------------------------------------");
            }
        } catch (IOException e) {
            log.error("", e);
        }
        return "";
    }*/

    /**
     * 按工资多少进行聚合分桶
     */
/*    public Object aggrBucketHistogram() {
        try {

            AggregationBuilder aggr = AggregationBuilders.histogram("salary_histogram")
                .field("salary")
                .extendedBounds(0, 12000)
                .interval(3000);
            // 查询源构建器
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.size(0);
            searchSourceBuilder.aggregation(aggr);
            // 创建查询请求对象，将查询条件配置到其中
            SearchRequest request = new SearchRequest("mydlq-user");
            request.source(searchSourceBuilder);
            // 执行请求
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            // 获取响应中的聚合信息
            Aggregations aggregations = response.getAggregations();
            // 输出内容
            if (RestStatus.OK.equals(response.status())) {
                // 分桶
                Histogram byCompanyAggregation = aggregations.get("salary_histogram");
                List<? extends Histogram.Bucket> buckets = byCompanyAggregation.getBuckets();
                // 输出各个桶的内容
                log.info("-------------------------------------------");
                log.info("聚合信息:");
                for (Histogram.Bucket bucket : buckets) {
                    log.info("桶名："+bucket.getKeyAsString()+" | 总数："+ bucket.getDocCount());
                }
                log.info("-------------------------------------------");
            }
        } catch (IOException e) {
            log.error("", e);
        }
        return  "";
    }*/

    /**
     * 按出生日期进行分桶
     */
   /* public Object aggrBucketDateHistogram() {
        try {
            AggregationBuilder aggr = AggregationBuilders.dateHistogram("birthday_histogram")
                .field("birthDate")
                .interval(1)
                .dateHistogramInterval(DateHistogramInterval.YEAR)
                .format("yyyy");
            // 查询源构建器
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.size(0);
            searchSourceBuilder.aggregation(aggr);
            // 创建查询请求对象，将查询条件配置到其中
            SearchRequest request = new SearchRequest("mydlq-user");
            request.source(searchSourceBuilder);
            // 执行请求
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            // 获取响应中的聚合信息
            Aggregations aggregations = response.getAggregations();
            // 输出内容
            if (RestStatus.OK.equals(response.status())) {
                // 分桶
                Histogram byCompanyAggregation = aggregations.get("birthday_histogram");

                List<? extends Histogram.Bucket> buckets = byCompanyAggregation.getBuckets();
                // 输出各个桶的内容
                log.info("-------------------------------------------");
                log.info("聚合信息:");
                for (Histogram.Bucket bucket : buckets) {
                    log.info("桶名："+ bucket.getKeyAsString()+"| 总数："+ bucket.getDocCount());
                }
                log.info("-------------------------------------------");
            }
        } catch (IOException e) {
            log.error("", e);
        }
        return "";
    }*/

    /**
     * topHits 按岁数分桶、然后统计每个员工工资最高值
     */
   /* public Object aggregationTopHits() {
        try {
            AggregationBuilder testTop = AggregationBuilders.topHits("salary_max_user")
                .size(1)
                .sort("salary", SortOrder.DESC);
            AggregationBuilder salaryBucket = AggregationBuilders.terms("salary_bucket")
                .field("age")
                .size(10);
            salaryBucket.subAggregation(testTop);
            // 查询源构建器
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.size(0);
            searchSourceBuilder.aggregation(salaryBucket);
            // 创建查询请求对象，将查询条件配置到其中
            SearchRequest request = new SearchRequest("mydlq-user");
            request.source(searchSourceBuilder);
            // 执行请求
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            // 获取响应中的聚合信息
            Aggregations aggregations = response.getAggregations();
            // 输出内容
            if (RestStatus.OK.equals(response.status())) {
                // 分桶
                Terms byCompanyAggregation = aggregations.get("salary_bucket");
                List<? extends Terms.Bucket> buckets = byCompanyAggregation.getBuckets();
                // 输出各个桶的内容
                log.info("-------------------------------------------");
                log.info("聚合信息:");
                for (Terms.Bucket bucket : buckets) {
                    log.info("桶名："+ bucket.getKeyAsString());
                    ParsedTopHits topHits = bucket.getAggregations().get("salary_max_user");
                    for (SearchHit hit:topHits.getHits()){
                        log.info(hit.getSourceAsString());
                    }
                }
                log.info("-------------------------------------------");
            }
        } catch (IOException e) {
            log.error("", e);
        }
        return "";
    }*/

}









