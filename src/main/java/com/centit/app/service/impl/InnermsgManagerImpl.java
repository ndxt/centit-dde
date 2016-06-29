package com.centit.app.service.impl;

import com.centit.app.dao.InnermsgDao;
import com.centit.app.dao.InnermsgRecipientDao;
import com.centit.app.dao.MsgannexDao;
import com.centit.app.dao.UserMailConfigDao;
import com.centit.app.po.*;
import com.centit.app.service.FileinfoManager;
import com.centit.app.service.InnermsgManager;
import com.centit.app.util.JavaMailUtils;
import com.centit.core.service.BaseEntityManagerImpl;
import com.centit.support.utils.DatetimeOpt;
import com.centit.sys.service.CodeRepositoryUtil;
import com.centit.sys.util.InstantMsgFactory;
import com.centit.sys.util.SysParametersUtils;
import com.sun.mail.imap.IMAPMessage;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.regex.Pattern;

public class InnermsgManagerImpl extends BaseEntityManagerImpl<Innermsg> implements InnermsgManager/* , MessageSender */ {
    /**
     *
     */
    private static final long serialVersionUID = 1488868818774386122L;

    private static final Log LOG = LogFactory.getLog(InnermsgManagerImpl.class);

    private InnermsgDao innermsgDao;
    private InnermsgRecipientDao innermsgRecipientDao;
    private UserMailConfigDao userMailConfigDao;
    private MsgannexDao msgannexDao;

    private FileinfoManager fileinfoManager;


    public void setFileinfoManager(FileinfoManager fileinfoManager) {
        this.fileinfoManager = fileinfoManager;
    }
    // 即时消息发送接口
    // private IInstantMsg instantMsg = InstantMsgFactory.getInstance();

    public void setInnermsgDao(InnermsgDao baseDao) {

        this.innermsgDao = baseDao;
        setBaseDao(this.innermsgDao);
    }

    public void setUserMailConfigDao(UserMailConfigDao userMailConfigDao) {
        this.userMailConfigDao = userMailConfigDao;
    }

    public void setInnermsgRecipientDao(InnermsgRecipientDao innermsgRecipientDao) {
        this.innermsgRecipientDao = innermsgRecipientDao;
    }

    @Override
    public void saveMsg(Innermsg innermsg) {
        Set<String> userCodes = new HashSet<String>();
        if (Pattern.matches(".*;.*", innermsg.getReceiveUserCode())) {
            userCodes.addAll(Arrays.asList(innermsg.getReceiveUserCode().split("\\s*;\\s*")));
        } else {
            userCodes.add(innermsg.getReceiveUserCode());
        }

        innermsg.setMsgcode(this.innermsgDao.getNextMsgCode());

        innermsg.setHoldusers(Long.valueOf(userCodes.size() + 1));
        // 已读
        innermsg.setMsgstate(CodeRepositoryUtil.getValue(Innermsg.MSG_STAT, Innermsg.MSG_STAT_R));
        // 消息 公告  msgtype


        Set<InnermsgRecipient> irs = new HashSet<InnermsgRecipient>();
        for (String userCode : userCodes) {
            InnermsgRecipient ir = new InnermsgRecipient(this.innermsgRecipientDao.getNextMsgRecipiCode(), innermsg,
                    userCode, CodeRepositoryUtil.getValue(Innermsg.MSG_TYPE, Innermsg.MSG_TYPE_P),
                    CodeRepositoryUtil.getValue(Innermsg.MSG_STAT, Innermsg.MSG_STAT_U));
            irs.add(ir);
        }
        innermsg.setInnermsgRecipients(irs);

        //消息附件
        this.innerMsgannex(innermsg);

        super.saveObject(innermsg);
        //this.msgannexDao.batchSaveObject(new ArrayList<Msgannex>(innermsg.getMsgannexs()));

        InstantMsgFactory.getInstance().push(new ArrayList<String>(userCodes), innermsg.getMsgtitle());
    }

