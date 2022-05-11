package com.centit.dde.ftp;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.bizopt.BuiltInOperation;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.utils.ConstantValue;
import com.centit.dde.utils.DataSetOptUtil;
import com.centit.framework.common.ResponseData;
import com.centit.product.metadata.dao.SourceInfoDao;
import com.centit.support.algorithm.StringBaseOpt;
import org.apache.commons.net.ftp.FTPClient;

import java.util.Map;

public class FtpUploadOperation extends FtpOperation {

    public FtpUploadOperation(SourceInfoDao sourceInfoDao) {
        super(sourceInfoDao);
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        String ftpServiceId = BuiltInOperation.getJsonFieldString(bizOptJson, "ftpService", null);

        String sourDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        DataSet dataSet = bizModel.fetchDataSetByName(sourDsName);
        String path = BuiltInOperation.getJsonFieldString(bizOptJson, "filePath", "/");

        Map<String, Object> fileInfo = DataSetOptUtil.getFileFormDataset(dataSet, bizOptJson);

        FTPClient ftpClient = connectFtp(ftpServiceId);
        if(ftpClient==null){
            return BuiltInOperation.createResponseData(0,1,ResponseData.ERROR_USER_CONFIG,
                "FPT服务配置信息错误！");
        }
        ftpClient.changeWorkingDirectory(path);
        ftpClient.storeFile(StringBaseOpt.castObjectToString(fileInfo.get(ConstantValue.FILE_NAME)),
            DataSetOptUtil.getInputStreamFormFile(fileInfo));

        disConnectFtp(ftpClient);
        return BuiltInOperation.createResponseSuccessData(1);
    }
}
