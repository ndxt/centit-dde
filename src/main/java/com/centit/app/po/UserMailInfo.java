package com.centit.app.po;

import java.util.ArrayList;
import java.util.Date;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import com.centit.support.utils.DatetimeOpt;


/**
 * create by scaffold
 *
 * @author codefan@hotmail.com
 */

public class UserMailInfo implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private UserMailInfoId cid;

    private String mailPassword;
    private String mailType;

    /**
     * 消息编号
     */
    private int msgNumber;
    /**
     * 邮件标题
     */
    private String mailSubject;
    /**
     * 发送日期
     */
    private String sendDate;
    /**
     * 邮件发送人
     */
    private String mailFrom;
    /**
     * 是否新邮件
     */
    private boolean isNew;

    /**
     * 邮件内容
     */
    private String context;

    /**
     * 发送邮件地址
     */
    private String[] toEmailAddress;

    /** 邮件session */
    //private Session session;

    /**
     * 抄送人
     */
    private String[] cc;

    /**
     * 密送人
     */
    private String[] bcc;

    /**
     * 所有附件路径<系统中的完整路径>
     */
    private String[] filePaths;

    private String host;
    private String protocol;
    private Long port;

    // Constructors

    /**
     * default constructor
     */
    public UserMailInfo() {
    }

    /**
     * minimal constructor
     */
    public UserMailInfo(UserMailInfoId id

    ) {
        this.cid = id;

    }

    /**
     * full constructor
     */
    public UserMailInfo(UserMailInfoId id

            , String mailPassword, String mailType) {
        this.cid = id;

        this.mailPassword = mailPassword;
        this.mailType = mailType;
    }

    public UserMailInfoId getCid() {
        return this.cid;
    }

    public void setCid(UserMailInfoId id) {
        this.cid = id;
    }

    public String getUserCode() {
        if (this.cid == null)
            this.cid = new UserMailInfoId();
        return this.cid.getUserCode();
    }

    public void setUserCode(String userCode) {
        if (this.cid == null)
            this.cid = new UserMailInfoId();
        this.cid.setUserCode(userCode);
    }

    public String getMailAccount() {
        if (this.cid == null)
            this.cid = new UserMailInfoId();
        return this.cid.getMailAccount();
    }

    public void setMailAccount(String mailAccount) {
        if (this.cid == null)
            this.cid = new UserMailInfoId();
        this.cid.setMailAccount(mailAccount);
    }

    // Property accessors

    public String getMailPassword() {
        return this.mailPassword;
    }

    public void setMailPassword(String mailPassword) {
        this.mailPassword = mailPassword;
    }

    public String getMailType() {
        return this.mailType;
    }

    public void setMailType(String mailType) {
        this.mailType = mailType;
    }

    public void copy(UserMailInfo other) {

        this.setUserCode(other.getUserCode());
        this.setMailAccount(other.getMailAccount());

        this.mailPassword = other.getMailPassword();
        this.mailType = other.getMailType();

    }

    public void copyNotNullProperty(UserMailInfo other) {

        if (other.getUserCode() != null)
            this.setUserCode(other.getUserCode());
        if (other.getMailAccount() != null)
            this.setMailAccount(other.getMailAccount());

        if (other.getMailPassword() != null)
            this.mailPassword = other.getMailPassword();
        if (other.getMailType() != null)
            this.mailType = other.getMailType();

    }

    public void clearProperties() {

        this.mailPassword = null;
        this.mailType = null;
    }


    /**
     * 获得邮件发送信息
     *
     * @param 参照ContextType类中定义常量 0:为普通文本形式发送邮件,1:html形式发送
     * @return
     */
    public Message getMessage(int type) throws Exception {
        Message msg = null;
        try {
            msg = new MimeMessage((Session) null/*session*/);
            msg.setSubject(mailSubject);//设置主题
            if (sendDate != null) {
                Date date = DatetimeOpt.convertStringToDate(sendDate);
                msg.setSentDate(date);
            } else {
                msg.setSentDate(new Date());
            }
            msg.setFrom(new InternetAddress(mailFrom));//设置发件人
            msg.setRecipients(RecipientType.TO,
                    getEmailAddress(toEmailAddress));
            if (cc != null && cc.length > 0) {
                msg.setRecipients(Message.RecipientType.CC, getEmailAddress(cc));//抄送人地址
            }
            if (bcc != null && bcc.length > 0) {
                msg.setRecipients(Message.RecipientType.CC, getEmailAddress(bcc));//密送人地址
            }
            //添加邮件内容
            Multipart mp = null;
            if (filePaths != null && filePaths.length > 0) {
                mp = new MimeMultipart("mixed");
            } else {
                mp = new MimeMultipart();
            }
            //发送正文内容
            mp.addBodyPart(getTextMultipart(type));
            //发送附件
            ArrayList<MimeBodyPart> parts = getAttachmentMultipart();
            for (MimeBodyPart mbp : parts) {
                mp.addBodyPart(mbp);
            }

            msg.setContent(mp);
        } catch (Exception e) {
            throw e;
        }
        return msg;
    }

    /**
     * 得到发送邮件地址
     *
     * @param toEmailAddress
     * @return
     */
    private InternetAddress[] getEmailAddress(String[] toEmailAddress) throws Exception {
        if (toEmailAddress == null || toEmailAddress.length <= 0) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < toEmailAddress.length; i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(toEmailAddress[i]);
        }

        return InternetAddress.parse(sb.toString());
    }

    /**
     * 获得邮件内容 type : 0,普通文本形式发送邮件;1,html形式发送邮件
     *
     * @return
     */
    private MimeBodyPart getTextMultipart(int type) throws Exception {
        MimeBodyPart mbp = null;
        try {
            mbp = new MimeBodyPart();
            if (type == 0) {
                mbp.setContent(context, "text/plain;charset=GB2312");
            } else if (type == 1) {
                mbp.setContent(context, "text/html;charset=GB2312");
            }
        } catch (Exception e) {
            throw e;
        }
        return mbp;
    }

    /**
     * 包装所有邮件附件
     *
     * @return
     */
    private ArrayList<MimeBodyPart> getAttachmentMultipart() throws Exception {
        if (filePaths == null || filePaths.length <= 0) {
            return null;
        }
        ArrayList<MimeBodyPart> parts = null;
        try {
            /*
             * int fileSize=0;
            for(int i=0;i<filePaths.length;i++){
                File file=new File(filePaths[i]);
                fileSize+=file.length();
            }
            *不需要有2MB的大小限制
             * if(fileSize> 2*1024*1024){
                throw new Exception("你上传附件超过限制");
            }*/
            parts = new ArrayList<MimeBodyPart>();
            for (int i = 0; i < filePaths.length; i++) {
                MimeBodyPart mbp = new MimeBodyPart();
                if (filePaths[i] != null && !"".equals(filePaths[i])) {
                    FileDataSource fds = new FileDataSource(filePaths[i]);
                    mbp.setDataHandler(new DataHandler(fds));
                    mbp.setFileName(MimeUtility.encodeText(fds.getName(), "GB2312", "B"));
                    parts.add(mbp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return parts;
    }

    public int getMsgNumber() {
        return msgNumber;
    }

    public void setMsgNumber(int msgNumber) {
        this.msgNumber = msgNumber;
    }

    public String getMailSubject() {
        return mailSubject;
    }

    public void setMailSubject(String mailSubject) {
        this.mailSubject = mailSubject;
    }

    public String getSendDate() {
        return sendDate;
    }

    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }

    public String getMailFrom() {
        return mailFrom;
    }

    public void setMailFrom(String mailFrom) {
        this.mailFrom = mailFrom;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean isNew) {
        this.isNew = isNew;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String[] getToEmailAddress() {
        return toEmailAddress;
    }

    public void setToEmailAddress(String[] toEmailAddress) {
        this.toEmailAddress = toEmailAddress;
    }

    public String[] getCc() {
        return cc;
    }

    public void setCc(String[] cc) {
        this.cc = cc;
    }

    public String[] getBcc() {
        return bcc;
    }

    public void setBcc(String[] bcc) {
        this.bcc = bcc;
    }

    public String[] getFilePaths() {
        return filePaths;
    }

    public void setFilePaths(String[] filePaths) {
        this.filePaths = filePaths;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public Long getPort() {
        return port;
    }

    public void setPort(Long port) {
        this.port = port;
    }
}