    @Override
    public void saveAnnouncement(Innermsg innermsg) {
        this.innerMsgannex(innermsg);
        super.saveObject(innermsg);
        InstantMsgFactory.getInstance().pushAll(innermsg.getMsgtitle());
    }

    @Override
    public void deleteObject(Innermsg o) {
        List<String> paths = this.getInnermsgAnnexPaths(o);
        try {
            super.deleteObject(o);
            if (!(CollectionUtils.isEmpty(paths))) {
                this.deleteInnermsgAnnex(paths);
            }
        } catch (RuntimeException e) {
            throw e;
        }
    }

    @Override
    public void deleteMsg(Innermsg innermsg, String mailType) {
        long holdUsers = this.innermsgRecipientDao.countHoldUsers(new InnermsgRecipient(innermsg));
        Innermsg imsg = this.getObject(innermsg);
        // 发件
        if (CodeRepositoryUtil.getValue(Innermsg.MAIL_TYPE, Innermsg.MAIL_TYPE_O).equals(mailType)) {

            // 收件人已无人持有此信息，删除
            if (0 == holdUsers) {
                List<String> paths = this.getInnermsgAnnexPaths(innermsg);
                try {
                    this.deleteObject(imsg);
                    if (!(CollectionUtils.isEmpty(paths))) {
                        this.deleteInnermsgAnnex(paths);
                    }
                } catch (RuntimeException e) {
                    throw e;
                }

            } else {
                // 持有数-1，将此消息在发件人位置不可见
                imsg.setHoldusers(imsg.getHoldusers() - 1);
                imsg.setMsgstate(CodeRepositoryUtil.getValue(Innermsg.MSG_STAT, Innermsg.MSG_STAT_D));

                this.saveObject(imsg);
            }

            // 收件人删除消息
        } else if (CodeRepositoryUtil.getValue(Innermsg.MAIL_TYPE, Innermsg.MAIL_TYPE_I).equals(mailType)) {
            // 当前人为最后一个持有人
            InnermsgRecipient ir = new InnermsgRecipient(imsg, innermsg.getReceiveUserCode());
            this.innermsgRecipientDao.delete(ir);
            if (1 == holdUsers) {
                // 发件人已删除此条消息

                //或邮件删除
                if (CodeRepositoryUtil.getValue(Innermsg.MSG_STAT, Innermsg.MSG_STAT_D).equals(imsg.getMsgstate())) {
                    List<String> paths = this.getInnermsgAnnexPaths(innermsg);

                    try {
                        this.deleteObject(imsg);
                        if (!(CollectionUtils.isEmpty(paths))) {
                            this.deleteInnermsgAnnex(paths);
                        }
                    } catch (RuntimeException e) {
                        throw e;
                    }
                }
            }
        }

    }

    @Override
    public void deleteMail(Innermsg innermsg) {
        List<String> paths = this.getInnermsgAnnexPaths(innermsg);

        try {
            this.deleteObject(innermsg);
            if (!(CollectionUtils.isEmpty(paths))) {
                this.deleteInnermsgAnnex(paths);
            }
        } catch (RuntimeException e) {
            throw e;
        }
    }


    /**
     * 获取附件路径
     *
     * @param o
     * @return
     */
    private List<String> getInnermsgAnnexPaths(Innermsg o) {
        if (CollectionUtils.isEmpty(o.getMsgannexs())) {
            return new ArrayList<String>();
        }

        List<String> paths = new ArrayList<String>();
        for (Msgannex annex : o.getMsgannexs()) {
            paths.add(annex.getFileinfo().getPath());

        }

        return paths;
    }

    /**
     * 删除消息附件
     *
     * @param imsg
     */
    private void deleteInnermsgAnnex(List<String> paths) {
        for (String path : paths) {
            this.fileinfoManager.deleteFileOnDisk(path);
        }
    }

