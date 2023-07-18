package com.centit.dde.ftp;

import com.centit.dde.adapter.utils.ConstantValue;
import com.centit.dde.core.BizOptFlow;
import com.centit.product.metadata.dao.SourceInfoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class FtpRegisterOperation {

    @Autowired
    BizOptFlow bizOptFlow;

    @Autowired
    SourceInfoDao sourceInfoDao;

    @PostConstruct
    void registerOperation(){
        //注册FTP下载组件
        bizOptFlow.registerOperation(ConstantValue.FTP_FILE_DOWNLOAD,new FtpDownloadOperation(sourceInfoDao));
        //注册FTP上传组件
        bizOptFlow.registerOperation(ConstantValue.FTP_FILE_UPLOAD,new FtpUploadOperation(sourceInfoDao));
    }
}
