package com.centit.dde.utils;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class EsIndexNameExistsUtils {
    private static  final ConcurrentHashMap<RestHighLevelClient, List<String>> indexNameCache = new ConcurrentHashMap<>();

    public static   boolean indexNameExists(RestHighLevelClient restHighLevelClient,String indexName) throws IOException {
        boolean exists;
        if (!indexNameCache.containsKey(restHighLevelClient)){
            exists = restHighLevelClient.indices().exists(new GetIndexRequest(indexName),RequestOptions.DEFAULT);
            if (exists){
                List<String> synchronizedList = Collections.synchronizedList(new ArrayList<>());
                synchronizedList.add(indexName);
                indexNameCache.put(restHighLevelClient,synchronizedList);
            }
        }else {
            List<String> synchronizedList = indexNameCache.get(restHighLevelClient);
            if (!synchronizedList.contains(indexName)){
                exists = restHighLevelClient.indices().exists(new GetIndexRequest(indexName),RequestOptions.DEFAULT);
                if (exists){
                    synchronizedList.add(indexName);
                }
            }else {
                exists=true;
            }
        }
        return exists;
    }
}
