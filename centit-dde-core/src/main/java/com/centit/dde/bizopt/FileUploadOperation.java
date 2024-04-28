package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.dataset.FileDataSet;
import com.centit.dde.utils.DataSetOptUtil;
import com.centit.fileserver.common.FileInfoOpt;
import com.centit.fileserver.po.FileInfo;
import com.centit.framework.common.ResponseData;
import com.centit.support.algorithm.StringBaseOpt;
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
            return BuiltInOperation.createResponseData(0, 1, ResponseData.ERROR_OPERATION,
                "文件上传失败，请选择数据集！");
        }
        FileDataSet mapFileInfo = DataSetOptUtil.attainFileDataset(bizModel, dataSet, bizOptJson, false);
        if(StringUtils.isBlank(mapFileInfo.getFileName())){
            return BuiltInOperation.createResponseData(0, 1, ResponseData.ERROR_OPERATION,
                sourDsName + "：文件上传失败，该数据集文件名称不能为空！");
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