    @Override
    public void saveSendMail(Innermsg innermsg2) {
        //DwzResultParam param = new DwzResultParam();

        String pattern = "\\s*;\\s*";
        UserMailConfig c = this.userMailConfigDao.getObjectById(innermsg2.getEmailid());
        String[] to = StringUtils.hasText(innermsg2.getTo()) ? verificationMailAccount(innermsg2.getTo().trim()
                .split(pattern)) : null;
        String[] cc = StringUtils.hasText(innermsg2.getCc()) ? verificationMailAccount(innermsg2.getCc().trim()
                .split(pattern)) : null;
        String[] bcc = StringUtils.hasText(innermsg2.getBcc()) ? verificationMailAccount(innermsg2.getBcc().trim()
                .split(pattern)) : null;

        //消息附件
        Innermsg innermsg = this.innerMsgannex(innermsg2);

        // 发送邮件
        if (CodeRepositoryUtil.getValue(Innermsg.MAIL_TYPE, Innermsg.MAIL_TYPE_O).equalsIgnoreCase(
                innermsg.getMailtype())) {
            JavaMailSenderImpl sender = (JavaMailSenderImpl) JavaMailUtils.getJavaMailSender(c.getSmtpurl(),
                    c.getMailaccount(), c.getMailpassword());
            sender.setPort(c.getSmtpport().intValue());
            Properties p = new Properties();
            p.put("mail.smtp.auth", "true");

            sender.setJavaMailProperties(p);

            String uid = null;
            try {
                HashMap<String, File> attachment = new HashMap<String, File>();
                if (!CollectionUtils.isEmpty(innermsg.getMsgannexs())) {
                    for (Msgannex msgannex : innermsg.getMsgannexs()) {
                        attachment.put(msgannex.getFileinfo().getFilename(), new File(SysParametersUtils.getUploadHome() + msgannex.getFileinfo().getPath()));
                    }
                }

                uid = JavaMailUtils.sendMimeMessage(sender, innermsg.getMsgtitle(), innermsg.getMsgcontent(),
                        c.getMailaccount(), to, cc, bcc, CollectionUtils.isEmpty(attachment) ? null : attachment, false);
            } catch (UnsupportedEncodingException e) {
                LOG.error("用户 " + c.getUsercode() + " 账户 " + c.getMailaccount() + " 发送邮件失败", e);

                throw new RuntimeException("发送邮件失败");

            } catch (MessagingException e) {
                LOG.error("用户 " + c.getUsercode() + " 账户 " + c.getMailaccount() + " 发送邮件失败", e);

                throw new RuntimeException("发送邮件失败");
            }

            if (null == uid) {
                throw new RuntimeException("发送邮件失败");
            }

            innermsg.setMsgcode(uid);
        }

        // 保存草稿箱

        innermsg.setMsgtype(CodeRepositoryUtil.getValue(Innermsg.MSG_TYPE, Innermsg.MSG_TYPE_M));
        innermsg.setMsgstate(CodeRepositoryUtil.getValue(Innermsg.MSG_STAT, Innermsg.MSG_STAT_R));
        // innermsg.setMailtype(CodeRepositoryUtil.getValue(Innermsg.MAIL_TYPE, Innermsg.MAIL_TYPE_O));

        innermsg.setSenddate(new Date());

        innermsg.getInnermsgRecipients().addAll(
                this.listSenderMail(innermsg, to,
                        CodeRepositoryUtil.getValue(Innermsg.RECIPIENT_TYPE, Innermsg.RECIPIENT_TYPE_T)));
        innermsg.getInnermsgRecipients().addAll(
                this.listSenderMail(innermsg, cc,
                        CodeRepositoryUtil.getValue(Innermsg.RECIPIENT_TYPE, Innermsg.RECIPIENT_TYPE_C)));
        innermsg.getInnermsgRecipients().addAll(
                this.listSenderMail(innermsg, bcc,
                        CodeRepositoryUtil.getValue(Innermsg.RECIPIENT_TYPE, Innermsg.RECIPIENT_TYPE_B)));

        innermsg.setC(c);


        super.saveObject(innermsg);

    }

