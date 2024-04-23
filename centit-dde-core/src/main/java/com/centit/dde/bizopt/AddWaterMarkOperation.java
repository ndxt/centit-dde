package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.dataset.FileDataSet;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.dde.utils.ConstantValue;
import com.centit.dde.utils.DataSetOptUtil;
import com.centit.fileserver.common.FileInfoOpt;
import com.centit.framework.common.ResponseData;
import com.centit.framework.model.security.CentitUserDetails;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.common.ObjectException;
import com.centit.support.file.FileIOOpt;
import com.centit.support.image.ImageOpt;
import com.centit.support.office.Watermark4Pdf;
import com.itextpdf.text.Image;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 可以给pdf 和 图标 添加水印
 */
public class AddWaterMarkOperation implements BizOperation {

    private FileInfoOpt fileInfoOpt;

    public AddWaterMarkOperation(FileInfoOpt fileInfoOpt) {
        this.fileInfoOpt = fileInfoOpt;
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        String fileType = bizOptJson.getString("optType");
        if("imageText".equalsIgnoreCase(fileType)){
            return addImageWaterMark(bizModel, bizOptJson, dataOptContext);
        } else if("pdfText".equalsIgnoreCase(fileType)){
            return addPdfWaterMark(bizModel, bizOptJson, dataOptContext);
        } else if("pdfImage".equalsIgnoreCase(fileType)){
            return addImage2Pdf(bizModel, bizOptJson, dataOptContext);
        } else {
            return BuiltInOperation.createResponseData(0, 1,
                ObjectException.FUNCTION_NOT_SUPPORT, "不支持的文件类型！");
        }
    }

    private ResponseData addImage2Pdf(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception{
        //获取参数
        String targetDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "id", bizModel.getModelName());

        String sourDsName = bizOptJson.getString("source");
        DataSet dataSet = bizModel.getDataSet(sourDsName);
        if (dataSet == null){
            return BuiltInOperation.createResponseData(0, 1,
                ObjectException.DATA_NOT_FOUND_EXCEPTION, "找不到源文件！");
        }
        int  page = NumberBaseOpt.castObjectToInteger(bizOptJson.get("page"), -1);
        int  x = NumberBaseOpt.castObjectToInteger(bizOptJson.get("x"), 0);
        int  y = NumberBaseOpt.castObjectToInteger(bizOptJson.get("y"), 0);
        int  w = NumberBaseOpt.castObjectToInteger(bizOptJson.get("width"), 0);
        int  h = NumberBaseOpt.castObjectToInteger(bizOptJson.get("height"), 0);

        Image image = null;
        String imageDate = bizOptJson.getString("imageDataset");
        if(StringUtils.isBlank(imageDate)){
            String imageFileId = bizOptJson.getString("imageFileId");
            InputStream inputStream = fileInfoOpt.loadFileStream(imageFileId);
            image = Watermark4Pdf.createPdfImage(
                FileIOOpt.readBytesFromInputStream(inputStream));
        } else {
            DataSet idataSet = bizModel.getDataSet(imageDate);
            if(idataSet != null) {
                FileDataSet imageDataset = DataSetOptUtil.castToFileDataSet(idataSet);
                image = Watermark4Pdf.createPdfImage(
                    FileIOOpt.readBytesFromInputStream(imageDataset.getFileInputStream()));
            }
        }
        if (image == null) {
            throw new ObjectException(ObjectException.DATA_NOT_FOUND_EXCEPTION,
                "找不到水印图片！");
        }

        if(dataSet.getSize() == 1) {
            FileDataSet fileDataSet;
            if (dataSet instanceof FileDataSet) {
                fileDataSet = (FileDataSet) dataSet;
            } else {
                fileDataSet = DataSetOptUtil.mapDataToFile(dataSet.getFirstRow(),
                    ConstantValue.FILE_NAME, ConstantValue.FILE_CONTENT);
            }
            if(fileDataSet!=null) {
                ByteArrayOutputStream pdfFile = new ByteArrayOutputStream();
                Watermark4Pdf.addImage2Pdf(fileDataSet.getFileInputStream(),
                    pdfFile, page, image, x, y, w, h);

                FileDataSet pdfDataset = new FileDataSet(fileDataSet.getFileName(),
                    pdfFile.size(), pdfFile);
                bizModel.putDataSet(targetDsName, pdfDataset);
                return BuiltInOperation.createResponseSuccessData(1);
            } else {
                return BuiltInOperation.createResponseSuccessData(0);
            }
        }
        //文件列表
        if(dataSet.getSize() > 1) {
            List<Map<String, Object>> fileList = new ArrayList<>();
            for(Map<String, Object> rowData : dataSet.getDataAsList()){
                FileDataSet fileDataSet = DataSetOptUtil.mapDataToFile(rowData,
                    ConstantValue.FILE_NAME, ConstantValue.FILE_CONTENT);
                if(fileDataSet!=null){
                    ByteArrayOutputStream pdfFile = new ByteArrayOutputStream();
                    Watermark4Pdf.addImage2Pdf(fileDataSet.getFileInputStream(),
                        pdfFile, page, image, x, y, w, h);
                    fileList.add(CollectionsOpt.createHashMap(
                        ConstantValue.FILE_NAME, fileDataSet.getFileName(),
                        ConstantValue.FILE_SIZE, pdfFile.size(),
                        ConstantValue.FILE_CONTENT, pdfFile
                    ));
                }
            }
            bizModel.putDataSet(targetDsName, new DataSet(fileList));
            return BuiltInOperation.createResponseSuccessData(fileList.size());
        }
        return BuiltInOperation.createResponseSuccessData(0);
    }

