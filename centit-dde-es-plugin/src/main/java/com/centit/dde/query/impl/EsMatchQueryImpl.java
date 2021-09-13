package com.centit.dde.query.impl;

import com.alibaba.fastjson.JSONArray;
import com.centit.dde.entity.EsReadVo;
import com.centit.dde.query.EsQuery;
import com.centit.dde.query.utils.ElasticsearchReadUtils;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;

import java.io.IOException;

public class EsMatchQueryImpl implements EsQuery {
    @Override
    public JSONArray query(RestHighLevelClient restHighLevelClient, EsReadVo esReadVo) throws IOException {
        String fieldName = esReadVo.getFieldAttributeInfos().get(0).getFieldName();
        Object value = esReadVo.getFieldAttributeInfos().get(0).getValue();
        Float boots = esReadVo.getFieldAttributeInfos().get(0).getBoots();
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery(fieldName, value);
        if (boots>0)termQueryBuilder.boost(boots);
        return ElasticsearchReadUtils.returnBuilde(esReadVo,termQueryBuilder,restHighLevelClient);
    }
}
