package com.centit.app.po;

import java.util.Date;

/**
 * create by scaffold
 *
 * @author codefan@hotmail.com
 */

public class OaReply implements java.io.Serializable {
    private static final long serialVersionUID = 1L;


    private Long replyid;

    private Long threadid;
    private String reply;
    private Date replytime;
    private String userid;
    private String username;

    // Constructors

    /**
     * default constructor
     */
    public OaReply() {
    }

    /**
     * minimal constructor
     */
    public OaReply(
            Long replyid
    ) {


        this.replyid = replyid;

    }

    /**
     * full constructor
     */
    public OaReply(
            Long replyid
            , Long threadid, String reply, Date replytime, String userid, String username) {


        this.replyid = replyid;

        this.threadid = threadid;
        this.reply = reply;
        this.replytime = replytime;
        this.userid = userid;
        this.username = username;
    }


    public Long getReplyid() {
        return this.replyid;
    }

    public void setReplyid(Long replyid) {
        this.replyid = replyid;
    }
    // Property accessors

    public Long getThreadid() {
        return this.threadid;
    }

    public void setThreadid(Long threadid) {
        this.threadid = threadid;
    }

    public String getReply() {
        return this.reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public Date getReplytime() {
        return this.replytime;
    }

    public void setReplytime(Date replytime) {
        this.replytime = replytime;
    }

    public String getUserid() {
        return this.userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public void copy(OaReply other) {

        this.setReplyid(other.getReplyid());

        this.threadid = other.getThreadid();
        this.reply = other.getReply();
        this.replytime = other.getReplytime();
        this.userid = other.getUserid();
        this.username = other.getUsername();

    }

    public void copyNotNullProperty(OaReply other) {

        if (other.getReplyid() != null)
            this.setReplyid(other.getReplyid());

        if (other.getThreadid() != null)
            this.threadid = other.getThreadid();
        if (other.getReply() != null)
            this.reply = other.getReply();
        if (other.getReplytime() != null)
            this.replytime = other.getReplytime();
        if (other.getUserid() != null)
            this.userid = other.getUserid();
        if (other.getUsername() != null)
            this.username = other.getUsername();

    }

    public void clearProperties() {

        this.threadid = null;
        this.reply = null;
        this.replytime = null;
        this.userid = null;
        this.username = null;

    }
}
