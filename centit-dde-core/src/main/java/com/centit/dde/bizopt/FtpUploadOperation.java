package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.dataset.FileDataSet;
import com.centit.dde.utils.DataSetOptUtil;
import com.centit.dde.utils.FtpOperation;
import com.centit.framework.common.ResponseData;
import com.centit.product.metadata.dao.SourceInfoDao;
import org.apache.commons.net.ftp.FTPClient;

public class FtpUploadOperation extends FtpOperation {

    public FtpUploadOperation(SourceInfoDao sourceInfoDao) {
        super(sourceInfoDao);
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        String ftpServiceId = BuiltInOperation.getJsonFieldString(bizOptJson, "ftpService", null);

        String sourDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        DataSet dataSet = bizModel.getDataSet(sourDsName);
        String path = BuiltInOperation.getJsonFieldString(bizOptJson, "filePath", "/");

        FileDataSet fileInfo = DataSetOptUtil.attainFileDataset(dataSet, bizOptJson);

        FTPClient ftpClient = connectFtp(ftpServiceId);
        if(ftpClient==null){
            return BuiltInOperation.createResponseData(0,1,ResponseData.ERROR_USER_CONFIG,
                "FPT服务配置信息错误！");
        }
        try{
            ftpClient.changeWorkingDirectory(path);
            ftpClient.storeFile(fileInfo.getFileName(),
                fileInfo.getFileInputStream());
        } finally {
            disConnectFtp(ftpClient);
        }
        return BuiltInOperation.createResponseSuccessData(1);
    }
}
