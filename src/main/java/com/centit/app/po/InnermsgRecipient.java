package com.centit.app.po;

/**
 * create by scaffold
 *
 * @author codefan@hotmail.com
 */

public class InnermsgRecipient implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    private Innermsg innermsg;
    private String receive;
    private Long replymsgcode;
    private String receivetype;
    private String mailtype;
    private String msgstate;

    public InnermsgRecipient(String id, Innermsg innermsg, String receive, String receivetype, String msgstate) {
        this.id = id;
        this.innermsg = innermsg;
        this.receive = receive;
        this.receivetype = receivetype;
        this.msgstate = msgstate;
    }

    // Constructors

    /**
     * default constructor
     */
    public InnermsgRecipient() {
    }

    public InnermsgRecipient(Innermsg innermsg) {
        this.innermsg = innermsg;
    }

    public InnermsgRecipient(Innermsg innermsg, String receive) {
        this.innermsg = innermsg;
        this.receive = receive;
    }

    /**
     * minimal constructor
     */

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Property accessors

    public String getReceive() {
        return this.receive;
    }

    public void setReceive(String receive) {
        this.receive = receive;
    }

    public Long getReplymsgcode() {
        return this.replymsgcode;
    }

    public void setReplymsgcode(Long replymsgcode) {
        this.replymsgcode = replymsgcode;
    }

    public String getReceivetype() {
        return this.receivetype;
    }

    public void setReceivetype(String receivetype) {
        this.receivetype = receivetype;
    }

    public String getMailtype() {
        return this.mailtype;
    }

    public void setMailtype(String mailtype) {
        this.mailtype = mailtype;
    }

    public String getMsgstate() {
        return this.msgstate;
    }

    public void setMsgstate(String msgstate) {
        this.msgstate = msgstate;
    }

    public void copy(InnermsgRecipient other) {

        this.setId(other.getId());

        this.innermsg = other.getInnermsg();
        this.receive = other.getReceive();
        this.replymsgcode = other.getReplymsgcode();
        this.receivetype = other.getReceivetype();
        this.mailtype = other.getMailtype();
        this.msgstate = other.getMsgstate();

    }

    public void copyNotNullProperty(InnermsgRecipient other) {

        if (other.getId() != null)
            this.setId(other.getId());

        if (other.getInnermsg() != null)
            this.innermsg = other.getInnermsg();
        ;
        if (other.getReceive() != null)
            this.receive = other.getReceive();
        if (other.getReplymsgcode() != null)
            this.replymsgcode = other.getReplymsgcode();
        if (other.getReceivetype() != null)
            this.receivetype = other.getReceivetype();
        if (other.getMailtype() != null)
            this.mailtype = other.getMailtype();
        if (other.getMsgstate() != null)
            this.msgstate = other.getMsgstate();

    }

    public void clearProperties() {

        this.innermsg = null;
        this.receive = null;
        this.replymsgcode = null;
        this.receivetype = null;
        this.mailtype = null;
        this.msgstate = null;

    }

    public Innermsg getInnermsg() {
        return innermsg;
    }

    public void setInnermsg(Innermsg innermsg) {
        this.innermsg = innermsg;
    }
}
