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
import com.centit.dde.utils.FileDataSetOptUtil;
import com.centit.framework.appclient.HttpReceiveJSON;
import com.centit.framework.common.ResponseData;
import com.centit.product.metadata.po.SourceInfo;
import com.centit.product.metadata.service.SourceInfoMetadata;
import com.centit.product.metadata.transaction.AbstractSourceConnectThreadHolder;
import com.centit.support.algorithm.BooleanBaseOpt;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.common.ObjectException;
import com.centit.support.compiler.Pretreatment;
import com.centit.support.file.FileIOOpt;
import com.centit.support.json.JSONTransformer;
import com.centit.support.network.*;
import com.centit.support.xml.XMLObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhf
 * @author codefan 2019-09-05
 */
public class HttpServiceOperation implements BizOperation {
    private SourceInfoMetadata sourceInfoMetadata;

    public HttpServiceOperation(SourceInfoMetadata sourceInfoMetadata) {
        this.sourceInfoMetadata = sourceInfoMetadata;
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        //请求服务ip地址
        String serverIpAddressId = BuiltInOperation.getJsonFieldString(bizOptJson, "databaseId", "");
        if (StringUtils.isBlank(serverIpAddressId))
            return BuiltInOperation.createResponseData(0, 1,
                ResponseData.ERROR_FIELD_INPUT_NOT_VALID,
                dataOptContext.getI18nMessage("error.701.field_is_blank", "http service Id"));

        //请求接口
        String interfaceAddress = BuiltInOperation.getJsonFieldString(bizOptJson, "httpUrl", null);
        if (StringUtils.isBlank(interfaceAddress))
            return BuiltInOperation.createResponseData(0, 1, 500, "接口地址不能为空！");

        SourceInfo sourceInfo = sourceInfoMetadata.fetchSourceInfo(serverIpAddressId);
        if (sourceInfo == null)
            return ResponseData.makeErrorMessage(ResponseData.ERROR_PRECONDITION_FAILED, "无效请求地址！");

        String transUrl = Pretreatment.mapTemplateString(interfaceAddress, new BizModelJSONTransform(bizModel));
        if(StringUtils.isNotBlank(transUrl)){
            transUrl = interfaceAddress;
        }

        //构建请求头数据
        Map<String, String> headers = new HashMap<>();
        String requestType = BuiltInOperation.getJsonFieldString(bizOptJson, "requestType", "");
        String soapNameSpace = null;
        HttpExecutorContext httpExecutorContext = getHttpClientContext(sourceInfo);
        int timeOut = NumberBaseOpt.castObjectToInteger(bizOptJson.get("timeout"), -1);

        if (sourceInfo.getExtProps() != null) {
            Map<String, String> extProps = CollectionsOpt.objectMapToStringMap(sourceInfo.getExtProps());
            for(Map.Entry<String, String> ent : extProps.entrySet()) {
                if(StringUtils.equalsAnyIgnoreCase(ent.getKey(), //"Content-Type",
                    "Accept", "Accept-Encoding", "Accept-Language", "Connection",
                    "Host", "User-Agent", "Cache-Control", "Date", "Pragma",
                    "Trailer", "Transfer-Encoding", "Via", "Referer", "Cookie")) {
                    headers.put(ent.getKey(), ent.getValue());
                }
            }
            if(timeOut == -1){
                timeOut = NumberBaseOpt.castObjectToInteger(extProps.get("timeout"), -1);
            }
            soapNameSpace = extProps.get("soapNameSpace");
            if(StringUtils.endsWith(soapNameSpace, "/")){
                soapNameSpace = soapNameSpace.substring(0, soapNameSpace.length() - 1);
            }
        }
        httpExecutorContext.timout(timeOut);

        boolean inheritHeader = BooleanBaseOpt.castObjectToBoolean(bizOptJson.get("inheritHeader"), false);
        if(inheritHeader){
            Map<String, String> parentHeaders = (Map<String, String>) dataOptContext.getStackData(ConstantValue.REQUEST_HEADERS_TAG);
            if(parentHeaders!=null) {
                for(Map.Entry<String, String> ent : parentHeaders.entrySet()) {
                    //accept-language:"zh-CN,zh;q=0.9"
                    //x-auth-token:"e4fcbbb5-db3a-4b4c-a065-a261f7d4d8fa"
                    if("x-auth-token".equalsIgnoreCase(ent.getKey())) {
                        headers.put(ent.getKey(), ent.getValue());
                    } else if("accept-language".equalsIgnoreCase(ent.getKey())) {
                        headers.put(ent.getKey(), ent.getValue());
                    }
                }
            }
        }

        Map<String, Object> localHeaders = getRequestParams(bizOptJson, bizModel, "headList", "headName", "headValue");
        if(!localHeaders.isEmpty()){
            headers.putAll(CollectionsOpt.objectMapToStringMap(localHeaders));
        }

        String requestServerAddress = sourceInfo.getDatabaseUrl();
        if(ConstantValue.SOAP_REQUEST_TYPE.equals(requestType)){
            if(StringUtils.startsWith(transUrl, "/")){
                transUrl = transUrl.substring(1);
            }
            headers.put("SOAPAction", soapNameSpace + "/" + transUrl);
        } else {
            requestServerAddress = sourceInfo.getDatabaseUrl() + transUrl;
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
        if(!localCookiess.isEmpty()){
            cookies.putAll(CollectionsOpt.objectMapToStringMap(localCookiess));
        }
        httpExecutorContext.cookies(cookies);
        //获取请求参数
        Map<String, Object> requestParams = getRequestParams(bizOptJson, bizModel);
        boolean inheritParams = BooleanBaseOpt.castObjectToBoolean(bizOptJson.get("inheritParameter"), false);
        if(inheritParams){
            requestParams = CollectionsOpt.unionTwoMap(requestParams,
                CollectionsOpt.objectToMap(dataOptContext.getStackData(ConstantValue.REQUEST_PARAMS_TAG)));
        }

        // 添加url中的参数 __request_params
        // requestParams.putAll(CollectionsOpt.objectToMap(bizModel.getStackData(ConstantValue.REQUEST_PARAMS_TAG)));
        //请求方式
        String requestMode = BuiltInOperation.getJsonFieldString(bizOptJson, "requestMode", "post").toLowerCase();
        HttpReceiveJSON receiveJson = null;
        switch (requestMode) {
            case "post":
            case "put":
                if (ConstantValue.FILE_REQUEST_TYPE.equals(requestType)) { // 发送文件（附件）的请求
                    String source = bizOptJson.getString("source");
                    DataSet dataSet = bizModel.getDataSet(source);
                    if (dataSet == null){
                        return BuiltInOperation.createResponseData(0, 1,
                            ObjectException.DATA_NOT_FOUND_EXCEPTION,
                            dataOptContext.getI18nMessage("dde.604.data_source_not_found"));
                    }
                    FileDataSet fileInfo = FileDataSetOptUtil.attainFileDataset(bizModel, dataSet, bizOptJson, false);
                    InputStream fileIS = fileInfo.getFileInputStream();
                    if (fileIS != null) {
                        if("put".equals(requestMode)){
                            receiveJson = HttpReceiveJSON.valueOfJson(HttpExecutor.inputStreamUploadPut(httpExecutorContext,
                                UrlOptUtils.appendParamsToUrl(requestServerAddress, requestParams), fileIS,
                                "file", ContentType.APPLICATION_OCTET_STREAM, fileInfo.getFileName()));
                        } else {
                            receiveJson = HttpReceiveJSON.valueOfJson(HttpExecutor.inputStreamUpload(httpExecutorContext,
                                UrlOptUtils.appendParamsToUrl(requestServerAddress, requestParams), fileIS,
                                "file", ContentType.APPLICATION_OCTET_STREAM, fileInfo.getFileName()));
                        }
                    }
                } else { // json 或者 form
                    Object requestBody = getRequestBody(bizOptJson, bizModel);
                    if("put".equals(requestMode)){
                        if (ConstantValue.FORM_REQUEST_TYPE.equals(requestType)) {
                            receiveJson = HttpReceiveJSON.valueOfJson(HttpExecutor.formPut(httpExecutorContext,
                                UrlOptUtils.appendParamsToUrl(requestServerAddress, requestParams), requestBody));
                        } else if (ConstantValue.SOAP_REQUEST_TYPE.equals(requestType)) {
                            // WEB SERVICE 请求， 对返回的内容进行 处理
                            String xmlEntity = buildSoapXml(soapNameSpace, transUrl, requestBody, requestParams);
                            receiveJson = HttpReceiveJSON.dataOf(XMLObject.xmlStringToObject(
                                HttpExecutor.xmlPut(httpExecutorContext, requestServerAddress, xmlEntity)));
                        } else {
                            receiveJson = HttpReceiveJSON.valueOfJson(HttpExecutor.jsonPut(httpExecutorContext,
                                UrlOptUtils.appendParamsToUrl(requestServerAddress, requestParams), requestBody));
                        }
                    } else {
                        if (ConstantValue.FORM_REQUEST_TYPE.equals(requestType)) {
                            receiveJson = HttpReceiveJSON.valueOfJson(HttpExecutor.formPost(httpExecutorContext,
                                UrlOptUtils.appendParamsToUrl(requestServerAddress, requestParams), requestBody, false));
                        } else if (ConstantValue.SOAP_REQUEST_TYPE.equals(requestType)) {
                            // WEB SERVICE 请求， 对返回的内容进行 处理
                            String xmlEntity = buildSoapXml(soapNameSpace, transUrl, requestBody, requestParams);
                            receiveJson = HttpReceiveJSON.dataOf(XMLObject.xmlStringToObject(
                                HttpExecutor.xmlPost(httpExecutorContext, requestServerAddress, xmlEntity)));
                        } else {
                            receiveJson = HttpReceiveJSON.valueOfJson(HttpExecutor.jsonPost(httpExecutorContext,
                                UrlOptUtils.appendParamsToUrl(requestServerAddress, requestParams), requestBody, false));
                        }
                    }
                }
                break;
            case "get":
                Boolean returnAsFile = BooleanBaseOpt.castObjectToBoolean(bizOptJson.getString("returnAsFile"), false);
                //TODO 添加 是否是获取 文件的参数, 需要验证
                if(returnAsFile){
                    FileDataSet fileDataSet = fetchFileDataSetByUrl(httpExecutorContext, requestServerAddress, requestParams);
                    String id = BuiltInOperation.getJsonFieldString(bizOptJson, "id", bizModel.getModelName());
                    bizModel.putDataSet(id, fileDataSet);
                    return BuiltInOperation.createResponseSuccessData(1);
                }
                receiveJson = HttpReceiveJSON.valueOfJson(HttpExecutor.simpleGet(httpExecutorContext, requestServerAddress, requestParams));
                if (receiveJson.getCode() != ResponseData.RESULT_OK && receiveJson.getCode() != ResponseData.HTTP_OK) {
                    return BuiltInOperation.createResponseData(0, 1, receiveJson.getCode(), receiveJson.getMessage());
                }
                break;
            case "delete":
                receiveJson = HttpReceiveJSON.valueOfJson(HttpExecutor.simpleDelete(httpExecutorContext, requestServerAddress, requestParams));
                break;
            default:
                return BuiltInOperation.createResponseData(0, 1, 500, "无效请求！");
        }

        if (receiveJson != null) {
            DataSet dataSet;
            if(receiveJson.getData() == null){
                dataSet = DataSet.toDataSet(receiveJson.toResponseData());
            } else{
                dataSet = DataSet.toDataSet(receiveJson.getData());
            }
            String id = BuiltInOperation.getJsonFieldString(bizOptJson, "id", bizModel.getModelName());
            bizModel.putDataSet(id, dataSet);
            return BuiltInOperation.createResponseSuccessData(dataSet.getSize());
        } else {
            return BuiltInOperation.createResponseData(0, 1, ResponseData.ERROR_OPERATION, "无数据");
        }
    }

    private String buildSoapXml(String soapNameSpace, String actionName, Object requestBody, Map<String, Object> requestParams){
        String xmlBoday;
        if(requestBody instanceof Map){
            xmlBoday = XMLObject.objectToXMLString("act:"+actionName, requestBody, false, false);
        } else {
            xmlBoday = XMLObject.objectToXMLString("act:"+actionName, requestParams, false, false);
        }
        StringBuilder sb = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n");
        sb.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" ")
            .append("xmlns:act=\"").append(soapNameSpace).append("\" >")
            .append("<soapenv:Header/> <soapenv:Body/>");
        sb.append(xmlBoday);
        sb.append("</soapenv:Body> </soapenv:Envelope>");
        return sb.toString();
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
    private Map<String, Object> getRequestParams(JSONObject bizOptJson, BizModel bizModel) {
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

    private FileDataSet fetchFileDataSetByUrl(HttpExecutorContext executorContext, String uri, Map<String, Object> queryParam) throws IOException {
        HttpGet httpGet = new HttpGet(UrlOptUtils.appendParamsToUrl(uri, queryParam));
        CloseableHttpClient httpClient = null;
        boolean createSelfClient = executorContext.getHttpclient() == null;
        if (createSelfClient) {
            httpClient = executorContext.getHttpProxy() == null ?
                HttpExecutor.createHttpClient() :
                HttpExecutor.createHttpClient(executorContext.getHttpProxy());
        } else {
            httpClient = executorContext.getHttpclient();
        }
        // 添加对应的参数设置
        HttpExecutor.prepareHttpRequest(executorContext, httpGet);
        try (CloseableHttpResponse response = httpClient.execute(httpGet, executorContext.getHttpContext())) {
            Header[] contentTypeHeader = response.getHeaders("Content-Type");
            if (contentTypeHeader == null || contentTypeHeader.length < 1 ||
                StringUtils.indexOf(
                    contentTypeHeader[0].getValue(), "text/") >= 0
            ) {
                String responseContent = Utf8ResponseHandler.INSTANCE
                    .handleResponse(response);
                throw new ObjectException(responseContent);
            }
            String fileName = HttpExecutor.extraFileName(response);
            if(StringUtils.isBlank(fileName)){
                fileName = UrlOptUtils.fetchFilenameFromUrl(uri);
            }
            FileDataSet fileDataSet = new FileDataSet();
            try (InputStream inputStream = InputStreamResponseHandler.INSTANCE
                .handleResponse(response)) {
                int fileSize = inputStream.available();
                if(fileSize<0){
                    fileSize = 10485760;
                } else {
                    fileSize += 10;
                }
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream(fileSize);
                FileIOOpt.writeInputStreamToOutputStream(inputStream, outputStream);
                fileDataSet.setFileContent(fileName, outputStream.size(), outputStream);
            }
            return fileDataSet;
        } finally {
            if (createSelfClient) {
                httpClient.close();
            }
        }
    }
}
