package com.centit.dde.query.impl;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.entity.EsReadVo;
import com.centit.dde.query.EsQuery;
import com.centit.dde.query.utils.ElasticsearchReadUtils;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;

import java.io.IOException;

/**
 * 多词精确查询， 只要满足条件中的某个词就会返回相应的文档
 */
public class EsTermsQueryImpl implements EsQuery {
    @Override
    public JSONObject query(RestHighLevelClient restHighLevelClient, EsReadVo esReadVo) throws IOException {
        QueryBuilder queryBuilder = ElasticsearchReadUtils.queryBuilder(esReadVo.getFieldAttributeInfos().get(0),esReadVo.getQueryType());
        return ElasticsearchReadUtils.returnBuilde(esReadVo,queryBuilder,restHighLevelClient);
    }
}
