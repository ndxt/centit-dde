package com.centit.app.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import javax.mail.search.SearchTerm;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.StringUtils;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.pop3.POP3Folder;

/**
 * 操作JavaMail工具类
 *
 * @author sx
 * @create 2012-12-17
 */
public final class JavaMailUtils {
    private static final String EMPTY_STRING = "";
    /**
     * 收件人
     */
    private static final String TO = "TO";
    /**
     * 抄送
     */
    private static final String CC = "CC";
    /**
     * 密送
     */
    private static final String BCC = "BCC";

    /**
     * GB2312编码
     */
    private static final String ENCODE_GB2312 = "gb2312";
    /**
     * UTF-8编码
     */
    private static final String ENCODE_UTF8 = "UTF-8";

    /**
     * GBK编码
     */
    private static final String ENCODE_GBK = "gbk";

    /**
     * 格式化日期
     */
    public static final String DATE_PATTERN = "yyyy年MM月dd日  HH:mm:ss";

    /**
     * 私有构造方法
     */
    private JavaMailUtils() {
        super();
    }

    /**
     * 读完邮件之后要关闭与Folder和Store的连接
     *
     * @param store  {@link Store}
     * @param folder {@link Folder}
     * @param flag   关闭Folder连接时是否删除被标识删除的文件
     * @throws MessagingException
     */
    public static void close(Store store, Folder folder, boolean flag) throws MessagingException {

        if (folder.isOpen()) {
            folder.close(flag);
        }
        if (store.isConnected()) {
            store.close();
        }
    }

    /**
     * 返回当前用户的Pop3邮件存储
     *
     * @param port     端口
     * @param username 用户名
     * @param password 密码
     * @return 当前用户的邮件存储
     * @throws MessagingException
     */
    public static Store getPop3Store(String host, int port, String username, String password) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.auth", "true");
        Session session = Session.getDefaultInstance(props);
        URLName urlName = new URLName("pop3", host, port, null, username, password);
        Store store = session.getStore(urlName);
        if (!store.isConnected()) {
            store.connect();
        }

