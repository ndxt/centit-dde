package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.dataset.FileDataSet;
import com.centit.dde.utils.ConstantValue;
import com.centit.dde.utils.FileDataSetOptUtil;
import com.centit.framework.common.ResponseData;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.common.ObjectException;
import com.centit.support.file.FileType;
import com.centit.support.office.OfficeToPdf;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 将 word、excel、html、ppt 转换为 pdf
 */
public class DocToPdfOperation implements BizOperation {

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        //获取参数
        String targetDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "id", bizModel.getModelName());
        String fileType = bizOptJson.getString("fileType"); // doc, docx, xls, xlsx
        String sourDsName = bizOptJson.getString("source");
        DataSet dataSet = bizModel.getDataSet(sourDsName);
        if (dataSet == null){
            return BuiltInOperation.createResponseData(0, 1,
                ObjectException.DATA_NOT_FOUND_EXCEPTION,
                dataOptContext.getI18nMessage("dde.604.data_source_not_found"));
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
                String extName = FileType.getFileExtName(fileDataSet.getFileName());
                if(StringUtils.isBlank(extName)){//|| !"ext".equalsIgnoreCase(fileType)
                    extName = fileType;
                }
                if("doc".equalsIgnoreCase(extName) || "docx".equalsIgnoreCase(extName)) {
                    OfficeToPdf.word2Pdf(fileDataSet.getFileInputStream(), pdfFile, extName.toLowerCase() );
                } else if("xls".equalsIgnoreCase(extName) || "xlsx".equalsIgnoreCase(extName)) {
                    OfficeToPdf.excel2Pdf(fileDataSet.getFileInputStream(), pdfFile);
                } else {
                    return BuiltInOperation.createResponseData(0, 1,
                        ObjectException.FUNCTION_NOT_SUPPORT,
                        dataOptContext.getI18nMessage("dde.613.file_type_not_support"));
                }
                FileDataSet pdfDataset = new FileDataSet(FileType.truncateFileExtNameWithPath(fileDataSet.getFileName()) + ".pdf",
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
                    String extName = FileType.getFileExtName(fileDataSet.getFileName());
                    if(StringUtils.isBlank(extName)){
                        extName = fileType;
                    }
                    ByteArrayOutputStream pdfFile = new ByteArrayOutputStream();
                    if("doc".equalsIgnoreCase(extName) || "docx".equalsIgnoreCase(extName)) {
                        OfficeToPdf.word2Pdf(fileDataSet.getFileInputStream(), pdfFile, extName.toLowerCase() );
                    } else if("xls".equalsIgnoreCase(extName) || "xlsx".equalsIgnoreCase(extName)) {
                        OfficeToPdf.excel2Pdf(fileDataSet.getFileInputStream(), pdfFile);
                    } else {
                        continue;
                    }
                    fileList.add(CollectionsOpt.createHashMap(
                        ConstantValue.FILE_NAME, FileType.truncateFileExtNameWithPath(fileDataSet.getFileName()) + ".pdf",
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
