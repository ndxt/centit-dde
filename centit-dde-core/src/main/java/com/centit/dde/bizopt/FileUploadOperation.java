package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.dataset.FileDataSet;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.dde.utils.DataSetOptUtil;
import com.centit.fileserver.common.FileInfoOpt;
import com.centit.fileserver.po.FileInfo;
import com.centit.framework.common.ResponseData;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.common.ObjectException;
import com.centit.support.json.JSONTransformer;
import org.apache.commons.lang3.StringUtils;

/**
 * 文件上传节点
 */
public class FileUploadOperation implements BizOperation {

    private FileInfoOpt fileInfoOpt;

    public FileUploadOperation(FileInfoOpt fileInfoOpt) {
        this.fileInfoOpt = fileInfoOpt;
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        String sourDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        String targetDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "id", sourDsName);

        DataSet dataSet = bizModel.getDataSet(sourDsName);
        if (dataSet == null) {
            return BuiltInOperation.createResponseData(0, 1,
                ObjectException.DATA_NOT_FOUND_EXCEPTION,
                dataOptContext.getI18nMessage("dde.604.data_source_not_found"));
        }
        FileDataSet mapFileInfo = DataSetOptUtil.attainFileDataset(bizModel, dataSet, bizOptJson, false);
        if(StringUtils.isBlank(mapFileInfo.getFileName())){
            return BuiltInOperation.createResponseData(0, 1,
                ResponseData.ERROR_FIELD_INPUT_NOT_VALID,
                dataOptContext.getI18nMessage("error.701.field_is_blank", "fileName"));
        }

        FileInfo fileInfo = new FileInfo();
        fileInfo.setFileName(mapFileInfo.getFileName());
        fileInfo.setOptId(dataOptContext.getOptId());
        fileInfo.setOsId(dataOptContext.getOsId());
        fileInfo.setOptMethod("api");
        fileInfo.setFileOwner(dataOptContext.getCurrentUserCode());
        fileInfo.setFileUnit(dataOptContext.getCurrentUnitCode());
        if(dataSet.getFirstRow().containsKey("optTag")) {
            fileInfo.setOptTag(StringBaseOpt.objectToString(dataSet.getFirstRow().get("optTag")));
        }
        long fileSize = mapFileInfo.getFileSize();
        if(fileSize<=0){
            fileSize = mapFileInfo.getFileInputStream().available();
        }
        fileInfo.setFileSize(fileSize);
        String fileId = fileInfoOpt.saveFile(fileInfo, fileSize, mapFileInfo.getFileInputStream());
        fileInfo.setFileId(fileId);
        bizModel.putDataSet(targetDsName, new DataSet(fileInfo));
        return BuiltInOperation.createResponseSuccessData(dataSet.getSize());
    }
}
