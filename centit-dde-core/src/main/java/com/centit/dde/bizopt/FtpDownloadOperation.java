package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.adapter.utils.ConstantValue;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.utils.FtpOperation;
import com.centit.framework.common.ResponseData;
import com.centit.product.metadata.dao.SourceInfoDao;
import com.centit.support.algorithm.CollectionsOpt;
import org.apache.commons.net.ftp.FTPClient;

import java.io.ByteArrayOutputStream;

public class FtpDownloadOperation extends FtpOperation {

    public FtpDownloadOperation(SourceInfoDao sourceInfoDao){
        super(sourceInfoDao);
    }
    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {

        String ftpServiceId = BuiltInOperation.getJsonFieldString(bizOptJson, "ftpService", null);
        String filePath = BuiltInOperation.getJsonFieldString(bizOptJson, "filePath", "/");
        String fileName = BuiltInOperation.getJsonFieldString(bizOptJson, "fileName", "/");

        FTPClient ftpClient = connectFtp(ftpServiceId);
        if(ftpClient==null){
            return BuiltInOperation.createResponseData(0,1,ResponseData.ERROR_USER_CONFIG,
                "FPT服务配置信息错误！");
        }
        ByteArrayOutputStream outs = new ByteArrayOutputStream();
        try {
            ftpClient.changeWorkingDirectory(filePath);
            ftpClient.retrieveFile(fileName, outs);
        } finally {
            disConnectFtp(ftpClient);
        }
        // outs.close();
        DataSet objectToDataSet = DataSet.toDataSet(
            CollectionsOpt.createHashMap(ConstantValue.FILE_NAME, fileName,
                ConstantValue.FILE_SIZE, outs.size(),
                ConstantValue.FILE_CONTENT ,outs));
        String id = bizOptJson.getString("id");
        bizModel.putDataSet(id,objectToDataSet);
        return BuiltInOperation.createResponseSuccessData(1);
    }
}
