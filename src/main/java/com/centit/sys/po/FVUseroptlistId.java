package com.centit.sys.po;

/**
 * FVUseroptlistId entity.
 *
 * @author MyEclipse Persistence Tools
 */
//用户业务操作 view的主键
public class FVUseroptlistId implements java.io.Serializable {

    // Fields
    private static final long serialVersionUID = 1L;
    private String usercode;    //用户代码
    private String optcode;        //业务代码

    // Constructors

    /**
     * default constructor
     */
    public FVUseroptlistId() {
    }

    /**
     * minimal constructor
     */
    public FVUseroptlistId(String usercode, String optcode) {
        this.usercode = usercode;
        this.optcode = optcode;

    }


    // Property accessors

    public String getUsercode() {
        return this.usercode;
    }

    public void setUsercode(String usercode) {
        this.usercode = usercode;
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
        if (!(other instanceof FVUseroptlistId))
            return false;
        FVUseroptlistId castOther = (FVUseroptlistId) other;

        return ((this.getUsercode() == castOther.getUsercode()) || (this
                .getUsercode() != null
                && castOther.getUsercode() != null && this.getUsercode()
                .equals(castOther.getUsercode())))
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
                + (getOptcode() == null ? 0 : this.getOptcode().hashCode());
        return result;
    }

}