    /**
     * 消息附件
     *
     * @param innermsg
     * @return
     */
    private Innermsg innerMsgannex(Innermsg innermsg) {
        List<Fileinfo> fileinfos = new ArrayList<Fileinfo>();
        if (!CollectionUtils.isEmpty(innermsg.getMsgannexs())) {
            for (Msgannex o : innermsg.getMsgannexs()) {
                o.setInnermsg(innermsg);
                o.setMsgannexId(this.innermsgDao.getNextMsgCode());


                fileinfos.add(o.getFileinfo());
            }

            //调用文件接口，确认文件上传成功
            if (!CollectionUtils.isEmpty(fileinfos)) {
                this.fileinfoManager.update4ConfirmFileinfos(fileinfos);
            }
        }

        return innermsg;
    }

    private void receiveMail(Innermsg innermsg, Message msg, String mailUID, UserMailConfig c)
            throws MessagingException, IOException {
        String pattern = "\\s*,\\s*";
        Innermsg imsg = new Innermsg(mailUID);

        imsg.setReceiveUserCode(innermsg.getReceiveUserCode());

        // 收件箱
        imsg.setMailtype(CodeRepositoryUtil.getValue(Innermsg.MAIL_TYPE, Innermsg.MAIL_TYPE_I));
        // 类别为邮件
        imsg.setMsgtype(CodeRepositoryUtil.getValue(Innermsg.MSG_TYPE, Innermsg.MSG_TYPE_M));
        // 邮件状态 未读
        imsg.setMsgstate(CodeRepositoryUtil.getValue(Innermsg.MSG_STAT, Innermsg.MSG_STAT_U));

        // 发件人
        imsg.setSender(JavaMailUtils.getFrom(msg));

        imsg.setMsgtitle(JavaMailUtils.getSubject(msg));

        imsg.setMsgcontent(JavaMailUtils.getText(msg, new StringBuffer()));
        imsg.setTo(JavaMailUtils.getTo(msg));
        imsg.setCc(JavaMailUtils.getCC(msg));
        imsg.setBcc(JavaMailUtils.getBCC(msg));

        imsg.setSenddate(DatetimeOpt.convertStringToDate(JavaMailUtils.getSendDate(msg), JavaMailUtils.DATE_PATTERN));

        String[] to = StringUtils.hasText(imsg.getTo()) ? imsg.getTo().trim().split(pattern) : null;
        String[] cc = StringUtils.hasText(imsg.getCc()) ? imsg.getCc().trim().split(pattern) : null;
        String[] bcc = StringUtils.hasText(imsg.getBcc()) ? imsg.getBcc().trim().split(pattern) : null;
        imsg.getInnermsgRecipients().addAll(
                this.listSenderMail(imsg, to,
                        CodeRepositoryUtil.getValue(Innermsg.RECIPIENT_TYPE, Innermsg.RECIPIENT_TYPE_T)));
        imsg.getInnermsgRecipients().addAll(
                this.listSenderMail(imsg, cc,
                        CodeRepositoryUtil.getValue(Innermsg.RECIPIENT_TYPE, Innermsg.RECIPIENT_TYPE_C)));
        imsg.getInnermsgRecipients().addAll(
                this.listSenderMail(imsg, bcc,
                        CodeRepositoryUtil.getValue(Innermsg.RECIPIENT_TYPE, Innermsg.RECIPIENT_TYPE_B)));

        //附件
        Map<String, InputStream> attachMent = JavaMailUtils.getAttachMent(msg, new HashMap<String, InputStream>());
        for (Map.Entry entry : attachMent.entrySet()) {
            Fileinfo fileinfo = new Fileinfo();

            fileinfo.setFilename((String) entry.getKey());
            fileinfo.setFileext(FilenameUtils.getExtension(fileinfo.getFilename()));
            fileinfo.setRecorder(c.getUsercode());
            fileinfo.setRecorderDate(new Date());

            fileinfo = this.fileinfoManager.saveFileinfo(fileinfo, (InputStream) entry.getValue(), "EMAIL");
            imsg.getMsgannexs().add(new Msgannex(this.innermsgDao.getNextMsgCode(), imsg, fileinfo));
        }

        imsg.setC(c);

        super.saveObject(imsg);
    }

