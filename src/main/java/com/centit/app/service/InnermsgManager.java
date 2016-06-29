package com.centit.app.service;

import com.centit.app.po.Innermsg;
import com.centit.core.service.BaseEntityManager;

public interface InnermsgManager extends BaseEntityManager<Innermsg> {

    /**
     * 保存即时消息
     *
     * @param innermsg
     */
    void saveMsg(Innermsg innermsg);

    /**
     * 保存公告
     *
     * @param innermsg
     */
    void saveAnnouncement(Innermsg innermsg);

    /**
     * 删除即时消息
     *
     * @param innermsg
     * @param mailType 收取消息或发送消息识别,套用MailType
     */
    void deleteMsg(Innermsg innermsg, String mailType);


    /**
     * 删除邮件
     *
     * @param innermsg
     */
    void deleteMail(Innermsg innermsg);

    /**
     * 发送邮件
     *
     * @param innermsg
     */
    void saveSendMail(Innermsg innermsg);

    /**
     * 接收服务器邮件
     *
     * @param innermsg
     */
    int saveReceiveMail(Innermsg innermsg);
    // /**
    // * 查询回复消息
    // * @param replymsgcode
    // * @return
    // */
    // public List<Innermsg> listReplyMsgs(Long replymsgcode);
    //
    // /**
    // * 查询个人邮件消息
    // * @param receive
    // * @return
    // */
    // public List<Innermsg> listMyMsgs(String receive);
    //
    // /**
    // * 保存消息
    // */
    // public void saveInnermsg(Innermsg innermsg);
    //
    //
    // /**
    // * 根据消息编号获取文件信息
    // * @param msgcode
    // * @return
    // */
    // public List<FileInfo> listFilesByMsg(Long msgcode);
}
