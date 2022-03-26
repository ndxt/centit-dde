package com.centit.dde.write;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.entity.EsWriteVo;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * es保存工具类
 */
public class ElasticsearchWriteUtils {
    /**
     * 批量插入或者更新   es存在就更新，不存在就插入
     */
    public static JSONObject batchSaveDocuments(RestHighLevelClient restHighLevelClient, List<String> jsonDatas, EsWriteVo esSearchWriteEntity) throws IOException {
        BulkRequest requestBulk = new BulkRequest(esSearchWriteEntity.getIndexName());
        for (String jsonData : jsonDatas) {
            String documentid = getDocument(jsonData, esSearchWriteEntity.getDocumentIds());
            //判断文档id是否已经存在，如果存在就做更新操作 反之
            if (documentIdExist(restHighLevelClient, esSearchWriteEntity.getIndexName(),documentid)){
                UpdateRequest updateRequest= new UpdateRequest(esSearchWriteEntity.getIndexName(),documentid);
                updateRequest.doc(jsonData, XContentType.JSON);
                requestBulk.add(updateRequest);
            }else {
                IndexRequest indexReq = new IndexRequest().source(jsonData, XContentType.JSON);
                indexReq.id(documentid);
                requestBulk.add(indexReq);
            }
        }
        int updateCount=0;
        int addCount=0;
        int faildCount=0;
        BulkResponse bulkResponse = restHighLevelClient.bulk(requestBulk, RequestOptions.DEFAULT);
        JSONObject jsonObject = new JSONObject();
        JSONObject faildData = new JSONObject();
        for(BulkItemResponse bulkItemResponse : bulkResponse){
            DocWriteResponse itemResponse = bulkItemResponse.getResponse();
            if (itemResponse instanceof  IndexResponse){
                updateCount++;
            }
            if (itemResponse instanceof UpdateResponse){
                addCount++;
            }
            if(bulkItemResponse.isFailed()){
                faildData.put(bulkItemResponse.getId(),bulkItemResponse.getFailureMessage());
                faildCount++;
            }
        }
        jsonObject.put("addCount",addCount);
        jsonObject.put("updateCount",updateCount);
        jsonObject.put("faildCount",faildCount);
        if (faildCount>0){
            jsonObject.put("errorData",faildData);
        }
        return jsonObject;
    }

    /**
     * 组装documentid
     * @param jsonData  保存对象
     * @param fields   作为document文档id的字段 （0个或者多个）
     * @return documentid
     */
    private static String getDocument(String jsonData,String fields){
        StringBuilder doucmentId = new StringBuilder();
        if (StringUtils.isNotBlank(fields)) {
            String[] fieldArr = fields.split(",");
            JSONObject jsonObject = JSONObject.parseObject(jsonData);
            for (int i = 0; i < fieldArr.length; i++) {
                doucmentId.append(jsonObject.get(fieldArr[i]));
            }
        }else {
            doucmentId.append(UUID.randomUUID().toString().replaceAll("-",""));
        }
        return doucmentId.toString();
    }

    //判断文档id是否已经存在
    public static boolean documentIdExist(RestHighLevelClient restHighLevelClient,String indexName,String documentId) throws IOException {
        GetRequest request = new GetRequest(indexName).id(documentId);
        return restHighLevelClient.exists(request, RequestOptions.DEFAULT);
    }
}
