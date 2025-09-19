package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.adapter.po.CallApiLogDetail;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.dataset.FileDataSet;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.dde.utils.BizOptUtils;
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
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.common.ObjectException;
import com.centit.support.compiler.Pretreatment;
import com.centit.support.file.FileIOOpt;
import com.centit.support.file.FileType;
import com.centit.support.json.JSONOpt;
import com.centit.support.json.JSONTransformer;
import com.centit.support.network.*;
import com.centit.support.xml.XMLObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author zhf
 * @author codefan 2019-09-05
 */
public class HttpServiceOperation implements BizOperation {
    private SourceInfoMetadata sourceInfoMetadata;

    public HttpServiceOperation(SourceInfoMetadata sourceInfoMetadata) {
        this.sourceInfoMetadata = sourceInfoMetadata;
    }

    private void saveDebugLog(JSONObject bizOptJson, DataOptContext dataOptContext,
                              String url, Map<String, Object> requestParams, Object requestBody){
        //debug模式下，添加put 和 post 的日志
        if(ConstantValue.RUN_TYPE_DEBUG.equals(dataOptContext.getRunType()) ||
            (ConstantValue.LOGLEVEL_CHECK_DEBUG & dataOptContext.getLogLevel()) != 0){
            CallApiLogDetail detailLog = BizOptUtils.createLogDetail(bizOptJson, dataOptContext);
            detailLog.setLogInfo(JSON.toJSONString(CollectionsOpt.createHashMap(
                "url", UrlOptUtils.appendParamsToUrl(url, requestParams),
                       "requestBody", requestBody)));
        }
    }

