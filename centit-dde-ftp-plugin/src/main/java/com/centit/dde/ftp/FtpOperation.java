package com.centit.dde.ftp;

import com.centit.dde.core.BizOperation;
import com.centit.product.metadata.dao.SourceInfoDao;
import com.centit.product.metadata.po.SourceInfo;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.algorithm.StringBaseOpt;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.UnknownHostException;

public abstract class FtpOperation implements BizOperation {


    // 这个地方可以考虑 用线程变量 来绑定 ftp 客户端链接
    // 暂时不做这个。
    public SourceInfoDao sourceInfoDao;

    public FtpOperation(SourceInfoDao sourceInfoDao) {
        this.sourceInfoDao = sourceInfoDao;
    }

    public FTPClient connectFtp(String sourceId){
        SourceInfo ftpService = sourceInfoDao.getDatabaseInfoById(sourceId);
        if(ftpService==null){
            return null;
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
            e.printStackTrace();
            return null;
        }

        try {
            FTPClient ftp = new FTPClient();
            int reply;
            if (hasProxy)
                ftp.setProxy(proxy);
            ftp.connect(ftpUrl, ftpPort);//连接FTP服务器
            //如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
            ftp.login(ftpService.getUsername(), ftpService.getClearPassword());//登录
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return null;
            }
            return ftp;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