    private ResponseData addImageWaterMark(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) {
        //获取参数 BufferedImage image, String waterMark, String fontName, Color color, int size, int x, int y
        String targetDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "id", bizModel.getModelName());
        //String waterMarkStr = bizOptJson.getString("waterMark");
        String font = bizOptJson.getString("font");
        if(StringUtils.isBlank(font)){
            font = "宋体";
        }
        String color = bizOptJson.getString("color");
        int fontSize = bizOptJson.getIntValue("fontSize");
        JSONArray textArray = bizOptJson.getJSONArray("textList");
        List<ImageOpt.ImageTextInfo> textList = new ArrayList<>();
        if(textArray!=null) {
            BizModelJSONTransform transform = new BizModelJSONTransform(bizModel);
            for (Object obj : textArray){
                if(obj instanceof JSONObject){
                    JSONObject textObj = (JSONObject) obj;
                    String text = textObj.getString("text");
                    String transMark = StringBaseOpt.castObjectToString(
                        DataSetOptUtil.fetchFieldValue(transform, text));
                    if(StringUtils.isNotBlank(transMark)){
                        text = transMark;
                    }
                    textList.add(ImageOpt.createImageText(textObj.getIntValue("x"),
                        textObj.getIntValue("y"), text ));
                }
            }
        }
        String sourDsName = bizOptJson.getString("source");
        Color markColor = ImageOpt.castObjectToColor(color, Color.WHITE);
        DataSet dataSet = bizModel.getDataSet(sourDsName);