    private HttpReceiveJSON stringToReceiveJson(String jsonStr, boolean unpacked){
        if(unpacked){
            return HttpReceiveJSON.valueOfJson(jsonStr);
        }else{
            return HttpReceiveJSON.dataOfJson(jsonStr);
        }
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
            return ResponseData.makeErrorMessage(ResponseData.ERROR_PRECONDITION_FAILED, "无效请求Restful服务！");

        String transUrl = Pretreatment.mapUrlTemplateAsFormula(interfaceAddress, new BizModelJSONTransform(bizModel));
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
            int p = transUrl.indexOf(":");
            if(p>0) { // actionName : inputName
                headers.put("SOAPAction", soapNameSpace + "/" + transUrl.substring(0, p));
                transUrl = transUrl.substring(p + 1); // inputName
            }else {
                headers.put("SOAPAction", soapNameSpace + "/" + transUrl);
            }
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
        boolean autoUnpack = BooleanBaseOpt.castObjectToBoolean(bizOptJson.get("autoUnpack"), true);
        HttpReceiveJSON receiveJson = null;
        switch (requestMode) {
            case "post":
            case "put":
                if(ConstantValue.MULTIPART_REQUEST_TYPE.equals(requestType)){
                    String source = bizOptJson.getString("source");
                    DataSet dataSet = bizModel.getDataSet(source);
                    Object requestBody = getRequestBody(bizOptJson, bizModel);
                    String fieldName = BuiltInOperation.getJsonFieldString(bizOptJson, "fileFieldName", "file");
                    MultipartEntityBuilder builder = buildMultiPartEntity(requestBody, fetchMultiFiles(dataSet, bizOptJson, fieldName));
                    if("put".equals(requestMode)){
                        HttpPut httpPut = new HttpPut(UrlOptUtils.appendParamsToUrl(requestServerAddress, requestParams));
                        httpPut.setHeader("Content-Type", "multipart/form-data; boundary=" + HttpExecutor.BOUNDARY);
                        httpPut.setEntity(builder.build());
                        receiveJson = stringToReceiveJson(HttpExecutor.httpExecute(httpExecutorContext, httpPut), autoUnpack);
                    } else {
                        HttpPost httpPost = new HttpPost(UrlOptUtils.appendParamsToUrl(requestServerAddress, requestParams));
                        httpPost.setHeader("Content-Type", "multipart/form-data; boundary=" + HttpExecutor.BOUNDARY);
                        httpPost.setEntity(builder.build());
                        receiveJson = stringToReceiveJson(HttpExecutor.httpExecute(httpExecutorContext, httpPost), autoUnpack);
                    }
                } else if (ConstantValue.FILE_REQUEST_TYPE.equals(requestType)) { // 发送文件（附件）的请求
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
                            receiveJson = stringToReceiveJson(HttpExecutor.inputStreamUploadPut(httpExecutorContext,
                                UrlOptUtils.appendParamsToUrl(requestServerAddress, requestParams), fileIS,
                                "file", ContentType.APPLICATION_OCTET_STREAM, fileInfo.getFileName()), autoUnpack);
                        } else {
                            receiveJson = stringToReceiveJson(HttpExecutor.inputStreamUpload(httpExecutorContext,
                                UrlOptUtils.appendParamsToUrl(requestServerAddress, requestParams), fileIS,
                                "file", ContentType.APPLICATION_OCTET_STREAM, fileInfo.getFileName()), autoUnpack);
                        }
                    }
                } else { // json 或者 form
                    Object requestBody = getRequestBody(bizOptJson, bizModel);
                    if("put".equals(requestMode)){
                        if (ConstantValue.FORM_REQUEST_TYPE.equals(requestType)) {
                            saveDebugLog(bizOptJson, dataOptContext, requestServerAddress, requestParams, requestBody);
                            receiveJson = stringToReceiveJson(HttpExecutor.formPut(httpExecutorContext,
                                UrlOptUtils.appendParamsToUrl(requestServerAddress, requestParams), requestBody), autoUnpack);
                        } else if (ConstantValue.SOAP_REQUEST_TYPE.equals(requestType)) {
                            // WEB SERVICE 请求， 对返回的内容进行 处理
                            String xmlEntity = buildSoapXml(soapNameSpace, transUrl, requestBody, requestParams);
                            saveDebugLog(bizOptJson, dataOptContext, requestServerAddress, null, xmlEntity);
                            receiveJson = HttpReceiveJSON.dataOf(XMLObject.xmlStringToObject(
                                HttpExecutor.xmlPut(httpExecutorContext, requestServerAddress, xmlEntity)));
                        } else {
                            saveDebugLog(bizOptJson, dataOptContext, requestServerAddress, requestParams, requestBody);
                            receiveJson = stringToReceiveJson(HttpExecutor.jsonPut(httpExecutorContext,
                                UrlOptUtils.appendParamsToUrl(requestServerAddress, requestParams), requestBody), autoUnpack);
                        }
                    } else {
                        if (ConstantValue.FORM_REQUEST_TYPE.equals(requestType)) {
                            saveDebugLog(bizOptJson, dataOptContext, requestServerAddress, requestParams, requestBody);
                            receiveJson = stringToReceiveJson(HttpExecutor.formPost(httpExecutorContext,
                                UrlOptUtils.appendParamsToUrl(requestServerAddress, requestParams), requestBody, false), autoUnpack);
                        } else if (ConstantValue.SOAP_REQUEST_TYPE.equals(requestType)) {
                            // WEB SERVICE 请求， 对返回的内容进行 处理
                            String xmlEntity = buildSoapXml(soapNameSpace, transUrl, requestBody, requestParams);
                            saveDebugLog(bizOptJson, dataOptContext, requestServerAddress, null, xmlEntity);
                            receiveJson = HttpReceiveJSON.dataOf(XMLObject.xmlStringToObject(
                                HttpExecutor.xmlPost(httpExecutorContext, requestServerAddress, xmlEntity)));
                        } else {
                            saveDebugLog(bizOptJson, dataOptContext, requestServerAddress, requestParams, requestBody);
                            receiveJson = stringToReceiveJson(HttpExecutor.jsonPost(httpExecutorContext,
                                UrlOptUtils.appendParamsToUrl(requestServerAddress, requestParams), requestBody, false), autoUnpack);
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
                receiveJson = stringToReceiveJson(HttpExecutor.simpleGet(httpExecutorContext, requestServerAddress, requestParams), autoUnpack);
                if (receiveJson.getCode() != ResponseData.RESULT_OK && receiveJson.getCode() != ResponseData.HTTP_OK) {
                    return BuiltInOperation.createResponseData(0, 1, receiveJson.getCode(), receiveJson.getMessage());
                }
                break;
            case "delete":
                receiveJson = stringToReceiveJson(HttpExecutor.simpleDelete(httpExecutorContext, requestServerAddress, requestParams), autoUnpack);
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
            if(autoUnpack) {
                return BuiltInOperation.createResponseData(dataSet.getSize(), 0, receiveJson.getCode(), receiveJson.getMessage());
            } else {
                return BuiltInOperation.createResponseSuccessData(dataSet.getSize());
            }
        } else {
            return BuiltInOperation.createResponseData(0, 1, ResponseData.ERROR_OPERATION, "无数据");
        }
    }

    private String buildSoapXml(String soapNameSpace, String actionName, Object requestBody, Map<String, Object> requestParams){
        if(requestBody instanceof Map) {
            Map<?, ?> requestBodyMap = (Map<?, ?>) requestBody;
            if (!requestBodyMap.isEmpty())
                return SoapWsdlParser.buildSoapXml(soapNameSpace, actionName, requestBody);
        }
        return SoapWsdlParser.buildSoapXml(soapNameSpace, actionName, requestParams);
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
        String json = bizOptJson.getString("requestBody");
        if (StringUtils.isNotBlank(json)) {
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

    public static Map<String, FileDataSet> fetchMultiFiles(DataSet dataSet, JSONObject jsonStep, String fieldName) {
        Map<String, FileDataSet>  files = new LinkedHashMap<>();
        if(dataSet instanceof FileDataSet){
            files.put(fieldName, (FileDataSet) dataSet);
            return files;
        }

        String fileNameDesc = BuiltInOperation.getJsonFieldString(jsonStep, ConstantValue.FILE_NAME, "");
        String fileContentDesc = BuiltInOperation.getJsonFieldString(jsonStep, ConstantValue.FILE_CONTENT, "");
        int fileInd = 0;
        for(Map<String, Object> objectMap : dataSet.getDataAsList()){
            FileDataSet fileDataSet = FileDataSetOptUtil.mapDataToFile(objectMap, fileNameDesc, fileContentDesc);
            if(fileDataSet != null){
                String fileFieldName = StringBaseOpt.objectToString(objectMap.get(fieldName));
                if(StringUtils.isBlank(fileFieldName)){
                    fileFieldName = fileInd > 0 ? fieldName + fileInd: fieldName;
                }
                files.put(fileFieldName, fileDataSet);
                fileInd ++;
            }
        }
        return files;
    }

    private static MultipartEntityBuilder buildMultiPartEntity(Object requestBody, Map<String, FileDataSet> files) {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.RFC6532);
        builder.setBoundary(HttpExecutor.BOUNDARY);
        if(requestBody instanceof Map) {
            Map<String, Object> formObjects = CollectionsOpt.objectToMap(requestBody);
            if (formObjects != null) {
                ContentType contentType = ContentType.create("text/plain", Consts.UTF_8);
                for (Map.Entry<String, Object> param : formObjects.entrySet()) {
                    builder.addTextBody(param.getKey(),
                        JSONOpt.objectToJSONString(param.getValue()), contentType);
                }
            }
        }
        if(!files.isEmpty()){
            for(Map.Entry<String, FileDataSet> ent : files.entrySet()){
                String contentType = FileType.getFileMimeType(ent.getValue().getFileName());
                builder.addBinaryBody(ent.getKey(), ent.getValue().getFileInputStream(),
                    ContentType.create(contentType),
                    ent.getValue().getFileName());
            }
        }
        return builder;
    }

}