    /**
     * Pop3 InBox Message
     *
     * @param c
     * @return
     * @throws MessagingException
     */
    private Message[] listPop3Message(UserMailConfig c) throws MessagingException {
        Store store = JavaMailUtils.getPop3Store(c.getReceiveurl(), c.getReceiveport().intValue(), c.getMailaccount(),
                c.getMailpassword());
        Folder folder = JavaMailUtils.getFolder(store);

        return JavaMailUtils.getMessage(folder);
    }

    private Message[] listImapMessage(Folder folder) throws MessagingException {
        Message[] message = JavaMailUtils.getMessage(folder);
        List<Message> msgs = new ArrayList<Message>();

        IMAPMessage imapMsg = null;
        for (Message msg : message) {
            imapMsg = (IMAPMessage) msg;
            // 标示未读邮件
            if (!imapMsg.isSet(Flag.SEEN)) {

                msgs.add(msg);
            }
        }

        return msgs.toArray(new Message[msgs.size()]);
    }

    private Folder getImapFolder(UserMailConfig c) throws MessagingException {
        Store store = JavaMailUtils.getImapStore(c.getReceiveurl(), c.getMailaccount(), c.getMailpassword());
        Folder folder = JavaMailUtils.getFolder(store);
        return folder;
    }

    @Override
    public int saveReceiveMail(Innermsg innermsg) {

        UserMailConfig c = this.userMailConfigDao.getObjectById(innermsg.getEmailid());
        String lastUID = null;

        //DwzResultParam param = new DwzResultParam();

        int length = 0;
        try {
            Folder folder = null;
            Message[] message = null;

            if (CodeRepositoryUtil.getValue("MAIL_RECEIVE_TYP", "P").equalsIgnoreCase(c.getMailreceivetype())) {
                message = this.listPop3Message(c);
            } else if (CodeRepositoryUtil.getValue("MAIL_RECEIVE_TYP", "I").equalsIgnoreCase(c.getMailreceivetype())) {
                folder = this.getImapFolder(c);
                if (0 == folder.getNewMessageCount()) {
                    return length;
                }
                message = this.listImapMessage(folder);
            } else {
                throw new RuntimeException("收取邮件协议配置错误");
            }

            if (ArrayUtils.isEmpty(message)) {
                throw new RuntimeException("无新邮件");
            }

            for (Message msg : message) {
                String mailUID = null;
                try {
                    mailUID = JavaMailUtils.getMessageUID(msg.getFolder(), msg);
                    if (CodeRepositoryUtil.getValue("MAIL_RECEIVE_TYP", "I").equalsIgnoreCase(c.getMailreceivetype())) {
                        // 防止 与 序列 重复ID
                        mailUID = "IMAP_" + mailUID;
                    }
                    length++;
                } catch (MessagingException e) {
                    continue;
                }

                // IMAP 不需要比对UID
                if ((!StringUtils.hasText(mailUID) || (StringUtils.hasText(c.getLastUID()) && mailUID.compareTo(c
                        .getLastUID()) < 1))
                        && CodeRepositoryUtil.getValue("MAIL_RECEIVE_TYP", "P")
                        .equalsIgnoreCase(c.getMailreceivetype())) {
                    continue;
                }

                this.receiveMail(innermsg, msg, mailUID, c);

                // 标志 SEE及DELETE
                msg.setFlag(Flag.SEEN, true);
                msg.setFlag(Flag.DELETED, true);

                lastUID = mailUID;

                folder = msg.getFolder();


            }

            JavaMailUtils.close(folder.getStore(), folder, true);

        } catch (MessagingException e) {
            LOG.error("用户 " + c.getUsercode() + " 账户 " + c.getMailaccount() + " 接收邮件失败", e);

            throw new RuntimeException("接收邮件失败");
        } catch (UnsupportedEncodingException e) {
            LOG.error("用户 " + c.getUsercode() + " 账户 " + c.getMailaccount() + " 接收邮件失败", e);

            throw new RuntimeException("接收邮件失败");
        } catch (IOException e) {
            LOG.error("用户 " + c.getUsercode() + " 账户 " + c.getMailaccount() + " 接收邮件失败", e);

            throw new RuntimeException("接收邮件失败");
        }

        // 更新此邮箱最新拉取的邮件
        if (StringUtils.hasText(lastUID)
                && CodeRepositoryUtil.getValue("MAIL_RECEIVE_TYP", "P").equalsIgnoreCase(c.getMailreceivetype())) {
            c.setLastUID(lastUID);

            this.userMailConfigDao.saveObject(c);
        }

        return length;
    }

