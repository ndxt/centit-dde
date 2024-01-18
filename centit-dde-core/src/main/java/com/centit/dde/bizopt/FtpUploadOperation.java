package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.dataset.FileDataSet;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.dde.utils.DataSetOptUtil;
import com.centit.dde.utils.FtpOperation;
import com.centit.framework.common.ResponseData;
import com.centit.product.metadata.dao.SourceInfoDao;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.json.JSONTransformer;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;

public class FtpUploadOperation extends FtpOperation implements BizOperation {

    public FtpUploadOperation(SourceInfoDao sourceInfoDao) {
        super(sourceInfoDao);
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        String ftpServiceId = BuiltInOperation.getJsonFieldString(bizOptJson, "ftpService", null);

        String sourDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        DataSet dataSet = bizModel.getDataSet(sourDsName);
        String filePathDesc = BuiltInOperation.getJsonFieldString(bizOptJson, "filePath", "");

        BizModelJSONTransform transformer = new BizModelJSONTransform(bizModel);
        String filePath = StringBaseOpt.objectToString(JSONTransformer.transformer(filePathDesc, transformer));
        if(StringUtils.isBlank(filePath)){
            if(StringUtils.isBlank(filePathDesc)){
                filePath = "/";
            } else {
                filePath = filePathDesc;
            }
        }

        FileDataSet fileInfo = DataSetOptUtil.attainFileDataset(dataSet, bizOptJson);

        FTPClient ftpClient = connectFtp(ftpServiceId);
        if(ftpClient==null){
            return BuiltInOperation.createResponseData(0, 1, ResponseData.ERROR_USER_CONFIG,
                "FPT服务配置信息错误！");
        }
        try{
            ftpClient.changeWorkingDirectory(filePath);
            ftpClient.storeFile(fileInfo.getFileName(),
                fileInfo.getFileInputStream());
        } finally {
            disConnectFtp(ftpClient);
        }
        return BuiltInOperation.createResponseSuccessData(1);
    }
}
