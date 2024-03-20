package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.dataset.FileDataSet;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.dde.utils.ConstantValue;
import com.centit.dde.utils.DataSetOptUtil;
import com.centit.framework.appclient.HttpReceiveJSON;
import com.centit.framework.common.ResponseData;
import com.centit.product.metadata.dao.SourceInfoDao;
import com.centit.product.metadata.po.SourceInfo;
import com.centit.product.metadata.transaction.AbstractSourceConnectThreadHolder;
import com.centit.support.algorithm.BooleanBaseOpt;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.compiler.Pretreatment;
import com.centit.support.json.JSONTransformer;
import com.centit.support.network.HttpExecutor;
import com.centit.support.network.HttpExecutorContext;
import com.centit.support.network.UrlOptUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhf
 */
public class HttpServiceOperation implements BizOperation {
    private SourceInfoDao sourceInfoDao;

    public HttpServiceOperation(SourceInfoDao sourceInfoDao) {
        this.sourceInfoDao = sourceInfoDao;
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        //请求服务ip地址
        String serverIpAddressId = BuiltInOperation.getJsonFieldString(bizOptJson, "databaseId", "");
        if (StringUtils.isBlank(serverIpAddressId))
            return BuiltInOperation.createResponseData(0, 1, 500, "服务ip地址不能为空！");

        //请求接口
        String interfaceAddress = BuiltInOperation.getJsonFieldString(bizOptJson, "httpUrl", null);
        if (StringUtils.isBlank(interfaceAddress))
            return BuiltInOperation.createResponseData(0, 1, 500, "接口地址不能为空！");

        SourceInfo sourceInfo = sourceInfoDao.getDatabaseInfoById(serverIpAddressId);
        if (sourceInfo == null)
            return ResponseData.makeErrorMessage(ResponseData.ERROR_PRECONDITION_FAILED, "无效请求地址！");

        String serverIpAddress = sourceInfo.getDatabaseUrl();
        String requestServerAddress = serverIpAddress + interfaceAddress;
        requestServerAddress = Pretreatment.mapTemplateString(requestServerAddress, new BizModelJSONTransform(bizModel));

        //构建请求头数据
        Map<String, String> headers = new HashMap<>();
        /* 这个不能使用这个  x-auth-token ; 感觉不合理
        HttpServletRequest request = RequestThreadLocal.getLocalThreadWrapperRequest();
        if (request != null) {
            HttpSession session = request.getSession();
            headers.put(WebOptUtils.SESSION_ID_TOKEN, session == null ? null : session.getId());
        }*/

        HttpExecutorContext httpExecutorContext = getHttpClientContext(sourceInfo);

        if (sourceInfo.getExtProps() != null) {
            Map<String, String> extProps = CollectionsOpt.objectMapToStringMap(sourceInfo.getExtProps());
            for(Map.Entry<String, String> ent : extProps.entrySet()) {
                if(StringUtils.equalsAnyIgnoreCase(ent.getKey(),
                    "Content-Type", "Accept", "Accept-Encoding", "Accept-Language", "Connection",
                    "Host", "User-Agent", "Cache-Control", "Date", "Pragma",
                    "Trailer", "Transfer-Encoding", "Via", "Referer", "Cookie")) {
                    headers.put(ent.getKey(), ent.getValue());
                }
            }
        }

        boolean inheritHeader = BooleanBaseOpt.castObjectToBoolean(bizOptJson.get("inheritHeader"), false);
        if(inheritHeader){
            Map<String, String> parentHeaders = (Map<String, String>) dataOptContext.getStackData(ConstantValue.REQUEST_HEADERS_TAG);
            if(parentHeaders!=null)
                headers.putAll(parentHeaders);
        }

        Map<String, Object> localHeaders = getRequestParams(bizOptJson, bizModel, "headList", "headName", "headValue");
        if(localHeaders != null && localHeaders.size()>0){
            headers.putAll(CollectionsOpt.objectMapToStringMap(localHeaders));
        }
        httpExecutorContext.headers(headers);

        //构建请求cookies
        Map<String, String> cookies = new HashMap<>();
        boolean inheritCookies = BooleanBaseOpt.castObjectToBoolean(bizOptJson.get("inheritCookie"), false);
        if(inheritCookies){
            Map<String, String> parentCookies = (Map<String, String>) dataOptContext.getStackData(ConstantValue.REQUEST_COOKIES_TAG);
            if(parentCookies!=null)
                cookies.putAll(parentCookies);
        }

        Map<String, Object> localCookiess = getRequestParams(bizOptJson, bizModel, "cookieList", "cookieName", "cookieValue");
        if(localCookiess != null && localCookiess.size()>0){
            cookies.putAll(CollectionsOpt.objectMapToStringMap(localCookiess));
        }
        httpExecutorContext.cookies(cookies);

        //获取请求参数
        Map<String, Object> requestParams = getRequestParams(bizOptJson, bizModel);
        boolean inheritParams = BooleanBaseOpt.castObjectToBoolean(bizOptJson.get("inheritParameter"), false);
        if(inheritParams){
            requestParams = CollectionsOpt.unionTwoMap(requestParams, (Map<String, Object>)
                    dataOptContext.getStackData(ConstantValue.REQUEST_PARAMS_TAG));
        }


        // 添加url中的参数 __request_params
        // requestParams.putAll(CollectionsOpt.objectToMap(bizModel.getStackData(ConstantValue.REQUEST_PARAMS_TAG)));
        //请求方式
        String requestMode = BuiltInOperation.getJsonFieldString(bizOptJson, "requestMode", "post");
        HttpReceiveJSON receiveJson = null;
        switch (requestMode.toLowerCase()) {
            case "post":
                String requestType = BuiltInOperation.getJsonFieldString(bizOptJson, "requestType", "");
                if (ConstantValue.FILE_REQUEST_TYPE.equals(requestType)) {
                    String source = bizOptJson.getString("source");
                    DataSet dataSet = bizModel.getDataSet(source);
                    FileDataSet fileInfo = DataSetOptUtil.attainFileDataset(bizModel, dataSet, bizOptJson, false);
                    InputStream fileIS = fileInfo.getFileInputStream();

                    if (fileIS != null) {
                        receiveJson = HttpReceiveJSON.valueOfJson(HttpExecutor.inputStreamUpload(httpExecutorContext,
                            UrlOptUtils.appendParamsToUrl(requestServerAddress, requestParams), fileIS,
                            "file", ContentType.APPLICATION_OCTET_STREAM, fileInfo.getFileName()));
                    }
                } else {
                    Object requestBody = getRequestBody(bizOptJson, bizModel);
                    receiveJson = HttpReceiveJSON.valueOfJson(HttpExecutor.jsonPost(httpExecutorContext,
                        UrlOptUtils.appendParamsToUrl(requestServerAddress, requestParams), requestBody, false));
                }
                break;
            case "put":
                Object requestBody = getRequestBody(bizOptJson, bizModel);
                receiveJson = HttpReceiveJSON.valueOfJson(HttpExecutor.jsonPut(httpExecutorContext,
                    UrlOptUtils.appendParamsToUrl(requestServerAddress, requestParams), requestBody));
                break;
            case "get":
                receiveJson = HttpReceiveJSON.valueOfJson(HttpExecutor.simpleGet(httpExecutorContext, requestServerAddress, requestParams));
                if (receiveJson.getCode() != ResponseData.RESULT_OK) {
                    return BuiltInOperation.createResponseData(0, 1, receiveJson.getCode(), receiveJson.getMessage());
                }
                break;
            case "delete":
                DataSet dataSet = DataSet.toDataSet(HttpExecutor.simpleDelete(httpExecutorContext, requestServerAddress, requestParams));
                return BuiltInOperation.createResponseSuccessData(dataSet.getSize());
            default:
                return BuiltInOperation.createResponseData(0, 1, 500, "无效请求！");
        }
        if (receiveJson != null) {
            DataSet dataSet = DataSet.toDataSet(receiveJson.getData());
            String id = BuiltInOperation.getJsonFieldString(bizOptJson, "id", bizModel.getModelName());
            bizModel.putDataSet(id, dataSet);
            return BuiltInOperation.createResponseSuccessData(dataSet.getSize());
        } else {
            return BuiltInOperation.createResponseData(0, 1, ResponseData.ERROR_OPERATION, "无数据");
        }
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

    private Map<String, Object> getRequestParams(JSONObject bizOptJson, BizModel bizModel, String parameterName, String key, String value) {
        //请求参数列表
        Map<String, String> params = BuiltInOperation.jsonArrayToMap(bizOptJson.getJSONArray(parameterName), key, value);
        Map<String, Object> mapObject = new HashMap<>();
        if (params != null) {
            BizModelJSONTransform jsonTransform=new BizModelJSONTransform(bizModel);
            for (Map.Entry<String, String> map : params.entrySet()) {
                if (StringUtils.isNotBlank(map.getValue())) {
                    mapObject.put(map.getKey(), jsonTransform.attainExpressionValue(map.getValue()));
                }
            }
        }
        return mapObject;
    }
    //计算请求参数列表中的参数
    private Map<String, Object> getRequestParams(JSONObject bizOptJson,BizModel bizModel) {
        return getRequestParams(bizOptJson, bizModel, "parameterList", "urlname", "urlValue");
    }

    private Object getRequestBody(JSONObject bizOptJson, BizModel bizModel) {
        Object requestBody = "";
        String json = BuiltInOperation.getJsonFieldString(bizOptJson, "requestBody", "");
        if (json != null) {
            String requestBodyData = json.trim();
            if (requestBodyData.startsWith("{") && requestBodyData.endsWith("}")) {
                requestBody = JSONTransformer.transformer(JSON.parse(json), new BizModelJSONTransform(bizModel));
            } else {
                requestBody = JSONTransformer.transformer(json, new BizModelJSONTransform(bizModel));
            }
        }
        return requestBody;
    }

}
