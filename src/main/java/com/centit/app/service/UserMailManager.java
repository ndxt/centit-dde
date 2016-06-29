/**
 *
 */
package com.centit.app.service;

import java.util.List;

import com.centit.app.po.UserMailInfo;

/**
 * MAIL消息发送、接收，以及配置管理等
 *
 * @author ljy
 * @create 2012-5-9
 */
public interface UserMailManager {

    /**
     * 得到收件箱所有内容
     *
     * @param userCode 接收人编码
     * @param account  接收人邮箱帐号
     * @return
     */
    public List<UserMailInfo> listUserMailsByAccount(String userCode, String account);

    /**
     * 根据邮件ID获取邮件详细信息
     *
     * @param userCode 接收人编码
     * @param account  接收人邮箱帐号
     * @return
     */
    public UserMailInfo getMailInfoByID(int msgNumber, String userCode, String account);

    /**
     * 用户发送EMAIL消息
     *
     * @param mailInfo email消息对象
     * @param userCode 发送人编码
     * @param account  发送人邮箱帐号
     */
    public void sendUserMail(UserMailInfo mailInfo, String userCode, String account);
}
