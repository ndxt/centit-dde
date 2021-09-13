package com.centit.dde.query.impl;

import com.alibaba.fastjson.JSONArray;
import com.centit.dde.entity.EsReadVo;
import com.centit.dde.entity.FieldAttributeInfo;
import com.centit.dde.query.EsQuery;
import com.centit.dde.query.utils.ElasticsearchReadUtils;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermsQueryBuilder;

import java.io.IOException;

public class EsTermsQueryImpl implements EsQuery {
    @Override
    public JSONArray query(RestHighLevelClient restHighLevelClient, EsReadVo esReadVo) throws IOException {
        FieldAttributeInfo fieldAttributeInfo = esReadVo.getFieldAttributeInfos().get(0);
        String fieldName = fieldAttributeInfo.getFieldName();
        String value = (String) fieldAttributeInfo.getValue();
        String[] split=null;
        if (value.contains(",")){
             split = value.split(",");
        }
        TermsQueryBuilder termsQueryBuilder = QueryBuilders.termsQuery(fieldName, split==null?value:split);
        return ElasticsearchReadUtils.returnBuilde(esReadVo,termsQueryBuilder,restHighLevelClient);
    }
}
