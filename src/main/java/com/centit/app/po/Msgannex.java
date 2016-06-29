package com.centit.app.po;

/**
 * create by scaffold
 *
 * @author codefan@hotmail.com
 */

public class Msgannex implements java.io.Serializable {
    private static final long serialVersionUID = -5489303426894156607L;

    public String getMsgannexId() {
        return msgannexId;
    }

    public void setMsgannexId(String msgannexId) {
        this.msgannexId = msgannexId;
    }

    public Innermsg getInnermsg() {
        return innermsg;
    }

    public void setInnermsg(Innermsg innermsg) {
        this.innermsg = innermsg;
    }

    public Fileinfo getFileinfo() {
        return fileinfo;
    }

    public void setFileinfo(Fileinfo fileinfo) {
        this.fileinfo = fileinfo;
    }

    private String msgannexId;
    private Innermsg innermsg;
    private Fileinfo fileinfo;


    public Msgannex() {
    }

    public Msgannex(String msgannexId, Innermsg innermsg, Fileinfo fileinfo) {
        this.msgannexId = msgannexId;
        this.innermsg = innermsg;
        this.fileinfo = fileinfo;
    }

    public Msgannex(Innermsg innermsg, Fileinfo fileinfo) {

        this.innermsg = innermsg;
        this.fileinfo = fileinfo;
    }

    public Msgannex(String msgannexId) {
        this.msgannexId = msgannexId;
    }

    /**
     *


     private MsgannexId cid;

     // Constructors

     public Msgannex() {
     }


     public Msgannex(MsgannexId id

     ) {
     this.cid = id;

     }

     public MsgannexId getCid() {
     return this.cid;
     }

     public void setCid(MsgannexId id) {
     this.cid = id;
     }

     public String getMsgcode() {
     if (this.cid == null)
     this.cid = new MsgannexId();
     return this.cid.getMsgcode();
     }

     public void setMsgcode(String msgcode) {
     if (this.cid == null)
     this.cid = new MsgannexId();
     this.cid.setMsgcode(msgcode);
     }

     public String getFilecode() {
     if (this.cid == null)
     this.cid = new MsgannexId();
     return this.cid.getFilecode();
     }

     public void setFilecode(String filecode) {
     if (this.cid == null)
     this.cid = new MsgannexId();
     this.cid.setFilecode(filecode);
     }

     // Property accessors

     public void copy(Msgannex other) {

     this.setMsgcode(other.getMsgcode());
     this.setFilecode(other.getFilecode());

     }

     public void copyNotNullProperty(Msgannex other) {

     if (other.getMsgcode() != null)
     this.setMsgcode(other.getMsgcode());
     if (other.getFilecode() != null)
     this.setFilecode(other.getFilecode());

     }*/
}
