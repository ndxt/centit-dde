package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.dataset.FileDataSet;
import com.centit.dde.utils.ConstantValue;
import com.centit.dde.utils.DatasetVariableTranslate;
import com.centit.fileserver.common.FileBaseInfo;
import com.centit.fileserver.common.FileInfoOpt;
import com.centit.framework.common.ResponseData;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.algorithm.ZipCompressor;
import com.centit.support.common.ObjectException;
import com.centit.support.file.FileIOOpt;
import com.centit.support.file.FileType;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipOutputStream;

/**
 * 文件下载节点
 */
public class FileDownloadOperation implements BizOperation {

    private FileInfoOpt fileInfoOpt;

    public FileDownloadOperation(FileInfoOpt fileInfoOpt) {
        this.fileInfoOpt = fileInfoOpt;
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        String sourDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        String targetDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "id", sourDsName);
        String fileId = BuiltInOperation.getJsonFieldString(bizOptJson, "fileId", "");
        String fileName = BuiltInOperation.getJsonFieldString(bizOptJson, ConstantValue.FILE_NAME, "");
        DataSet dataSet = bizModel.getDataSet(sourDsName);
        if (dataSet == null) {
            return BuiltInOperation.createResponseData(0, 1, ObjectException.DATA_NOT_FOUND_EXCEPTION,
                dataOptContext.getI18nMessage("dde.604.data_source_not_found"));
        }
        ArrayList<String> fileIds = new ArrayList<>();

        Map<String, Object> mapFirstRow = dataSet.getFirstRow();
        if(StringUtils.isNotBlank(fileId)){
            Object idObj = new DatasetVariableTranslate(dataSet).attainExpressionValue(fileId);
            if(idObj instanceof Collection){
                for(Object obj :(Collection<Object>) idObj){
                    String fid = StringBaseOpt.castObjectToString(obj);
                    if(StringUtils.isNotBlank(fid)) {
                        fileIds.add(fid);
                    }
                }
            } else {
                if(idObj!=null){
                    String fid = StringBaseOpt.castObjectToString(idObj);
                    if(StringUtils.isNotBlank(fid)) {
                        fileIds.add(fid);
                    } else {
                        fileIds.add(fileId);
                    }
                } else {
                    fileIds.add(fileId);
                }
            }
        } else {
            fileIds.add(StringBaseOpt.castObjectToString(mapFirstRow.get(ConstantValue.FILE_ID)));
        }

        if(StringUtils.isNotBlank(fileName)){
            fileName = new DatasetVariableTranslate(dataSet).mapTemplateString(fileName);
        } else {
            fileName = StringBaseOpt.castObjectToString(mapFirstRow.get(ConstantValue.FILE_NAME));
        }

        FileDataSet objectToDataSet = new FileDataSet();
        if(fileIds.size()==1) {
            FileBaseInfo fileInfo = fileInfoOpt.getFileInfo(fileIds.get(0));

            if(fileInfo!=null) {
                if (StringUtils.isBlank(fileName))
                    fileName = fileInfo.getFileName();
                objectToDataSet.setFileInfo(fileInfo);

            }
            try(InputStream inputStream = fileInfoOpt.loadFileStream(fileIds.get(0))) {
                ByteArrayOutputStream outs = new ByteArrayOutputStream();
                FileIOOpt.writeInputStreamToOutputStream(inputStream, outs);
                objectToDataSet.setFileContent(fileName, outs.size(), outs); // outs
            }
        } else {
            ByteArrayOutputStream outBuf = new ByteArrayOutputStream();
            ZipOutputStream out = ZipCompressor.convertToZipOutputStream(outBuf);
            Map<String, Integer> fileNameMap = new HashMap<>(fileIds.size()+4);
            for(String fid : fileIds) {
                FileBaseInfo fileInfo = fileInfoOpt.getFileInfo(fid);
                if(fileInfo!=null) {
                    InputStream inputStream = fileInfoOpt.loadFileStream(fid);
                    String fn = fileInfo.getFileName();
                    while(fileNameMap.containsKey(fn)){
                        int copies = fileNameMap.get(fn)+1;
                        fileNameMap.put(fn, copies);
                        fn = FileType.truncateFileExtName(fn) +"("+copies+")." + FileType.getFileExtName(fn);
                    }
                    fileNameMap.put(fn, 1);
                    ZipCompressor.compressFile(inputStream, fn, out, "");
                }
            }
            out.close();
            objectToDataSet.setFileContent(fileName, outBuf.size(), outBuf);
        }
        bizModel.putDataSet(targetDsName, objectToDataSet);
        return BuiltInOperation.createResponseSuccessData(objectToDataSet.getSize());
    }
}
