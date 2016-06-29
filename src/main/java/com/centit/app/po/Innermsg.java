package com.centit.app.po;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 内部消息（邮件）与公告 接受代码, 其实可以独立出来, 因为他 和发送人 是 一对多的关系
 * <p/>
 * 与原即时消息合并，所有消息均为即时编写发送，暂不包含草稿及定时发送。
 * <p/>
 * 定时清理策略由系统统一配置，见数据字典。
 *
 * @author sx
 * @create 2012-12-18
 */

public class Innermsg implements java.io.Serializable {

    // IMAP 邮件下载后标识
    public static final String MAIL_DOWNLOAD = "CENTIT_OA_MAIL_DOWNLOAD";
    /**
     * P=个人为消息 A=机构为公告 M=邮件
     */
    public static final String MSG_TYPE = "MSG_TYPE";
    public static final String MSG_TYPE_P = "P";
    public static final String MSG_TYPE_A = "A";
    public static final String MSG_TYPE_M = "M";
    /**
     * I=收件箱 O=发件箱 D=草稿箱 T=废件箱
     */
    public static final String MAIL_TYPE = "MAIL_TYPE";
    public static final String MAIL_TYPE_I = "I";
    public static final String MAIL_TYPE_O = "O";
    public static final String MAIL_TYPE_D = "D";
    public static final String MAIL_TYPE_T = "T";
    /**
     * T=收件人 C=抄送 B=密送
     */
    public static final String RECIPIENT_TYPE = "RECIPIENT_TYPE";
    public static final String RECIPIENT_TYPE_T = "T";
    public static final String RECIPIENT_TYPE_C = "C";
    public static final String RECIPIENT_TYPE_B = "B";
    /**
     * U=未读 R=已读 D=删除
     */
    public static final String MSG_STAT = "MSG_STAT";
    public static final String MSG_STAT_U = "U";
    public static final String MSG_STAT_R = "R";
    public static final String MSG_STAT_D = "D";

    /**
     *
     */

    private static final long serialVersionUID = 6069584019462742970L;

    private String msgcode;

    private String sender;
    private Date senddate;
    private String msgtitle;
    private String msgtype;
    private String mailtype;
    private String mailUnDelType;
    private String receivename;
    private Long holdusers;
    private String msgstate;
    private String msgcontent;

    private UserMailConfig c;
    private Set<Msgannex> msgannexs = null;// new ArrayList<Msgannex>();
    private Set<InnermsgRecipient> innermsgRecipients = null;// new ArrayList<InnermsgRecipient>();

    // 收件人usercode
    private String receiveUserCode;

    private String to;
    private String cc;
    private String bcc;
    private String emailid;

    // Constructors

    /**
     * default constructor
     */
    public Innermsg() {
    }

    /**
     * minimal constructor
     */
    public Innermsg(String msgcode) {

        this.msgcode = msgcode;

    }

    /**
     * full constructor
     */
    public Innermsg(String msgcode, String sender, Date senddate, String msgtitle, String msgtype, String mailtype,
                    String receivename, Long holdusers, String msgstate, String msgcontent, String emailid) {

        this.msgcode = msgcode;

        this.sender = sender;
        this.senddate = senddate;
        this.msgtitle = msgtitle;
        this.msgtype = msgtype;
        this.mailtype = mailtype;
        this.receivename = receivename;
        this.holdusers = holdusers;
        this.msgstate = msgstate;
        this.msgcontent = msgcontent;
    }

    public String getMsgcode() {
        return this.msgcode;
    }

    public void setMsgcode(String msgcode) {
        this.msgcode = msgcode;
    }

    // Property accessors

