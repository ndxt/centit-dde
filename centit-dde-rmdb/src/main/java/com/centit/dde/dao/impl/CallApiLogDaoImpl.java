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
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.database.utils.PageDesc;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.ParsedSum;
import org.elasticsearch.search.aggregations.metrics.SumAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
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
        if(StringUtils.length(callApiLog.getOtherMessage())> 10240){
            logger.error("日志信息 ：" + callApiLog.getOptId()+
                " otherMessage length is ：" + StringUtils.length(callApiLog.getOtherMessage()));
        }
        callApiLogIndexer.saveNewDocument(callApiLog);
    }

    @Override
    public void saveLogDetails(CallApiLog callApiLog) {
        CallApiLogDetails details = new CallApiLogDetails();
        details.setLogId(callApiLog.getLogId());
        details.setTaskId(callApiLog.getTaskId());
        details.setRunBeginTime(callApiLog.getRunBeginTime());
        int logDetailCount = callApiLog.getDetailLogs().size();
        //保留最后20条日志明细
        if(logDetailCount > 100){
            List<CallApiLogDetail> detailLogs = new ArrayList<>();
            for(int i=0; i<10; i++){
                detailLogs.add(callApiLog.getDetailLogs().get(i));
            }
            detailLogs.add(callApiLog.getDetailLogs().get(0));
            for(int i=90; i>=1; i--){
                detailLogs.add(callApiLog.getDetailLogs().get(logDetailCount-i));
            }
            details.setDetailLogs(detailLogs);
        } else {
            details.setDetailLogs(callApiLog.getDetailLogs());
        }
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

    }

    @Override
    public List<Map<String, Object>> listLogsByProperties(Map<String, Object> param, PageDesc pageDesc) {
        for(String sKey : param.keySet()){
            if(sKey.startsWith("runBeginTime") || sKey.startsWith("runEndTime")){
                param.computeIfPresent(sKey, (k, timeValue) -> convertLocalToUTC(timeValue.toString()));
            }
        }
        Pair<Long, List<Map<String, Object>>> queryOut =
            callApiLogSearcher.search(param, StringBaseOpt.objectToString(param.get("query")), pageDesc.getPageNo(), pageDesc.getPageSize());
        pageDesc.setTotalRows(queryOut.getLeft().intValue());
        List<Map<String, Object>> objectList =  queryOut.getRight();
        if(objectList==null) return null;
        for(Map<String, Object> obj : objectList){
            obj.put("runBeginTime", DatetimeOpt.smartPraseDate((String)obj.get("runBeginTime")));
            obj.put("runEndTime", DatetimeOpt.smartPraseDate((String)obj.get("runEndTime")));
        }
        return objectList;
    }

    private  String convertLocalToUTC(String localDateTimeStr) {
        // 解析本地时间
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(localDateTimeStr, formatter);
        // 假设输入是系统默认时区的时间
        ZonedDateTime zonedLocalTime = localDateTime.atZone(ZoneId.systemDefault());
        // 转换为UTC时区
        ZonedDateTime utcTime = zonedLocalTime.withZoneSameInstant(ZoneId.of("UTC"));
        // 添加毫秒部分（示例中为331）
        ZonedDateTime utcTimeWithMillis = utcTime.withNano(331 * 1_000_000);
        // 格式化为ISO 8601格式
        return utcTimeWithMillis.format(DateTimeFormatter.ISO_INSTANT);
    }

    @Override
    public JSONArray statApiCallSum(String statType, String typeId, Date startDate, Date endDate){
        SearchRequest searchRequest = new SearchRequest("callapilog");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        // 构建过滤条件
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        if("topUnit".equalsIgnoreCase(statType)) {
            boolQuery.must(QueryBuilders.termQuery("topUnit", typeId));
        } else if("application".equalsIgnoreCase(statType)) {
            boolQuery.must(QueryBuilders.termQuery("applicationId", typeId));
        } else if("opt".equalsIgnoreCase(statType)) {
            boolQuery.must(QueryBuilders.termQuery("optId", typeId));
        } else {
            boolQuery.must(QueryBuilders.termQuery("taskId", typeId));
        }

        RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("runBeginTime")
            .gte(startDate)
            .lte(endDate);
        boolQuery.must(rangeQuery);

        sourceBuilder.query(boolQuery);
        //大于5天按照天聚合， 否则按照小时聚合
        long intervals; String dateFormate;
        if(DatetimeOpt.calcSpanDays(startDate, endDate) >= 5 ){
            intervals = 86400000L;
            dateFormate = DatetimeOpt.defaultDatePattern; // "yyyy-MM-dd";
        } else {
            intervals = 3600000L;
            dateFormate = DatetimeOpt.datetimePattern;// "yyyy-MM-dd HH:mm:ss";
        }
        // 构建聚合
        DateHistogramAggregationBuilder dateHistogramAggregation = AggregationBuilders.dateHistogram("hourly")
            .field("runBeginTime")
            .interval(intervals) // 3600000 milliseconds = 1 hour
            .format(dateFormate) // 明确日期格式
            .timeZone(ZoneId.systemDefault()); // 设置时区

        SumAggregationBuilder errorPiecesSum = AggregationBuilders.sum("errorPiecesSum").field("errorPieces");
        SumAggregationBuilder successPiecesSum = AggregationBuilders.sum("successPiecesSum").field("successPieces");
        dateHistogramAggregation.subAggregation(errorPiecesSum);
        dateHistogramAggregation.subAggregation(successPiecesSum);
        sourceBuilder.aggregation(dateHistogramAggregation);
        searchRequest.source(sourceBuilder);

        JSONArray result = new JSONArray();
        RestHighLevelClient client = null;
        try{ // 使用 try-with-resources
            client = callApiLogSearcher.fetchClient();
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            Histogram hourlyHistogram = searchResponse.getAggregations().get("hourly");
            for (Histogram.Bucket hourlyBucket : hourlyHistogram.getBuckets()) {
                String keyAsString = hourlyBucket.getKeyAsString();
                ParsedSum errorPiecesValue = hourlyBucket.getAggregations().get("errorPiecesSum");
                ParsedSum successPiecesValue = hourlyBucket.getAggregations().get("successPiecesSum");

                JSONObject sums = new JSONObject();
                sums.put("runBeginTime", keyAsString); // hourly
                sums.put("errorPieces", errorPiecesValue.getValue());
                sums.put("successPieces", successPiecesValue.getValue());

                result.add(sums);
            }
        } catch (IOException | ElasticsearchException e) { // 捕获更广泛的异常
            logger.error("Error occurred while processing statType: {} param: {}, start date: {}, end date: {}",
                statType, typeId, startDate, endDate, e);
        } finally {
            callApiLogSearcher.releaseClient(client);
        }
        return result;
    }

    @Override
    public JSONArray statCallSumByOs(String osId, Date startDate, Date endDate){
        SearchRequest searchRequest = new SearchRequest("callapilog");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        // 构建过滤条件
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.must(QueryBuilders.termQuery("applicationId", osId));
        RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("runBeginTime")
            .gte(startDate)
            .lte(endDate);
        boolQuery.must(rangeQuery);
        sourceBuilder.query(boolQuery);

        // 构建聚合
        DateHistogramAggregationBuilder dateHistogramAggregation = AggregationBuilders.dateHistogram("daily")
            .field("runBeginTime")
            .interval(86400000L) // 3600000 milliseconds = 1 hour
            .format(DatetimeOpt.defaultDatePattern) // 明确日期格式 yyyy-MM-dd
            .timeZone(ZoneId.systemDefault()); // 设置时区

        sourceBuilder.aggregation(dateHistogramAggregation);
        searchRequest.source(sourceBuilder);
        JSONArray result = new JSONArray();
        RestHighLevelClient client = null;
        try{ // 使用 try-with-resources
            client = callApiLogSearcher.fetchClient();
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            Histogram dailyHistogram = searchResponse.getAggregations().get("daily");
            for (Histogram.Bucket dailyBucket : dailyHistogram.getBuckets()) {
                String keyAsString = dailyBucket.getKeyAsString();
                long docCount = dailyBucket.getDocCount();
                JSONObject sums = new JSONObject();
                sums.put("runBeginTime", keyAsString); // daily
                sums.put("callSum", docCount);
                result.add(sums);
            }
        } catch (IOException | ElasticsearchException e) { // 捕获更广泛的异常
            logger.error("Error occurred while processing application: {}, start date: {}, end date: {}",
                osId, startDate, endDate, e);
        } finally {
            callApiLogSearcher.releaseClient(client);
        }
        return result;
    }

    @Override
    public JSONArray statTopTask(String osId, String countType, int topSize, Date startDate, Date endDate)  {
        SearchRequest searchRequest = new SearchRequest("callapilog");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        // 构建过滤条件
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.must(QueryBuilders.termQuery("applicationId", osId));
        if("failed".equalsIgnoreCase(countType)) {
            boolQuery.must( QueryBuilders.rangeQuery("errorPieces").gt(0));
        }
        RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("runBeginTime")
            .gte(startDate)
            .lte(endDate);
        boolQuery.must(rangeQuery);

        sourceBuilder.query(boolQuery);

        // 构建聚合
        TermsAggregationBuilder termsAggregation = AggregationBuilders.terms("top_task_ids")
            .field("taskId")
            .size(topSize) // 只取前30个
            .order(BucketOrder.count(false)); // 按条目数降序排列

        sourceBuilder.aggregation(termsAggregation);
        JSONArray result = new JSONArray();
        searchRequest.source(sourceBuilder);
        RestHighLevelClient client = null;
        try{ // 使用 try-with-resources
            client = callApiLogSearcher.fetchClient();
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            ParsedTerms topTaskIds = searchResponse.getAggregations().get("top_task_ids");
            for (Terms.Bucket bucket : topTaskIds.getBuckets()) {
                String keyAsString = bucket.getKeyAsString();
                long docCount = bucket.getDocCount();

                result.add(CollectionsOpt.createHashMap("taskId", keyAsString, "callSum", docCount));
            }
        } catch (IOException | ElasticsearchException e) { // 捕获更广泛的异常
            logger.error("Error occurred while processing application: {}, countType: {}, start date: {}, end date: {}",
                osId, countType, startDate, endDate, e);
        } finally {
            callApiLogSearcher.releaseClient(client);
        }
        return result;
    }

}
