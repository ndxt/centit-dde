/**
 *
 */
package com.centit.sys.service;

/**
 * 系统消息<邮件等>发送接口
 *
 * @author ljy
 * @create 2012-2-22
 */
public interface MessageSender {

    /**
     * 发送email消息
     * @param sender     发送人内部用户编码
     * @param receiver   接收人内部用户编码
     * @param msgSubject 邮件主题
     * @param msgContent 邮件内容
     * @return
     */
    //public boolean sendMessage(String sender,String receiver ,String msgSubject,String msgContent);

}
