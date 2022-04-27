package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.*;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.dde.utils.BizOptUtils;
import com.centit.dde.utils.ConstantValue;
import com.centit.framework.appclient.HttpReceiveJSON;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.WebOptUtils;
import com.centit.framework.filter.RequestThreadLocal;
import com.centit.product.adapter.po.SourceInfo;
import com.centit.product.metadata.dao.SourceInfoDao;
import com.centit.product.metadata.transaction.AbstractSourceConnectThreadHolder;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.compiler.Pretreatment;
import com.centit.support.json.JSONTransformer;
import com.centit.support.network.HttpExecutor;
import com.centit.support.network.HttpExecutorContext;
import com.centit.support.network.UrlOptUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpSession;
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

    private HttpExecutorContext getHttpClientContext(SourceInfo databaseInfo) throws Exception {
        if (databaseInfo != null) {
            HttpExecutorContext executorContext = AbstractSourceConnectThreadHolder.fetchHttpContext(databaseInfo);
            if (executorContext != null) {
                return executorContext;
            }
        }
        return HttpExecutorContext.create();
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        String sourDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "id", bizModel.getModelName());
        String httpMethod = BuiltInOperation.getJsonFieldString(bizOptJson, "requestMode", "post");
        String httpUrlCode = BuiltInOperation.getJsonFieldString(bizOptJson, "databaseId", "");
        String loginUrlCode = BuiltInOperation.getJsonFieldString(bizOptJson, "loginService", null);
        String json = BuiltInOperation.getJsonFieldString(bizOptJson, "querySQL", "");
        String httpUrl = BuiltInOperation.getJsonFieldString(bizOptJson, "httpUrl", null);
        Object requestBody="";
        if(json!=null) {
            String requestBodyData = json.trim();
            if (requestBodyData.startsWith("{") && requestBodyData.endsWith("}")){
                requestBody =JSONTransformer.transformer(JSON.parse(json), new BizModelJSONTransform(bizModel));
            }else {
                requestBody = JSONTransformer.transformer(json, new BizModelJSONTransform(bizModel));
            }
        }
        Map<String, String> params = BuiltInOperation.jsonArrayToMap(bizOptJson.getJSONArray("parameterList"), "urlname", "urlvalue");
        Map<String, Object> mapObject = new HashMap<>();
        if (params != null) {
            for (Map.Entry<String, String> map : params.entrySet()) {
                if (!StringBaseOpt.isNvl(map.getValue())) {
                    mapObject.put(map.getKey(), map.getValue());
                }
            }
        }
        SourceInfo loginUrlInfo=null;
        if (StringUtils.isNotBlank(loginUrlCode)){
            loginUrlInfo = sourceInfoDao.getDatabaseInfoById(loginUrlCode);
        }
        SourceInfo httpUrlCodeInfo=null;
        Map<String, String> headers = new HashMap<>();
        if (StringUtils.isNotBlank(httpUrlCode)){
            httpUrlCodeInfo = sourceInfoDao.getDatabaseInfoById(httpUrlCode);
            if (httpUrlCodeInfo!=null){
                JSONObject extProps = httpUrlCodeInfo.getExtProps();
                if (extProps !=null){
                    headers =CollectionsOpt.objectMapToStringMap(extProps);
                }
            }
        }
        if (RequestThreadLocal.getLocalThreadWrapperRequest()!=null){
            HttpSession session = RequestThreadLocal.getLocalThreadWrapperRequest().getSession();
            headers.put(WebOptUtils.SESSION_ID_TOKEN/*"x-auth-token"*/, session==null?null:session.getId());
        }
        HttpExecutorContext httpExecutorContext = getHttpClientContext(loginUrlInfo);
        httpExecutorContext.headers(headers);
        if (httpUrlCodeInfo == null && ( StringUtils.isBlank(httpUrl) || !httpUrl.contains("://")) ){
            return ResponseData.makeErrorMessage(ResponseData.ERROR_PRECONDITION_FAILED,"无效请求地址！");
        }
        httpUrl= httpUrlCodeInfo == null || httpUrl.contains("://") ?
            httpUrl : httpUrlCodeInfo.getDatabaseUrl() + httpUrl;

        httpUrl = Pretreatment.mapTemplateString(httpUrl,new BizModelJSONTransform(bizModel));
        DataSet dataSet = new DataSet();
        HttpReceiveJSON receiveJson;
        mapObject.putAll(CollectionsOpt.objectToMap(bizModel.getStackData(ConstantValue.REQUEST_PARAMS_TAG)));
        switch (httpMethod.toLowerCase()) {
            case "post":
                receiveJson = HttpReceiveJSON.valueOfJson(HttpExecutor.jsonPost(httpExecutorContext,
                    UrlOptUtils.appendParamsToUrl(httpUrl, mapObject), requestBody,false));
                dataSet = BizOptUtils.castObjectToDataSet(receiveJson.getData());
                break;
            case "put":
                receiveJson = HttpReceiveJSON.valueOfJson(HttpExecutor.jsonPut(httpExecutorContext,
                    UrlOptUtils.appendParamsToUrl(httpUrl, mapObject), requestBody));
                dataSet = BizOptUtils.castObjectToDataSet(receiveJson.getData());
                break;
            case "get":
                receiveJson = HttpReceiveJSON.valueOfJson(HttpExecutor.simpleGet(httpExecutorContext, httpUrl, mapObject));
                if (receiveJson.getCode() != ResponseData.RESULT_OK) {
                    return BuiltInOperation.createResponseData(0, receiveJson.getCode(), receiveJson.getMessage());
                } else {
                    dataSet = BizOptUtils.castObjectToDataSet(receiveJson.getData());
                }
                break;
            case "delete":
                dataSet = BizOptUtils.castObjectToDataSet(HttpExecutor.simpleDelete(httpExecutorContext, httpUrl, mapObject));
                break;
            default:
                break;
        }
        if (dataSet != null) {
            bizModel.putDataSet(sourDsName, dataSet);
            return BuiltInOperation.createResponseSuccessData(dataSet.getSize());
        } else {
            return BuiltInOperation.createResponseData(0, 0, "无数据");
        }
    }
}
