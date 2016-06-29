/**
 *
 */
package com.centit.app.service.impl;

import com.centit.sys.dao.AddressBookDao;
import com.centit.sys.po.AddressBook;
import com.centit.sys.po.FUserinfo;
import com.centit.sys.service.CodeRepositoryUtil;
import com.centit.sys.service.MessageSender;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;

/**
 * @author ljy codefan
 * @create 2012-2-22
 */
public class EmailMessageSenderImpl implements MessageSender {

    private static final Log log = LogFactory.getLog(EmailMessageSenderImpl.class);
    private AddressBookDao addressBookDao;

    public void setAddressBookDao(AddressBookDao baseDao) {
        this.addressBookDao = baseDao;
    }

    public static boolean sendEmailMessage(String mailTo, String mailFrom, String msgSubject, String msgContent) {

        MultiPartEmail multMail = new MultiPartEmail();

        // SMTP
        multMail.setHostName(CodeRepositoryUtil.getValue("SysMail", "host_name"));

        // 需要提供公用的邮件用户名和密码
        multMail.setAuthentication(
                CodeRepositoryUtil.getValue("SysMail", "host_user"),
                CodeRepositoryUtil.getValue("SysMail", "host_password"));
        try {
            //multMail.setFrom(CodeRepositoryUtil.getValue("SysMail", "admin_email"));
            multMail.setFrom(mailFrom);
            multMail.addTo(mailTo);
            multMail.setSubject(msgSubject);
            multMail.setMsg(msgContent);
            multMail.send();
            return true;
        } catch (EmailException e) {
            e.printStackTrace();
        }
        return false;
    }

    //@Override
    public boolean sendMessage(String sender, String receiver, String msgSubject, String msgContent) {
        String mailFrom = CodeRepositoryUtil.getValue("SysMail", "admin_email");
        FUserinfo userinfo = CodeRepositoryUtil.getUserInfoByCode(receiver);
        if (userinfo == null) {
            log.error("找不到用户：" + receiver);
            return false;
        }
        Long addrid = userinfo.getAddrbookid();
        if (addrid == null || addrid == 0) {
            log.info("用户：" + receiver + "没有相关的通信信息");
            return false;
        }
        AddressBook addr = addressBookDao.getObjectById(addrid);
        if (addr == null) {
            log.info("用户：" + receiver + "没有相关的通信信息");
            return false;
        }
        String email = addr.getEmail();
        if (email != null && !"".equals(email))
            sendEmailMessage(addr.getEmail(), mailFrom, msgSubject, msgContent);
        email = addr.getEmail2();
        if (email != null && !"".equals(email))
            sendEmailMessage(addr.getEmail(), mailFrom, msgSubject, msgContent);
        email = addr.getEmail3();
        if (email != null && !"".equals(email))
            sendEmailMessage(addr.getEmail(), mailFrom, msgSubject, msgContent);
        log.info("向用户" + receiver + "发送邮件。");
        return true;
    }
}
