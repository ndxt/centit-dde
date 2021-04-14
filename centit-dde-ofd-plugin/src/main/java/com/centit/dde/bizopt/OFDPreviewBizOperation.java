package com.centit.dde.bizopt;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataSet;
import com.centit.dde.core.SimpleDataSet;
import com.centit.dde.vo.OFDPreviewVo;
import com.centit.framework.common.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.*;

/**
 * 预览OFD文件
 */
public class OFDPreviewBizOperation implements BizOperation {

    public static final Log log = LogFactory.getLog(OFDPreviewBizOperation.class);

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson){
        OFDPreviewVo ofdPreviewVo = bizOptJson.toJavaObject(OFDPreviewVo.class);
        String httpUrl = ofdPreviewVo.getHttpUrl();
        if ( StringUtils.isBlank(httpUrl)){
            return ResponseData.makeErrorMessage("服务地址为空，请填写服务地址！");
        }
        //获取数据集
        DataSet dataSet = bizModel.getDataSet(ofdPreviewVo.getSource());
        Object data = dataSet.getData();
        if (data instanceof OutputStream){
            ByteArrayOutputStream byteArrayOutputStream =  (ByteArrayOutputStream)data;
            httpUrl=httpUrl.endsWith("/")?httpUrl:httpUrl+"/";
            JSONObject jsonObject = httpPostRequest(httpUrl, byteArrayOutputStream, System.currentTimeMillis()+".ofd");
            if (jsonObject==null){
                return ResponseData.makeErrorMessage("请求预览服务异常！");
            }
            String previewUrl=httpUrl+"reader?file="+jsonObject.getString("name");
            bizModel.putDataSet(ofdPreviewVo.getId(),new SimpleDataSet(previewUrl));
            return ResponseData.makeSuccessResponse("ok");
        }
        return null;
    }


    public static JSONObject httpPostRequest(String urlParam,ByteArrayOutputStream byteArrayOutputStream,String fileName){
        if (StringUtils.isNotBlank(urlParam)){
            urlParam=urlParam+"reader?&type=upload";
        }
        TimeInterval timer = DateUtil.timer();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost uploadFile = new HttpPost(urlParam);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        // builder.addTextBody("field1", "yes", ContentType.TEXT_PLAIN);
        ByteArrayInputStream input = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        // 把文件加到HTTP的post请求中
        builder.addBinaryBody("file", input, ContentType.APPLICATION_OCTET_STREAM,fileName);
        HttpEntity multipart = builder.build();
        uploadFile.setEntity(multipart);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(uploadFile);
            HttpEntity responseEntity = response.getEntity();
            String sResponse= EntityUtils.toString(responseEntity, "UTF-8");
            log.info("请求OFD预览服务,请求地址："+urlParam+"，返回结果："+sResponse+"请求耗时："+timer.interval());
            return JSONObject.parseObject(sResponse);
        } catch (IOException e) {
            log.error("请求OFD预览服务异常，异常信息："+e.getMessage());
        }finally {
            try {
                if (response!=null){
                    response.close();
                }
                if (httpClient!=null){
                    httpClient.close();
                }
            }catch (Exception e){
                e.getMessage();
            }
        }
       return null;
    }
}
