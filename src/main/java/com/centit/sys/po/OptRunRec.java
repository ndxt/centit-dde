package com.centit.sys.po;

import java.util.Date;

/**
 * create by scaffold
 *
 * @author codefan@hotmail.com
 */

public class OptRunRec implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private OptRunRecId cid;


    private Long reqtimes;
    private Long runtimes;
    private Date lastreqtime;


    private Date createDate = new Date();
    private Date lastModifyDate;
    // Constructors

    /**
     * default constructor
     */
    public OptRunRec() {
    }

    /**
     * minimal constructor
     */
    public OptRunRec(OptRunRecId id

    ) {
        this.cid = id;


    }

    /**
     * full constructor
     */
    public OptRunRec(OptRunRecId id

            , Long reqtimes, Long runtimes, Date lastreqtime) {
        this.cid = id;


        this.reqtimes = reqtimes;
        this.runtimes = runtimes;
        this.lastreqtime = lastreqtime;
    }

    public OptRunRecId getCid() {
        return this.cid;
    }

    public void setCid(OptRunRecId id) {
        this.cid = id;
    }

    public String getActionurl() {
        if (this.cid == null)
            this.cid = new OptRunRecId();
        return this.cid.getActionurl();
    }

    public void setActionurl(String actionurl) {
        if (this.cid == null)
            this.cid = new OptRunRecId();
        this.cid.setActionurl(actionurl);
    }

    public String getFuncname() {
        if (this.cid == null)
            this.cid = new OptRunRecId();
        return this.cid.getFuncname();
    }

    public void setFuncname(String funcname) {
        if (this.cid == null)
            this.cid = new OptRunRecId();
        this.cid.setFuncname(funcname);
    }


    // Property accessors

    public Long getReqtimes() {
        return this.reqtimes;
    }

    public void setReqtimes(Long reqtimes) {
        this.reqtimes = reqtimes;
    }

    public Long getRuntimes() {
        return this.runtimes;
    }

    public void setRuntimes(Long runtimes) {
        this.runtimes = runtimes;
    }

    public Date getLastreqtime() {
        return this.lastreqtime;
    }

    public void setLastreqtime(Date lastreqtime) {
        this.lastreqtime = lastreqtime;
    }


    public void copy(OptRunRec other) {

        this.setActionurl(other.getActionurl());
        this.setFuncname(other.getFuncname());

        this.reqtimes = other.getReqtimes();
        this.runtimes = other.getRuntimes();
        this.lastreqtime = other.getLastreqtime();

    }

    public void copyNotNullProperty(OptRunRec other) {

        if (other.getActionurl() != null)
            this.setActionurl(other.getActionurl());
        if (other.getFuncname() != null)
            this.setFuncname(other.getFuncname());

        if (other.getReqtimes() != null)
            this.reqtimes = other.getReqtimes();
        if (other.getRuntimes() != null)
            this.runtimes = other.getRuntimes();
        if (other.getLastreqtime() != null)
            this.lastreqtime = other.getLastreqtime();

    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastModifyDate() {
        return lastModifyDate;
    }

    public void setLastModifyDate(Date lastModifyDate) {
        this.lastModifyDate = lastModifyDate;
    }
}
