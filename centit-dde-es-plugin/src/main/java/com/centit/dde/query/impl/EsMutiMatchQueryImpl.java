package com.centit.dde.query.impl;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.entity.EsReadVo;
import com.centit.dde.query.EsQuery;
import com.centit.dde.query.utils.ElasticsearchReadUtils;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;

import java.io.IOException;

/**
 * 一词多字段查询
 * muti_match标识了只要有一个字段里有匹配短语的词就返回
 */
public class EsMutiMatchQueryImpl implements EsQuery {
    @Override
    public JSONObject query(RestHighLevelClient restHighLevelClient, EsReadVo esReadVo) throws IOException {
        QueryBuilder queryBuilder = ElasticsearchReadUtils.queryBuilder(esReadVo.getFieldAttributeInfos().get(0),esReadVo.getQueryType());
        return ElasticsearchReadUtils.returnBuilde(esReadVo,queryBuilder,restHighLevelClient);
    }
}
