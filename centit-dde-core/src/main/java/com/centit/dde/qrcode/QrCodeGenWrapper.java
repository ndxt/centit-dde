package com.centit.dde.qrcode;

import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.dataset.FileDataSet;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.compiler.ObjectTranslate;
import com.centit.support.compiler.VariableFormula;
import com.centit.support.image.ImageOpt;
import com.centit.support.image.QrCodeConfig;
import com.centit.support.image.QrCodeGenerator;
import com.centit.support.office.ImagesToPdf;
import com.google.zxing.WriterException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Slf4j
public class QrCodeGenWrapper {

    protected static final Logger logger = LoggerFactory.getLogger(QrCodeGenWrapper.class);

    public static Object createQrCode(Object qrCodeParams)  {
        if(!(qrCodeParams instanceof Map)){
            return  "未正确传入指定参数！";
        }
        JSONObject codeParams = JSONObject.from(qrCodeParams);

        JSONObject qrParams = codeParams.getJSONObject("qrParams");
        QrCodeConfig qrCodeConfig = loadQrCodeConfig(qrParams);
        Object dataParams = codeParams.get("dataParams");

        if(dataParams instanceof Collection){
            List<BufferedImage> imageList = new ArrayList<>();

            String dataField = qrParams.getString("dataField");
            String topTextField = qrParams.getString("topTextField");
            String downTextField = qrParams.getString("downTextField");

            for(Object obj : (Collection<?>)dataParams){
                String msg=null;
                VariableFormula formula = new VariableFormula();
                formula.setTrans(new ObjectTranslate(obj));

                if(StringUtils.isNotBlank(dataField)){
                    msg = StringBaseOpt.castObjectToString(formula.calcFormula(dataField));
                }
                if(StringUtils.isBlank(msg)) {
                    msg = StringBaseOpt.castObjectToString(obj);
                }
                qrCodeConfig.setTopText(StringBaseOpt.castObjectToString(formula.calcFormula(topTextField)));
                qrCodeConfig.setDownText(StringBaseOpt.castObjectToString(formula.calcFormula(downTextField)));
                qrCodeConfig.setMsg(msg);
                try {
                    BufferedImage bufferedImage = QrCodeGenerator.asBufferedImage(qrCodeConfig);
                    imageList.add(bufferedImage);
                }catch (IOException | WriterException e){
                    logger.error(e.getMessage());
                }

            }
            String fileName = qrParams.getString("fileName");
            try ( ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
                ImagesToPdf.bufferedImagesToA4SizePdf(imageList, byteArrayOutputStream);
                fileName = StringUtils.isNotBlank(fileName) ? ( fileName.endsWith(".pdf") ? fileName : fileName + ".pdf")
                    : System.currentTimeMillis() + ".pdf";
                FileDataSet dataSet = new FileDataSet();
                InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
                dataSet.setFileContent(fileName, inputStream.available(), inputStream);
                return dataSet;
            } catch (IOException e) {
                logger.error(e.getMessage());
                return null;
            }

        } else {
            try {
                qrCodeConfig.setTopText(qrParams.getString("topText"));
                qrCodeConfig.setDownText(qrParams.getString("downText"));
                qrCodeConfig.setMsg(StringBaseOpt.castObjectToString(dataParams));
                BufferedImage bufferedImage = QrCodeGenerator.asBufferedImage(qrCodeConfig);
                return ImageOpt.imageToByteArray(bufferedImage);
            }catch (IOException | WriterException e){
                logger.error(e.getMessage());
                return null;
            }
        }

    }

    //构建生成二维码参数
    private static QrCodeConfig loadQrCodeConfig(JSONObject codeParams){
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