        if(dataSet.getSize() == 1) {
            FileDataSet fileDataSet;
            if (dataSet instanceof FileDataSet) {
                fileDataSet = (FileDataSet) dataSet;
            } else {
                fileDataSet = DataSetOptUtil.mapDataToFile(dataSet.getFirstRow(),
                    ConstantValue.FILE_NAME, ConstantValue.FILE_CONTENT);
            }
            if(fileDataSet!=null) {
                ByteArrayOutputStream imageFile = new ByteArrayOutputStream();
                if(ImageOpt.addTextToImage(fileDataSet.getFileInputStream(), imageFile,
                    font, markColor, fontSize,textList)) {
                    FileDataSet pdfDataset = new FileDataSet(fileDataSet.getFileName(),
                        imageFile.size(), imageFile);
                    bizModel.putDataSet(targetDsName, pdfDataset);
                    return BuiltInOperation.createResponseSuccessData(dataSet.getSize());
                }
            }
            return BuiltInOperation.createResponseSuccessData(0);
        }
        //文件列表
        if(dataSet.getSize() > 1) {
            List<Map<String, Object>> fileList = new ArrayList<>();
            for(Map<String, Object> rowData : dataSet.getDataAsList()){
                FileDataSet fileDataSet = DataSetOptUtil.mapDataToFile(rowData,
                    ConstantValue.FILE_NAME, ConstantValue.FILE_CONTENT);
                if(fileDataSet!=null){
                    ByteArrayOutputStream imageFile = new ByteArrayOutputStream();
                    if(ImageOpt.addTextToImage(fileDataSet.getFileInputStream(), imageFile,
                        font, markColor, fontSize, textList)) {
                        fileList.add(CollectionsOpt.createHashMap(
                            ConstantValue.FILE_NAME, fileDataSet.getFileName(),
                            ConstantValue.FILE_SIZE, imageFile.size(),
                            ConstantValue.FILE_CONTENT, imageFile
                        ));
                    }
                }
            }
            bizModel.putDataSet(targetDsName, new DataSet(fileList));
            return BuiltInOperation.createResponseSuccessData(fileList.size());
        }
        return BuiltInOperation.createResponseSuccessData(0);
    }

    private ResponseData addPdfWaterMark(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) {
        //获取参数
        String targetDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "id", bizModel.getModelName());
        String waterMarkStr = bizOptJson.getString("waterMark");
        float opacity = bizOptJson.getFloatValue("opacity");
        float rotation = bizOptJson.getFloatValue("rotation");
        float fontSize = bizOptJson.getFloatValue("fontSize");
        String sourDsName = bizOptJson.getString("source");
        DataSet dataSet = bizModel.getDataSet(sourDsName);
        if (dataSet == null){
            return BuiltInOperation.createResponseData(0, 1,
                ObjectException.DATA_NOT_FOUND_EXCEPTION, "找不到源文件！");
        }
        if(StringUtils.isNotBlank(waterMarkStr)){
            BizModelJSONTransform transform = new BizModelJSONTransform(bizModel, dataSet.getData());
            String transMark = StringBaseOpt.castObjectToString(
                DataSetOptUtil.fetchFieldValue(transform, waterMarkStr));
            if(StringUtils.isNotBlank(transMark)){
                waterMarkStr = transMark;
            }
        } else {
            CentitUserDetails userDetails = dataOptContext.getCurrentUserDetail();
            waterMarkStr = userDetails.getUserCode() + "-" + DatetimeOpt.currentDatetime();
        }
        if(dataSet.getSize() == 1) {
            FileDataSet fileDataSet;
            if (dataSet instanceof FileDataSet) {
                fileDataSet = (FileDataSet) dataSet;
            } else {
                fileDataSet = DataSetOptUtil.mapDataToFile(dataSet.getFirstRow(),
                    ConstantValue.FILE_NAME, ConstantValue.FILE_CONTENT);
            }
            if(fileDataSet!=null) {
                ByteArrayOutputStream pdfFile = new ByteArrayOutputStream();
                Watermark4Pdf.addWatermark4Pdf(fileDataSet.getFileInputStream(),
                    pdfFile, waterMarkStr, opacity, rotation, fontSize);

                FileDataSet pdfDataset = new FileDataSet(fileDataSet.getFileName(),
                    pdfFile.size(), pdfFile);
                bizModel.putDataSet(targetDsName, pdfDataset);
                return BuiltInOperation.createResponseSuccessData(1);
            } else {
                return BuiltInOperation.createResponseSuccessData(0);
            }
        }
        //文件列表
        if(dataSet.getSize() > 1) {
            List<Map<String, Object>> fileList = new ArrayList<>();
            for(Map<String, Object> rowData : dataSet.getDataAsList()){
                FileDataSet fileDataSet = DataSetOptUtil.mapDataToFile(rowData,
                    ConstantValue.FILE_NAME, ConstantValue.FILE_CONTENT);
                if(fileDataSet!=null){
                    ByteArrayOutputStream pdfFile = new ByteArrayOutputStream();
                    Watermark4Pdf.addWatermark4Pdf(fileDataSet.getFileInputStream(),
                        pdfFile, waterMarkStr, opacity, rotation, fontSize);
                    fileList.add(CollectionsOpt.createHashMap(
                        ConstantValue.FILE_NAME, fileDataSet.getFileName(),
                        ConstantValue.FILE_SIZE, pdfFile.size(),
                        ConstantValue.FILE_CONTENT, pdfFile
                    ));
                }
            }
            bizModel.putDataSet(targetDsName, new DataSet(fileList));
            return BuiltInOperation.createResponseSuccessData(fileList.size());
        }
        return BuiltInOperation.createResponseSuccessData(0);
    }
}
