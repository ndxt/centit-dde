package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.utils.ConstantValue;
import com.centit.dde.utils.DataSetOptUtil;
import com.centit.fileserver.common.FileInfo;
import com.centit.fileserver.common.FileStore;
import com.centit.framework.common.ResponseData;
import com.centit.framework.security.model.CentitUserDetails;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.StringBaseOpt;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件上传节点
 */
public class FileUploadBizOperation implements BizOperation {

    FileStore fileStore;

    public FileUploadBizOperation(FileStore fileStore) {
        this.fileStore = fileStore;
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
        fileInfo.setOptTag(dataOptContext.getPacketId());
        fileInfo.setOptMethod("api");
        Object session=bizModel.getStackData(ConstantValue.SESSION_DATA_TAG);
        if(session instanceof CentitUserDetails){
            CentitUserDetails centitUserDetails=(CentitUserDetails)session;
            fileInfo.setFileUnit(centitUserDetails.getTopUnitCode());
            fileInfo.setFileOwner(centitUserDetails.getUserCode());
        }
        String fileId=fileStore.saveFile(fileInfo, -1, DataSetOptUtil.getInputStreamFormFile(mapFileInfo));
        fileInfo.setFileId(fileId);
        bizModel.putDataSet(targetDsName, new DataSet(fileInfo));
        return BuiltInOperation.createResponseSuccessData(dataSet.getSize());
    }
}
