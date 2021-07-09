package com.centit.dde.webservice.restful;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.DataSet;
import com.centit.dde.po.DataPacket;
import com.centit.dde.po.DataPacketCopy;
import com.centit.dde.service.ExchangeService;
import com.centit.dde.services.DataPacketCopyService;
import com.centit.dde.services.DataPacketService;
import com.centit.dde.utils.ConstantValue;
import com.centit.dde.webservice.RestFulPacketWebServices;
import com.centit.fileserver.utils.UploadDownloadUtils;
import com.centit.framework.common.JsonResultUtils;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.ResponseMapData;
import com.centit.framework.core.controller.BaseController;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.file.FileIOOpt;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.ContextLoaderListener;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class RestFulPacketWebServicesImpl implements RestFulPacketWebServices {

    @Override
    public void runTestTaskExchange(String packetId, HttpServletRequest request, HttpServletResponse response) throws IOException {
        returnObject(packetId, ConstantValue.RUN_TYPE_COPY, request, response);
    }

    @Override
    public void debugRunTestTaskExchange(String packetId, HttpServletRequest request, HttpServletResponse response)throws IOException {
        returnObject(packetId, ConstantValue.RUN_TYPE_COPY, request, response);
    }

    @Override
    public void runTaskExchange(String packetId, HttpServletRequest request, HttpServletResponse response)throws IOException {
        returnObject(packetId, ConstantValue.RUN_TYPE_NORMAL, request, response);
    }

    @Override
    public void debugRunTaskExchange(String packetId, HttpServletRequest request, HttpServletResponse response)throws IOException {
        returnObject(packetId, ConstantValue.RUN_TYPE_NORMAL, request, response);
    }

    @Override
    public void runPostTest(String packetId, HttpServletRequest request, HttpServletResponse response)throws IOException {
        returnObject(packetId, ConstantValue.RUN_TYPE_COPY, request, response);
    }

    @Override
    public void runTaskPost(String packetId, HttpServletRequest request, HttpServletResponse response)throws IOException {
        returnObject(packetId, ConstantValue.RUN_TYPE_NORMAL, request, response);
    }


    private void returnObject(String packetId,String runType,HttpServletRequest request,HttpServletResponse response) throws IOException {
        DataPacketService dataPacketService = ContextLoaderListener.getCurrentWebApplicationContext().getBean(DataPacketService.class);
        DataPacketCopyService dataPacketCopyService = ContextLoaderListener.getCurrentWebApplicationContext().getBean(DataPacketCopyService.class);
        ExchangeService exchangeService = ContextLoaderListener.getCurrentWebApplicationContext().getBean(ExchangeService.class);
        Map<String, Object> params = BaseController.collectRequestParameters(request);
        params.put("runType",runType);
        if(!"GET".equals(request.getMethod())) {
            if (StringUtils.contains(request.getHeader("content-type"), "application")) {
                String bodyString = FileIOOpt.readStringFromInputStream(request.getInputStream(), String.valueOf(Charset.forName("utf-8")));
                if (!StringBaseOpt.isNvl(bodyString)) {
                    params.put("requestBody", bodyString);
                }
            } else {
                InputStream inputStream = UploadDownloadUtils.fetchInputStreamFromMultipartResolver(request).getRight();
                if (inputStream != null) {
                    params.put("requestFile", inputStream);
                }
            }
        }
        Object bizModel;
        if (ConstantValue.RUN_TYPE_NORMAL.equals(runType)) {
            DataPacket dataPacket = dataPacketService.getDataPacket(packetId);
            bizModel = dataPacketService.fetchDataPacketDataFromBuf(dataPacket, params);
            if (bizModel == null) {
                bizModel = exchangeService.runTask(packetId, params);
                dataPacketService.setDataPacketBuf(bizModel, dataPacket, params);
            }
        } else {
            DataPacketCopy dataPacketCopy = dataPacketCopyService.getDataPacket(packetId);
            bizModel = dataPacketCopyService.fetchDataPacketDataFromBuf(dataPacketCopy, params);
            if (bizModel == null) {
                bizModel = exchangeService.runTask(packetId, params);
                dataPacketCopyService.setDataPacketBuf(bizModel, dataPacketCopy, params);
            }
        }
        if (bizModel instanceof DataSet) {
            InputStream in;
            String fileName;
            Map<String, Object> mapFirstRow = ((DataSet) bizModel).getFirstRow();
            if (mapFirstRow != null && mapFirstRow.containsKey(ConstantValue.FILE_CONTENT)
                && (mapFirstRow.get(ConstantValue.FILE_CONTENT) instanceof OutputStream
                || mapFirstRow.get(ConstantValue.FILE_CONTENT) instanceof InputStream)) {
                if (mapFirstRow.get(ConstantValue.FILE_CONTENT) instanceof OutputStream) {
                    ByteArrayOutputStream outputStream = (ByteArrayOutputStream) ((DataSet) bizModel).getFirstRow().get(ConstantValue.FILE_CONTENT);
                    in = new ByteArrayInputStream(outputStream.toByteArray());
                } else {
                    in = (InputStream) mapFirstRow.get(ConstantValue.FILE_CONTENT);
                }
                fileName = (String) ((DataSet) bizModel).getFirstRow().get(ConstantValue.FILE_NAME);
                UploadDownloadUtils.downFileRange(request, response, in,
                    in.available(), fileName, request.getParameter("downloadType"), null);
                return;
            }
        }
        if (bizModel instanceof ResponseMapData) {
            JsonResultUtils.writeSingleDataJson(((ResponseMapData) bizModel).getCode(),((ResponseMapData) bizModel).getMessage(),bizModel, response,null);
            return;
        }
        if(bizModel instanceof ResponseData){
            JsonResultUtils.writeResponseDataAsJson((ResponseData) bizModel,response);
            return;
        }
        response.getOutputStream().write(JSONObject.toJSONString(bizModel).getBytes(StandardCharsets.UTF_8));
    }
}
