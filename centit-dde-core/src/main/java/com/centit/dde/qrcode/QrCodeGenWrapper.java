package com.centit.dde.qrcode;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.dataset.FileDataSet;

import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.image.QrCodeConfig;
import com.centit.support.image.QrCodeGenerator;
import com.centit.support.office.ImagesToPdf;
import com.lowagie.text.Image;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class QrCodeGenWrapper {
    private static final int QUIET_ZONE_SIZE = 4;


    public static Object createQrCode(Object qrCodeParams){
        if (qrCodeParams instanceof Map) {
            JSONObject codeParams = JSON.parseObject(StringBaseOpt.castObjectToString(qrCodeParams));
            if (codeParams != null){
                JSONObject qrParams = codeParams.getJSONObject("qrParams");
                Object dataParams = codeParams.get("dataParams");
                if (qrParams == null || dataParams == null){
                    return "qrParams 和 dataParams 不能为空！";
                }
                //写入二维码数据的字段，多个逗号隔开  不传默认全部字段写入
                String dataField = qrParams.getString("dataField");
                List<String> fieldNames = new ArrayList<>();
                if (StringUtils.isNotBlank(dataField)){
                    fieldNames.addAll(Arrays.stream(dataField.split(",")).collect(Collectors.toList()));
                }
                List<Image> imageList = new ArrayList<>();
                QrCodeConfig qrCodeConfig = createQrCodeConfig(qrParams);
                if (dataParams instanceof Collection){
                    JSONArray dataParamsArr = codeParams.getJSONArray("dataParams");
                    Object topText = qrParams.get("topText");
                    Object downText = qrParams.get("downText");
                    for (int i = 0; i < dataParamsArr.size(); i++) {
                        String tempTopText = "";
                        String tempDownText = "";
                        if (topText instanceof Collection){
                            tempTopText = StringBaseOpt.castObjectToString(qrParams.getJSONArray("topText").get(i));
                        }
                        if (topText instanceof String){
                            tempTopText =  StringBaseOpt.castObjectToString(topText);
                        }
                        if (downText instanceof Collection){
                            tempDownText = StringBaseOpt.castObjectToString(qrParams.getJSONArray("downText").get(i));
                        }
                        if (downText instanceof String){
                            tempDownText = StringBaseOpt.castObjectToString(downText);
                        }
                        qrCodeConfig.setTopText(tempTopText);
                        qrCodeConfig.setDownText(tempDownText);
                        Object o = dataParamsArr.get(i);
                        if (o instanceof  Map){
                            JSONObject jsonObject = JSON.parseObject(StringBaseOpt.castObjectToString(o));
                            if (fieldNames.size() > 0){
                                JSONObject context = new JSONObject();
                                for (String fieldName : fieldNames) {
                                    context.put(fieldName,jsonObject.get(fieldName));
                                }
                                qrCodeConfig.setMsg(context.toJSONString());
                            }else {
                                qrCodeConfig.setMsg(jsonObject.toJSONString());
                            }
                        }
                        if (o instanceof String){
                            qrCodeConfig.setMsg((String) o);
                        }
                        try( ByteArrayOutputStream outputStream = new ByteArrayOutputStream()){
                            BufferedImage bufferedImage = QrCodeGenerator.asBufferedImage(qrCodeConfig);
                            ImageIO.write(bufferedImage, "JPG", outputStream);
                            Image image = Image.getInstance(outputStream.toByteArray());
                            imageList.add(image);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (dataParams instanceof Map){
                    JSONObject dataParamsMap = codeParams.getJSONObject("dataParams");
                    Boolean qrCodeToPdf = qrParams.getBoolean("qrCodeToPdf");
                    qrCodeConfig.setTopText(StringBaseOpt.castObjectToString(qrParams.get("topText")));
                    qrCodeConfig.setDownText(StringBaseOpt.castObjectToString(qrParams.get("downText")));
                    if (fieldNames.size() > 0){
                        JSONObject context = new JSONObject();
                        for (String fieldName : fieldNames) {
                            context.put(fieldName,dataParamsMap.get(fieldName));
                        }
                        qrCodeConfig.setMsg(context.toJSONString());
                    }else {
                        qrCodeConfig.setMsg(dataParamsMap.toJSONString());
                    }
                    try( ByteArrayOutputStream outputStream = new ByteArrayOutputStream()){
                        BufferedImage bufferedImage = QrCodeGenerator.asBufferedImage(qrCodeConfig);
                        ImageIO.write(bufferedImage, "JPG", outputStream);
                        //写入pdf
                        if (qrCodeToPdf){
                            imageList.add(Image.getInstance(outputStream.toByteArray()));
                        }else {
                            //直接返回二维码
                            String fileName = codeParams.getString("fileName");
                            String codeName = StringUtils.isNotBlank(fileName) ? fileName.endsWith(".jpg") ? fileName
                                : fileName + ".jpg"
                                : System.currentTimeMillis()+".jpg";
                            FileDataSet dataSet = new FileDataSet();
                            InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
                            dataSet.setFileContent(codeName, inputStream.available(), inputStream);
                            return dataSet;
                        }
                    } catch (Exception e) {
                        return "生成二维码异常！";
                    }
                }
                if (imageList.size() > 0 ){
                    try ( ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()){
                        ImagesToPdf.imagesToA4SizePdf(imageList, byteArrayOutputStream);
                        String fileName = codeParams.getString("fileName");
                        String codeName = StringUtils.isNotBlank(fileName) ? fileName.endsWith(".pdf") ? fileName :  fileName + ".pdf"
                            : System.currentTimeMillis()+".pdf";
                        FileDataSet dataSet = new FileDataSet();
                        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
                        dataSet.setFileContent(codeName, inputStream.available(), inputStream);
                        return dataSet;
                    } catch (Exception e) {
                        return "写入PDF文件异常！";
                    }
                }
            }
        }
        return  "未正确传入指定参数！";
    }

    //构建生成二维码参数
    private static QrCodeConfig createQrCodeConfig(JSONObject codeParams){
        QrCodeConfig config = new QrCodeConfig();

            //.setMsg(StringBaseOpt.castObjectToString(writeQrCodeData))
        config.setQrHeight(codeParams.getInteger("height"));
        config.setQrWidth(codeParams.getInteger("width"));
        config.setPadding(codeParams.getInteger("padding"));
            //.setTopText(topText)
        config.setTopTextFontSize(codeParams.getInteger("topTextFontSize"));
        config.setTopTextFontType(codeParams.getString("topTextFontType"));
            //.setDownText(downText)
        config.setDownTextFontSize(codeParams.getInteger("downTextFontSize"));
        config.setDownTextFontType(codeParams.getString("downTextFontType"));
        config.setLogo(codeParams.getString("logImageUrl"));
        return config;
    }

}
