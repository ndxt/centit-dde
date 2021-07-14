package com.centit.dde.webservice.wsdl;

import com.centit.dde.po.DataPacket;
import com.centit.dde.po.DataPacketCopy;
import com.centit.dde.service.ExchangeService;
import com.centit.dde.services.DataPacketCopyService;
import com.centit.dde.services.DataPacketService;
import com.centit.dde.utils.ConstantValue;
import com.centit.fileserver.utils.UploadDownloadUtils;
import com.centit.framework.core.controller.BaseController;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.file.FileIOOpt;
import com.centit.support.xml.XMLObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.ContextLoaderListener;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;

@WebService(serviceName = "SoapPacketWebServices",//接口名称，与service中的name一致
    targetNamespace = "http://wsdl.webservice.dde.centit.com/",//与接口中的命名空间一致，一般是接口的包名
    endpointInterface = "com.centit.dde.webservice.wsdl.SoapPacketWebServices")//接口地址
public class SoapPacketWebServicesImpl implements SoapPacketWebServices {

    @Resource
    WebServiceContext webServiceContext;

    @Override
    public void runTestTaskExchange(String packetId) throws IOException {
        HttpServletRequest request = (HttpServletRequest) webServiceContext.getMessageContext().get(MessageContext.SERVLET_REQUEST);
        HttpServletResponse response = (HttpServletResponse)webServiceContext.getMessageContext().get(MessageContext.SERVLET_RESPONSE);
        returnObject(packetId, ConstantValue.RUN_TYPE_COPY, request, response);
    }

    @Override
    public void debugRunTestTaskExchange(String packetId)throws IOException {
        HttpServletRequest request = (HttpServletRequest) webServiceContext.getMessageContext().get(MessageContext.SERVLET_REQUEST);
        HttpServletResponse response = (HttpServletResponse)webServiceContext.getMessageContext().get(MessageContext.SERVLET_RESPONSE);
        returnObject(packetId, ConstantValue.RUN_TYPE_COPY, request, response);
    }

    @Override
    public String runTaskExchange(String packetId)throws IOException {
        HttpServletRequest request = (HttpServletRequest) webServiceContext.getMessageContext().get(MessageContext.SERVLET_REQUEST);
        HttpServletResponse response = (HttpServletResponse)webServiceContext.getMessageContext().get(MessageContext.SERVLET_RESPONSE);
        return returnObject(packetId, ConstantValue.RUN_TYPE_NORMAL, request, response);
    }

    @Override
    public void debugRunTaskExchange(String packetId)throws IOException {
        HttpServletRequest request = (HttpServletRequest) webServiceContext.getMessageContext().get(MessageContext.SERVLET_REQUEST);
        HttpServletResponse response = (HttpServletResponse)webServiceContext.getMessageContext().get(MessageContext.SERVLET_RESPONSE);
        returnObject(packetId, ConstantValue.RUN_TYPE_NORMAL, request, response);
    }

    @Override
    public void runPostTest(String packetId)throws IOException {
        HttpServletRequest request = (HttpServletRequest) webServiceContext.getMessageContext().get(MessageContext.SERVLET_REQUEST);
        HttpServletResponse response = (HttpServletResponse)webServiceContext.getMessageContext().get(MessageContext.SERVLET_RESPONSE);
        returnObject(packetId, ConstantValue.RUN_TYPE_COPY, request, response);
    }

    @Override
    public void runTaskPost(String packetId)throws IOException {
        HttpServletRequest request = (HttpServletRequest) webServiceContext.getMessageContext().get(MessageContext.SERVLET_REQUEST);
        HttpServletResponse response = (HttpServletResponse)webServiceContext.getMessageContext().get(MessageContext.SERVLET_RESPONSE);
        returnObject(packetId, ConstantValue.RUN_TYPE_NORMAL, request, response);
    }

    private String returnObject(String packetId,String runType,HttpServletRequest request,HttpServletResponse response) throws IOException {
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
       /* if (bizModel instanceof DataSet) {
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
        }*/
        String text = XMLObject.objectToXMLString("response", bizModel);
        return text;
      /*  response.setCharacterEncoding("UTF-8");
        response.setContentType("text/xml; charset=utf-8");
        response.getOutputStream().write(text.getBytes(StandardCharsets.UTF_8));
        response.getOutputStream().close();*/

       /* JsonResultUtils.writeSingleDataXml(bizModel, response);
        response.getWriter().close();*/
    }
}
