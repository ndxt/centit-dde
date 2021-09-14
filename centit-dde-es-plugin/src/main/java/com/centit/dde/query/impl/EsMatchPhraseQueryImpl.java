package com.centit.dde.query.impl;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.entity.EsReadVo;
import com.centit.dde.query.EsQuery;
import com.centit.dde.query.utils.ElasticsearchReadUtils;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;

import java.io.IOException;

/**
 * 短语查询
 *  match_phrase属于短语匹配，能保证分词间的邻近关系，相当于对文档的关键词进行重组以匹配查询内容，对于匹配了短语"森 小 林"的文档，下面的条件必须为true：
 *       森 、小、 林必须全部出现在某个字段中
 *      小的位置必须比森的位置大1
 *      林的位置必须比森的位置大2
 */
public class EsMatchPhraseQueryImpl implements EsQuery {
    @Override
    public JSONObject query(RestHighLevelClient restHighLevelClient, EsReadVo esReadVo) throws IOException {
        QueryBuilder queryBuilder = ElasticsearchReadUtils.queryBuilder(esReadVo.getFieldAttributeInfos().get(0),esReadVo.getQueryType());
        return ElasticsearchReadUtils.returnBuilde(esReadVo,queryBuilder,restHighLevelClient);
    }
}
