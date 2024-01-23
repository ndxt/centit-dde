package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.adapter.utils.ConstantValue;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.dde.utils.FtpOperation;
import com.centit.framework.common.ResponseData;
import com.centit.product.metadata.dao.SourceInfoDao;
import com.centit.support.algorithm.BooleanBaseOpt;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.json.JSONTransformer;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.ByteArrayOutputStream;

public class FtpDownloadOperation extends FtpOperation implements BizOperation {

    public FtpDownloadOperation(SourceInfoDao sourceInfoDao){
        super(sourceInfoDao);
    }
    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {

        String ftpServiceId = BuiltInOperation.getJsonFieldString(bizOptJson, "ftpService", null);
        String filePathDesc = BuiltInOperation.getJsonFieldString(bizOptJson, "filePath", "");
        String fileNameDesc = BuiltInOperation.getJsonFieldString(bizOptJson, "fileName", "");
        String sourceCharset = BuiltInOperation.getJsonFieldString(bizOptJson, "sourceCharset", "GBK");
        String targetCharset = BuiltInOperation.getJsonFieldString(bizOptJson, "targetCharset", "iso-8859-1");
        BizModelJSONTransform transformer = new BizModelJSONTransform(bizModel);
        String filePath = StringBaseOpt.objectToString(JSONTransformer.transformer(filePathDesc, transformer));
        if(StringUtils.isBlank(filePath)){
            if(StringUtils.isBlank(filePathDesc)){
                filePath = "/";
            } else {
                filePath = filePathDesc;
            }
        }
        String fileName = StringBaseOpt.objectToString(JSONTransformer.transformer(fileNameDesc, transformer));
        if(StringUtils.isBlank(fileName)){
            fileName = fileNameDesc;
        }

        FTPClient ftpClient = connectFtp(ftpServiceId);
        if(ftpClient==null){
            return BuiltInOperation.createResponseData(0,1,ResponseData.ERROR_USER_CONFIG,
                "FPT服务配置信息错误！");
        }
        ByteArrayOutputStream outs = new ByteArrayOutputStream();
        boolean change;
        boolean file;
        String path="";
        try {
            change=ftpClient.changeWorkingDirectory(new String(filePath.getBytes(sourceCharset),targetCharset));
            path=ftpClient.printWorkingDirectory();
            file=ftpClient.retrieveFile(new String(fileName.getBytes(sourceCharset),targetCharset), outs);
        } finally {
            disConnectFtp(ftpClient);
        }
        // outs.close();
        DataSet objectToDataSet = DataSet.toDataSet(
            CollectionsOpt.createHashMap(ConstantValue.FILE_NAME, fileName,
                ConstantValue.FILE_SIZE, outs.size(),
                ConstantValue.FILE_CONTENT ,outs,"当前路径",path,"切换目录是否成功", change,"获取文件是否成功",file));
        String id = bizOptJson.getString("id");
        bizModel.putDataSet(id,objectToDataSet);
        return BuiltInOperation.createResponseSuccessData(1);
    }
}
