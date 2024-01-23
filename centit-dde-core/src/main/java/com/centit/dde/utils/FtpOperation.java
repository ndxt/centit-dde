package com.centit.dde.utils;

import com.centit.product.metadata.dao.SourceInfoDao;
import com.centit.product.metadata.po.SourceInfo;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.common.ObjectException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.UnknownHostException;

public abstract class FtpOperation {


    // 这个地方可以考虑 用线程变量 来绑定 ftp 客户端链接
    // 暂时不做这个。
    public SourceInfoDao sourceInfoDao;

    public FtpOperation(SourceInfoDao sourceInfoDao) {
        this.sourceInfoDao = sourceInfoDao;
    }

    public FTPClient connectFtp(SourceInfo ftpService){
        if(ftpService==null){
            throw new ObjectException(ObjectException.DATA_VALIDATE_ERROR, "ftp服务资源找不到，资源ID"+ ftpService.getDatabaseCode());
        }
        String ftpUrl = ftpService.getDatabaseUrl();
        int ftpPort = NumberBaseOpt.castObjectToInteger(
            ftpService.getExtProp("ftpPort"), 21);
        String proxyHost = StringBaseOpt.castObjectToString(ftpService.getExtProp("proxyHost"));
        boolean hasProxy = StringUtils.isNotBlank(proxyHost);
        Proxy proxy = null;
        try {
            if (hasProxy) {
                String proxyType = StringBaseOpt.castObjectToString(ftpService.getExtProp("proxyType"));
                Proxy.Type type = "direct".equalsIgnoreCase(proxyType) ? Proxy.Type.DIRECT :
                    ("socks".equalsIgnoreCase(proxyType) ? Proxy.Type.SOCKS : Proxy.Type.HTTP);
                int proxyPort = NumberBaseOpt.castObjectToInteger(
                    ftpService.getExtProp("proxyPort"), 8080);
                proxy = new Proxy(type,
                    new InetSocketAddress(InetAddress.getByName(proxyHost), proxyPort));
            }
        } catch (UnknownHostException e) {
            throw new ObjectException(ObjectException.DATA_VALIDATE_ERROR, "ftp代理设置不正确，："+ e.getMessage(), e);
        }

        try {
            FTPClient ftp = new FTPClient();
            String controlEncoding =  StringBaseOpt.castObjectToString(ftpService.getExtProp("controlEncoding"));
            if(StringUtils.isNotBlank(controlEncoding)) {
                ftp.setControlEncoding(controlEncoding);
            }
            int reply;
            if (hasProxy)
                ftp.setProxy(proxy);
            ftp.connect(ftpUrl, ftpPort);//连接FTP服务器
            //如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
            if(StringUtils.isNotBlank(ftpService.getUsername())) {
                boolean loginSuccess=ftp.login(ftpService.getUsername(), ftpService.getClearPassword());//登录
                if(!loginSuccess){
                    throw new ObjectException(ObjectException.DATA_VALIDATE_ERROR, "ftp登录用户名密码设置不正确！");
                }
            }
            ftp.setFileType(FTP.BINARY_FILE_TYPE);// 设置文件传输类型

            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                throw new ObjectException(ObjectException.DATA_VALIDATE_ERROR, "登录ftp服务失败！");
            }
            ftp.enterLocalPassiveMode();
            return ftp;
        } catch (IOException e) {
            throw new ObjectException(ObjectException.DATA_VALIDATE_ERROR, "登录ftp服务失败，："+ e.getMessage(), e);
        }
    }

    public void disConnectFtp(FTPClient ftp ){
        try {
            ftp.logout();
            ftp.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
