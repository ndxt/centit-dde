package com.centit.sys.po;

/**
 * FRolepowerId entity.
 *
 * @author MyEclipse Persistence Tools
 */

public class FRolepowerId implements java.io.Serializable {

    // Fields

    private static final long serialVersionUID = 1L;
    private String rolecode; // 角色代码
    private String optcode; // 操作代码

    // Constructors

    /**
     * default constructor
     */
    public FRolepowerId() {
    }

    /**
     * full constructor
     */
    public FRolepowerId(String rolecode, String optcode) {
        this.rolecode = rolecode;
        this.optcode = optcode;
    }

    // Property accessors

    public String getRolecode() {
        return this.rolecode;
    }

    public void setRolecode(String rolecode) {
        this.rolecode = rolecode;
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
        if (!(other instanceof FRolepowerId))
            return false;
        FRolepowerId castOther = (FRolepowerId) other;

        return ((this.getRolecode() == castOther.getRolecode()) || (this
                .getRolecode() != null && castOther.getRolecode() != null && this
                .getRolecode().equals(castOther.getRolecode())))
                && ((this.getOptcode() == castOther.getOptcode()) || (this
                .getOptcode() != null && castOther.getOptcode() != null && this
                .getOptcode().equals(castOther.getOptcode())));
    }

    public int hashCode() {
        int result = 17;

        result = 37 * result
                + (getRolecode() == null ? 0 : this.getRolecode().hashCode());
        result = 37 * result
                + (getOptcode() == null ? 0 : this.getOptcode().hashCode());
        return result;
    }

}
