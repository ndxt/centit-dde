package com.centit.dde.query.impl;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.entity.EsReadVo;
import com.centit.dde.query.EsQuery;
import com.centit.dde.query.utils.ElasticsearchReadUtils;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;

import java.io.IOException;

/**
 * 范围查询
 */
public class EsRangeQueryImpl implements EsQuery {
    @Override
    public JSONObject query(RestHighLevelClient restHighLevelClient, EsReadVo esReadVo) throws IOException {
        QueryBuilder queryBuilder = ElasticsearchReadUtils.queryBuilder(esReadVo.getFieldAttributeInfos().get(0),esReadVo.getQueryType());
        return ElasticsearchReadUtils.returnBuilde(esReadVo,queryBuilder,restHighLevelClient);
    }
}
