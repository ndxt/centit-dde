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
        //保留100条日志明细, 前90条，后10条
        if(logDetailCount > 100){
            List<CallApiLogDetail> detailLogs = new ArrayList<>();
            for(int i=0; i<90; i++){
                detailLogs.add(callApiLog.getDetailLogs().get(i));
            }
            CallApiLogDetail separate = new CallApiLogDetail();
            separate.setStepNo(91);
            separate.setOptNodeId("separate line");
            separate.setLogType("info");
            separate.setRunBeginTime(new Date());
            separate.setRunEndTime(new Date());
            separate.setLogInfo("日志明细共"+logDetailCount+"条，中间"+(logDetailCount-100)+"条被截断，仅保留100条，前90条，后10条");
            separate.setErrorPieces(0);
            separate.setSuccessPieces(0);
            detailLogs.add(separate);
            for(int i=10; i>=1; i--){
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

    /**
     * 根据optId统计接口的响应时间和成功率（整体统计）
     * @param optId 接口ID
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 统计结果
     */
    public JSONObject statApiEfficiency(String optId, Date startDate, Date endDate){
        SearchRequest searchRequest = new SearchRequest("callapilog");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        // 构建过滤条件
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.must(QueryBuilders.termQuery("optId", optId));
        RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("runBeginTime")
            .gte(startDate)
            .lte(endDate);
        boolQuery.must(rangeQuery);

        sourceBuilder.query(boolQuery);
        sourceBuilder.fetchSource(new String[]{"runBeginTime", "runEndTime", "successPieces", "errorPieces"}, null);

        searchRequest.source(sourceBuilder);

        JSONObject result = new JSONObject();
        RestHighLevelClient client = null;
        long totalResponseTime = 0;
        long totalCount = 0;
        long totalSuccessPieces = 0;
        long totalErrorPieces = 0;

        try {
            client = callApiLogSearcher.fetchClient();
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

            // 遍历所有匹配的文档，计算总响应时间和成功率相关数据
            for (org.elasticsearch.search.SearchHit hit : searchResponse.getHits().getHits()) {
                Map<String, Object> source = hit.getSourceAsMap();

                // 计算响应时间
                Long runBeginTime = (Long) source.get("runBeginTime");
                Long runEndTime = (Long) source.get("runEndTime");
                if (runBeginTime != null && runEndTime != null) {
                    totalResponseTime += (runEndTime - runBeginTime);
                    totalCount++;
                }

                // 累加成功率相关数据
                Integer successPieces = (Integer) source.get("successPieces");
                Integer errorPieces = (Integer) source.get("errorPieces");
                if (successPieces != null) {
                    totalSuccessPieces += successPieces;
                }
                if (errorPieces != null) {
                    totalErrorPieces += errorPieces;
                }
            }

            // 计算平均响应时间（毫秒）
            double avgResponseTime = totalCount > 0 ?
                (double) totalResponseTime / totalCount : 0;
            result.put("avgResponseTime", avgResponseTime);

            // 计算成功率
            long totalPieces = totalSuccessPieces + totalErrorPieces;
            double successRate = totalPieces > 0 ?
                (double) totalSuccessPieces / totalPieces : 0;
            result.put("successRate", successRate);

            // 添加总请求数
            result.put("totalCount", totalCount);

        } catch (IOException | ElasticsearchException e) {
            logger.error("Error occurred while processing optId: {}, start date: {}, end date: {}",
                optId, startDate, endDate, e);
        } finally {
            callApiLogSearcher.releaseClient(client);
        }
        return result;
    }
}
