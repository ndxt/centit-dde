package com.centit.sys.po;

import java.util.Date;

/**
 * FOptflownoinfoId entity.
 *
 * @author MyEclipse Persistence Tools
 */
//业务操作流水号信息表主键
public class FOptflownoinfoId implements java.io.Serializable {

    // Fields

    /**
     *
     */
    private static final long serialVersionUID = -900395631646312525L;
    private String usercode;    //用户代码
    private Date optdate;        //操作日期
    private String optcode;        //操作编码

    // Constructors

    /**
     * default constructor
     */
    public FOptflownoinfoId() {
    }

    /**
     * full constructor
     */
    public FOptflownoinfoId(String usercode, Date optdate, String optcode) {
        this.usercode = usercode;
        this.optdate = optdate;
        this.optcode = optcode;
    }

    // Property accessors

    public String getUsercode() {
        return this.usercode;
    }

    public void setUsercode(String usercode) {
        this.usercode = usercode;
    }

    public Date getOptdate() {
        return this.optdate;
    }

    public void setOptdate(Date optdate) {
        this.optdate = optdate;
    }

    public String getOptcode() {
        return this.optcode;
    }

    public void setOptcode(String optcode) {
        this.optcode = optcode;
    }

    public boolean equals(Object other) {
        if ((this == other))
            return true;
        if ((other == null))
            return false;
        if (!(other instanceof FOptflownoinfoId))
            return false;
        FOptflownoinfoId castOther = (FOptflownoinfoId) other;

        return ((this.getUsercode() == castOther.getUsercode()) || (this
                .getUsercode() != null
                && castOther.getUsercode() != null && this.getUsercode()
                .equals(castOther.getUsercode())))
                && ((this.getOptdate() == castOther.getOptdate()) || (this
                .getOptdate() != null
                && castOther.getOptdate() != null && this.getOptdate()
                .equals(castOther.getOptdate())))
                && ((this.getOptcode() == castOther.getOptcode()) || (this
                .getOptcode() != null
                && castOther.getOptcode() != null && this.getOptcode()
                .equals(castOther.getOptcode())));
    }

    public int hashCode() {
        int result = 17;

        result = 37 * result
                + (getUsercode() == null ? 0 : this.getUsercode().hashCode());
        result = 37 * result
                + (getOptdate() == null ? 0 : this.getOptdate().hashCode());
        result = 37 * result
                + (getOptcode() == null ? 0 : this.getOptcode().hashCode());
        return result;
    }

}