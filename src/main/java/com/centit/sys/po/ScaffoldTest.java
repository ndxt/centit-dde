package com.centit.sys.po;

import java.util.Date;

/**
 * create by scaffold
 *
 * @author codefan@hotmail.com
 */

public class ScaffoldTest implements java.io.Serializable {
    private static final long serialVersionUID = 1L;


    private Long logid;

    private String loglevel;
    private String usercode;
    private Date opttime;
    private String optid;
    private String optcode;
    private String optcontent;
    private String oldvalue;

    // Constructors

    /**
     * default constructor
     */
    public ScaffoldTest() {
    }

    /**
     * minimal constructor
     */
    public ScaffoldTest(
            Long logid
            , String loglevel, String usercode, Date opttime, String optid, String optcontent) {


        this.logid = logid;

        this.loglevel = loglevel;
        this.usercode = usercode;
        this.opttime = opttime;
        this.optid = optid;
        this.optcontent = optcontent;
    }

    /**
     * full constructor
     */
    public ScaffoldTest(
            Long logid
            , String loglevel, String usercode, Date opttime, String optid, String optcode, String optcontent, String oldvalue) {


        this.logid = logid;

        this.loglevel = loglevel;
        this.usercode = usercode;
        this.opttime = opttime;
        this.optid = optid;
        this.optcode = optcode;
        this.optcontent = optcontent;
        this.oldvalue = oldvalue;
    }


    public Long getLogid() {
        return this.logid;
    }

    public void setLogid(Long logid) {
        this.logid = logid;
    }
    // Property accessors

    public String getLoglevel() {
        return this.loglevel;
    }

    public void setLoglevel(String loglevel) {
        this.loglevel = loglevel;
    }

    public String getUsercode() {
        return this.usercode;
    }

    public void setUsercode(String usercode) {
        this.usercode = usercode;
    }

    public Date getOpttime() {
        return this.opttime;
    }

    public void setOpttime(Date opttime) {
        this.opttime = opttime;
    }

    public String getOptid() {
        return this.optid;
    }

    public void setOptid(String optid) {
        this.optid = optid;
    }

    public String getOptcode() {
        return this.optcode;
    }

    public void setOptcode(String optcode) {
        this.optcode = optcode;
    }

    public String getOptcontent() {
        return this.optcontent;
    }

    public void setOptcontent(String optcontent) {
        this.optcontent = optcontent;
    }

    public String getOldvalue() {
        return this.oldvalue;
    }

    public void setOldvalue(String oldvalue) {
        this.oldvalue = oldvalue;
    }


    public void copy(ScaffoldTest other) {

        this.setLogid(other.getLogid());

        this.loglevel = other.getLoglevel();
        this.usercode = other.getUsercode();
        this.opttime = other.getOpttime();
        this.optid = other.getOptid();
        this.optcode = other.getOptcode();
        this.optcontent = other.getOptcontent();
        this.oldvalue = other.getOldvalue();

    }

    public void copyNotNullProperty(ScaffoldTest other) {

        if (other.getLogid() != null)
            this.setLogid(other.getLogid());

        if (other.getLoglevel() != null)
            this.loglevel = other.getLoglevel();
        if (other.getUsercode() != null)
            this.usercode = other.getUsercode();
        if (other.getOpttime() != null)
            this.opttime = other.getOpttime();
        if (other.getOptid() != null)
            this.optid = other.getOptid();
        if (other.getOptcode() != null)
            this.optcode = other.getOptcode();
        if (other.getOptcontent() != null)
            this.optcontent = other.getOptcontent();
        if (other.getOldvalue() != null)
            this.oldvalue = other.getOldvalue();

    }

    public void clearProperties() {

        this.loglevel = null;
        this.usercode = null;
        this.opttime = null;
        this.optid = null;
        this.optcode = null;
        this.optcontent = null;
        this.oldvalue = null;

    }
}
