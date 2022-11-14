package com.centit.dde.qrcode;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.dataset.FileDataSet;
import com.centit.dde.qrcode.config.QrCodeConfig;
import com.centit.dde.qrcode.utils.ImageUtil;
import com.centit.dde.qrcode.utils.MatrixToImageUtil;
import com.centit.support.algorithm.StringBaseOpt;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import com.google.zxing.qrcode.encoder.Encoder;
import com.google.zxing.qrcode.encoder.QRCode;
import com.lowagie.text.Image;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class QrCodeGenWrapper {
    private static final int QUIET_ZONE_SIZE = 4;

    public static QrCodeConfig.QrCodeConfigBuilder createQrCodeConfig() {
        return new QrCodeConfig.QrCodeConfigBuilder();
    }

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
                            BufferedImage bufferedImage = QrCodeGenWrapper.asBufferedImage(qrCodeConfig);
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
                        BufferedImage bufferedImage = QrCodeGenWrapper.asBufferedImage(qrCodeConfig);
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
                        ImageUtil.imagesToPdf(imageList,byteArrayOutputStream);
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
        return codeParams == null ?  QrCodeGenWrapper.createQrCodeConfig().build() :  QrCodeGenWrapper.createQrCodeConfig()
            //.setMsg(StringBaseOpt.castObjectToString(writeQrCodeData))
            .setQrHeight(codeParams.getInteger("height"))
            .setQrWidth(codeParams.getInteger("width"))
            .setPadding(codeParams.getInteger("padding"))
            //.setTopText(topText)
            .setTopTextFontSize(codeParams.getInteger("topTextFontSize"))
            .setTopTextFontType(codeParams.getString("topTextFontType"))
            //.setDownText(downText)
            .setDownTextFontSize(codeParams.getInteger("downTextFontSize"))
            .setDownTextFontType(codeParams.getString("downTextFontType"))
            .setLogo(codeParams.getString("logImageUrl"))
            .build();
    }

    /**
     * 生成二维码流
     * @param qrCodeConfig 二维码配置信息
     * @return BufferedImage 二维码流
     * @throws WriterException
     * @throws IOException
     */
    public static BufferedImage asBufferedImage(QrCodeConfig qrCodeConfig) throws Exception {
        BitMatrix bitMatrix = encode(qrCodeConfig);
        return MatrixToImageUtil.toBufferedImage(qrCodeConfig, bitMatrix);
    }


    /**
     * 对 zxing 的 QRCodeWriter 进行扩展, 解决白边过多的问题
     * <p/>
     * 源码参考 {@link com.google.zxing.qrcode.QRCodeWriter#encode(String, BarcodeFormat, int, int, Map)}
     */
    private static BitMatrix encode(QrCodeConfig qrCodeConfig) throws WriterException {
        ErrorCorrectionLevel errorCorrectionLevel = ErrorCorrectionLevel.Q;
        int quietZone = 1;
        if (qrCodeConfig.getHints() != null) {
            if (qrCodeConfig.getHints().containsKey(EncodeHintType.ERROR_CORRECTION)) {
                errorCorrectionLevel = ErrorCorrectionLevel.valueOf(qrCodeConfig.getHints().get(EncodeHintType.ERROR_CORRECTION).toString());
            }
            if (qrCodeConfig.getHints().containsKey(EncodeHintType.MARGIN)) {
                quietZone = Integer.parseInt(qrCodeConfig.getHints().get(EncodeHintType.MARGIN).toString());
            }
            if (quietZone > QUIET_ZONE_SIZE) {
                quietZone = QUIET_ZONE_SIZE;
            } else if (quietZone < 0) {
                quietZone = 0;
            }
        }
        QRCode code = Encoder.encode(qrCodeConfig.getMsg(), errorCorrectionLevel, qrCodeConfig.getHints());
        return renderResult(code, qrCodeConfig.getQrWidth(), qrCodeConfig.getQrHeight(), quietZone);
    }


    /**
     * 对 zxing 的 QRCodeWriter 进行扩展, 解决白边过多的问题
     * <p/>
     * 源码参考 {@link com.google.zxing.qrcode.QRCodeWriter #renderResult(QRCode, int, int, int)}
     *
     * @param code
     * @param width
     * @param height
     * @param quietZone 取值 [0, 4]
     * @return
     */
    private static BitMatrix renderResult(QRCode code, int width, int height, int quietZone) {
        ByteMatrix input = code.getMatrix();
        if (input == null) {
            throw new IllegalStateException();
        }
        // xxx 二维码宽高相等, 即 qrWidth == qrHeight
        int inputWidth = input.getWidth();
        int inputHeight = input.getHeight();
        int qrWidth = inputWidth + (quietZone * 2);
        int qrHeight = inputHeight + (quietZone * 2);
        // 白边过多时, 缩放
        int minSize = Math.min(width, height);
        int scale = calculateScale(qrWidth, minSize);
        if (scale > 0) {
            if (log.isDebugEnabled()) {
                log.debug("qrCode scale enable! scale: {}, qrSize:{}, expectSize:{}x{}", scale, qrWidth, width, height);
            }
            int padding, tmpValue;
            // 计算边框留白
            padding = (minSize - qrWidth * scale) / QUIET_ZONE_SIZE * quietZone;
            tmpValue = qrWidth * scale + padding;
            if (width == height) {
                width = tmpValue;
                height = tmpValue;
            } else if (width > height) {
                width = width * tmpValue / height;
                height = tmpValue;
            } else {
                height = height * tmpValue / width;
                width = tmpValue;
            }
        }
        int outputWidth = Math.max(width, qrWidth);
        int outputHeight = Math.max(height, qrHeight);

        int multiple = Math.min(outputWidth / qrWidth, outputHeight / qrHeight);
        int leftPadding = (outputWidth - (inputWidth * multiple)) / 2;
        int topPadding = (outputHeight - (inputHeight * multiple)) / 2;

        BitMatrix output = new BitMatrix(outputWidth, outputHeight);

        for (int inputY = 0, outputY = topPadding; inputY < inputHeight; inputY++, outputY += multiple) {
            // Write the contents of this row of the barcode
            for (int inputX = 0, outputX = leftPadding; inputX < inputWidth; inputX++, outputX += multiple) {
                if (input.get(inputX, inputY) == 1) {
                    output.setRegion(outputX, outputY, multiple, multiple);
                }
            }
        }

        return output;
    }


    /**
     * 如果留白超过15% , 则需要缩放
     * (15% 可以根据实际需要进行修改)
     *
     * @param qrCodeSize 二维码大小
     * @param expectSize 期望输出大小
     * @return 返回缩放比例, <= 0 则表示不缩放, 否则指定缩放参数
     */
    private static int calculateScale(int qrCodeSize, int expectSize) {
        if (qrCodeSize >= expectSize) {
            return 0;
        }
        int scale = expectSize / qrCodeSize;
        int abs = expectSize - scale * qrCodeSize;
        if (abs < expectSize * 0.15) {
            return 0;
        }
        return scale;
    }
}
