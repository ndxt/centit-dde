package com.centit.dde.query;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.entity.EsQueryVo;
import com.centit.dde.entity.FieldAttributeInfo;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.support.json.JSONTransformer;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
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
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 查询分类：
 *  1.简单查询：
 *      term   terms   match  ids   match_all   range   constant_score   match_phrase  match_phrase_prefix
 *      multi_match  query_string  simple_query_string  wildcard 等。
 *  2.组合查询：
 *      must  should  mustNot  filter
 *
 */
public class ElasticsearchReadUtils {
    private static final Logger logger = LoggerFactory.getLogger(ElasticsearchReadUtils.class);

    //执行查询  查询入口
    public static JSONObject executeQuery(RestHighLevelClient restHighLevelClient, BizModel bizModel, EsQueryVo esReadVo) throws Exception {
        List<FieldAttributeInfo> fieldAttributeInfos = esReadVo.getFieldAttributeInfos();
        if (fieldAttributeInfos == null || fieldAttributeInfos.size() == 0) {//查询全部
            return ElasticsearchReadUtils.returnBuilde(esReadVo, QueryBuilders.matchAllQuery(), restHighLevelClient);
        }
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        for (FieldAttributeInfo fieldAttributeInfo : fieldAttributeInfos) {
            String queryType = fieldAttributeInfo.getQueryType();
            switch (fieldAttributeInfo.getOperator()) {
                case EsQueryType.MUST:
                    boolQueryBuilder.must(queryBuilder(queryType, fieldAttributeInfo, bizModel));
                    break;
                case EsQueryType.SHOULD:
                    boolQueryBuilder.should(queryBuilder(queryType, fieldAttributeInfo, bizModel));
                    break;
                case EsQueryType.MUST_NOT:
                    boolQueryBuilder.mustNot(queryBuilder(queryType, fieldAttributeInfo, bizModel));
                    break;
                case EsQueryType.FILTER:
                    boolQueryBuilder.filter(queryBuilder(queryType, fieldAttributeInfo, bizModel));
                    break;
            }
        }
        return returnBuilde(esReadVo, boolQueryBuilder, restHighLevelClient);
    }

    private static QueryBuilder queryBuilder(String queryType, FieldAttributeInfo fieldAttributeInfo, BizModel bizModel) {
        String fieldName = fieldAttributeInfo.getFieldName();
        Float boost = fieldAttributeInfo.getBoost();
        String minimumShouldMatch = fieldAttributeInfo.getMinimumShouldMatch();
        String matchOperator = fieldAttributeInfo.getMatchOperator();
        String analyze = fieldAttributeInfo.getAnalyze();
        Integer slop = fieldAttributeInfo.getSlop();
        Object transformer = JSONTransformer.transformer(fieldAttributeInfo.getExpression(), new BizModelJSONTransform(bizModel));
        switch (queryType) {
            case EsQueryType.IDS://条件可传数组 ;可传字符串，多值用逗号隔开
                IdsQueryBuilder idsQueryBuilder = QueryBuilders.idsQuery();
                String[] ids = null;
                if (transformer instanceof String) {
                    String params = transformer.toString();
                    ids = params.contains(",") ? params.split(",") : new String[]{params};
                }
                if (transformer instanceof Collection) {
                    List<String> list = JSON.parseArray(JSON.toJSONString(transformer), String.class);
                    ids = list.toArray(new String[list.size()]);
                }
                idsQueryBuilder.addIds(ids);
                if (boost != null && boost > 0) {
                    idsQueryBuilder.boost(boost);
                }
                return idsQueryBuilder;
            case EsQueryType.TERM:
                TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery(fieldName, transformer);
                if (boost != null && boost > 0) {
                    termQueryBuilder.boost(boost);
                }
                return termQueryBuilder;
            case EsQueryType.TERMS://条件可传数组 ;可传字符串，多值用逗号隔开
                String[] values = null;
                if (transformer instanceof String) {
                    String params = transformer.toString();
                    values = params.contains(",") ? params.split(",") : new String[]{params};
                }
                if (transformer instanceof Collection) {
                    List<String> list = JSON.parseArray(JSON.toJSONString(transformer), String.class);
                    values = list.toArray(new String[list.size()]);
                }
                TermsQueryBuilder termsQueryBuilder = QueryBuilders.termsQuery(fieldName, values);
                if (boost != null && boost > 0) {
                    termsQueryBuilder.boost(boost);
                }
                return termsQueryBuilder;
            case EsQueryType.MATCH:
                MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery(fieldName, transformer);
                if (StringUtils.isNotBlank(minimumShouldMatch)) {
                    matchQueryBuilder.minimumShouldMatch(minimumShouldMatch);
                }
                if (StringUtils.isNotBlank(matchOperator)) {
                    matchQueryBuilder.operator(matchOperator.equals("AND") ? Operator.AND : Operator.OR);
                }
                if (StringUtils.isNotBlank(analyze)) {
                    matchQueryBuilder.analyzer(analyze);
                }
                if (boost != null && boost > 0) {
                    matchQueryBuilder.boost(boost);
                }
                return matchQueryBuilder;
            case EsQueryType.MATCH_PHRASE:
                MatchPhraseQueryBuilder matchPhraseQueryBuilder = QueryBuilders.matchPhraseQuery(fieldName, transformer);
                if (StringUtils.isNotBlank(analyze)) {
                    matchPhraseQueryBuilder.analyzer(analyze);
                }
                if (slop != null && slop > 0) {
                    matchPhraseQueryBuilder.slop(slop);

                }
                if (boost != null && boost > 0) {
                    matchPhraseQueryBuilder.boost(boost);
                }
                return matchPhraseQueryBuilder;
            case EsQueryType.MATCH_PHRASE_PREFIX:
                return QueryBuilders.matchPhrasePrefixQuery(fieldName, transformer);
            case EsQueryType.EXISTS:
                return QueryBuilders.existsQuery(fieldName);
            case EsQueryType.SQL:
                break;
            case EsQueryType.QUERY_STRING:
                return QueryBuilders.queryStringQuery((String) transformer);
            case EsQueryType.SIMPLE_QUERY_STRING:
                return QueryBuilders.simpleQueryStringQuery((String) transformer);
            case EsQueryType.CONSTANT_SCORE:
                return QueryBuilders.constantScoreQuery(QueryBuilders.matchQuery(fieldName, transformer));
            case EsQueryType.WILDCARD:
                return QueryBuilders.wildcardQuery(fieldName, (String) transformer);
            case EsQueryType.FUZZY:
                return QueryBuilders.fuzzyQuery(fieldName, transformer);
            case EsQueryType.RANGE:
                return null;
            case EsQueryType.MULTI_MATCH://默认
                return QueryBuilders.multiMatchQuery(transformer, fieldName);
        }
        return QueryBuilders.matchAllQuery();
    }

