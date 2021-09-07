package com.centit.dde.query;

import com.alibaba.fastjson.JSONArray;
import com.centit.dde.entity.EsReadVo;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;

public interface EsQuery {
    JSONArray query(RestHighLevelClient restHighLevelClient, EsReadVo esReadVo) throws IOException;
}