        return store;
    }

    /**
     * 返回当前用户的邮件存储
     *
     * @param port     端口
     * @param username 用户名
     * @param password 密码
     * @return 当前用户的邮件存储
     * @throws MessagingException
     */
    public static Store getImapStore(String host, String username, String password) throws MessagingException {
        Properties prop = System.getProperties();
        prop.put("mail.store.protocol", "imap");
        prop.put("mail.imap.host", host);

        Session session = Session.getInstance(prop);

        Store store = session.getStore("imap");
        store.connect(username, password);

        return store;
    }

    /**
     * 获取邮件收件箱文件夹
     *
     * @param store {@link Store}
     * @return {@link Folder}
     * @throws MessagingException
     */
    public static Folder getFolder(Store store) throws MessagingException {
        Folder folder = store.getFolder("INBOX");
        return folder;
    }

    /**
     * 获取当前邮箱邮件数
     *
     * @param folder {@link Folder}
     * @return 邮箱邮件数
     * @throws MessagingException
     */
    public static Message[] getMessage(Folder folder) throws MessagingException {
        if (!folder.isOpen()) {
            folder.open(Folder.READ_WRITE);
        }
        Message[] message = folder.getMessages();
        return message;
    }

    /**
     * 获取当前邮箱邮件数
     *
     * @param folder {@link Folder}
     * @return 邮箱邮件数
     * @throws MessagingException
     */
    public static Message[] getMessage(Folder folder, SearchTerm term) throws MessagingException {
        if (!folder.isOpen()) {
            folder.open(Folder.READ_ONLY);
        }
        Message[] message = folder.search(term);
        return message;
    }

    /**
     * 获取邮件MessageId
     *
     * @param mimeMessage {@link MimeMessage}
     * @return 邮件MessageId
     * @throws MessagingException
     */
    public static String getMessageId(MimeMessage mimeMessage) throws MessagingException {
        return mimeMessage.getMessageID();
    }

    /**
     * 获取邮件唯一的UID
     *
     * @param inboxFolder {@link POP3Folder}
     * @param message     {@link Message}
     * @return 邮件唯一的UID
     * @throws MessagingException
     */
    public static String getMessageUID(Folder inboxFolder, Message message) throws MessagingException {
        if (inboxFolder instanceof POP3Folder) {
            return ((POP3Folder) inboxFolder).getUID(message);
        }
        if (inboxFolder instanceof IMAPFolder) {
            return String.valueOf(((IMAPFolder) inboxFolder).getUID(message));
        }

        throw new MessagingException("邮件服务器不支持pop3 imap协议");
    }

    /**
     * 获取邮件主题
     *
     * @param message {@link Message}
     * @return 邮件主题
     * @throws UnsupportedEncodingException
     * @throws MessagingException
     */
    public static String getSubject(Message message) throws UnsupportedEncodingException, MessagingException {
        String subject = null;
        subject = MimeUtility.decodeText(message.getSubject());

        if (null == subject) {
            return EMPTY_STRING;
        }

        return subject;
    }

    /**
     * 获取邮件正文
     *
     * @param part {@link Part}
     * @param text {@link StringBuffer}
     * @return 邮件正文字符串
     * @throws MessagingException
     * @throws IOException
     */
    public static String getText(Part part, StringBuffer text) throws MessagingException, IOException {

        String contenttype = part.getContentType();
        int nameindex = contenttype.indexOf("name");
        boolean conname = false;
        if (nameindex != -1) {
            conname = true;
        }
        // 解析邮件格式
        if (part.isMimeType("text/plain") && !conname) {
            text.append((String) part.getContent());
        } else if (part.isMimeType("text/html") && !conname) {
            text.append((String) part.getContent());
        } else if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();
            int counts = multipart.getCount();
            for (int i = 0; i < counts; i++) {
                getText(multipart.getBodyPart(i), text);
            }
        } else if (part.isMimeType("message/rfc822")) {
            getText((Part) part.getContent(), text);
        }

        return text.toString();
    }

    /**
     * 获取此邮件的发件人
     *
     * @param message {@link Message}
     * @return 邮件发件人
     * @throws MessagingException
     */
    public static String getFrom(Message message) throws MessagingException {
        InternetAddress address[] = (InternetAddress[]) message.getFrom();
        String from = address[0].getAddress();
        if (null == from)
            from = EMPTY_STRING;
        String personal = address[0].getPersonal();
        if (null == personal)
            personal = EMPTY_STRING;
        return personal + '<' + from + '>';
    }

    /**
     * 获取收件人地址
     *
     * @param message {@link Message}
     * @return 收件人地址
     * @throws UnsupportedEncodingException
     * @throws MessagingException
     */
    public static String getTo(Message message) throws UnsupportedEncodingException, MessagingException {
        return getMailAddress(message, TO);
    }

    /**
     * 获取抄送人地址
     *
     * @param message {@link Message}
     * @return 抄送人地址
     * @throws UnsupportedEncodingException
     * @throws MessagingException
     */
    public static String getCC(Message message) throws UnsupportedEncodingException, MessagingException {
        return getMailAddress(message, CC);
    }

    /**
     * 获取密送人地址
     *
     * @param message {@link Message}
     * @return 密送人地址
     * @throws UnsupportedEncodingException
     * @throws MessagingException
     */
    public static String getBCC(Message message) throws UnsupportedEncodingException, MessagingException {
        return getMailAddress(message, BCC);
    }

    /**
     * 获得邮件的收件人，抄送，和密送的地址和姓名，根据所传递的参数的不同
     *
     * @param message {@link Message}
     * @param type    "to"----收件人 "cc"---抄送人地址 "bcc"---密送人地址
     * @return "to"----收件人 "cc"---抄送人地址 "bcc"---密送人地址
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    private static String getMailAddress(Message message, String type) throws MessagingException,
            UnsupportedEncodingException {
        String mailaddr = EMPTY_STRING;
        String addressType = type.toUpperCase();
        InternetAddress[] address = null;
        if (TO.equals(addressType) || CC.equals(addressType) || BCC.equals(addressType)) {
            if (TO.equals(addressType)) {
                address = (InternetAddress[]) message.getRecipients(Message.RecipientType.TO);
            } else if (CC.equals(addressType)) {
                address = (InternetAddress[]) message.getRecipients(Message.RecipientType.CC);
            } else {
                address = (InternetAddress[]) message.getRecipients(Message.RecipientType.BCC);
            }
            if (null != address) {
                for (int i = 0; i < address.length; i++) {
                    String email = address[i].getAddress();
                    if (null == email)
                        email = EMPTY_STRING;
                    else {
                        email = MimeUtility.decodeText(email);
                    }
                    String personal = address[i].getPersonal();
                    if (null == personal)
                        personal = EMPTY_STRING;
                    else {
                        personal = MimeUtility.decodeText(personal);
                    }
                    mailaddr = mailaddr + ',' + personal + '<' + email + '>';
                }

                if (!EMPTY_STRING.equals(mailaddr)) {
                    mailaddr = mailaddr.substring(1);
                }
            }
        }
        return mailaddr;
    }

    /**
     * 获取此邮件的发件时间
     *
     * @param message {@link Message}
     * @param pattern 格式化日期格式
     * @return 日期字符串
     * @throws MessagingException
     */
    public static String getSendDate(Message message) throws MessagingException {
        Date sentdate = message.getSentDate();
        SimpleDateFormat format = new SimpleDateFormat(DATE_PATTERN);
        return format.format(sentdate);
    }

    /**
     * 判断此邮件是否已读
     *
     * @param message
     * @return 未读返回false, 反之返回true
     * @throws MessagingException
     */
    public static boolean isNew(Message message) throws MessagingException {
        boolean isnew = false;
        Flags flags = message.getFlags();
        Flags.Flag[] flag = flags.getSystemFlags();

        for (int i = 0; i < flag.length; i++) {
            if (flag[i] == Flags.Flag.SEEN) {
                isnew = true;

                break;
            }
        }
        return isnew;
    }

    /**
     * 获取邮件附件
     *
     * @param part       {@link Part}
     * @param attachMent {@link Map}
     * @return 邮件附件，Key=附件名，Value=附件IO流
     * @throws UnsupportedEncodingException
     * @throws MessagingException
     * @throws IOException
     */
    public static Map<String, InputStream> getAttachMent(Part part, Map<String, InputStream> attachMent)
            throws UnsupportedEncodingException, MessagingException, IOException {
        String fileName = EMPTY_STRING;
        if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();
            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart mpart = multipart.getBodyPart(i);
                String disposition = mpart.getDisposition();
                if ((null != disposition)
                        && ((disposition.equals(Part.ATTACHMENT)) || (disposition.equals(Part.INLINE)))) {
                    fileName = mpart.getFileName();
                    // 文件名解码
                    if (StringUtils.hasLength(fileName)
                            && (-1 != fileName.toLowerCase().indexOf(ENCODE_GB2312) || -1 != fileName.toLowerCase()
                            .indexOf(ENCODE_GBK))) {
                        fileName = MimeUtility.decodeText(fileName);
                    }
                    attachMent.put(fileName, mpart.getInputStream());
                } else if (mpart.isMimeType("multipart/*")) {
                    getAttachMent(mpart, attachMent);
                } else {
                    fileName = mpart.getFileName();
                    // 文件名解码
                    if (StringUtils.hasLength(fileName)
                            && (-1 != fileName.toLowerCase().indexOf(ENCODE_GB2312) || -1 != fileName.toLowerCase()
                            .indexOf(ENCODE_GBK))) {
                        fileName = MimeUtility.decodeText(fileName);
                        attachMent.put(fileName, mpart.getInputStream());
                    }
                }
            }
        } else if (part.isMimeType("message/rfc822")) {
            getAttachMent((Part) part.getContent(), attachMent);
        }
        return attachMent;
    }

    /**
     * 删除指定邮件
     *
     * @param folder {@link Folder}
     * @param uids   邮件UID集合
     * @throws MessagingException
     */
    public static void deleteMessage(Folder folder, List<String> uids) throws MessagingException {
        folder.open(Folder.READ_WRITE);
        Message[] message = folder.getMessages();
        String uid = null;
        MimeMessage mimeMessage = null;
        int len = 0;
        for (int i = 0; i < message.length; i++) {
            mimeMessage = (MimeMessage) message[i];
            uid = getMessageUID(folder, mimeMessage);
            if (uids.contains(uid)) {
                len++;
                mimeMessage.setFlag(Flags.Flag.DELETED, true);
                // 已删除的数据和集合的数量量一样，退出循环
                if (len == uids.size()) {
                    break;
                }
            }
        }
    }

    /**
     * 发送邮件
     *
     * @param username 邮件帐户
     * @param password 密码
     * @return {@link JavaMailSender}
     */
    public static JavaMailSender getJavaMailSender(String host, String username, String password) {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(host);
        javaMailSender.setPort(25);
        javaMailSender.setProtocol(JavaMailSenderImpl.DEFAULT_PROTOCOL);
        javaMailSender.setUsername(username);
        javaMailSender.setPassword(password);
        // javaMailSender.setDefaultEncoding(ENCODER_UTF_8);
        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.smtp.auth", true);
        javaMailProperties.put("mail.smtp.timeout", 25000);

        return javaMailSender;
    }

    /**
     * 发送MineMessage
     *
     * @param javaMailSender {@link JavaMailSender}
     * @param subject        消息标题
     * @param text           消息内容
     * @param to             收件人
     * @param cc             抄送人
     * @param bcc            密送
     * @param attachment     附件
     * @param hasReceipt     是否需要回执
     * @param from           邮件发送人
     * @return 发送邮件的messageId
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    public static String sendMimeMessage(JavaMailSender javaMailSender, String subject, String text, String from,
                                         String[] to, String[] cc, String[] bcc, Map<String, File> attachment, boolean hasReceipt)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, ENCODE_UTF8);
        helper.setSubject(subject);
        helper.setText(text);
        // 发件人
        helper.setFrom(from);
        // 收件人
        helper.setTo(to);
        // 抄送
        if (!ArrayUtils.isEmpty(cc)) {
            helper.setCc(cc);

        }
        // 密送
        if (!ArrayUtils.isEmpty(bcc)) {
            helper.setBcc(bcc);
        }

        // 添加回执
        if (hasReceipt) {
            mimeMessage.addHeader("Disposition-Notification-To", "true");
        }
        // 附件
        if (null != attachment && !attachment.isEmpty()) {
            Set<Map.Entry<String, File>> att = attachment.entrySet();
            File attFile = null;
            for (Map.Entry<String, File> entry : att) {
                attFile = entry.getValue();
                // 附件路径正确，添加附件
                if (attFile.canRead() && attFile.isFile()) {
                    // 将中文文件名进行转码，防止乱码
                    helper.addAttachment(MimeUtility.encodeWord(entry.getKey()), entry.getValue());
                }
            }
        }

        javaMailSender.send(mimeMessage);

        return mimeMessage.getMessageID();
    }

    /**
     * 转换邮箱帐户名
     *
     * @param mailAccount 邮箱帐户
     * @return 帐户
     */
    public static String getEmailAccountName(String mailAccount) {
        String at = "@";
        String lt = "<";
        if (-1 == mailAccount.indexOf(at)) {
            return mailAccount;
        }

        if (-1 != mailAccount.indexOf(lt)) {
            Pattern p = Pattern.compile(mailAccount);
            Matcher m = p.matcher("^<.+@$");
            if (m.matches()) {
                return m.group();
            }

            return mailAccount;
        } else {
            return mailAccount.substring(0, mailAccount.indexOf(at));
        }

    }

}
