package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.dde.utils.ConstantValue;
import com.centit.dde.utils.FtpOperationUtil;
import com.centit.framework.common.ResponseData;
import com.centit.product.metadata.po.SourceInfo;
import com.centit.product.metadata.service.SourceInfoMetadata;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.json.JSONTransformer;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FtpDownloadOperation extends FtpOperationUtil implements BizOperation {

    public FtpDownloadOperation(SourceInfoMetadata sourceInfoMetadata){
        super(sourceInfoMetadata);
    }
    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws IOException {

        String ftpServiceId = BuiltInOperation.getJsonFieldString(bizOptJson, "ftpService", null);
        String filePathDesc = BuiltInOperation.getJsonFieldString(bizOptJson, "filePath", "");
        String fileNameDesc = BuiltInOperation.getJsonFieldString(bizOptJson, "fileName", "");

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

        SourceInfo ftpService = sourceInfoMetadata.fetchSourceInfo(ftpServiceId);
        FTPClient ftpClient = getFtp(ftpService);

        String pathEncoding =  StringBaseOpt.castObjectToString(ftpService.getExtProp("pathEncoding"));
        if(!StringBaseOpt.isNvl(pathEncoding)){
            filePath = new String(filePath.getBytes(pathEncoding), StandardCharsets.ISO_8859_1);
            fileName = new String(fileName.getBytes(pathEncoding), StandardCharsets.ISO_8859_1);
        }
        ByteArrayOutputStream outs = new ByteArrayOutputStream();
        boolean changeDir= ftpClient.changeWorkingDirectory(filePath);
        ftpClient.retrieveFile(fileName, outs);
        // outs.close();
        DataSet objectToDataSet = DataSet.toDataSet(
            CollectionsOpt.createHashMap(ConstantValue.FILE_NAME, fileName,
                ConstantValue.FILE_SIZE, outs.size(),
                ConstantValue.FILE_CONTENT ,outs,"changeDir",changeDir));
        String id = bizOptJson.getString("id");
        bizModel.putDataSet(id,objectToDataSet);
        return BuiltInOperation.createResponseSuccessData(1);
    }
}
