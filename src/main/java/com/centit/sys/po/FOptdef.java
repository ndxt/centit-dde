package com.centit.sys.po;

import java.util.Date;


/**
 * FAddressBook entity.
 *
 * @author codefan@hotmail.com
 */

public class FOptdef implements java.io.Serializable {
    private static final long serialVersionUID = 1L;


    private String optcode;//操作代码
    private String optname;    //操作名称
    private String optid;    //所属业务
    private String optmethod;//操作方法
    private String optdesc;    //操作说明
    private String isinworkflow;//是否是流程操作
    private Date createDate = new Date();
    private Date lastModifyDate;
    // Constructors

    /**
     * default constructor
     */
    public FOptdef() {
    }

    /**
     * minimal constructor
     */
    public FOptdef(
            String optcode
            , String optid) {


        this.optcode = optcode;

        this.optid = optid;
    }

    /**
     * full constructor
     */
    public FOptdef(
            String optcode
            , String optname, String optid, String optmethod, String optdesc) {


        this.optcode = optcode;

        this.optname = optname;
        this.optid = optid;
        this.optmethod = optmethod;
        this.optdesc = optdesc;
    }


    public FOptdef(String optcode, String optname, String optid, String optmethod,
                   String optdesc, String isinworkflow) {
        this.optcode = optcode;
        this.optname = optname;
        this.optid = optid;
        this.optmethod = optmethod;
        this.optdesc = optdesc;
        this.isinworkflow = isinworkflow;

    }

    public String getOptcode() {
        return this.optcode;
    }

    public void setOptcode(String optcode) {
        this.optcode = optcode;
    }

    public String toString() {
        return this.optname;
    }

    // Property accessors

    public String getOptname() {
        return this.optname;
    }

    public void setOptname(String optname) {
        this.optname = optname;
    }

    public String getOptid() {
        return this.optid;
    }

    public void setOptid(String optid) {
        this.optid = optid;
    }

    public String getOptmethod() {
        return this.optmethod;
    }

    public void setOptmethod(String optmethod) {
        this.optmethod = optmethod;
    }

    public String getOptdesc() {
        return this.optdesc;
    }

    public void setOptdesc(String optdesc) {
        this.optdesc = optdesc;
    }

    public void setIsinworkflow(String isinworkflow) {
        this.isinworkflow = isinworkflow;
    }

    public String getIsinworkflow() {
        return isinworkflow;
    }

    public void copy(FOptdef other) {

        this.optname = other.getOptname();
        this.optid = other.getOptid();
        this.optmethod = other.getOptmethod();
        this.optdesc = other.getOptdesc();
        this.isinworkflow = other.getIsinworkflow();
    }

    public void copyNotNullProperty(FOptdef other) {

        if (other.getOptname() != null)
            this.optname = other.getOptname();
        if (other.getOptid() != null)
            this.optid = other.getOptid();
        if (other.getOptmethod() != null)
            this.optmethod = other.getOptmethod();
        if (other.getOptdesc() != null)
            this.optdesc = other.getOptdesc();
        if (other.getIsinworkflow() != null)
            this.isinworkflow = other.getIsinworkflow();
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
