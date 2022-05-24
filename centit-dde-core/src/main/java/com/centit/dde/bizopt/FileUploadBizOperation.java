package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.utils.ConstantValue;
import com.centit.dde.utils.DataSetOptUtil;

import com.centit.fileserver.client.po.FileInfo;
import com.centit.fileserver.common.FileInfoOpt;
import com.centit.framework.common.ResponseData;
import com.centit.support.algorithm.StringBaseOpt;

import java.util.Map;

/**
 * 文件上传节点
 */
public class FileUploadBizOperation implements BizOperation {

    private FileInfoOpt fileInfoOpt;

    public FileUploadBizOperation(FileInfoOpt fileInfoOpt) {
        this.fileInfoOpt = fileInfoOpt;
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        String sourDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        String targetDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "id", sourDsName);

        DataSet dataSet = bizModel.fetchDataSetByName(sourDsName);
        if (dataSet == null) {
            return BuiltInOperation.createResponseData(0, 1, ResponseData.ERROR_OPERATION,
                bizOptJson.getString("SetsName") + "：文件上传失败，请选择数据集！");
        }
        Map<String, Object> mapFileInfo = DataSetOptUtil.getFileFormDataset(dataSet, bizOptJson);
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFileName(StringBaseOpt.objectToString(mapFileInfo.get(ConstantValue.FILE_NAME)));
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
        String fileId= fileInfoOpt.saveFile(fileInfo, -1, DataSetOptUtil.getInputStreamFormFile(mapFileInfo));
        fileInfo.setFileId(fileId);
        bizModel.putDataSet(targetDsName, new DataSet(fileInfo));
        return BuiltInOperation.createResponseSuccessData(dataSet.getSize());
    }
}
