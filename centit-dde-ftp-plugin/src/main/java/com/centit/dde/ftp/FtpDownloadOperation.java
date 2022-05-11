package com.centit.dde.ftp;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.bizopt.BuiltInOperation;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.utils.BizOptUtils;
import com.centit.dde.utils.ConstantValue;
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
        DataSet objectToDataSet = BizOptUtils.castObjectToDataSet(
            CollectionsOpt.createHashMap(ConstantValue.FILE_NAME, fileName,
                ConstantValue.FILE_SIZE, outs.size(),
                ConstantValue.FILE_CONTENT ,outs));
        String id = bizOptJson.getString("id");
        bizModel.putDataSet(id,objectToDataSet);
        return BuiltInOperation.createResponseSuccessData(1);
    }
}
