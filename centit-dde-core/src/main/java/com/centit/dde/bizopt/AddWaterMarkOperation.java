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
import com.centit.dde.utils.FileDataSetOptUtil;
import com.centit.fileserver.common.FileInfoOpt;
import com.centit.framework.common.ResponseData;
import com.centit.framework.model.security.CentitUserDetails;
import com.centit.support.algorithm.*;
import com.centit.support.common.ObjectException;
import com.centit.support.file.FileIOOpt;
import com.centit.support.file.FileType;
import com.centit.support.image.ImageOpt;
import com.centit.support.office.DocOptUtil;
import com.centit.support.office.Watermark4Pdf;
import com.itextpdf.text.Image;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 可以给pdf 和 图标 添加水印
 */
public class AddWaterMarkOperation implements BizOperation {

    private final FileInfoOpt fileInfoOpt;

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
        } else if("textShade".equalsIgnoreCase(fileType)){
            return addShade2Pdf(bizModel, bizOptJson, dataOptContext);
        } else {
            return BuiltInOperation.createResponseData(0, 1,
                ObjectException.FUNCTION_NOT_SUPPORT,
                dataOptContext.getI18nMessage("dde.613.file_type_not_support"));
        }
    }

    private ResponseData addImage2Pdf(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception{
        //获取参数
        String targetDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "id", bizModel.getModelName());

        String sourDsName = bizOptJson.getString("source");
        DataSet dataSet = bizModel.getDataSet(sourDsName);
        if (dataSet == null){
            return BuiltInOperation.createResponseData(0, 1,
                ObjectException.DATA_NOT_FOUND_EXCEPTION,
                dataOptContext.getI18nMessage("dde.604.data_source_not_found"));
        }
        int  page = NumberBaseOpt.castObjectToInteger(bizOptJson.get("page"), -1);
        float  x = NumberBaseOpt.castObjectToFloat(bizOptJson.get("x"), 0.5f);
        float  y = NumberBaseOpt.castObjectToFloat(bizOptJson.get("y"), 0.5f);
        float  w = NumberBaseOpt.castObjectToFloat(bizOptJson.get("width"), -1f);
        float  h = NumberBaseOpt.castObjectToFloat(bizOptJson.get("height"), -1f);
        float opacity = NumberBaseOpt.castObjectToFloat(bizOptJson.get("opacity"), 0.5f);

        Image image = null;
        String imageDate = bizOptJson.getString("imageDataset");
        if(StringUtils.isBlank(imageDate)){
            String imageFileId = bizOptJson.getString("imageFileId");
            try (InputStream inputStream = fileInfoOpt.loadFileStream(imageFileId)) {
                image = Watermark4Pdf.createPdfImage(
                    FileIOOpt.readBytesFromInputStream(inputStream));
            }
        } else {
            DataSet idataSet = bizModel.getDataSet(imageDate);
            if(idataSet != null) {
                FileDataSet imageDataset = FileDataSetOptUtil.castToFileDataSet(idataSet);
                try (InputStream inputStream = imageDataset.getFileInputStream()) {
                    image = Watermark4Pdf.createPdfImage(
                        FileIOOpt.readBytesFromInputStream(inputStream));
                }
            }
        }
        if (image == null) {
            throw new ObjectException(ObjectException.DATA_NOT_FOUND_EXCEPTION,
                dataOptContext.getI18nMessage("dde.604.img_file_not_found"));
        }

        if(dataSet.getSize() == 1) {
            FileDataSet fileDataSet;
            if (dataSet instanceof FileDataSet) {
                fileDataSet = (FileDataSet) dataSet;
            } else {
                fileDataSet = FileDataSetOptUtil.mapDataToFile(dataSet.getFirstRow(),
                    ConstantValue.FILE_NAME, ConstantValue.FILE_CONTENT);
            }
            if(fileDataSet!=null) {
                ByteArrayOutputStream pdfFile = new ByteArrayOutputStream();
                try (InputStream inputStream = fileDataSet.getFileInputStream()) {
                    Watermark4Pdf.addImage2Pdf(inputStream,
                        pdfFile, page, image, opacity, x, y, w, h);
                }

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
                FileDataSet fileDataSet = FileDataSetOptUtil.mapDataToFile(rowData,
                    ConstantValue.FILE_NAME, ConstantValue.FILE_CONTENT);
                if(fileDataSet!=null){
                    ByteArrayOutputStream pdfFile = new ByteArrayOutputStream();
                    try (InputStream inputStream = fileDataSet.getFileInputStream()) {
                        Watermark4Pdf.addImage2Pdf(inputStream,
                            pdfFile, page, image, opacity, x, y, w, h);
                    }
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

    private ResponseData addImageWaterMark(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws IOException {
        //获取参数 BufferedImage image, String waterMark, String fontName, Color color, int size, float x, float y
        String targetDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "id", bizModel.getModelName());
        String sourDsName = bizOptJson.getString("source");
        DataSet dataSet = bizModel.getDataSet(sourDsName);
        //String waterMarkStr = bizOptJson.getString("waterMark");
        String font = bizOptJson.getString("font");
        if(StringUtils.isBlank(font)){
            font = "宋体";
        }
        String color = bizOptJson.getString("color");
        int fontSize = NumberBaseOpt.castObjectToInteger(bizOptJson.get("fontSize"), 14);// bizOptJson.getIntValue("fontSize");
        JSONArray textArray = bizOptJson.getJSONArray("textList");
        List<ImageOpt.ImageTextInfo> textList = new ArrayList<>();
        if(textArray!=null) {
            BizModelJSONTransform transform = new BizModelJSONTransform(bizModel, dataSet.getData());
            for (Object obj : textArray){
                if(obj instanceof JSONObject){
                    JSONObject textObj = (JSONObject) obj;
                    String text = textObj.getString("text");
                    String transMark = StringBaseOpt.castObjectToString(
                        DataSetOptUtil.fetchFieldValue(transform, text));
                    if(StringUtils.isNotBlank(transMark)){
                        text = transMark;
                    }
                    textList.add(ImageOpt.createImageText(textObj.getFloatValue("x"),
                        textObj.getFloatValue("y"), text ));
                }
            }
        }
        //文字默认为黑色
        Color markColor = ImageOpt.castObjectToColor(color, Color.BLACK);
        if(dataSet.getSize() == 1) {
            FileDataSet fileDataSet;
            if (dataSet instanceof FileDataSet) {
                fileDataSet = (FileDataSet) dataSet;
            } else {
                fileDataSet = FileDataSetOptUtil.mapDataToFile(dataSet.getFirstRow(),
                    ConstantValue.FILE_NAME, ConstantValue.FILE_CONTENT);
            }
            if(fileDataSet!=null) {
                ByteArrayOutputStream imageFile = new ByteArrayOutputStream();
                String imageType = FileType.getFileExtName(fileDataSet.getFileName());
                if(StringUtils.isBlank(imageType)){
                    imageType = "png";
                }else {
                    imageType = imageType.toLowerCase();
                }
                try (InputStream inputStream = fileDataSet.getFileInputStream()) {
                    if(ImageOpt.addTextToImage(inputStream, imageType, imageFile,
                        font, markColor, fontSize, textList)) {
                        FileDataSet pdfDataset = new FileDataSet(fileDataSet.getFileName(),
                            imageFile.size(), imageFile);
                        bizModel.putDataSet(targetDsName, pdfDataset);
                        return BuiltInOperation.createResponseSuccessData(dataSet.getSize());
                    }
                }
            }
            return BuiltInOperation.createResponseSuccessData(0);
        }
        //文件列表
        if(dataSet.getSize() > 1) {
            List<Map<String, Object>> fileList = new ArrayList<>();
            for(Map<String, Object> rowData : dataSet.getDataAsList()){
                FileDataSet fileDataSet = FileDataSetOptUtil.mapDataToFile(rowData,
                    ConstantValue.FILE_NAME, ConstantValue.FILE_CONTENT);
                if(fileDataSet!=null){
                    String imageType = FileType.getFileExtName(fileDataSet.getFileName());
                    if(StringUtils.isBlank(imageType)){
                        imageType = "png";
                    }else {
                        imageType = imageType.toLowerCase();
                    }
                    ByteArrayOutputStream imageFile = new ByteArrayOutputStream();
                    try (InputStream inputStream = fileDataSet.getFileInputStream()) {
                        if(ImageOpt.addTextToImage(inputStream, imageType, imageFile,
                            font, markColor, fontSize, textList)) {
                            fileList.add(CollectionsOpt.createHashMap(
                                ConstantValue.FILE_NAME, fileDataSet.getFileName(),
                                ConstantValue.FILE_SIZE, imageFile.size(),
                                ConstantValue.FILE_CONTENT, imageFile
                            ));
                        }
                    }
                }
            }
            bizModel.putDataSet(targetDsName, new DataSet(fileList));
            return BuiltInOperation.createResponseSuccessData(fileList.size());
        }
        return BuiltInOperation.createResponseSuccessData(0);
    }

    private ResponseData addPdfWaterMark(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws IOException {
        //获取参数
        String targetDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "id", bizModel.getModelName());
        String waterMarkStr = bizOptJson.getString("waterMark");
        float opacity =  NumberBaseOpt.castObjectToFloat(bizOptJson.get("opacity"), 0.5f);
        float rotation = NumberBaseOpt.castObjectToFloat(bizOptJson.get("rotation"), -45f);
        float fontSize = NumberBaseOpt.castObjectToFloat(bizOptJson.get("fontSize"), 14f);
        boolean isRepeat= BooleanBaseOpt.castObjectToBoolean(bizOptJson.get("isRepeat"),false);
        String sourDsName = bizOptJson.getString("source");
        DataSet dataSet = bizModel.getDataSet(sourDsName);
        if (dataSet == null){
            return BuiltInOperation.createResponseData(0, 1,
                ObjectException.DATA_NOT_FOUND_EXCEPTION,
                dataOptContext.getI18nMessage("dde.604.data_source_not_found"));
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
                fileDataSet = FileDataSetOptUtil.mapDataToFile(dataSet.getFirstRow(),
                    ConstantValue.FILE_NAME, ConstantValue.FILE_CONTENT);
            }
            if(fileDataSet!=null) {
                ByteArrayOutputStream pdfFile = new ByteArrayOutputStream();
                try (InputStream inputStream = fileDataSet.getFileInputStream()) {
                    Watermark4Pdf.addWatermark4Pdf(inputStream,
                        pdfFile, waterMarkStr, opacity, rotation, fontSize, isRepeat);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

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
                FileDataSet fileDataSet = FileDataSetOptUtil.mapDataToFile(rowData,
                    ConstantValue.FILE_NAME, ConstantValue.FILE_CONTENT);
                if(fileDataSet!=null){
                    ByteArrayOutputStream pdfFile = new ByteArrayOutputStream();
                    try (InputStream inputStream = fileDataSet.getFileInputStream()) {
                        Watermark4Pdf.addWatermark4Pdf(inputStream,
                            pdfFile, waterMarkStr, opacity, rotation, fontSize, isRepeat);
                    }
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

    private ResponseData addShade2Pdf(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception{
        //获取参数
        String targetDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "id", bizModel.getModelName());
        String waterMarkStr = bizOptJson.getString("waterText");
        String color = bizOptJson.getString("color");
        //底纹默认为黄色
        Color shadeColor = ImageOpt.castObjectToColor(color, Color.YELLOW);

        String sourDsName = bizOptJson.getString("source");
        DataSet dataSet = bizModel.getDataSet(sourDsName);
        if (dataSet == null){
            return BuiltInOperation.createResponseData(0, 1,
                ObjectException.DATA_NOT_FOUND_EXCEPTION,
                dataOptContext.getI18nMessage("dde.604.data_source_not_found"));
        }
        if(StringUtils.isNotBlank(waterMarkStr)){
            BizModelJSONTransform transform = new BizModelJSONTransform(bizModel, dataSet.getData());
            String transMark = StringBaseOpt.castObjectToString(
                DataSetOptUtil.fetchFieldValue(transform, waterMarkStr));
            if(StringUtils.isNotBlank(transMark)){
                waterMarkStr = transMark;
            }
        } else {
            return BuiltInOperation.createResponseData(0, 1,
                ObjectException.DATA_NOT_FOUND_EXCEPTION,
                dataOptContext.getI18nMessage("dde.604.data_source_not_found2", "color"));
        }
        List<String> keyWords = CollectionsOpt.arrayToList(waterMarkStr.split(","));
        if(dataSet.getSize() == 1) {
            FileDataSet fileDataSet;
            if (dataSet instanceof FileDataSet) {
                fileDataSet = (FileDataSet) dataSet;
            } else {
                fileDataSet = FileDataSetOptUtil.mapDataToFile(dataSet.getFirstRow(),
                    ConstantValue.FILE_NAME, ConstantValue.FILE_CONTENT);
            }
            if(fileDataSet!=null) {
                ByteArrayOutputStream pdfFile = new ByteArrayOutputStream();
                try (InputStream inputStream = fileDataSet.getFileInputStream()) {
                    DocOptUtil.pdfHighlightKeywords(inputStream,
                        pdfFile, keyWords, shadeColor);
                }

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
                FileDataSet fileDataSet = FileDataSetOptUtil.mapDataToFile(rowData,
                    ConstantValue.FILE_NAME, ConstantValue.FILE_CONTENT);
                if(fileDataSet!=null){
                    ByteArrayOutputStream pdfFile = new ByteArrayOutputStream();
                    try (InputStream inputStream = fileDataSet.getFileInputStream()) {
                        DocOptUtil.pdfHighlightKeywords(inputStream,
                            pdfFile, keyWords, shadeColor);
                    }
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
