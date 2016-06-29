package com.centit.sys.po;


/**
 * FAddressBook entity.
 *
 * @author codefan@hotmail.com
 */

public class OptVariableId implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private String optid;

    private String varname;

    // Constructors

    /**
     * default constructor
     */
    public OptVariableId() {
    }

    /**
     * full constructor
     */
    public OptVariableId(String optid, String varname) {

        this.optid = optid;
        this.varname = varname;
    }


    public String getOptid() {
        return this.optid;
    }

    public void setOptid(String optid) {
        this.optid = optid;
    }

    public String getVarname() {
        return this.varname;
    }

    public void setVarname(String varname) {
        this.varname = varname;
    }


    public boolean equals(Object other) {
        if ((this == other))
            return true;
        if ((other == null))
            return false;
        if (!(other instanceof OptVariableId))
            return false;

        OptVariableId castOther = (OptVariableId) other;
        boolean ret = true;

        ret = ret && (this.getOptid() == castOther.getOptid() ||
                (this.getOptid() != null && castOther.getOptid() != null
                        && this.getOptid().equals(castOther.getOptid())));

        ret = ret && (this.getVarname() == castOther.getVarname() ||
                (this.getVarname() != null && castOther.getVarname() != null
                        && this.getVarname().equals(castOther.getVarname())));

        return ret;
    }

    public int hashCode() {
        int result = 17;

        result = 37 * result +
                (this.getOptid() == null ? 0 : this.getOptid().hashCode());

        result = 37 * result +
                (this.getVarname() == null ? 0 : this.getVarname().hashCode());

        return result;
    }
}
