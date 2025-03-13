package com.centit.dde.controller;

import com.alibaba.fastjson2.JSON;
import com.centit.dde.adapter.po.DataPacket;
import com.centit.dde.adapter.po.DataPacketInterface;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataOptResult;
import com.centit.dde.services.BizModelService;
import com.centit.dde.services.DataPacketDraftService;
import com.centit.dde.services.DataPacketService;
import com.centit.dde.utils.ConstantValue;
import com.centit.fileserver.utils.UploadDownloadUtils;
import com.centit.framework.common.JsonResultUtils;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.WebOptUtils;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.model.adapter.PlatformEnvironment;
import com.centit.framework.model.basedata.OsInfo;
import com.centit.framework.model.basedata.WorkGroup;
import com.centit.framework.model.security.CentitUserDetails;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.common.ObjectException;
import com.centit.support.file.FileIOOpt;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author codefan@sina.com
 */
public abstract class DoApiController extends BaseController {

    @Autowired
    protected PlatformEnvironment platformEnvironment;

    @Autowired
    protected DataPacketService dataPacketService;

    @Autowired
    protected DataPacketDraftService dataPacketDraftService;

    @Autowired
    protected BizModelService bizmodelService;

    protected Map<String, DataPacket> dataPacketCachedMap = new ConcurrentHashMap<>(10000);

    private void judgePower(@PathVariable String packetId, String loginUser, String runType, HttpServletRequest request) {
        if (ConstantValue.RUN_TYPE_DEBUG.equals(runType)) {
            if (StringUtils.isBlank(loginUser)) {
                throw new ObjectException(ResponseData.ERROR_USER_NOT_LOGIN,
                    getI18nMessage( "error.302.user_not_login", request));
            }
            DataPacketInterface dataPacket = dataPacketDraftService.getDataPacket(packetId);
            List<WorkGroup> userGroups = platformEnvironment.listWorkGroup(dataPacket.getOsId(), loginUser, null);
            if(CollectionUtils.isEmpty(userGroups)){
                throw new ObjectException(ResponseData.HTTP_NON_AUTHORITATIVE_INFORMATION,
                    getI18nMessage( "error.403.access_forbidden", request));
            }
        }
    }

