package com.centit.dde.query;

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
     * 查询分类：
     *  1.简单查询：
     *      term   terms   match  ids   match_all   range   constant_score   match_phrase  match_phrase_prefix
     *      multi_match  query_string  simple_query_string  wildcard 等。
     *  2.组合查询：
     *      must  should  mustNot  filter
     *
     */

    //查询入口
    public static JSONObject query(RestHighLevelClient restHighLevelClient, EsReadVo esReadVo) throws IOException {
        //bool查询类型
        String boolQueryType =  esReadVo.getQueryParameter().getBoolQueryType();
        List<FieldAttributeInfo> fieldAttributeInfos = esReadVo.getFieldAttributeInfos();
        QueryBuilder queryBuilder =null;
        BoolQueryBuilder boolQueryBuilder=null;
        if (StringUtils.isNotBlank(boolQueryType)){
            boolQueryBuilder = QueryBuilders.boolQuery();
            for (FieldAttributeInfo fieldAttributeInfo : fieldAttributeInfos) {
                queryBuilder = ElasticsearchReadUtils.queryBuilder(fieldAttributeInfo);
                switch (boolQueryType){
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
                }
            }
        }else {
            queryBuilder = ElasticsearchReadUtils.queryBuilder(fieldAttributeInfos.get(0));
        }
        return ElasticsearchReadUtils.returnBuilde(esReadVo,boolQueryBuilder==null?queryBuilder:boolQueryBuilder,restHighLevelClient);
    }

    private static QueryBuilder queryBuilder(FieldAttributeInfo fieldAttributeInfo){
        //简单的查询类型
        String simpleQueryType = fieldAttributeInfo.getSimpleQueryType();
        String fieldName = fieldAttributeInfo.getFieldName();
        Float boots = fieldAttributeInfo.getBoots();
        String value = fieldAttributeInfo.getValue();
        String analyzer = fieldAttributeInfo.getAnalyzer();
        switch (simpleQueryType){
            case EsQueryType.MATCH_ALL:
                return QueryBuilders.matchAllQuery();
            case EsQueryType.TERM:
                TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery(fieldName, value);
                termQueryBuilder.boost(boots);
                return termQueryBuilder;
            case EsQueryType.TERMS:
                String[] split = value.split(",");
                TermsQueryBuilder termsQueryBuilder = QueryBuilders.termsQuery(fieldName,split);
                termsQueryBuilder.boost(boots);
                return termsQueryBuilder;
            case EsQueryType.IDS:
                IdsQueryBuilder idsQueryBuilder = QueryBuilders.idsQuery();
                String[] idArr = value.split(",");
                idsQueryBuilder.addIds(idArr);
                idsQueryBuilder.boost(boots);
                return idsQueryBuilder;
            case EsQueryType.WILDCARD:
                WildcardQueryBuilder wildcardQueryBuilder = QueryBuilders.wildcardQuery(fieldName,value);
                wildcardQueryBuilder.boost(boots);
                return wildcardQueryBuilder;
            case EsQueryType.RANGE: //rang 查询待完善   目前只是简单实现
                String[] rangeValues = value.split(",");
                RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(fieldName);
                for (String data : rangeValues) {
                    //value=10_gte 表示 value >=10
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
            case EsQueryType.MATCH:
                MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery(fieldName, value);
                matchQueryBuilder.operator("AND".equals(fieldAttributeInfo.getOperator().toUpperCase())?Operator.AND: Operator.OR);
                if (StringUtils.isNotBlank(fieldAttributeInfo.getMinimumShouldMatch())){
                    matchQueryBuilder.minimumShouldMatch(fieldAttributeInfo.getMinimumShouldMatch());
                }
                matchQueryBuilder.analyzer(analyzer);
                matchQueryBuilder.boost(boots);
                return matchQueryBuilder;
            case EsQueryType.CONSTANT_SCORE:
                return null;
            case EsQueryType.MATCH_PHRASE:
                MatchPhraseQueryBuilder matchPhraseQueryBuilder = QueryBuilders.matchPhraseQuery(fieldName, value);
                matchPhraseQueryBuilder.analyzer(analyzer);
                matchPhraseQueryBuilder.boost(boots);
                matchPhraseQueryBuilder.slop(fieldAttributeInfo.getSlop());
                return  matchPhraseQueryBuilder;
            case EsQueryType.MATCH_PHRASE_PREFIX:
                MatchPhrasePrefixQueryBuilder matchPhrasePrefixQueryBuilder = QueryBuilders.matchPhrasePrefixQuery(fieldName, value);
                matchPhrasePrefixQueryBuilder.analyzer(analyzer);
                matchPhrasePrefixQueryBuilder.maxExpansions(fieldAttributeInfo.getMaxExpansions());
                matchPhrasePrefixQueryBuilder.slop(fieldAttributeInfo.getSlop());
                matchPhrasePrefixQueryBuilder.boost(boots);
                return matchPhrasePrefixQueryBuilder;
            case EsQueryType.MULTI_MATCH:
                String[] fieldNames = fieldName.split(",");
                MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(value, fieldNames);
                multiMatchQueryBuilder.analyzer(analyzer);
                if (StringUtils.isNotBlank(fieldAttributeInfo.getMinimumShouldMatch())){
                    multiMatchQueryBuilder.minimumShouldMatch(fieldAttributeInfo.getMinimumShouldMatch());
                }
                multiMatchQueryBuilder.operator("AND".equals(fieldAttributeInfo.getOperator().toUpperCase())?Operator.AND: Operator.OR);
                multiMatchQueryBuilder.slop(fieldAttributeInfo.getSlop());
                return multiMatchQueryBuilder;
            case EsQueryType.QUERYSTRING:
                return null;
            default:
                return null;
        }
    }

    /**
     * 返回普通结果（不需要高亮显示时返回）
     * @param searchResponse
     * @return
     */
    private static JSONArray resultPart(SearchResponse searchResponse,Boolean explain){
        JSONArray resultArrays= new JSONArray();
        // 根据状态和数据条数验证是否返回了数据
        if (RestStatus.OK.equals(searchResponse.status()) && searchResponse.getHits().getTotalHits().value > 0) {
            for (SearchHit hit : searchResponse.getHits()) {
                // 打分
                //float score = hit.getScore();
                JSONObject jsonObject = JSONObject.parseObject(hit.getSourceAsString());
                if (explain){
                    jsonObject.put("explain_info",hit.getExplanation());
                }
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
    private static  JSONArray returnHighlightResult(SearchResponse searchResponse, List<FieldAttributeInfo> fieldAttributeInfos,Boolean explain){
        List<String> fieldNames = new ArrayList<>();
        for (FieldAttributeInfo fieldAttributeInfo : fieldAttributeInfos) {
            if (fieldAttributeInfo.getIsHighligh()){
                fieldNames.add(fieldAttributeInfo.getFieldName());
            }
        }
        JSONArray jsonArray = new JSONArray();
        for (SearchHit hit : searchResponse.getHits()) {
            JSONObject jsonObject = JSON.parseObject(hit.getSourceAsString());
            if (explain){
                jsonObject.put("explain_info",hit.getExplanation());
            }
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
            /**
             * highlighterType可选:unified,plain和fvh
             * unified : 使用Lucene的统一highlighter。
             * 这个突出显示器将文本分成句子，并使用BM25算法对单个句子进行评分，
             * 就好像它们是语料库中的文档一样。它还支持准确的短语和多项（模糊，前缀，正则表达式）突出显示
             *
             *plain highlighter最适合在单一领域突出简单的查询匹配。
             * 为了准确反映查询逻辑，它会创建一个微小的内存索引，
             * 并通过Lucene的查询执行计划程序重新运行原始查询条件，
             * 以访问当前文档的低级匹配信息。对于需要突出显示的每个字段和每个文档都会
             * 重复此操作。如果要在复杂查询的大量文档中突出显示很多字段，
             * 我们建议使用unified highlighter postings或term_vector字段
             *
             *fvh highlighter使用Lucene的Fast Vector highlighter。此突出显示器可用于映射中term_vector设置为的
             * 字段with_positions_offsets。Fast Vector highlighter
             */
            highlightBuilder.highlighterType("unified");
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
        List<JSONObject> sortFields = queryParameter.getSortField();
        for (JSONObject sortField : sortFields) {
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
        //查全部数据(如果不写或者写false当总记录数超过10000时会返回总数10000,配置为true就会返回真实条数)
        searchSourceBuilder.trackTotalHits(true);
        //explain 返回文档的评分解释
        searchSourceBuilder.explain(queryParameter.getExplain());
        return searchSourceBuilder;
    }


    private static JSONObject pageInfo(QueryParameter queryParameter,QueryBuilder queryBuilder,RestHighLevelClient restHighLevelClient) throws IOException {
        CountRequest countRequest = new CountRequest(queryParameter.getIndexName());
        SearchSourceBuilder countSearchSourceBuilder = new SearchSourceBuilder();
        countSearchSourceBuilder.query(queryBuilder);
        countRequest.source(countSearchSourceBuilder);
        CountResponse countResponse = restHighLevelClient.count(countRequest, RequestOptions.DEFAULT);
        int totalCount =  (int)countResponse.getCount();
        int pagesTotal= totalCount== 0 ? 0: (totalCount%queryParameter.getPageSize() == 0 ? totalCount / queryParameter.getPageSize() : (totalCount / queryParameter.getPageSize()) + 1);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("pageNo",queryParameter.getPageNo());
        jsonObject.put("pageSize",queryParameter.getPageSize());
        jsonObject.put("totalCount",totalCount);
        jsonObject.put("pageTotal",pagesTotal);
        return jsonObject;
    }

    /**
     * 返回结果结果封装
     * @return
     */
    private  static  JSONObject returnBuilde(EsReadVo esReadVo,QueryBuilder queryBuilder,RestHighLevelClient restHighLevelClient) throws IOException {
        SearchRequest searchRequest = new SearchRequest(esReadVo.getQueryParameter().getIndexName());
        QueryParameter queryParameter = esReadVo.getQueryParameter();
        //封装分页  排序信息
        SearchSourceBuilder searchSourceBuilder = publicSearchSourceBuilde(queryParameter);
        //设置高亮显示字段
        queryHighlightBuilder(esReadVo.getFieldAttributeInfos(),searchSourceBuilder);
        searchSourceBuilder.query(queryBuilder);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        JSONObject returnData =new JSONObject();
        if (searchSourceBuilder.highlighter()!=null && searchSourceBuilder.highlighter().fields().size()>0){
            returnData.put("data",returnHighlightResult(searchResponse, esReadVo.getFieldAttributeInfos(),queryParameter.getExplain()));
        }else {
            returnData.put("data",resultPart(searchResponse,queryParameter.getExplain()));
        }
        //分页设置
        if (queryParameter.getIsReturnPageInfo()){
            returnData.put("pageInfo",ElasticsearchReadUtils.pageInfo(queryParameter,queryBuilder,restHighLevelClient));
        }
        return returnData;
    }

}