    /**
     * 返回普通结果（不需要高亮显示时返回）
     *
     * @param searchResponse
     * @return
     */
    private static JSONArray resultPart(SearchResponse searchResponse, Boolean explain) {
        JSONArray resultArrays = new JSONArray();
        // 根据状态和数据条数验证是否返回了数据
        if (RestStatus.OK.equals(searchResponse.status()) && searchResponse.getHits().getTotalHits().value > 0) {
            for (SearchHit hit : searchResponse.getHits()) {
                // 打分
                //float score = hit.getScore();
                JSONObject jsonObject = JSONObject.parseObject(hit.getSourceAsString());
                if (explain) {
                    jsonObject.put("explain_info", hit.getExplanation());
                }
                // 输出查询信息
                logger.debug("获取es数据:" + jsonObject.toJSONString());
                resultArrays.add(jsonObject);
            }
        }
        return resultArrays;
    }

    /**
     * 返回高亮结果（需要高亮是返回）
     *
     * @param searchResponse
     * @param fieldAttributeInfos
     * @return
     */
    private static JSONArray returnHighlightResult(SearchResponse searchResponse, List<FieldAttributeInfo> fieldAttributeInfos, Boolean explain) {
        List<String> fieldNames = new ArrayList<>();
        for (FieldAttributeInfo fieldAttributeInfo : fieldAttributeInfos) {
            if (fieldAttributeInfo.getIsHighligh()) {
                fieldNames.add(fieldAttributeInfo.getFieldName());
            }
        }
        JSONArray jsonArray = new JSONArray();
        for (SearchHit hit : searchResponse.getHits()) {
            JSONObject jsonObject = JSON.parseObject(hit.getSourceAsString());
            if (explain) {
                jsonObject.put("explain_info", hit.getExplanation());
            }
            //解析高亮字段
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            for (String fieldName : fieldNames) {
                HighlightField field = highlightFields.get(fieldName);
                if (field != null) {
                    Text[] fragments = field.fragments();
                    String n_field = "";
                    for (Text fragment : fragments) {
                        n_field += fragment;
                    }
                    //高亮标题覆盖原标题
                    jsonObject.put(fieldName, n_field);
                }
            }
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    /**
     * 高亮查询字段封装
     *
     * @param fieldAttributeInfos
     * @param searchSourceBuilder
     * @return
     */
    private static void queryHighlightBuilder(List<FieldAttributeInfo> fieldAttributeInfos, SearchSourceBuilder searchSourceBuilder) {
        //高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        if (fieldAttributeInfos.size() > 0) {
            //设置高亮字段
            for (FieldAttributeInfo fieldAttributeInfo : fieldAttributeInfos) {
                if (fieldAttributeInfo.getIsHighligh()) {
                    highlightBuilder.field(fieldAttributeInfo.getFieldName());
                }
            }
        }
        if (highlightBuilder.fields().size() > 0) {
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
     *
     * @param queryParameter
     * @return
     */
    private static SearchSourceBuilder publicSearchSourceBuilde(EsQueryVo queryParameter) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //设置查询返回字段和不返回的字段
        searchSourceBuilder.fetchSource(queryParameter.getReturnField(), null);
        //设置排序字段
        FieldSortBuilder fieldSortBuilder = null;
        List<JSONObject> sortFields = queryParameter.getSortField();
        for (JSONObject sortField : sortFields) {
            String fieldName = sortField.getString("fieldName");
            String sortValue = sortField.getString("sortValue");
            if (StringUtils.isNotBlank(fieldName) && StringUtils.isNotBlank(sortValue)) {
                fieldSortBuilder = SortBuilders.fieldSort(fieldName).order(sortValue.equals("DESC") ? SortOrder.DESC : SortOrder.ASC);
            }
        }
        if (fieldSortBuilder != null) {
            searchSourceBuilder.sort(fieldSortBuilder);
        }
        if (queryParameter.getPageSize() > 0 && queryParameter.getPageNo() > 0) {
            searchSourceBuilder.from((queryParameter.getPageNo() - 1) * queryParameter.getPageSize());
            searchSourceBuilder.size(queryParameter.getPageSize());
        }
        //设置查询超时时间
        searchSourceBuilder.timeout(new TimeValue(queryParameter.getTimeout(), TimeUnit.SECONDS));
        //查全部数据(如果不写或者写false当总记录数超过10000时会返回总数10000,配置为true就会返回真实条数)
        searchSourceBuilder.trackTotalHits(true);
        //explain 返回文档的评分解释
        if (queryParameter.getExplain()) {
            searchSourceBuilder.explain(queryParameter.getExplain());
        }
        return searchSourceBuilder;
    }


    private static JSONObject pageInfo(EsQueryVo queryParameter, QueryBuilder queryBuilder, RestHighLevelClient restHighLevelClient) throws IOException {
        CountRequest countRequest = new CountRequest(queryParameter.getQueryIndex());
        SearchSourceBuilder countSearchSourceBuilder = new SearchSourceBuilder();
        countSearchSourceBuilder.query(queryBuilder);
        countRequest.source(countSearchSourceBuilder);
        CountResponse countResponse = restHighLevelClient.count(countRequest, RequestOptions.DEFAULT);
        int totalCount = (int) countResponse.getCount();
        int pagesTotal = totalCount == 0 ? 0 : (totalCount % queryParameter.getPageSize() == 0 ? totalCount / queryParameter.getPageSize() : (totalCount / queryParameter.getPageSize()) + 1);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("pageNo", queryParameter.getPageNo());
        jsonObject.put("pageSize", queryParameter.getPageSize());
        jsonObject.put("totalCount", totalCount);
        jsonObject.put("pageTotal", pagesTotal);
        return jsonObject;
    }

    /**
     * 返回结果结果封装
     *
     * @return
     */
    private static JSONObject returnBuilde(EsQueryVo esReadVo, QueryBuilder queryBuilder, RestHighLevelClient restHighLevelClient) throws IOException {
        SearchRequest searchRequest = new SearchRequest(esReadVo.getQueryIndex(), new SearchSourceBuilder());
        //封装分页  排序信息
        SearchSourceBuilder searchSourceBuilder = publicSearchSourceBuilde(esReadVo);
        //设置高亮显示字段
        queryHighlightBuilder(esReadVo.getFieldAttributeInfos(), searchSourceBuilder);
        searchSourceBuilder.query(queryBuilder);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        JSONObject returnData = new JSONObject();
        if (searchSourceBuilder.highlighter() != null && searchSourceBuilder.highlighter().fields().size() > 0) {
            returnData.put("data", returnHighlightResult(searchResponse, esReadVo.getFieldAttributeInfos(), esReadVo.getExplain()));
        } else {
            returnData.put("data", resultPart(searchResponse, esReadVo.getExplain()));
        }
        //分页设置
        if (esReadVo.getPageSize() > 0 && esReadVo.getPageNo() > 0) {
            returnData.put("pageInfo", ElasticsearchReadUtils.pageInfo(esReadVo, queryBuilder, restHighLevelClient));
        }
        return returnData;
    }


    public List<T> queryBySQL(String sql) throws Exception {
       // String s = queryBySQL(sql, SqlFormat.JSON);
        SqlResponse sqlResponse = JsonUtils.string2Obj("", SqlResponse.class);
        List<T> result = new ArrayList<>();
        if (sqlResponse != null && !CollectionUtils.isEmpty(sqlResponse.getRows())) {
            for (List<String> row : sqlResponse.getRows()) {
                System.out.println(JSON.toJSONString(row));
            }
        }
        return result;
    }
}