    public String getSender() {
        return this.sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Date getSenddate() {
        return this.senddate;
    }

    public void setSenddate(Date senddate) {
        this.senddate = senddate;
    }

    public String getMsgtitle() {
        return this.msgtitle;
    }

    public void setMsgtitle(String msgtitle) {
        this.msgtitle = msgtitle;
    }

    public String getMsgtype() {
        return this.msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public String getMailtype() {
        return this.mailtype;
    }

    public void setMailtype(String mailtype) {
        this.mailtype = mailtype;
    }

    public String getReceivename() {
        return this.receivename;
    }

    public void setReceivename(String receivename) {
        this.receivename = receivename;
    }

    public Long getHoldusers() {
        return this.holdusers;
    }

    public void setHoldusers(Long holdusers) {
        this.holdusers = holdusers;
    }

    public String getMsgstate() {
        return this.msgstate;
    }

    public void setMsgstate(String msgstate) {
        this.msgstate = msgstate;
    }

    public String getMsgcontent() {
        return this.msgcontent;
    }

    public void setMsgcontent(String msgcontent) {
        this.msgcontent = msgcontent;
    }

    public String getEmailid() {
        return this.emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public Set<Msgannex> getMsgannexs() {
        if (this.msgannexs == null)
            this.msgannexs = new HashSet<Msgannex>();
        return this.msgannexs;
    }

    public void setMsgannexs(Set<Msgannex> msgannexs) {
        this.msgannexs = msgannexs;
    }

    public void addMsgannex(Msgannex msgannex) {
        if (this.msgannexs == null)
            this.msgannexs = new HashSet<Msgannex>();
        this.msgannexs.add(msgannex);
    }

    public void removeMsgannex(Msgannex msgannex) {
        if (this.msgannexs == null)
            return;
        this.msgannexs.remove(msgannex);
    }

    public Msgannex newMsgannex() {
        Msgannex res = new Msgannex();

        //res.setMsgcode(this.getMsgcode());

        return res;
    }

    /**
     * 替换子类对象数组，这个函数主要是考虑hibernate中的对象的状态，以避免对象状态不一致的问题
     */
    public void replaceMsgannexs(List<Msgannex> msgannexs) {
        List<Msgannex> newObjs = new ArrayList<Msgannex>();
        for (Msgannex p : msgannexs) {
            if (p == null)
                continue;
            Msgannex newdt = newMsgannex();
            //newdt.copyNotNullProperty(p);
            newObjs.add(newdt);
        }
        // delete
        boolean found = false;
        Set<Msgannex> oldObjs = new HashSet<Msgannex>();
        oldObjs.addAll(getMsgannexs());

        for (Iterator<Msgannex> it = oldObjs.iterator(); it.hasNext(); ) {
            Msgannex odt = it.next();
            found = false;
            for (Msgannex newdt : newObjs) {
                if (odt.getMsgannexId().equals(newdt.getMsgannexId())) {
                    found = true;
                    break;
                }
            }
            if (!found)
                removeMsgannex(odt);
        }
        oldObjs.clear();
        // insert or update
        for (Msgannex newdt : newObjs) {
            found = false;
            for (Iterator<Msgannex> it = getMsgannexs().iterator(); it.hasNext(); ) {
                Msgannex odt = it.next();
                if (odt.getMsgannexId().equals(newdt.getMsgannexId())) {
//                    odt.copy(newdt);
//                    found = true;
//                    break;
                }
            }
            if (!found)
                addMsgannex(newdt);
        }
    }

    public Set<InnermsgRecipient> getInnermsgRecipients() {
        if (this.innermsgRecipients == null)
            this.innermsgRecipients = new HashSet<InnermsgRecipient>();
        return this.innermsgRecipients;
    }

    public void setInnermsgRecipients(Set<InnermsgRecipient> innermsgRecipients) {
        this.innermsgRecipients = innermsgRecipients;
    }

    public void addInnermsgRecipient(InnermsgRecipient innermsgRecipient) {
        if (this.innermsgRecipients == null)
            this.innermsgRecipients = new HashSet<InnermsgRecipient>();
        this.innermsgRecipients.add(innermsgRecipient);
    }

    public void removeInnermsgRecipient(InnermsgRecipient innermsgRecipient) {
        if (this.innermsgRecipients == null)
            return;
        this.innermsgRecipients.remove(innermsgRecipient);
    }

    public InnermsgRecipient newInnermsgRecipient() {
        InnermsgRecipient res = new InnermsgRecipient();

        res.setInnermsg(this);

        return res;
    }

    /**
     * 替换子类对象数组，这个函数主要是考虑hibernate中的对象的状态，以避免对象状态不一致的问题
     */
    public void replaceInnermsgRecipients(List<InnermsgRecipient> innermsgRecipients) {
        List<InnermsgRecipient> newObjs = new ArrayList<InnermsgRecipient>();
        for (InnermsgRecipient p : innermsgRecipients) {
            if (p == null)
                continue;
            InnermsgRecipient newdt = newInnermsgRecipient();
            newdt.copyNotNullProperty(p);
            newObjs.add(newdt);
        }
        // delete
        boolean found = false;
        Set<InnermsgRecipient> oldObjs = new HashSet<InnermsgRecipient>();
        oldObjs.addAll(getInnermsgRecipients());

        for (Iterator<InnermsgRecipient> it = oldObjs.iterator(); it.hasNext(); ) {
            InnermsgRecipient odt = it.next();
            found = false;
            for (InnermsgRecipient newdt : newObjs) {
                if (odt.getId().equals(newdt.getId())) {
                    found = true;
                    break;
                }
            }
            if (!found)
                removeInnermsgRecipient(odt);
        }
        oldObjs.clear();
        // insert or update
        for (InnermsgRecipient newdt : newObjs) {
            found = false;
            for (Iterator<InnermsgRecipient> it = getInnermsgRecipients().iterator(); it.hasNext(); ) {
                InnermsgRecipient odt = it.next();
                if (odt.getId().equals(newdt.getId())) {
                    odt.copy(newdt);
                    found = true;
                    break;
                }
            }
            if (!found)
                addInnermsgRecipient(newdt);
        }
    }

    public void copy(Innermsg other) {

        this.setMsgcode(other.getMsgcode());

        this.sender = other.getSender();
        this.senddate = other.getSenddate();
        this.msgtitle = other.getMsgtitle();
        this.msgtype = other.getMsgtype();
        this.mailtype = other.getMailtype();
        this.receivename = other.getReceivename();
        this.holdusers = other.getHoldusers();
        this.msgstate = other.getMsgstate();
        this.msgcontent = other.getMsgcontent();
        // this.emailid = other.getEmailid();

        this.msgannexs = other.getMsgannexs();
        this.innermsgRecipients = other.getInnermsgRecipients();
    }

    public void copyNotNullProperty(Innermsg other) {

        if (other.getMsgcode() != null)
            this.setMsgcode(other.getMsgcode());

        if (other.getSender() != null)
            this.sender = other.getSender();
        if (other.getSenddate() != null)
            this.senddate = other.getSenddate();
        if (other.getMsgtitle() != null)
            this.msgtitle = other.getMsgtitle();
        if (other.getMsgtype() != null)
            this.msgtype = other.getMsgtype();
        if (other.getMailtype() != null)
            this.mailtype = other.getMailtype();
        if (other.getReceivename() != null)
            this.receivename = other.getReceivename();
        if (other.getHoldusers() != null)
            this.holdusers = other.getHoldusers();
        if (other.getMsgstate() != null)
            this.msgstate = other.getMsgstate();
        if (other.getMsgcontent() != null)
            this.msgcontent = other.getMsgcontent();
        // if (other.getEmailid() != null)
        // this.emailid = other.getEmailid();

        this.msgannexs = other.getMsgannexs();
        this.innermsgRecipients = other.getInnermsgRecipients();
    }

    public void clearProperties() {

        this.sender = null;
        this.senddate = null;
        this.msgtitle = null;
        this.msgtype = null;
        this.mailtype = null;
        this.receivename = null;
        this.holdusers = null;
        this.msgstate = null;
        this.msgcontent = null;
        // this.emailid = null;

        this.msgannexs = new HashSet<Msgannex>();
        this.innermsgRecipients = new HashSet<InnermsgRecipient>();
    }

    public String getReceiveUserCode() {
        return receiveUserCode;
    }

    public void setReceiveUserCode(String receiveUserCode) {
        this.receiveUserCode = receiveUserCode;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getBcc() {
        return bcc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    public String getMailUnDelType() {
        return mailUnDelType;
    }

    public void setMailUnDelType(String mailUnDelType) {
        this.mailUnDelType = mailUnDelType;
    }

    public UserMailConfig getC() {
        return c;
    }

    public void setC(UserMailConfig c) {
        this.c = c;
    }

}
