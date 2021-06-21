package com.centit.dde.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.entity.EsReadVo;
import com.centit.dde.entity.FieldAttributeInfo;
import com.centit.dde.entity.QueryParameter;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.*;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * es查询工具类
 *
 */
public class ElasticsearchReadUtils {
    private static  final Logger logger = LoggerFactory.getLogger(ElasticsearchReadUtils.class);

    /**
     * 组合查询
     * @param restHighLevelClient
     * @param searchVo
     * @return
     * @throws IOException
     */
    public static JSONArray combinationQuery(RestHighLevelClient restHighLevelClient, EsReadVo searchVo) throws IOException {
        QueryParameter queryParameter = searchVo.getQueryParameter();
        SearchRequest searchRequest = new SearchRequest(queryParameter.getIndexName());
        CountRequest countRequest = new CountRequest(queryParameter.getIndexName());
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        List<FieldAttributeInfo> fieldAttributeInfos = searchVo.getFieldAttributeInfos();
        for (FieldAttributeInfo fieldAttributeInfo : fieldAttributeInfos) {
            String combinationType = fieldAttributeInfo.getCombinationType();
            Boolean isSortField = fieldAttributeInfo.getIsSortField();
            if (isSortField){//添加排序字段
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("fieldName",fieldAttributeInfo.getFieldName());
                jsonObject.put("sortValue",fieldAttributeInfo.getSortValue());
                queryParameter.getSortField().add(jsonObject);
            }
            QueryBuilder queryBuilder = queryBuilder(fieldAttributeInfo);
            //must should  查询会计算相关文档得分情况       mustNot   filter  查询不会计算相关文档得分 并且会将查询结果换成
            switch (combinationType){
                case "must":
                    boolQueryBuilder.must(queryBuilder);
                    break;
                case "should":
                    boolQueryBuilder.should(queryBuilder);
                    break;
                case "mustNot":
                    boolQueryBuilder.mustNot(queryBuilder);
                    break;
                case "filter":
                    boolQueryBuilder.filter(queryBuilder);
                    break;
                default:
                    boolQueryBuilder.must(queryBuilder);
            }
        }
        Object minimumShouldMatch = queryParameter.getMinimumShouldMatch();
        if (minimumShouldMatch!=null){
            //百分比
           if (minimumShouldMatch instanceof  String && StringUtils.isNotBlank((String)minimumShouldMatch)){
               boolQueryBuilder.minimumShouldMatch((String) minimumShouldMatch);
           }
           //整形
           if (minimumShouldMatch instanceof  Integer && (Integer)minimumShouldMatch>0){
               boolQueryBuilder.minimumShouldMatch((Integer) minimumShouldMatch);
           }
        }
        SearchSourceBuilder searchSourceBuilder = publicSearchSourceBuilde(queryParameter);
        queryHighlightBuilder(fieldAttributeInfos,searchSourceBuilder);
        searchSourceBuilder.query(boolQueryBuilder);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        JSONArray jsonArray;
        if (searchSourceBuilder.highlighter()!=null && searchSourceBuilder.highlighter().fields().size()>0){
            jsonArray = returnHighlightResult(searchResponse, fieldAttributeInfos);
        }else {
            jsonArray =resultPart(searchResponse);
        }
        //分页设置
        if (queryParameter.getIsReturnPageInfo()){
            SearchSourceBuilder countSearchSourceBuilder = new SearchSourceBuilder();
            countSearchSourceBuilder.query(boolQueryBuilder);
            countRequest.source(countSearchSourceBuilder);
            CountResponse countResponse = restHighLevelClient.count(countRequest, RequestOptions.DEFAULT);
            int totalCount =  (int)countResponse.getCount();
            int pagesTotal= totalCount== 0 ? 0: (totalCount%queryParameter.getPageSize() == 0 ? totalCount / queryParameter.getPageSize() : (totalCount / queryParameter.getPageSize()) + 1);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("pageNo",queryParameter.getPageNo());
            jsonObject.put("pageSize",queryParameter.getPageSize());
            jsonObject.put("totalCount",totalCount);
            jsonObject.put("pagesTotal",pagesTotal);
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    private static QueryBuilder queryBuilder(FieldAttributeInfo fieldAttributeInfo){
        QueryBuilder queryBuilder=null;
        String fieldName = fieldAttributeInfo.getFieldName();
        Object value = fieldAttributeInfo.getValue();
        Float boots = fieldAttributeInfo.getBoots();
        String analyzer = fieldAttributeInfo.getAnalyzer();
        Integer slop = fieldAttributeInfo.getSlop();
        Integer maxExpansions = fieldAttributeInfo.getMaxExpansions();
        switch (fieldAttributeInfo.getQueryType()){
            case EsQueryType.TERM:
                if (StringUtils.isNotBlank(fieldName) && value !=null){
                    queryBuilder = QueryBuilders.termQuery(fieldName, value);
                    if (boots!=null && boots>0){
                        queryBuilder.boost(boots);
                    }
                }
                return queryBuilder;
            case EsQueryType.TERMS:
                if (StringUtils.isNotBlank(fieldName) && value !=null){
                    String fieldValues = JSON.toJSONString(value);
                    if (fieldValues.contains(",")){
                        String[] fieldValue = fieldValues.split(",");
                        queryBuilder = QueryBuilders.termsQuery(fieldName, fieldValue);
                        if (boots!=null && boots>0){
                            queryBuilder.boost(boots);
                        }
                    }
                }
                return queryBuilder;
            case EsQueryType.MATCH:
                if (StringUtils.isNotBlank(fieldName) && value !=null){
                    MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery(fieldName, value);
                    if (boots!=null && boots>0){
                        matchQueryBuilder.boost(boots);
                    }
                    if(StringUtils.isNotBlank(analyzer)){
                        matchQueryBuilder.analyzer(analyzer);
                    }
                    return  matchQueryBuilder;
                }
            case EsQueryType.MATCH_ALL:
                return QueryBuilders.matchAllQuery();
            case EsQueryType.RANGE:
                String fieldValue = (String)value;
                String[] split = fieldValue.split(",");
                RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(fieldName);
                for (String data : split) {
                    String[] val_expression = data.split("_");
                    if ("gt".equals(val_expression[1])){
                        rangeQueryBuilder.gt(val_expression[0]);
                    }
                    if ("gte".equals(val_expression[1])){
                        rangeQueryBuilder.gte(val_expression[0]);
                    }
                    if ("lt".equals(val_expression[1])){
                        rangeQueryBuilder.lt(val_expression[0]);
                    }
                    if ("lte".equals(val_expression[1])){
                        rangeQueryBuilder.lte(val_expression[0]);
                    }
                }
                return rangeQueryBuilder;
            case EsQueryType.FUZZINESS:
                return QueryBuilders.fuzzyQuery(fieldName, value);
            case EsQueryType.MATCH_PHRASE:
                MatchPhraseQueryBuilder matchPhraseQueryBuilder = QueryBuilders.matchPhraseQuery(fieldName, value);
                if (StringUtils.isNotBlank(analyzer)){
                    matchPhraseQueryBuilder.analyzer(analyzer);
                }
                if (boots!=null&&boots>0){
                    matchPhraseQueryBuilder.boost(boots);
                }
                if (slop!=null && slop>0){
                    matchPhraseQueryBuilder.slop(slop);
                }
                return  matchPhraseQueryBuilder;
            case EsQueryType.MATCH_PHRASE_PREFIX:
                MatchPhrasePrefixQueryBuilder matchPhrasePrefixQueryBuilder = QueryBuilders.matchPhrasePrefixQuery(fieldName, value);
                if (StringUtils.isNotBlank(analyzer)){
                    matchPhrasePrefixQueryBuilder.analyzer(analyzer);
                }
                if (maxExpansions!=null && maxExpansions>0){//不要轻易设置该参数，可能会严重影响查询性能，就算你设置为0时，es也会去扫描很多索引
                    matchPhrasePrefixQueryBuilder.maxExpansions(maxExpansions);
                }
                if (slop!=null && slop>0){
                    matchPhrasePrefixQueryBuilder.slop(slop);
                }
                if (boots!=null && boots>0){
                    matchPhrasePrefixQueryBuilder.boost(boots);
                }
                return matchPhrasePrefixQueryBuilder;
            default:
                return QueryBuilders.matchAllQuery();
        }
    }

    /**
     * 返回普通结果（不需要高亮显示时返回）
     * @param searchResponse
     * @return
     */
    private static JSONArray resultPart(SearchResponse searchResponse){
        JSONArray resultArrays= new JSONArray();
        // 根据状态和数据条数验证是否返回了数据
        if (RestStatus.OK.equals(searchResponse.status()) && searchResponse.getHits().getTotalHits().value > 0) {
            for (SearchHit hit : searchResponse.getHits()) {
                // 打分
                //float score = hit.getScore();
                JSONObject jsonObject = JSONObject.parseObject(hit.getSourceAsString());
                // 输出查询信息
                logger.debug("获取es数据:"+jsonObject.toJSONString());
                resultArrays.add(jsonObject);
            }
        }
        return resultArrays;
    }

    /**
     * 返回高亮结果（需要高亮是返回）
     * @param searchResponse
     * @param fieldAttributeInfos
     * @return
     */
    private static  JSONArray returnHighlightResult(SearchResponse searchResponse, List<FieldAttributeInfo> fieldAttributeInfos){
        List<String> fieldNames = new ArrayList<>();
        for (FieldAttributeInfo fieldAttributeInfo : fieldAttributeInfos) {
            if (fieldAttributeInfo.getIsHighligh()){
                fieldNames.add(fieldAttributeInfo.getFieldName());
            }
        }
        JSONArray jsonArray = new JSONArray();
        for (SearchHit hit : searchResponse.getHits()) {
            JSONObject jsonObject = JSON.parseObject(hit.getSourceAsString());
            //解析高亮字段
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            for (String fieldName : fieldNames) {
                HighlightField field= highlightFields.get(fieldName);
                if(field!= null){
                    Text[] fragments = field.fragments();
                    String n_field = "";
                    for (Text fragment : fragments) {
                        n_field += fragment;
                    }
                    //高亮标题覆盖原标题
                    jsonObject.put(fieldName,n_field);
                }
            }
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    /**
     * 高亮查询字段封装
     * @param fieldAttributeInfos
     * @param searchSourceBuilder
     * @return
     */
    private static void queryHighlightBuilder(List<FieldAttributeInfo> fieldAttributeInfos,SearchSourceBuilder searchSourceBuilder){
        //高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        if (fieldAttributeInfos.size()>0){
            //设置高亮字段
            for (FieldAttributeInfo fieldAttributeInfo : fieldAttributeInfos) {
                if (fieldAttributeInfo.getIsHighligh()){
                    highlightBuilder.field(fieldAttributeInfo.getFieldName());
                }
            }
        }
        if (highlightBuilder.fields().size()>0){
            highlightBuilder.preTags("<span style='color:red'>").postTags("</span>");
            //如果要多个字段高亮,这项要为false
            highlightBuilder.requireFieldMatch(false);
            //下面这两项,如果你要高亮如文字内容等有很多字的字段,必须配置,不然会导致高亮不全,文章内容缺失等
            highlightBuilder.fragmentSize(800000); //最大高亮分片数
            highlightBuilder.numOfFragments(0); //从第一个分片获取高亮片段
            searchSourceBuilder.highlighter(highlightBuilder);
        }
    }

    /**
     * 公共部分查询参数封装
     * @param queryParameter
     * @return
     */
    private static SearchSourceBuilder publicSearchSourceBuilde(QueryParameter queryParameter){
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //设置查询返回字段和不返回的字段
        searchSourceBuilder.fetchSource(queryParameter.getReturnField(),queryParameter.getNotReturnField());
        //设置排序字段
        FieldSortBuilder fieldSortBuilder=null;
        for (JSONObject sortField : queryParameter.getSortField()) {
            String fieldName = sortField.getString("fieldName");
            String sortValue = sortField.getString("sortValue");
            if (StringUtils.isNotBlank(fieldName) && StringUtils.isNotBlank(sortValue)){
                fieldSortBuilder = SortBuilders.fieldSort(fieldName).order(sortValue.equals("DESC")?SortOrder.DESC:SortOrder.ASC);
            }
        }
        if (fieldSortBuilder!=null){
            searchSourceBuilder.sort(fieldSortBuilder);
        }
        //设置查询超时时间
        searchSourceBuilder.timeout(new TimeValue(queryParameter.getTimeOut(), TimeUnit.SECONDS));
        searchSourceBuilder.from(queryParameter.getPageNo()>0?(queryParameter.getPageNo()-1)*queryParameter.getPageSize():0);
        searchSourceBuilder.size(queryParameter.getPageSize());
        searchSourceBuilder.explain(true);
        return searchSourceBuilder;
    }

}