package com.centit.sys.po;

import java.util.Date;

/**
 * create by scaffold
 *
 * @author codefan@hotmail.com
 */

public class OptLog implements java.io.Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -8051182992860454826L;
    public static final String LEVEL_INFO = "0";
    public static final String LEVEL_ERROR = "1";

    /**
     * 系统日志操作
     */
    public static final String P_OPT_LOG_METHOD = "P_OPT_LOG_METHOD";
    public static final String P_OPT_LOG_METHOD_C = "C";
    public static final String P_OPT_LOG_METHOD_U = "U";
    public static final String P_OPT_LOG_METHOD_D = "D";

    private Long logid;

    private String loglevel = LEVEL_INFO;
    private String usercode;
    private Date opttime = new Date();
    private String optid;
    private String tagid;
    private String optmethod;
    private String optcontent;
    private String oldvalue;

    // Constructors

    /**
     * default constructor
     */
    public OptLog() {
    }

    public OptLog(String usercode, String optid, String tagid, String optmethod, String optcontent, String oldvalue) {
        this.usercode = usercode;
        // this.usercode = ((FUserDetail)
        // (SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getUsercode();

        this.optid = optid;
        this.tagid = tagid;
        this.optmethod = optmethod;
        this.optcontent = optcontent;
        this.oldvalue = oldvalue;
    }

    /**
     * full constructor
     */
    public OptLog(Long logid, String loglevel, String usercode, Date opttime, String optid, String optmethod,
                  String optcontent, String oldvalue) {

        this.logid = logid;

        this.loglevel = loglevel;
        this.usercode = usercode;
        this.opttime = opttime;
        this.optid = optid;
        this.optmethod = optmethod;
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

    public String getOptmethod() {
        return this.optmethod;
    }

    public void setOptmethod(String optmethod) {
        this.optmethod = optmethod;
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

    public void copy(OptLog other) {

        this.setLogid(other.getLogid());

        this.loglevel = other.getLoglevel();
        this.usercode = other.getUsercode();
        this.opttime = other.getOpttime();
        this.optid = other.getOptid();
        this.optmethod = other.getOptmethod();
        this.optcontent = other.getOptcontent();
        this.oldvalue = other.getOldvalue();

    }

    public void copyNotNullProperty(OptLog other) {

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
        if (other.getOptmethod() != null)
            this.optmethod = other.getOptmethod();
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
        this.optmethod = null;
        this.optcontent = null;
        this.oldvalue = null;

    }

    public String getTagid() {
        return tagid;
    }

    public void setTagid(String tagid) {
        this.tagid = tagid;
    }
}
