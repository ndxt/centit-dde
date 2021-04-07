package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataSet;
import com.centit.dde.core.SimpleDataSet;
import com.centit.dde.transaction.AbstractSourceConnectThreadHolder;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.dde.utils.BizOptUtils;
import com.centit.framework.appclient.HttpReceiveJSON;
import com.centit.framework.common.ResponseData;
import com.centit.product.metadata.dao.SourceInfoDao;
import com.centit.product.metadata.po.SourceInfo;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.compiler.VariableFormula;
import com.centit.support.database.utils.DatabaseAccess;
import com.centit.support.database.utils.QueryAndNamedParams;
import com.centit.support.database.utils.QueryAndParams;
import com.centit.support.database.utils.QueryUtils;
import com.centit.support.network.HttpExecutor;
import com.centit.support.network.HttpExecutorContext;
import com.centit.support.network.UrlOptUtils;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhf
 */
public class HttpBizOperation implements BizOperation {
    private SourceInfoDao sourceInfoDao;

    public HttpBizOperation(SourceInfoDao sourceInfoDao) {
        this.sourceInfoDao = sourceInfoDao;
    }

    private HttpExecutorContext getHttpClientContext(String databaseCode,Map<String, Object> map) throws IOException {
        SourceInfo databaseInfo = sourceInfoDao.getDatabaseInfoById(databaseCode);
        //ToDO 根据资源获取session
        HttpClientContext context = HttpClientContext.create();
        BasicCookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        if(map.get("loginUrl")!=null) {
            HttpExecutor.formPost(HttpExecutorContext.create(httpClient).context(context),
                (String) map.get("loginUrl"),
                map, false);
        }
        context.setCookieStore(cookieStore);
        return HttpExecutorContext.create(httpClient).context(context);
    }
    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson) throws Exception {
        String sourDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "id", bizModel.getModelName());
        String httpMethod = BuiltInOperation.getJsonFieldString(bizOptJson, "requestMode", "post");
        String httpUrl = BuiltInOperation.getJsonFieldString(bizOptJson, "httpUrl", "");
        String databaseCode = BuiltInOperation.getJsonFieldString(bizOptJson, "databaseName", null);
        Object requestBody = VariableFormula.calculate(BuiltInOperation.getJsonFieldString(bizOptJson, "requestText", ""),
            new BizModelJSONTransform(bizModel));
        Map<String, String> params = BuiltInOperation.jsonArrayToMap(bizOptJson.getJSONArray("parameterList"), "urlname", "urlvalue");
        Map<String, Object> mapObject = new HashMap<>();
        if (params != null) {
            for (Map.Entry<String, String> map : params.entrySet()) {
                if (!StringBaseOpt.isNvl(map.getValue())) {
                    mapObject.put(map.getKey(), map.getValue());
                }
            }
        }
        mapObject.putAll(bizModel.getModelTag());
        DataSet dataSet = new SimpleDataSet();
        HttpReceiveJSON receiveJSON;
        HttpExecutorContext httpExecutorContext = getHttpClientContext(databaseCode,mapObject);
        switch (httpMethod.toLowerCase()) {
            case "post":
                receiveJSON= HttpReceiveJSON.valueOfJson(HttpExecutor.jsonPost(httpExecutorContext, UrlOptUtils.appendParamsToUrl(httpUrl, mapObject), requestBody));
                dataSet = BizOptUtils.castObjectToDataSet(receiveJSON.getData());
                break;
            case "put":
                receiveJSON= HttpReceiveJSON.valueOfJson(HttpExecutor.jsonPut(httpExecutorContext, UrlOptUtils.appendParamsToUrl(httpUrl, mapObject), requestBody));
                dataSet = BizOptUtils.castObjectToDataSet(receiveJSON.getData());
                break;
            case "get":
                receiveJSON= HttpReceiveJSON.valueOfJson(HttpExecutor.simpleGet(httpExecutorContext, httpUrl, mapObject));
                if (receiveJSON.getCode() != ResponseData.RESULT_OK) {
                    return BuiltInOperation.getResponseData(0, receiveJSON.getCode(), receiveJSON.getMessage());
                } else {
                    dataSet = BizOptUtils.castObjectToDataSet(receiveJSON.getData());
                }
                break;
            case "delete":
                dataSet = BizOptUtils.castObjectToDataSet(HttpExecutor.simpleDelete(httpExecutorContext, httpUrl, mapObject));
            default:
                break;
        }
        bizModel.putDataSet(sourDsName, dataSet);
        return BuiltInOperation.getResponseSuccessData(dataSet.getSize());
    }
}
