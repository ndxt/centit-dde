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
        FileDataSet mapFileInfo = DataSetOptUtil.attainFileDataset(dataSet, bizOptJson);
        if(StringUtils.isBlank(mapFileInfo.getFileName())){
            return BuiltInOperation.createResponseData(0, 1, ResponseData.ERROR_OPERATION,
                sourDsName + "：文件上传失败，该数据集文件名称不能为空！");
        }
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFileName(mapFileInfo.getFileName());
        fileInfo.setOptId(dataOptContext.getOptId());
        fileInfo.setOsId(dataOptContext.getOsId());
        //TODO optTag 这个不是当前业务主键，这个应该是不对的
        fileInfo.setOptTag(dataOptContext.getPacketId());
        fileInfo.setOptMethod("api");
        fileInfo.setFileOwner(dataOptContext.getCurrentUserCode());
        fileInfo.setFileUnit(dataOptContext.getCurrentUnitCode());

        /*Object session=bizModel.getStackData(ConstantValue.SESSION_DATA_TAG);
        if(session instanceof CentitUserDetails){
            CentitUserDetails centitUserDetails=(CentitUserDetails)session;
            fileInfo.setFileUnit(centitUserDetails.getTopUnitCode());
            fileInfo.setFileOwner(centitUserDetails.getUserCode());
        }*/
        String fileId= fileInfoOpt.saveFile(fileInfo, -1, mapFileInfo.getFileInputStream());
        fileInfo.setFileId(fileId);
        bizModel.putDataSet(targetDsName, new DataSet(fileInfo));
        return BuiltInOperation.createResponseSuccessData(dataSet.getSize());
    }
}
