package com.centit.dde.query.impl;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.entity.EsReadVo;
import com.centit.dde.entity.FieldAttributeInfo;
import com.centit.dde.query.EsQuery;
import com.centit.dde.query.utils.ElasticsearchReadUtils;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.io.IOException;
import java.util.List;

public class EsBooleanQueryImpl implements EsQuery {
    @Override
    public JSONObject query(RestHighLevelClient restHighLevelClient, EsReadVo esReadVo) throws IOException {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        List<FieldAttributeInfo> fieldAttributeInfos = esReadVo.getFieldAttributeInfos();
        for (FieldAttributeInfo fieldAttributeInfo : fieldAttributeInfos) {
            QueryBuilder queryBuilder = ElasticsearchReadUtils.queryBuilder(fieldAttributeInfo,fieldAttributeInfo.getCombQueryType());
            switch (fieldAttributeInfo.getCombinationType()){
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
        return ElasticsearchReadUtils.returnBuilde(esReadVo,boolQueryBuilder,restHighLevelClient);
    }
}
