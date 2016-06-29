package com.centit.app.po;


/**
 * FAddressBook entity.
 *
 * @author codefan@hotmail.com
 */

public class PublicinfologId implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private String usercode;

    private String infocode;

    // Constructors

    /**
     * default constructor
     */
    public PublicinfologId() {
    }

    /**
     * full constructor
     */
    public PublicinfologId(String usercode, String infocode) {

        this.usercode = usercode;
        this.infocode = infocode;
    }


    public String getUsercode() {
        return this.usercode;
    }

    public void setUsercode(String usercode) {
        this.usercode = usercode;
    }

    public String getInfocode() {
        return this.infocode;
    }

    public void setInfocode(String infocode) {
        this.infocode = infocode;
    }


    public boolean equals(Object other) {
        if ((this == other))
            return true;
        if ((other == null))
            return false;
        if (!(other instanceof PublicinfologId))
            return false;

        PublicinfologId castOther = (PublicinfologId) other;
        boolean ret = true;

        ret = ret && (this.getUsercode() == castOther.getUsercode() ||
                (this.getUsercode() != null && castOther.getUsercode() != null
                        && this.getUsercode().equals(castOther.getUsercode())));

        ret = ret && (this.getInfocode() == castOther.getInfocode() ||
                (this.getInfocode() != null && castOther.getInfocode() != null
                        && this.getInfocode().equals(castOther.getInfocode())));

        return ret;
    }

    public int hashCode() {
        int result = 17;

        result = 37 * result +
                (this.getUsercode() == null ? 0 : this.getUsercode().hashCode());

        result = 37 * result +
                (this.getInfocode() == null ? 0 : this.getInfocode().hashCode());

        return result;
    }
}
