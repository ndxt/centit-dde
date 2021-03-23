package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.config.ElasticSearchConfig;
import com.centit.dde.config.ElasticsearchUtil;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataSet;
import com.centit.dde.core.SimpleDataSet;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.ResponseSingleData;
import com.centit.product.metadata.dao.SourceInfoDao;
import com.centit.product.metadata.po.SourceInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.nio.reactor.IOReactorException;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ESBizOperation implements BizOperation {
    public static final Log log = LogFactory.getLog(ESBizOperation.class);


    private SourceInfoDao sourceInfoDao;

    public ESBizOperation( ) {
    }

    public ESBizOperation(SourceInfoDao sourceInfoDao) {
        this.sourceInfoDao = sourceInfoDao;
    }

    //需要的参数  //es查询操作
    /*
     * 1.查询索引   indexName
     * 2.查询类型   1：精确查询   2条件查询  3分页查询
     * 3.查询字段 field  查询字段值
     * 4.操作类型   Q:query 查询   I:insert插入
     * */
    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson) throws Exception {
        //操作类型  Q:query 查询   反之 insert插入
        String operationType = bizOptJson.getString("operationType");
        return operationType.equals("Q")?queryEs(bizModel,bizOptJson):insertEs(bizModel,bizOptJson);
    }

    //ES查询操作
    private ResponseData queryEs(BizModel bizModel, JSONObject bizOptJson) throws IOReactorException {
        //索引名
        String indexName = bizOptJson.getString("indexName");
        //查询类型   1：精确查询   2条件查询  3分页查询
        String queryType = bizOptJson.getString("queryType");
        //查询字段 field  查询字段值
        String queryField = bizOptJson.getString("queryField");
        Integer pageNo = bizOptJson.getInteger("pageNo");
        Integer pageSize = bizOptJson.getInteger("pageSize");
        RestHighLevelClient restHighLevelClient = restHighLevelClient(bizOptJson.getString("databaseName"));
        JSONArray resultData;
        String fields="";
        String fieldValue="";
        if (queryField.contains(":")){
            String[] split = queryField.split(":");
            fields =split[0];
            fieldValue =split[1];
        }
        long startTime = System.currentTimeMillis();
        switch (queryType){
            //精确查询
            case "1":
                resultData = ElasticsearchUtil.accurateQuery(restHighLevelClient, fields, fieldValue,indexName);
                break;
            //条件查询
            case "2":
                resultData = ElasticsearchUtil.matchQuery(restHighLevelClient, fields, fieldValue,indexName);
                break;
            //查询全部（分页查询） 默认分页查询
            default:
                resultData = ElasticsearchUtil.matchAllQuery(restHighLevelClient, pageNo==null?1:pageNo, pageSize==null?100:pageSize,indexName);
        }
        log.info("查询es耗时："+(System.currentTimeMillis()-startTime)+"ms");
        SimpleDataSet dataSet = new SimpleDataSet();
        dataSet.setData(resultData);
        String id = BuiltInOperation.getJsonFieldString(bizOptJson, "id", bizModel.getModelName());
        bizModel.putDataSet(id,dataSet);
        return ResponseSingleData.makeResponseData(resultData);
    }
    //ES插入操作
    private ResponseData insertEs(BizModel bizModel, JSONObject bizOptJson) throws IOException {
        //索引名
        String indexName = bizOptJson.getString("indexName");
        String id = BuiltInOperation.getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        DataSet dataSet = bizModel.getDataSet(id);
        JSONArray jsonArray = JSONObject.parseArray(String.valueOf(dataSet.getData()));
        RestHighLevelClient restHighLevelClient = restHighLevelClient(bizOptJson.getString("databaseName"));
        long startTime = System.currentTimeMillis();
        Boolean indexResponse = ElasticsearchUtil.saveDocuments(restHighLevelClient,jsonArray, indexName);
        log.info("插入ES数据耗时："+(System.currentTimeMillis()-startTime)+"ms");
        SimpleDataSet resultDataSet = new SimpleDataSet();
        resultDataSet.setData(indexResponse);
        bizModel.putDataSet(id,resultDataSet);
        return ResponseSingleData.makeResponseData(indexResponse);
    }

    //json串转map
    public static Map<String, Object> jsonStrToMap(String json) {
        JSONObject jsonObject = JSONObject.parseObject(json);
        Map<String, Object> valueMap = new HashMap<String, Object>();
        valueMap.putAll(jsonObject);
        return valueMap;
    }
    //获取 RestHighLevelClient 对象
    private RestHighLevelClient restHighLevelClient(String databaseName) throws IOReactorException {
        long startTime = System.currentTimeMillis();
        SourceInfo sourceInfo = sourceInfoDao.getDatabaseInfoById(databaseName);
        log.info("获取元数据信息耗时："+(System.currentTimeMillis()-startTime)+"ms");
        String dataSourceUrl=sourceInfo.getDatabaseUrl();
        startTime=System.currentTimeMillis();
       /* ElasticSearchProperties properties = new ElasticSearchProperties();
        properties.setIpPorts(dataSourceUrl);
        properties.setUserName(sourceInfo.getUsername());
        properties.setPassWord(sourceInfo.getPassword());
        ElasticSearchConfig  searchConfig = new ElasticSearchConfig(properties);
        RestHighLevelClient restHighLevelClient = searchConfig.createInstance();*/
        ElasticSearchConfig elasticSearchConfigTwo = new ElasticSearchConfig();
        RestHighLevelClient restHighLevelClient = elasticSearchConfigTwo.restHighLevelClient(elasticSearchConfigTwo.restClientBuilder(dataSourceUrl), sourceInfo.getUsername(), sourceInfo.getClearPassword());
        log.info("获取restHighLevelClient客户端耗时："+(System.currentTimeMillis()-startTime)+"ms");
        return restHighLevelClient;
    }

}
