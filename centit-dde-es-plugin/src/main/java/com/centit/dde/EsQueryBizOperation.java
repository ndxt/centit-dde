package com.centit.dde;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.bizopt.BuiltInOperation;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.framework.common.ResponseData;
import com.centit.framework.core.dao.PageQueryResult;
import com.centit.search.document.FileDocument;
import com.centit.search.document.ObjectDocument;
import com.centit.search.service.ESServerConfig;
import com.centit.search.service.Impl.ESSearcher;
import com.centit.search.service.IndexerSearcherFactory;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.database.utils.PageDesc;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EsQueryBizOperation implements BizOperation {
    private final ESServerConfig esServerConfig;

    public EsQueryBizOperation(ESServerConfig esServerConfig) {
        this.esServerConfig = esServerConfig;
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext){
        Map<String,Object> queryParam = new HashMap<>(6);
        queryParam.put("osId",dataOptContext.getOsId());
        BizModelJSONTransform transform = new BizModelJSONTransform(bizModel);
        if (!bizOptJson.getBoolean("queryAll")){
            Object optTag = transform.attainExpressionValue(bizOptJson.getString("optTag"));
            Object unitCode = transform.attainExpressionValue(bizOptJson.getString("unitCode"));
            Object userCode = transform.attainExpressionValue(bizOptJson.getString("userCode"));
            if (optTag != null ) queryParam.put("optTag", optTag);
            if (unitCode != null ) queryParam.put("unitCode", unitCode);
            if (userCode != null ) queryParam.put("userCode",userCode);
        }

        int pageNo  = NumberBaseOpt.castObjectToInteger(transform.attainExpressionValue(bizOptJson.getString("pageNo")),1);
        int pageSize = NumberBaseOpt.castObjectToInteger(transform.attainExpressionValue(bizOptJson.getString("pageSize")),20);

        boolean indexFile = "indexFile".equals(bizOptJson.get("indexType"));
        ESSearcher esSearcher = indexFile ? IndexerSearcherFactory.obtainSearcher(esServerConfig, FileDocument.class)
            :IndexerSearcherFactory.obtainSearcher(esServerConfig, ObjectDocument.class);

        String keyword = StringBaseOpt.castObjectToString(transform.attainExpressionValue(bizOptJson.getString("queryParameter")));
        if(StringUtils.isBlank(keyword)) {
            return ResponseData.makeErrorMessage("查询关键字不能为空！");
        }
        //Pair<Long, List<Map<String, Object>>> search = esSearcher.search(queryParam, keyword, pageNo,pageSize);
        QueryBuilder queryBuilder = queryBuilder(queryParam, keyword, indexFile, esSearcher, bizOptJson, transform);
        String[] excludes = null;
        if (!bizOptJson.getBoolean("returnAllField")){
            //排除这个大字段，这个字段结果太长时会报错或者超时
            excludes = new String[]{"content"};
        }
        Pair<Long, List<Map<String, Object>>> search = esSearcher.esSearch(queryBuilder,null,excludes,pageNo,pageSize);
        PageDesc pageDesc = new PageDesc();
        pageDesc.setPageNo(pageNo);
        pageDesc.setPageSize(pageSize);
        pageDesc.setTotalRows(NumberBaseOpt.castObjectToInteger(search.getLeft()));
        PageQueryResult<Map<String, Object>> result = PageQueryResult.createResult(search.getRight(), pageDesc);
        DataSet dataSet = new DataSet(result);
        bizModel.putDataSet(bizOptJson.getString("id"),dataSet);
        return BuiltInOperation.createResponseSuccessData(dataSet.getSize());
    }

    private QueryBuilder queryBuilder(Map<String,Object> queryParam, String keyword,Boolean indexFile,
                                      ESSearcher esSearcher,JSONObject bizOptJson,BizModelJSONTransform transform){
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (queryParam != null){
            for (Map.Entry<String, Object> entry : queryParam.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (StringUtils.isBlank(key) || value == null) continue;
                if (value.getClass().isArray()) {
                    boolQueryBuilder.must(QueryBuilders.termsQuery(key,(String[])value));
                } else if (value instanceof Collection) {
                    boolQueryBuilder.must(QueryBuilders.termsQuery(key,CollectionsOpt.listToArray((Collection)value)));
                } else {
                    boolQueryBuilder.must(QueryBuilders.termQuery(key, value));
                }
            }
        }
        esSearcher.initTypeFields(indexFile?FileDocument.class:ObjectDocument.class);
        if (StringUtils.isNotBlank(keyword)){
            MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(keyword, esSearcher.getQueryFields());
            Boolean custom = bizOptJson.getBoolean("custom");
            if (custom){
                String analyzer = StringBaseOpt.castObjectToString(transform.attainExpressionValue(bizOptJson.getString("analyzer")));
                String minimumShouldMatch = StringBaseOpt.castObjectToString(
                    transform.attainExpressionValue(bizOptJson.getString("minimumShouldMatch")));
                if (StringUtils.isNotBlank(analyzer)){
                    multiMatchQueryBuilder.analyzer(analyzer);
                }
                if (StringUtils.isNotBlank(minimumShouldMatch)){
                    multiMatchQueryBuilder.minimumShouldMatch(minimumShouldMatch);
                }
            }
            boolQueryBuilder.must(multiMatchQueryBuilder);
        }
        return boolQueryBuilder;
    }
}
