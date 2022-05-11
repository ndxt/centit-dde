package com.centit.dde.ftp;

import com.centit.dde.core.BizOptFlow;
import com.centit.dde.utils.ConstantValue;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
public class FtpRegisterOperation {
    @Resource
    BizOptFlow bizOptFlow;


    @PostConstruct
    void registerOperation(){
        //注册FTP下载组件
        bizOptFlow.registerOperation(ConstantValue.FTP_FILE_DOWNLOAD,new FtpFileDownLoadOperation());
        //注册FTP上传组件
        bizOptFlow.registerOperation(ConstantValue.FTP_FILE_UPLOAD,new FtpFileUpLoadOperation());
    }
}
