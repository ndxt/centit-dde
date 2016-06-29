package com.centit.app.po;

/**
 * create by scaffold
 *
 * @author codefan@hotmail.com
 */

public class UserMailConfig implements java.io.Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -2240238869291576010L;

    private String emailid;

    private String usercode;
    private String mailaccount;
    private String mailpassword;
    private String mailsendtype;
    private String mailreceivetype;
    private String smtpurl;
    private Long smtpport;
    private String receiveurl;
    private Long receiveport;
    private Long intervaltime;
    private Long retaindays;

    private String lastUID;

    // Constructors

    /**
     * default constructor
     */
    public UserMailConfig() {
    }

    /**
     * minimal constructor
     */
    public UserMailConfig(String emailid, String usercode, String mailaccount) {

        this.emailid = emailid;

        this.usercode = usercode;
        this.mailaccount = mailaccount;
    }

    /**
     * full constructor
     */
    public UserMailConfig(String emailid, String usercode, String mailaccount, String mailpassword, String mailtype,
                          String smtpurl, Long smtpport, String receiveUrl, Long pop3port, Long intervaltime, Long retaindays) {

        this.emailid = emailid;

        this.usercode = usercode;
        this.mailaccount = mailaccount;
        this.mailpassword = mailpassword;
        this.mailsendtype = mailtype;
        this.smtpurl = smtpurl;
        this.smtpport = smtpport;
        this.receiveurl = receiveUrl;
        this.receiveport = pop3port;
        this.intervaltime = intervaltime;
        this.retaindays = retaindays;
    }

    public String getEmailid() {
        return this.emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    // Property accessors

    public String getUsercode() {
        return this.usercode;
    }

    public void setUsercode(String usercode) {
        this.usercode = usercode;
    }

    public String getMailaccount() {
        return this.mailaccount;
    }

    public void setMailaccount(String mailaccount) {
        this.mailaccount = mailaccount;
    }

    public String getMailpassword() {
        return this.mailpassword;
    }

    public void setMailpassword(String mailpassword) {
        this.mailpassword = mailpassword;
    }

    public String getMailsendtype() {
        return this.mailsendtype;
    }

    public void setMailsendtype(String mailtype) {
        this.mailsendtype = mailtype;
    }

    public String getSmtpurl() {
        return this.smtpurl;
    }

    public void setSmtpurl(String smtpurl) {
        this.smtpurl = smtpurl;
    }

    public Long getSmtpport() {
        return this.smtpport;
    }

    public void setSmtpport(Long smtpport) {
        this.smtpport = smtpport;
    }

    public String getReceiveurl() {
        return this.receiveurl;
    }

    public void setReceiveurl(String receiveUrl) {
        this.receiveurl = receiveUrl;
    }

    public Long getReceiveport() {
        return this.receiveport;
    }

    public void setReceiveport(Long pop3port) {
        this.receiveport = pop3port;
    }

    public Long getIntervaltime() {
        return this.intervaltime;
    }

    public void setIntervaltime(Long intervaltime) {
        this.intervaltime = intervaltime;
    }

    public Long getRetaindays() {
        return this.retaindays;
    }

    public void setRetaindays(Long retaindays) {
        this.retaindays = retaindays;
    }

    public void copy(UserMailConfig other) {

        this.setEmailid(other.getEmailid());

        this.usercode = other.getUsercode();
        this.mailaccount = other.getMailaccount();
        this.mailpassword = other.getMailpassword();
        this.mailsendtype = other.getMailsendtype();
        this.smtpurl = other.getSmtpurl();
        this.smtpport = other.getSmtpport();
        this.receiveurl = other.getReceiveurl();
        this.receiveport = other.getReceiveport();
        this.intervaltime = other.getIntervaltime();
        this.retaindays = other.getRetaindays();

    }

    public void copyNotNullProperty(UserMailConfig other) {

        if (other.getEmailid() != null)
            this.setEmailid(other.getEmailid());

        if (other.getUsercode() != null)
            this.usercode = other.getUsercode();
        if (other.getMailaccount() != null)
            this.mailaccount = other.getMailaccount();
        if (other.getMailpassword() != null)
            this.mailpassword = other.getMailpassword();
        if (other.getMailsendtype() != null)
            this.mailsendtype = other.getMailsendtype();
        if (other.getSmtpurl() != null)
            this.smtpurl = other.getSmtpurl();
        if (other.getSmtpport() != null)
            this.smtpport = other.getSmtpport();
        if (other.getReceiveurl() != null)
            this.receiveurl = other.getReceiveurl();
        if (other.getReceiveport() != null)
            this.receiveport = other.getReceiveport();
        if (other.getIntervaltime() != null)
            this.intervaltime = other.getIntervaltime();
        if (other.getRetaindays() != null)
            this.retaindays = other.getRetaindays();

    }

    public void clearProperties() {

        this.usercode = null;
        this.mailaccount = null;
        this.mailpassword = null;
        this.mailsendtype = null;
        this.smtpurl = null;
        this.smtpport = null;
        this.receiveurl = null;
        this.receiveport = null;
        this.intervaltime = null;
        this.retaindays = null;

    }

    public String getLastUID() {
        return lastUID;
    }

    public void setLastUID(String lastUID) {
        this.lastUID = lastUID;
    }

    public String getMailreceivetype() {
        return mailreceivetype;
    }

    public void setMailreceivetype(String mailreceivetype) {
        this.mailreceivetype = mailreceivetype;
    }
}
