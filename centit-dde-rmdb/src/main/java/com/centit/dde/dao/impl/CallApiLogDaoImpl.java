package com.centit.dde.dao.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.adapter.dao.CallApiLogDao;
import com.centit.dde.adapter.po.CallApiLog;
import com.centit.dde.adapter.po.CallApiLogDetail;
import com.centit.dde.adapter.po.CallApiLogDetails;
import com.centit.search.service.ESServerConfig;
import com.centit.search.service.Impl.ESIndexer;
import com.centit.search.service.Impl.ESSearcher;
import com.centit.search.service.IndexerSearcherFactory;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.database.utils.PageDesc;
import org.apache.commons.lang3.tuple.Pair;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.ParsedValueCount;
import org.elasticsearch.search.aggregations.metrics.ValueCountAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository(value = "callApiLogDao")
public class CallApiLogDaoImpl implements CallApiLogDao {

    private final static Logger logger = LoggerFactory.getLogger(CallApiLogDaoImpl.class);

    private final ESIndexer callApiLogIndexer;
    private final ESSearcher callApiLogSearcher;
    private final ESIndexer callApiLogDetailIndexer;
    private final ESSearcher callApiLogDetailSearcher;

    private final ESServerConfig esServerConfig;

    @Autowired
    public CallApiLogDaoImpl(@Autowired ESServerConfig esServerConfig) {
        this.esServerConfig = esServerConfig;
        this.callApiLogIndexer = IndexerSearcherFactory.obtainIndexer(esServerConfig, CallApiLog.class);
        this.callApiLogSearcher = IndexerSearcherFactory.obtainSearcher(esServerConfig, CallApiLog.class);
        this.callApiLogDetailIndexer = IndexerSearcherFactory.obtainIndexer(esServerConfig, CallApiLogDetails.class);
        this.callApiLogDetailSearcher = IndexerSearcherFactory.obtainSearcher(esServerConfig, CallApiLogDetails.class);
    }

    @Override
    public void saveLog(CallApiLog callApiLog) {
        callApiLogIndexer.saveNewDocument(callApiLog);
    }

    @Override
    public void saveLogDetails(CallApiLog callApiLog) {
        CallApiLogDetails details = new CallApiLogDetails();
        details.setLogId(callApiLog.getLogId());
        details.setTaskId(callApiLog.getTaskId());
        details.setRunBeginTime(callApiLog.getRunBeginTime());
        details.setDetailLogs(callApiLog.getDetailLogs());
        callApiLogDetailIndexer.saveNewDocument(details);
    }

    @Override
    public CallApiLog getLog(String logId) {
        JSONObject object = callApiLogSearcher.getDocumentById("logId", logId);
        if(object==null){
            return null;
        }
        return object.toJavaObject(CallApiLog.class);
    }

    @Override
    public List<CallApiLogDetail> listLogDetails(String logId) {
        JSONObject object = callApiLogDetailSearcher.getDocumentById("logId", logId);
        if(object==null){
            return null;
        }
        return JSONArray.parseArray(object.getString("detailLogs"), CallApiLogDetail.class);
    }

    @Override
    public CallApiLog getLogWithDetail(String logId) {
        CallApiLog callLog = getLog(logId);
        if(callLog==null){
            return null;
        }
        callLog.setDetailLogs(listLogDetails(logId));
        return callLog;
    }

    @Override
    public void deleteLogById(String logId) {
        callApiLogIndexer.deleteDocument(logId);
    }

    @Override
    public void deleteLogDetailById(String logId) {
        callApiLogDetailIndexer.deleteDocument(logId);
    }

    @Override
    public List<Map<String, Object>> listLogsByProperties(Map<String, Object> param, PageDesc pageDesc) {
        Pair<Long, List<Map<String, Object>>> queryOut =
            callApiLogSearcher.search(param, null, pageDesc.getPageNo(), pageDesc.getPageSize());
        pageDesc.setTotalRows(queryOut.getLeft().intValue());
        List<Map<String, Object>> objectList =  queryOut.getRight();
        if(objectList==null) return null;
        for(Map<String, Object> obj : objectList){
            obj.put("runBeginTime", DatetimeOpt.smartPraseDate((String)obj.get("runBeginTime")));
            obj.put("runEndTime", DatetimeOpt.smartPraseDate((String)obj.get("runEndTime")));
        }
        return objectList;
    }

    @Override
    public Map<String, Long> statApiCallSumByHour(String taskId, Date startDate, Date endDate){
        SearchRequest searchRequest = new SearchRequest("callapilog");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        // 构建过滤条件
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.must(QueryBuilders.termQuery("taskId", taskId));

        RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("runBeginTime")
            .gte(startDate)
            .lte(endDate);
        boolQuery.must(rangeQuery);

        sourceBuilder.query(boolQuery);

        // 构建聚合
        DateHistogramAggregationBuilder dateHistogramAggregation = AggregationBuilders.dateHistogram("hourly")
            .field("runBeginTime")
            .interval(360000L);
        ValueCountAggregationBuilder countAggregation = AggregationBuilders.count("count").field("taskId");
        dateHistogramAggregation.subAggregation(countAggregation);

        sourceBuilder.aggregation(dateHistogramAggregation);

        searchRequest.source(sourceBuilder);
        RestHighLevelClient client = null;
        Map<String, Long> result = new HashMap<>();
        try {
            client = callApiLogSearcher.fetchClient(); // 假设 ESSearcher 有 getClient 方法
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            Histogram hourlyHistogram = searchResponse.getAggregations().get("hourly");
            for (Histogram.Bucket hourlyBucket : hourlyHistogram.getBuckets()) {
                String keyAsString = hourlyBucket.getKeyAsString();
                ParsedValueCount count = hourlyBucket.getAggregations().get("count");
                result.put(keyAsString, count.getValue());
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (client != null) {
                callApiLogSearcher.releaseClient(client);
            }
        }
        return result;
    }

    @Override
    public int deleteTaskLog(String packetId, Date runBeginTime, boolean isError) {

        return 0;
    }

}
