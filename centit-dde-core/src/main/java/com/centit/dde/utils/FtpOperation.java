package com.centit.dde.utils;

import ch.qos.logback.core.joran.conditional.ThenAction;
import com.centit.product.metadata.api.ISourceInfo;
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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class FtpOperation {

    private static final Map<ISourceInfo, FTPClient> ftpClientPools = new ConcurrentHashMap<>(16);

    // 这个地方可以考虑 用线程变量 来绑定 ftp 客户端链接
    // 暂时不做这个。
    public SourceInfoDao sourceInfoDao;

    public FtpOperation(SourceInfoDao sourceInfoDao) {
        this.sourceInfoDao = sourceInfoDao;
    }

    public FTPClient getFtp(SourceInfo ftpService) throws IOException {
        FTPClient ftpClient = ftpClientPools.get(ftpService);
        if (ftpClient == null) {
            ftpClient = connectFtp(ftpService);
            ftpClientPools.put(ftpService, ftpClient);
        }
        if (!ftpClient.isConnected()) {
            reconnect(ftpClient,ftpService);
        }
        ftpClient.sendNoOp();
        ftpClient.changeWorkingDirectory("/");
        return ftpClient;
    }

    private void reconnect(FTPClient ftp, SourceInfo ftpService) throws IOException {
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
            throw new ObjectException(ObjectException.DATA_VALIDATE_ERROR, "ftp代理设置不正确，：" + e.getMessage(), e);
        }
        String controlEncoding = StringBaseOpt.castObjectToString(ftpService.getExtProp("controlEncoding"));
        if (StringUtils.isNotBlank(controlEncoding)) {
            ftp.setControlEncoding(controlEncoding);
        }
        int reply;
        if (hasProxy) {
            ftp.setProxy(proxy);
        }
        ftp.connect(ftpUrl, ftpPort);//连接FTP服务器
        //如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
        if (StringUtils.isNotBlank(ftpService.getUsername())) {
            boolean loginSuccess = ftp.login(ftpService.getUsername(), ftpService.getClearPassword());//登录
            if (!loginSuccess) {
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
    }

    private FTPClient connectFtp(SourceInfo ftpService) {
        if (ftpService == null) {
            throw new ObjectException(ObjectException.DATA_VALIDATE_ERROR, "ftp服务资源找不到，资源ID" + ftpService.getDatabaseCode());
        }
        try {
            FTPClient ftp = new FTPClient();
            reconnect(ftp, ftpService);
            return ftp;
        } catch (IOException e) {
            throw new ObjectException(ObjectException.DATA_VALIDATE_ERROR, "登录ftp服务失败，：" + e.getMessage(), e);
        }
    }


}
