package com.centit.dde.test;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.client.*;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.AlternativeJdkIdGenerator;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

@Slf4j
public class CreateIndex {
    public static void main(String[] args) throws Exception {
      // System.out.println(new CreateIndex().createIndexJson("q_data_packet"));
       /* RestClientBuilder restClientBuilder = new CreateIndex().restClientBuilder("127.0.0.1:9201,127.0.0.1:9202,127.0.0.1:9203");
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(restClientBuilder);*/
        /**
         * 多索引查询
         *//*
       *//* MultiGetRequest multiGetRequest = new MultiGetRequest();
       //参数：索引名，文档id
        multiGetRequest.add("q_data_packet","7f91561521ea454eb863d47675d977b8");
        multiGetRequest.add("employee","1");
        MultiGetResponse mget = restHighLevelClient.mget(multiGetRequest, RequestOptions.DEFAULT);
        MultiGetItemResponse[] responses = mget.getResponses();
        for (MultiGetItemResponse respons : responses) {
            System.out.println(respons.getResponse().getSourceAsString());
        }*//*
        SearchRequest searchRequest = new SearchRequest("q_data_packet");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("packetName","收文   发文");
        matchQueryBuilder.analyzer("ik_max_word");
        matchQueryBuilder.operator(Operator.AND);
        matchQueryBuilder.minimumShouldMatch("12");
        searchSourceBuilder.query(matchQueryBuilder);
        searchRequest.source(searchSourceBuilder);
        SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = search.getHits();
        for (SearchHit hit : hits.getHits()) {
            System.out.println(hit.getSourceAsString());
        }
        restHighLevelClient.close();*/
        String str=null;
        Assert.notNull(str,"参数不能为null");
        System.out.println(1111111111);
    }

    public   boolean createIndexJson(String index) throws IOException {
        RestClientBuilder restClientBuilder = restClientBuilder("127.0.0.1:9200,127.0.0.1:9201,127.0.0.1:9202");
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(restClientBuilder);
        // 开始创建库
        CreateIndexRequest request = new CreateIndexRequest(index);
        //配置文件
        ClassPathResource seResource = new ClassPathResource("settings.json");
        InputStream seInputStream = seResource.getInputStream();
        String seJson = String.join("\n", IOUtils.readLines(seInputStream,"UTF-8"));
        seInputStream.close();
        //映射文件
        ClassPathResource mpResource = new ClassPathResource("mapping.json");
        InputStream mpInputStream = mpResource.getInputStream();
        String mpJson = String.join("\n",IOUtils.readLines(mpInputStream,"UTF-8"));
        mpInputStream.close();
        request.settings(seJson, XContentType.JSON);
        request.mapping(mpJson, XContentType.JSON);

        //设置别名
        request.alias(new Alias(index+"_alias"));
        CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
        boolean falg = createIndexResponse.isAcknowledged();
        if(falg){
            System.out.println("创建索引库:"+index+"成功！" );
        }
        restHighLevelClient.close();
        return falg;
    }

    private  RestClientBuilder restClientBuilder(String ipAddress) {
        String[] split = ipAddress.split(",");
        HttpHost[] hosts = Arrays.stream(split)
            .map(this::makeHttpHost)
            .filter(Objects::nonNull)
            .toArray(HttpHost[]::new);
        return RestClient.builder(hosts);
    }

    private static final int ADDRESS_LENGTH = 2;
    private static final String HTTP_SCHEME = "http";
    private HttpHost makeHttpHost(String str) {
        assert StringUtils.isNotEmpty(str);
        String[] address = str.split(":");
        if (address.length == ADDRESS_LENGTH) {
            String ip = address[0];
            int port = Integer.parseInt(address[1]);
            log.info("ES连接ip"+ip+"和port:"+port);
            return new HttpHost(ip, port, HTTP_SCHEME);
        } else {
            log.error("传入的ip参数不正确！");
            return null;
        }
    }
}
