package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.adapter.utils.ConstantValue;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.dataset.FileDataSet;
import com.centit.dde.utils.DataSetOptUtil;
import com.centit.fileserver.common.FileInfoOpt;
import com.centit.framework.common.ResponseData;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.image.ImageOpt;
import com.centit.support.image.QrCodeConfig;
import com.centit.support.image.QrCodeGenerator;
import com.centit.support.office.ImagesToPdf;
import com.google.zxing.WriterException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QrCodeOperation  implements BizOperation {

    protected static final Logger logger = LoggerFactory.getLogger(QrCodeOperation.class);

    private FileInfoOpt fileInfoOpt;

    public QrCodeOperation(FileInfoOpt fileInfoOpt) {
        this.fileInfoOpt = fileInfoOpt;
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {

        String source = bizOptJson.getString("source");
        DataSet dataSet = bizModel.getDataSet(source);
        if (dataSet == null) {
            return BuiltInOperation.createResponseData(0, 1, ResponseData.ERROR_OPERATION,
                bizOptJson.getString("SetsName") + "：生成二维码异常，请指定数据集！");
        }
        String id = bizOptJson.getString("id");
        String createType = bizOptJson.getString("createType");
        String dataField = bizOptJson.getString("dataField");
        String topTextField = bizOptJson.getString("topTextField");
        String downTextField = bizOptJson.getString("downTextField");
        String fileName = bizOptJson.getString("fileName");
        QrCodeConfig qrCodeConfig = loadQrCodeConfig(bizOptJson);

        List<Map<String, Object>> resultData = new ArrayList<>();
        List<BufferedImage> imageList = new ArrayList<>();

        for(Map<String, Object> rowData : dataSet.getDataAsList()){
            String msg=null;

            if(StringUtils.isNotBlank(dataField)){
                msg = StringBaseOpt.castObjectToString(DataSetOptUtil.fetchFieldValue(rowData, dataField));
            }
            if(StringUtils.isBlank(msg)) {
                msg = StringBaseOpt.castObjectToString(rowData);
            }
            qrCodeConfig.setTopText(StringBaseOpt.castObjectToString(DataSetOptUtil.fetchFieldValue(rowData, topTextField)));
            qrCodeConfig.setDownText(StringBaseOpt.castObjectToString(DataSetOptUtil.fetchFieldValue(rowData, downTextField)));
            qrCodeConfig.setMsg(msg);
            try {
                BufferedImage bufferedImage = QrCodeGenerator.createQRImage(qrCodeConfig);
                imageList.add(bufferedImage);
                if("single".equals(createType)){
                    String qrCodeFileName = StringBaseOpt.castObjectToString(DataSetOptUtil.fetchFieldValue(rowData, fileName));
                    qrCodeFileName = StringUtils.isNotBlank(qrCodeFileName) ? ( qrCodeFileName.endsWith(".jpg") ? qrCodeFileName : qrCodeFileName + ".jpg")
                        : System.currentTimeMillis() + ".jpg";
                    InputStream inputStream = ImageOpt.imageToInputStream(bufferedImage);
                    Map<String, Object> mapData = new HashMap<>();
                    mapData.put(ConstantValue.FILE_NAME, qrCodeFileName);
                    mapData.put(ConstantValue.FILE_SIZE, inputStream.available());
                    mapData.put(ConstantValue.FILE_CONTENT, inputStream);
                    resultData.add(mapData);
                }
            }catch (IOException | WriterException e){
                logger.error(e.getMessage());
            }
        }

        if("single".equals(createType)){ // 每条记录一个二维码
            DataSet objectToDataSet = resultData.size()==1 ? new DataSet(resultData.get(0)) : new DataSet(resultData);
            bizModel.putDataSet(id, objectToDataSet);
            return BuiltInOperation.createResponseSuccessData(resultData.size());
        } else if("pdf".equals(createType)){ // 图片合并为 pdf
            fileName = StringBaseOpt.castObjectToString(DataSetOptUtil.fetchFieldValue(dataSet.getData(), fileName), fileName);
            try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
                ImagesToPdf.bufferedImagesToA4SizePdf(imageList, byteArrayOutputStream);
                String pdfFileName = StringUtils.isNotBlank(fileName) ? ( fileName.endsWith(".pdf") ? fileName : fileName + ".pdf")
                    : System.currentTimeMillis() + ".pdf";
                FileDataSet fileDataSet = new FileDataSet();
                InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
                fileDataSet.setFileContent(pdfFileName, inputStream.available(), inputStream);
                bizModel.putDataSet(id, fileDataSet);
                return BuiltInOperation.createResponseSuccessData(1);
            } catch (IOException e) {
                logger.error(e.getMessage());
                return BuiltInOperation.createResponseSuccessData(0);
            }
        } else { // 图片合并为一个 大的 jpg
            fileName = StringBaseOpt.castObjectToString(DataSetOptUtil.fetchFieldValue(dataSet.getData(), fileName), fileName);
            // 合并时每行几个二维码
            int	qrEachRow = NumberBaseOpt.castObjectToInteger(bizOptJson.get("qrEachRow"), 2);
            int whiteSpace = qrCodeConfig.getPadding()>0 ? qrCodeConfig.getPadding() * 5 : 10;
            BufferedImage mergeIamge = ImageOpt.mergeImages(imageList, qrEachRow, whiteSpace);
            String qrCodeFileName = StringUtils.isNotBlank(fileName) ? ( fileName.endsWith(".jpg") ? fileName : fileName + ".jpg")
                : System.currentTimeMillis() + ".jpg";

            FileDataSet fileDataSet = new FileDataSet();
            InputStream inputStream = ImageOpt.imageToInputStream(mergeIamge);
            fileDataSet.setFileContent(qrCodeFileName, inputStream.available(), inputStream);
            bizModel.putDataSet(id, fileDataSet);
            return BuiltInOperation.createResponseSuccessData(1);
        }
    }

    //构建生成二维码参数
    private QrCodeConfig loadQrCodeConfig(JSONObject codeParams){
        QrCodeConfig config = new QrCodeConfig();

        int	height = NumberBaseOpt.castObjectToInteger(codeParams.get("height"), 200);
        config.setQrHeight(height);
        config.setQrWidth(height);
        config.setPadding(NumberBaseOpt.castObjectToInteger(codeParams.get("padding"), 4));
        //.setTopText(topText)
        config.setTopTextFontSize(codeParams.getInteger("topTextFontSize"));
        config.setTopTextFontType(codeParams.getString("topTextFontType"));
        //.setDownText(downText)
        config.setDownTextFontSize(codeParams.getInteger("downTextFontSize"));
        config.setDownTextFontType(codeParams.getString("downTextFontType"));
        config.setOffColor(ImageOpt.castObjectToColor(codeParams.get("offColor"), Color.white).getRGB());
        config.setOnColor(ImageOpt.castObjectToColor(codeParams.get("onColor"), Color.black).getRGB());

        config.setTextColor(ImageOpt.castObjectToColor(codeParams.get("textColor"), Color.black));
        config.setFrameColor(ImageOpt.castObjectToColor(codeParams.get("frameColor"), Color.white));

        String fileId = codeParams.getString("logoImage");
        if(StringUtils.isNotBlank(fileId)){
            try {
                InputStream inputStream = fileInfoOpt.loadFileStream(fileId);
                if (inputStream != null) {
                    BufferedImage logoImage = ImageIO.read(inputStream);
                    config.setLogoImage(logoImage);
                }
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }

        return config;
    }

}
