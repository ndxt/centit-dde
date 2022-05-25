package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONObject;
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
import org.apache.commons.lang3.StringUtils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

/**
 * 文件下载节点
 */
public class FileDownloadBizOperation implements BizOperation {

    private FileInfoOpt fileInfoOpt;

    public FileDownloadBizOperation(FileInfoOpt fileInfoOpt) {
        this.fileInfoOpt = fileInfoOpt;
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        String sourDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        String targetDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "id", sourDsName);
        String fileId = BuiltInOperation.getJsonFieldString(bizOptJson, "fileId", "");
        String fileName = BuiltInOperation.getJsonFieldString(bizOptJson, ConstantValue.FILE_NAME, "");
        DataSet dataSet = bizModel.fetchDataSetByName(sourDsName);
        if (dataSet == null) {
            return BuiltInOperation.createResponseData(0, 1, ResponseData.ERROR_OPERATION,
                bizOptJson.getString("SetsName") + "：文件下载失败，请选择数据集！");
        }
        Map<String, Object> mapFirstRow = dataSet.getFirstRow();
        if(StringUtils.isNotBlank(fileId)){
            fileId = new DatasetVariableTranslate(dataSet).mapTemplateString(fileId);
        } else {
            fileId = StringBaseOpt.castObjectToString(mapFirstRow.get(ConstantValue.FILE_ID));
        }
        InputStream inputStream = new FileInputStream(fileInfoOpt.getFile(fileId));
        if(StringUtils.isNotBlank(fileName)){
            fileName = new DatasetVariableTranslate(dataSet).mapTemplateString(fileName);
        } else {
            fileName = StringBaseOpt.castObjectToString(mapFirstRow.get(ConstantValue.FILE_NAME));
        }
        FileBaseInfo fileInfo = fileInfoOpt.getFileInfo(fileId);

        FileDataSet objectToDataSet = new FileDataSet();
        objectToDataSet.setFileContent( fileName, inputStream.available(), inputStream);
        objectToDataSet.setFileInfo(fileInfo);
        bizModel.putDataSet(targetDsName, objectToDataSet);

        return BuiltInOperation.createResponseSuccessData(objectToDataSet.getSize());
    }
}
