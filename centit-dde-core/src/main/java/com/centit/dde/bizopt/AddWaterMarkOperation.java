package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.dataset.FileDataSet;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.dde.utils.ConstantValue;
import com.centit.dde.utils.DataSetOptUtil;
import com.centit.framework.common.ResponseData;
import com.centit.framework.model.security.CentitUserDetails;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.common.ObjectException;
import com.centit.support.office.Watermark4Pdf;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 可以给pdf 和 图标 添加水印
 */
public class AddWaterMarkOperation implements BizOperation {

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        String fileType = bizOptJson.getString("fileType");
        if("image".equalsIgnoreCase(fileType)){
            return addImageWaterMark(bizModel, bizOptJson, dataOptContext);
        } else if("pdf".equalsIgnoreCase(fileType)){
            return addPdfWaterMark(bizModel, bizOptJson, dataOptContext);
        } else {
            return BuiltInOperation.createResponseData(0, 1,
                ObjectException.FUNCTION_NOT_SUPPORT, "不支持的文件类型！");
        }
    }

    private ResponseData addImageWaterMark(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) {
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
                return BuiltInOperation.createResponseSuccessData(dataSet.getSize());
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