    private List<InnermsgRecipient> listSenderMail(Innermsg innermsg, String[] account, String recipientType) {
        if (ArrayUtils.isEmpty(account)) {
            return new ArrayList<InnermsgRecipient>();
        }
        List<InnermsgRecipient> irs = new ArrayList<InnermsgRecipient>();
        for (String acc : account) {
            InnermsgRecipient ir = new InnermsgRecipient(this.innermsgRecipientDao.getNextMsgRecipiCode(), innermsg,
                    acc, CodeRepositoryUtil.getValue(Innermsg.MSG_TYPE, Innermsg.MSG_TYPE_M),
                    CodeRepositoryUtil.getValue(Innermsg.MSG_STAT, Innermsg.MSG_STAT_U));
            ir.setMailtype(recipientType);
            irs.add(ir);
        }

        return irs;
    }

    /**
     * 验证邮箱账户
     *
     * @param account
     * @return
     */
    private static String[] verificationMailAccount(String[] account) {
        // Pattern p =
        // Pattern.compile("^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$");
        List<String> l = new ArrayList<String>();
        for (String acc : account) {
            // if (p.matcher(acc).find()) {
            l.add(acc);
            // }
        }

        return l.toArray(new String[l.size()]);
    }

    public void setMsgannexDao(MsgannexDao msgannexDao) {
        this.msgannexDao = msgannexDao;
    }
// public List<Innermsg> listReplyMsgs(Long replymsgcode){
    //
    // return innermsgDao.listMsgsByReplycode(replymsgcode);
    // }
    //
    // public List<Innermsg> listMyMsgs(String receive){
    // return innermsgDao.listMsgsByReceive(receive);
    // }
    //
    // public boolean sendMessage(String sender, String receiver,
    // String msgSubject, String msgContent) {
    // Innermsg innermsg = new Innermsg();
    // innermsg.setSender(sender);
    // //innermsg.setReceive(receiver);
    // innermsg.setMsgtitle(msgSubject);
    // innermsg.setMsgcontent(msgContent);
    // innermsg.setSenddate(DatetimeOpt.currentSqlDate());
    // innermsg.setMsgstate("U");
    // //innermsg.setReceivetype("P");
    //
    // innermsgDao.saveObject(innermsg);
    // return true;
    // }
    //
    // public void saveInnermsg(Innermsg innermsg) {
    // Long msgCode = innermsg.getMsgcode();
    // if(msgCode == null || msgCode == 0){
    // msgCode = innermsgDao.getNextMsgCode();
    // innermsg.setMsgcode(msgCode);
    // }
    //
    // //获取文件编号，解析并封装Innermsg
    // String[] codeArr = innermsg.getFileCodes();
    // if (codeArr != null) {
    // for (String fileCode : codeArr) {
    // Msgannex msgannex = new Msgannex();
    // msgannex.setMsgcode(msgCode);
    // msgannex.setFilecode(fileCode);
    // innermsg.addMsgannex(msgannex);
    // }
    // }
    //
    // innermsgDao.saveObject(innermsg);
    // }
    //
    // public List<FileInfo> listFilesByMsg(Long msgcode) {
    // return fileinfoDao.listFilesByMsg(msgcode);
    // }
    //
    // public FileInfoDao getFileinfoDao() {
    // return fileinfoDao;
    // }
    //
    // public void setFileinfoDao(FileInfoDao fileInfoDao) {
    // this.fileinfoDao = fileInfoDao;
    // }

}
