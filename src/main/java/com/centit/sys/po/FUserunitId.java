package com.centit.sys.po;

/**
 * FUserunitId entity.
 *
 * @author MyEclipse Persistence Tools
 */
//用户所属机构主键
public class FUserunitId implements java.io.Serializable {

    // Fields
    private static final long serialVersionUID = 1L;
    private String unitcode;    //机构代码
    private String usercode;    //用户代码
    private String userstation; //岗位
    private String userrank;    //职务

    // Constructors

    /**
     * default constructor
     */
    public FUserunitId() {
    }

    /**
     * full constructor
     */
    public FUserunitId(String unitcode, String usercode, String userstation, String userrank) {
        this.unitcode = unitcode;
        this.usercode = usercode;
        this.userstation = userstation;
        this.userrank = userrank;
    }

    public String getUserstation() {
        return this.userstation;
    }

    public void setUserstation(String userstation) {
        this.userstation = userstation;
    }

    public String getUserrank() {
        return this.userrank;
    }

    public void setUserrank(String userrank) {
        this.userrank = userrank;
    }

    // Property accessors

    public String getUnitcode() {
        return this.unitcode;
    }

    public void setUnitcode(String unitcode) {
        this.unitcode = unitcode;
    }

    public String getUsercode() {
        return this.usercode;
    }

    public void setUsercode(String usercode) {
        this.usercode = usercode;
    }

    public boolean equals(Object other) {
        if ((this == other))
            return true;
        if ((other == null))
            return false;
        if (!(other instanceof FUserunitId))
            return false;
        FUserunitId castOther = (FUserunitId) other;

        return ((this.getUnitcode() == castOther.getUnitcode()) || (this
                .getUnitcode() != null
                && castOther.getUnitcode() != null && this.getUnitcode()
                .equals(castOther.getUnitcode())))
                && ((this.getUsercode() == castOther.getUsercode()) || (this
                .getUsercode() != null
                && castOther.getUsercode() != null && this
                .getUsercode().equals(castOther.getUsercode())))
                && ((this.getUserstation() == castOther.getUserstation()) || (this
                .getUserstation() != null
                && castOther.getUserstation() != null && this
                .getUserstation().equals(castOther.getUserstation())))
                && ((this.getUserrank() == castOther.getUserrank()) || (this
                .getUserrank() != null
                && castOther.getUserrank() != null && this
                .getUserrank().equals(castOther.getUserrank())));
    }

    public int hashCode() {
        int result = 17;

        result = 37 * result
                + (getUnitcode() == null ? 0 : this.getUnitcode().hashCode());
        result = 37 * result
                + (getUsercode() == null ? 0 : this.getUsercode().hashCode());
        result = 37 * result
                + (getUserstation() == null ? 0 : this.getUserstation().hashCode());
        result = 37 * result
                + (getUserrank() == null ? 0 : this.getUserrank().hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "[机构代码=" + unitcode + ", 用户代码=" + usercode + "]";
    }

}