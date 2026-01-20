package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.dde.utils.DataSetOptUtil;
import com.centit.framework.common.ResponseData;
import com.centit.framework.core.dao.PageQueryResult;
import com.centit.product.metadata.po.SourceInfo;
import com.centit.product.metadata.service.SourceInfoMetadata;
import com.centit.product.metadata.transaction.AbstractSourceConnectThreadHolder;
import com.centit.search.document.FileDocument;
import com.centit.search.document.ObjectDocument;
import com.centit.search.service.ESServerConfig;
import com.centit.search.service.Impl.ESSearcher;
import com.centit.search.service.IndexerSearcherFactory;
import com.centit.search.service.Searcher;
import com.centit.support.algorithm.BooleanBaseOpt;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.database.utils.PageDesc;
import com.centit.support.json.JSONTransformer;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.*;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EsQueryBizOperation implements BizOperation {

    private final ESServerConfig esServerConfig;

    private  SourceInfoMetadata sourceInfoMetadata;

    public EsQueryBizOperation(ESServerConfig esServerConfig, SourceInfoMetadata sourceInfoMetadata) {
        this.esServerConfig = esServerConfig;
        this.sourceInfoMetadata = sourceInfoMetadata;
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext)throws Exception{
        BizModelJSONTransform transform = new BizModelJSONTransform(bizModel);

        int pageNo  = NumberBaseOpt.castObjectToInteger(DataSetOptUtil.fetchFieldValue(
            transform, bizOptJson.getString("pageNo")),1);
        int pageSize = NumberBaseOpt.castObjectToInteger(DataSetOptUtil.fetchFieldValue(
            transform, bizOptJson.getString("pageSize")),20);
        String indexType = bizOptJson.getString("indexType");
        if("custom".equals(indexType)){
            return customQueryOperation(bizModel, bizOptJson, transform, pageNo, pageSize, dataOptContext);
        } else {
            Map<String,Object> queryParam = new HashMap<>(6);
            queryParam.put("osId",dataOptContext.getOsId());
            if (!bizOptJson.getBoolean("queryAll")){
                Object optTag = DataSetOptUtil.fetchFieldValue(transform, bizOptJson.getString("optTag"));
                if (optTag != null ) queryParam.put("optTag", optTag);
                Object unitCode = DataSetOptUtil.fetchFieldValue(transform, bizOptJson.getString("unitCode"));
                if (unitCode != null ) queryParam.put("unitCode", unitCode);
                Object userCode = DataSetOptUtil.fetchFieldValue(transform, bizOptJson.getString("userCode"));
                if (userCode != null )  queryParam.put("userCode",userCode);
            }
            boolean indexFile = "indexFile".equals(indexType);
            ESSearcher esSearcher = indexFile ?
                IndexerSearcherFactory.obtainSearcher(esServerConfig, FileDocument.class) :
                IndexerSearcherFactory.obtainSearcher(esServerConfig, ObjectDocument.class);

            String keyword = StringBaseOpt.castObjectToString(DataSetOptUtil.fetchFieldValue(transform,
                bizOptJson.getString("queryParameter")));
            QueryBuilder queryBuilder = queryBuilder(queryParam, keyword, indexFile, esSearcher, bizOptJson, transform);

            Pair<Long, List<Map<String, Object>>> search = esSearcher.esSearch(queryBuilder,
                ESSearcher.mapSortBuilder(queryParam), null, null, pageNo, pageSize);

            PageDesc pageDesc = new PageDesc();
            pageDesc.setPageNo(pageNo);
            pageDesc.setPageSize(pageSize);
            pageDesc.setTotalRows(NumberBaseOpt.castObjectToInteger(search.getLeft()));
            PageQueryResult<Map<String, Object>> result = PageQueryResult.createResult(search.getRight(), pageDesc);
            DataSet dataSet = new DataSet(result);
            bizModel.putDataSet(bizOptJson.getString("id"), dataSet);
            return BuiltInOperation.createResponseSuccessData(dataSet.getSize());
        }
    }

    private QueryBuilder queryBuilder(Map<String,Object> queryParam, String keyword, Boolean indexFile,
                                      ESSearcher esSearcher, JSONObject bizOptJson, BizModelJSONTransform transform){

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        for (Map.Entry<String, Object> entry : queryParam.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (StringUtils.isNotBlank(key) && value != null) {
                boolQueryBuilder.must(QueryBuilders.termsQuery(key,value));
            }
        }

        esSearcher.initTypeFields(indexFile?FileDocument.class:ObjectDocument.class);
        if (StringUtils.isNotBlank(keyword)){
            QueryStringQueryBuilder stringQueryBuilder =
                QueryBuilders.queryStringQuery(keyword).fields(esSearcher.getQueryFields());
                //QueryBuilders.multiMatchQuery(keyword, esSearcher.getQueryFields());
            if (bizOptJson.getBoolean("custom")){
                String analyzer = StringBaseOpt.castObjectToString(DataSetOptUtil.fetchFieldValue(transform,
                    bizOptJson.getString("analyzer")));
                String minimumShouldMatch = StringBaseOpt.castObjectToString(
                    DataSetOptUtil.fetchFieldValue(transform, bizOptJson.getString("minimumShouldMatch")));
                if (StringUtils.isNotBlank(analyzer)) stringQueryBuilder.analyzer(analyzer);
                if (StringUtils.isNotBlank(minimumShouldMatch)) stringQueryBuilder.minimumShouldMatch(minimumShouldMatch);
            }
            boolQueryBuilder.must(stringQueryBuilder);
        }
        return boolQueryBuilder;
    }

    private ResponseData customQueryOperation(BizModel bizModel, JSONObject bizOptJson, BizModelJSONTransform transform,
                                              Integer pageNo, Integer pageSize, DataOptContext dataOptContext ) throws Exception {
        String databaseCode = BuiltInOperation.getJsonFieldString(bizOptJson, "databaseName", null);
        SourceInfo esInfo = sourceInfoMetadata.fetchSourceInfo(databaseCode);
        String indexName = BuiltInOperation.getJsonFieldString(bizOptJson, "indexName", null);
        if (StringUtils.isBlank(indexName)){
            return ResponseData.makeErrorMessage(ResponseData.ERROR_FIELD_INPUT_NOT_VALID,
                dataOptContext.getI18nMessage("error.701.field_is_blank", "indexName"));
        }

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //过滤条件 filterColumnName  filterValue
        JSONArray filterList = bizOptJson.getJSONArray("filterList");
        if (filterList != null){
            for (int i = 0; i < filterList.size(); i++) {
                JSONObject filterInfo = filterList.getJSONObject(i);
                Object filterValue = JSONTransformer.transformer(filterInfo.getString("filterValue"), transform);
                if (filterValue != null) {
                    String columnName = filterInfo.getString("filterColumnName");
                    makeFilterCondition(columnName, StringBaseOpt.castObjectToString(filterValue), boolQueryBuilder);
                }
            }
        }
        List<String> highLightFields = new ArrayList<>();
        //前端页面加个高级选项，选项里面加指定的查询列 查询列默认高亮显示 是个数组 queryColumnName
        JSONArray queryColumns = bizOptJson.getJSONArray("queryColumns");
        String[] queryColumnList = null;
        if (queryColumns != null){
            queryColumnList = new String[queryColumns.size()];
            for (int i = 0; i < queryColumns.size(); i++) {
                JSONObject fieldObj = queryColumns.getJSONObject(i);
                queryColumnList[i] =fieldObj.getString("queryColumnName");
                String isHighLight = fieldObj.getString("isHighLight");
                //先全部不返回，用于测试
                if(BooleanBaseOpt.castObjectToBoolean(isHighLight, true)) {
                    highLightFields.add(queryColumnList[i]);
                }
            }
        }

        String queryWord = StringBaseOpt.castObjectToString(
            JSONTransformer.transformer(bizOptJson.getString("queryParameter"), transform));
        if (StringUtils.isNotBlank(queryWord)){
            //添加查询关键字
            QueryStringQueryBuilder stringQueryBuilder = QueryBuilders.queryStringQuery(queryWord);
            if(queryColumnList != null){
                for(String s : queryColumnList){
                    stringQueryBuilder.field(s);
                }
            }
            //最小匹配度 百分比
            int minimumShouldMatch = bizOptJson.getIntValue("minimumShouldMath");
            if (minimumShouldMatch>0) stringQueryBuilder.minimumShouldMatch(minimumShouldMatch+"%");
            boolQueryBuilder.must(stringQueryBuilder);
        } else { // TODO 这个不懂
            MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
            boolQueryBuilder.must(matchAllQueryBuilder);
        }

        SearchRequest searchRequest = new SearchRequest(indexName);
        //封装分页  排序信息
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //排序字段 sortColumnName    sortValue
        JSONArray sortColumns = bizOptJson.getJSONArray("sortColumns");
        //设置排序字段
        List<SortBuilder<?>> sorts = new ArrayList<>();
        if (sortColumns != null){
            for (int i = 0; i < sortColumns.size(); i++) {
                JSONObject sortField = sortColumns.getJSONObject(i);
                String sortColumnName = sortField.getString("sortColumnName");
                String sortType = sortField.getString("sortValue");
                if(StringUtils.isNotBlank(sortType) && !StringUtils.equalsAnyIgnoreCase(sortType, "ASC", "DESC")) {
                    sortType = StringBaseOpt.castObjectToString(DataSetOptUtil.fetchFieldValue(transform, sortType));
                }
                //默认升序
                if (StringUtils.isNotBlank(sortColumnName) /*&& StringUtils.isNotBlank(sortType)*/) {
                    FieldSortBuilder order = SortBuilders.fieldSort(sortColumnName).order(
                        "DESC".equalsIgnoreCase(sortType) ? SortOrder.DESC : SortOrder.ASC);
                    sorts.add(order);
                }
            }
        }

        searchSourceBuilder.sort(sorts);
        searchSourceBuilder.from((pageNo - 1) * pageSize);
        searchSourceBuilder.size(pageSize);

        //设置查询超时时间 1分钟
        //searchSourceBuilder.timeout(new TimeValue(60*1000, TimeUnit.SECONDS));
        //查全部数据(如果不写或者写false当总记录数超过10000时会返回总数10000,配置为true就会返回真实条数)
        searchSourceBuilder.trackTotalHits(true);
        //explain 返回文档的评分解释
        searchSourceBuilder.explain(false);
        //设置高亮显示字段
        //高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        if (!highLightFields.isEmpty()) {
            //设置高亮字段
            for (String hlf : highLightFields) {
                highlightBuilder.field(hlf);
            }
            String color = StringBaseOpt.castObjectToString(bizOptJson.getString("color"),"red");
            highlightBuilder.preTags("<span style='color:"+color+"'>").postTags("</span>");
            highlightBuilder.highlighterType("unified");
            highlightBuilder.requireFieldMatch(true);
            //下面这两项,如果你要高亮如文字内容等有很多字的字段,必须配置,不然会导致高亮不全,文章内容缺失等
            //最大高亮分片数
            highlightBuilder.fragmentSize(Searcher.SEARCH_FRAGMENT_SIZE);
            //从第一个分片获取高亮片段
            highlightBuilder.numOfFragments(Searcher.SEARCH_FRAGMENT_NUM);
        }
        searchSourceBuilder.highlighter(highlightBuilder);
        searchSourceBuilder.query(boolQueryBuilder);
        searchRequest.source(searchSourceBuilder);

        RestHighLevelClient esClient = AbstractSourceConnectThreadHolder.fetchESClient(esInfo);
        SearchResponse searchResponse = esClient.search(searchRequest, RequestOptions.DEFAULT);
        JSONObject returnData = new JSONObject();
        if (searchSourceBuilder.highlighter() != null && !searchSourceBuilder.highlighter().fields().isEmpty()) {
            returnData.put("data", returnHighlightResult(searchResponse, true));
        } else {
            returnData.put("data", resultPart(searchResponse, true));
        }
        //分页设置
        returnData.put("pageInfo",pageInfo(indexName,pageNo,pageSize, boolQueryBuilder, esClient));
        String id = bizOptJson.getString("id");
        bizModel.putDataSet(id,new DataSet(returnData));
        return ResponseData.makeResponseData(returnData.getJSONArray("data").size());
    }

    /*
     * 返回高亮结果（需要高亮是返回）
     *
     * @param searchResponse
     * @return
     */
    private static JSONArray returnHighlightResult(SearchResponse searchResponse, Boolean explain) {
        JSONArray jsonArray = new JSONArray();
        for (SearchHit hit : searchResponse.getHits()) {
            JSONObject jsonObject = JSON.parseObject(hit.getSourceAsString());
            if (explain) jsonObject.put("explain_info", hit.getExplanation());
            //解析高亮字段
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            for (Map.Entry<String, HighlightField> ent : highlightFields.entrySet()) {
                HighlightField field = ent.getValue();
                if (field != null) {
                    Text[] fragments = field.fragments();
                    StringBuilder sb = new StringBuilder();
                    for (Text fragment : fragments) {
                        sb.append(fragment.string().trim());
                    }
                    //高亮标题覆盖原标题
                    jsonObject.put(ent.getKey(), sb.toString());
                }
            }
            jsonArray.add(jsonObject);
        }
        return jsonArray;
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
                resultArrays.add(jsonObject);
            }
        }
        return resultArrays;
    }

    private static JSONObject pageInfo(String indexName, int pageNo,int pageSize,QueryBuilder queryBuilder,
                                       RestHighLevelClient restHighLevelClient) throws IOException {
        CountRequest countRequest = new CountRequest(indexName);
        SearchSourceBuilder countSearchSourceBuilder = new SearchSourceBuilder();
        countSearchSourceBuilder.query(queryBuilder);
        countRequest.source(countSearchSourceBuilder);
        CountResponse countResponse = restHighLevelClient.count(countRequest, RequestOptions.DEFAULT);
        int totalCount = (int) countResponse.getCount();
        int pagesTotal = totalCount == 0 ? 0 : (totalCount % pageSize == 0 ? totalCount / pageSize : (totalCount / pageSize) + 1);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("pageNo", pageNo);
        jsonObject.put("pageSize", pageSize);
        jsonObject.put("totalCount", totalCount);
        jsonObject.put("pageTotal", pagesTotal);
        return jsonObject;
    }

    private void makeFilterCondition(String field, String filterValue,BoolQueryBuilder boolQueryBuilder){
        String fieldSuffix = "";
        if(field.endsWith("_gt") || field.endsWith("_ge") || field.endsWith("_lt") || field.endsWith("_le") ){
            field =  field.substring(0,field.length() - 4);
            fieldSuffix = field.substring(field.length() - 3).toLowerCase();
        }
        switch (fieldSuffix) {
            case "_gt":
                boolQueryBuilder.must(QueryBuilders.rangeQuery(field).gt(filterValue));
                break;
            case "_ge":
                boolQueryBuilder.must(QueryBuilders.rangeQuery(field).gte(filterValue));
                break;
            case "_lt":
                boolQueryBuilder.must(QueryBuilders.rangeQuery(field).lt(filterValue));
                break;
            case "_le":
                boolQueryBuilder.must(QueryBuilders.rangeQuery(field).lte(filterValue));
                break;
            default:
                if (filterValue == null || filterValue.equals("null")) {
                    // 处理字段为空的情况
                    boolQueryBuilder.mustNot(QueryBuilders.existsQuery(field));
                } else {
                    if (StringUtils.isNotEmpty(filterValue)) {
                        if (filterValue.contains(",")) {
                            TermsQueryBuilder termsQuery = QueryBuilders.termsQuery(field, filterValue.split(","));
                            boolQueryBuilder.must(termsQuery);
                        } else {
                            TermQueryBuilder termQuery = QueryBuilders.termQuery(field, filterValue);
                            boolQueryBuilder.must(termQuery);
                        }
                    }
                }
        }
    }
    //时间字段处理
}
