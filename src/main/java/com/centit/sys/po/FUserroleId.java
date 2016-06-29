package com.centit.sys.po;

import java.util.Date;

/**
 * FUserroleId entity.
 *
 * @author MyEclipse Persistence Tools
 */
//用户角色的主键
public class FUserroleId implements java.io.Serializable {

    // Fields

    private static final long serialVersionUID = 893187890652550538L;

    private String usercode;     //用户代码
    private String rolecode;     //角色代码
    private Date obtaindate;    //获得角色时间

    // Constructors

    /**
     * default constructor
     */
    public FUserroleId() {
    }

    /**
     * full constructor
     */
    public FUserroleId(String usercode, String rolecode, Date obtaindate) {
        this.usercode = usercode;
        this.rolecode = rolecode;
        this.obtaindate = obtaindate;
    }

    // Property accessors

    public String getUsercode() {
        return this.usercode;
    }

    public void setUsercode(String usercode) {
        this.usercode = usercode;
    }

    public String getRolecode() {
        return this.rolecode;
    }

    public void setRolecode(String rolecode) {
        this.rolecode = rolecode;
    }

    public Date getObtaindate() {
        return this.obtaindate;
    }

    public void setObtaindateToToday() {
        this.obtaindate = new java.util.Date();
    }

    public void setObtaindate(Date obtaindate) {
        this.obtaindate = obtaindate;
    }

    public boolean equals(Object other) {
        if ((this == other))
            return true;
        if ((other == null))
            return false;
        if (!(other instanceof FUserroleId))
            return false;
        FUserroleId castOther = (FUserroleId) other;

        return ((this.getUsercode() == castOther.getUsercode()) || (this
                .getUsercode() != null
                && castOther.getUsercode() != null && this.getUsercode()
                .equals(castOther.getUsercode())))
                && ((this.getRolecode() == castOther.getRolecode()) || (this
                .getRolecode() != null
                && castOther.getRolecode() != null && this
                .getRolecode().equals(castOther.getRolecode())))
                && ((this.getObtaindate() == castOther.getObtaindate()) || (this
                .getObtaindate() != null
                && castOther.getObtaindate() != null && this
                .getObtaindate().equals(castOther.getObtaindate())));
    }

    public int hashCode() {
        int result = 17;

        result = 37 * result
                + (getUsercode() == null ? 0 : this.getUsercode().hashCode());
        result = 37 * result
                + (getRolecode() == null ? 0 : this.getRolecode().hashCode());
        result = 37
                * result
                + (getObtaindate() == null ? 0 : this.getObtaindate()
                .hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "[用户代码=" + usercode + ", 角色代码=" + rolecode + "]";
    }

}