    protected void returnObject(String packetId, String runType, String taskType, List<String> urlParams,
                                HttpServletRequest request, HttpServletResponse response) throws IOException {
        // judgePower(packetId,runType,request);
        // 获取接口信息
        DataPacketInterface dataPacketInterface;
        if(ConstantValue.RUN_TYPE_DEBUG.equals(runType)){
            dataPacketInterface = dataPacketDraftService.getDataPacket(packetId);
        } else {
            DataPacket dataPacket = dataPacketService.getDataPacket(packetId);
            DataPacket cachedPacket = dataPacketCachedMap.get(packetId);
            if(cachedPacket == null || DatetimeOpt.compareTwoDate(dataPacket.getPublishDate(), cachedPacket.getPublishDate()) != 0){
                dataPacketInterface = dataPacket;
                dataPacketCachedMap.put(packetId, dataPacket);
            } else {
                dataPacketInterface = cachedPacket;
            }
        }

        if (dataPacketInterface == null) {
            throw new ObjectException(ObjectException.DATA_NOT_FOUND_EXCEPTION,
               getI18nMessage("dde.604.api_not_found", request, packetId));
        }
        if (ConstantValue.TASK_TYPE_MSG.equals(dataPacketInterface.getTaskType())) {
            throw new ObjectException(ResponseData.HTTP_METHOD_NOT_ALLOWED,
                getI18nMessage("dde.405.method_not_support", request, packetId));
        }
        if (!taskType.equals(dataPacketInterface.getTaskType()) &&
            !ConstantValue.TASK_TYPE_TIME.equals(dataPacketInterface.getTaskType())) {
            throw new ObjectException(ResponseData.HTTP_METHOD_NOT_ALLOWED,
                getI18nMessage("dde.405.request_type_not_match", request, packetId));
        }
        if (ConstantValue.RUN_TYPE_NORMAL.equals(runType)
            && dataPacketInterface.getIsDisable() != null && dataPacketInterface.getIsDisable()) {
            throw new ObjectException(ResponseData.HTTP_METHOD_NOT_ALLOWED,
                getI18nMessage("dde.405.api_is_disable", request, packetId));
        }
        //根据api默认值初始化， 并且准备执行上下文环境
        Map<String, Object> params = new HashMap<>(dataPacketInterface.getPacketParamsValue());
        params.putAll(collectRequestParameters(request));
        //保存内部逻辑变量，有些时候需要将某些值传递到其它标签节点，这时候需要用到它
        DataOptContext dataOptContext = new DataOptContext(this.messageSource, WebOptUtils.getCurrentLocale(request));
        dataOptContext.setRunType(runType);
        Object obj =  params.get("debugId");
        if(obj!=null) {
            dataOptContext.setDebugId(StringBaseOpt.castObjectToString(obj));
        }
        obj =  params.get("breakStepNo");
        if(obj!=null) {
            dataOptContext.setBreakStepNo(NumberBaseOpt.castObjectToInteger(obj, -1));
        }

        if (ConstantValue.TASK_TYPE_POST.equals(taskType) || ConstantValue.TASK_TYPE_PUT.equals(taskType) ||
            "POST".equalsIgnoreCase(request.getMethod()) || "PUT".equalsIgnoreCase(request.getMethod())) {
            String contentType = request.getHeader("Content-Type");
            if (StringUtils.contains(contentType, "application/json")) {
                String bodyString = FileIOOpt.readStringFromInputStream(request.getInputStream(), String.valueOf(StandardCharsets.UTF_8));
                dataOptContext.setStackData(ConstantValue.REQUEST_BODY_TAG, JSON.parse(bodyString));
            } else if(StringUtils.contains(contentType, "application/x-www-form-urlencoded")){
                Map<String, Object> bodyMap = new HashMap<>(32);
                for(Map.Entry<String, String[]> ent : request.getParameterMap().entrySet()){
                    if(ent.getValue().length==1){
                        bodyMap.put(ent.getKey(), ent.getValue()[0]);
                    }else {
                        bodyMap.put(ent.getKey(), ent.getValue());
                    }
                }
                dataOptContext.setStackData(ConstantValue.REQUEST_BODY_TAG, bodyMap);
            }  else if(StringUtils.contains(contentType, "application/octet-stream")){
                String fileName = request.getHeader("filename");
                String fileName2 = StringBaseOpt.castObjectToString(params.get("fileName"));
                if (StringUtils.isBlank(fileName)) {
                    fileName = request.getHeader("fileName");
                    if(StringUtils.isBlank(fileName)) {
                        if (StringUtils.isNotBlank(fileName2)) {
                            fileName = fileName2;
                        }
                    }
                }
                if (StringUtils.isNotBlank(fileName) && StringUtils.isBlank(fileName2)) {
                    params.put("fileName", fileName);
                }

                InputStream inputStream = UploadDownloadUtils.fetchInputStreamFromMultipartResolver(request).getRight();
                if (inputStream != null) {
                    dataOptContext.setStackData(ConstantValue.REQUEST_FILE_TAG,
                        CollectionsOpt.createHashMap(
                            "fileName", fileName,
                            "fileSize", inputStream.available(),
                            "fileContent", inputStream));
                }
            } else if(StringUtils.contains(contentType, "text/plain")){
                String bodyString = FileIOOpt.readStringFromInputStream(request.getInputStream(),
                    String.valueOf(StandardCharsets.UTF_8));
                dataOptContext.setStackData(ConstantValue.REQUEST_BODY_TAG, bodyString);
            } else if(StringUtils.contains(contentType, "multipart/form-data")){
                MultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
                MultipartHttpServletRequest multiRequest = resolver.resolveMultipart(request);
                Map<String, Object> bodyMap = new HashMap<>(32);
                Map<String, String[]> mParams =  multiRequest.getParameterMap();
                if(mParams!=null) {
                    for (Map.Entry<String, String[]> entry : mParams.entrySet()) {
                        if(entry.getValue()!=null) {
                            if (entry.getValue().length == 1) {
                                bodyMap.put(entry.getKey(), entry.getValue()[0]);
                            } else {
                                bodyMap.put(entry.getKey(), entry.getValue());
                            }
                        }
                    }
                }

                Map<String, MultipartFile> map = multiRequest.getFileMap();
                for (Map.Entry<String, MultipartFile> entry : map.entrySet()) {
                    CommonsMultipartFile cMultipartFile = (CommonsMultipartFile) entry.getValue();
                    FileItem fi = cMultipartFile.getFileItem();
                    String fieldName = fi.getFieldName();
                    String itemType = fi.getHeaders().getHeader("Content-Type");
                    if (!fi.isFormField() || itemType.contains("application/octet-stream")) {
                        String filename = fi.getHeaders().getHeader("filename");
                        if (StringUtils.isBlank(filename)) {
                            filename = StringBaseOpt.castObjectToString(params.get("filename"));
                            if (StringUtils.isBlank(filename)) {
                                filename = StringBaseOpt.castObjectToString(params.get("fileName"));
                            }
                        }
                        String fileName2 = StringBaseOpt.castObjectToString(params.get("fileName"));
                        if (StringUtils.isNotBlank(filename) && StringUtils.isBlank(fileName2)) {
                            params.put("fileName", filename);
                        }
                        InputStream inputStream = fi.getInputStream();
                        if (inputStream != null) {
                            Map<String, Object> fileData = CollectionsOpt.createHashMap(
                                "fileName", filename,
                                "fileSize", inputStream.available(),
                                "fileContent", inputStream);
                            dataOptContext.setStackData(ConstantValue.REQUEST_FILE_TAG, fileData);
                            bodyMap.put(fieldName, fileData);
                        }
                    } else if (itemType.contains("application/json")) {
                        String bodyString = fi.getString();
                        bodyMap.put(fieldName, JSON.parse(bodyString));
                    } else { //if (itemType.contains("text/plain")) {
                        String bodyString = fi.getString();
                        bodyMap.put(fieldName, bodyString);
                    }
                }
                dataOptContext.setStackData(ConstantValue.REQUEST_BODY_TAG, bodyMap);
            } else { //
                throw new ObjectException(ObjectException.FUNCTION_NOT_SUPPORT,
                    getI18nMessage("dde.613.form_type_not_support", request, contentType));
            }
        }

        dataOptContext.setStackData(ConstantValue.REQUEST_PARAMS_TAG, params);
        if(urlParams != null && !urlParams.isEmpty()) {
            dataOptContext.setStackData(ConstantValue.REQUEST_URL_PARAMS_TAG, urlParams);
        }

        Map<String, String> cookies = WebOptUtils.fetchRequestCookies(request);
        if(cookies!=null){
            dataOptContext.setStackData(ConstantValue.REQUEST_COOKIES_TAG, cookies);
        }

        Map<String, String> headers = WebOptUtils.fetchRequestHeaders(request);
        if(cookies!=null){
            dataOptContext.setStackData(ConstantValue.REQUEST_HEADERS_TAG, headers);
        }
        //request.getCookies().
        OsInfo osInfo = platformEnvironment.getOsInfo(dataPacketInterface.getOsId());
        dataOptContext.setStackData(ConstantValue.APPLICATION_INFO_TAG, osInfo);
        CentitUserDetails userDetails = WebOptUtils.getCurrentUserDetails(request);
        if(userDetails!=null) {
            dataOptContext.setStackData(ConstantValue.SESSION_DATA_TAG, userDetails);
            dataOptContext.setTopUnit(userDetails.getTopUnitCode());
        } // 准备运行环境完毕
        // 调用DDE数据执行引擎
        DataOptResult result = bizmodelService.runBizModel(dataPacketInterface, dataOptContext);
        // 返回
        if (result.getResultType() == DataOptResult.RETURN_FILE_STREAM) {
            InputStream in = result.getReturnFileStream();
            if (in != null /*&& fileName != null*/) {
                UploadDownloadUtils.downFileRange(request, response, in,
                    in.available(), result.getReturnFilename(), request.getParameter("downloadType"), null);
            } else {
                JsonResultUtils.writeOriginalObject(
                    ResponseData.makeErrorMessage(ObjectException.DATA_NOT_FOUND_EXCEPTION,
                        getI18nMessage("dde.604.return_file_not_found", request)), response);
            }
            return;
        }
        if (result.getResultType() == DataOptResult.RETURN_DATA_AS_RAW) {
            JsonResultUtils.writeOriginalObject(result.getResultObject(), response);
        } else {
            JsonResultUtils.writeOriginalObject(result.toResponseData(), response);
        }
    }


}
