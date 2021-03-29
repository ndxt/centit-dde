package com.centit.dde.config;

import com.alibaba.fastjson.JSONObject;
import com.centit.product.metadata.po.SourceInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.elasticsearch.client.Node;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

import java.util.Arrays;
import java.util.Objects;

public class ElasticSearchConfig {
    public static final Log log = LogFactory.getLog(ElasticSearchConfig.class);


    private static final int ADDRESS_LENGTH = 2;

    private static final String HTTP_SCHEME = "http";


    public RestHighLevelClient restHighLevelClient(SourceInfo sourceInfo){
        RestClientBuilder restClientBuilder = restClientBuilder(sourceInfo.getDatabaseUrl());
        JSONObject poolData = sourceInfo.getExtProps();
        String username = sourceInfo.getUsername();
        String password = sourceInfo.getPassword();
        //请求获取数据的超时时间(即响应时间)，单位毫秒。
        Integer socketTimeout =poolData.getString("socketTimeout")==null?10000: Integer.valueOf(poolData.getString("socketTimeout"));
        //设置连接超时时间，单位毫秒。指的是连接目标url的连接超时时间，即客服端发送请求到与目标url建立起连接的最大时间。
        Integer connectTimeout = poolData.getString("connectTimeout")==null?10000: Integer.valueOf(poolData.getString("connectTimeout"));
        //设置从connect Manager(连接池)获取Connection 超时时间，单位毫秒。
        Integer connectionRequestTimeout = poolData.getString("connectionRequestTimeout")==null?10000: Integer.valueOf(poolData.getString("connectionRequestTimeout"));
        Integer ioThreadCount =poolData.getString("ioThreadCount")==null?10: Integer.valueOf(poolData.getString("ioThreadCount"));
        //配置身份验证
      if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)){
          final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
          credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
          restClientBuilder.setHttpClientConfigCallback(
              httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider));
      }
        //设置连接超时和套接字超时
        restClientBuilder.setRequestConfigCallback(
            requestConfigBuilder -> requestConfigBuilder
                .setSocketTimeout(socketTimeout)
                .setConnectTimeout(connectTimeout)
                .setConnectionRequestTimeout(connectionRequestTimeout));
        //配置HTTP异步请求ES的线程数
        restClientBuilder.setHttpClientConfigCallback(
            httpAsyncClientBuilder -> httpAsyncClientBuilder
                .setDefaultIOReactorConfig(
                    IOReactorConfig.custom().setIoThreadCount(ioThreadCount)
                        .build()));
        //设置监听器，每次节点失败都可以监听到，可以作额外处理
        restClientBuilder.setFailureListener(new RestClient.FailureListener() {
            @Override
            public void onFailure(Node node) {
                super.onFailure(node);
                log.error(node.getHost() + "--->该节点失败了");
            }
        });
        return new RestHighLevelClient(restClientBuilder);
    }

    public RestClientBuilder restClientBuilder(String ipAddress) {
        String[] split = ipAddress.split(",");
        HttpHost[] hosts = Arrays.stream(split)
            .map(this::makeHttpHost)
            .filter(Objects::nonNull)
            .toArray(HttpHost[]::new);
        return RestClient.builder(hosts);
    }


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
