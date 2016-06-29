package com.centit.sys.po;


/**
 * FAddressBook entity.
 *
 * @author codefan@hotmail.com
 */

public class OptRunRecId implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private String actionurl;

    private String funcname;

    // Constructors

    /**
     * default constructor
     */
    public OptRunRecId() {
    }

    /**
     * full constructor
     */
    public OptRunRecId(String actionurl, String funcname) {

        this.actionurl = actionurl;
        this.funcname = funcname;
    }


    public String getActionurl() {
        return this.actionurl;
    }

    public void setActionurl(String actionurl) {
        this.actionurl = actionurl;
    }

    public String getFuncname() {
        return this.funcname;
    }

    public void setFuncname(String funcname) {
        this.funcname = funcname;
    }


    public boolean equals(Object other) {
        if ((this == other))
            return true;
        if ((other == null))
            return false;
        if (!(other instanceof OptRunRecId))
            return false;

        OptRunRecId castOther = (OptRunRecId) other;
        boolean ret = true;

        ret = ret && (this.getActionurl() == castOther.getActionurl() ||
                (this.getActionurl() != null && castOther.getActionurl() != null
                        && this.getActionurl().equals(castOther.getActionurl())));

        ret = ret && (this.getFuncname() == castOther.getFuncname() ||
                (this.getFuncname() != null && castOther.getFuncname() != null
                        && this.getFuncname().equals(castOther.getFuncname())));

        return ret;
    }

    public int hashCode() {
        int result = 17;

        result = 37 * result +
                (this.getActionurl() == null ? 0 : this.getActionurl().hashCode());

        result = 37 * result +
                (this.getFuncname() == null ? 0 : this.getFuncname().hashCode());

        return result;
    }
}
