package com.centit.app.po;

/**
 * FAddressBook entity.
 *
 * @author codefan@hotmail.com
 */

public class MsgannexId implements java.io.Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1050836150202156985L;

    private String msgcode;

    private String filecode;

    // Constructors

    /**
     * default constructor
     */
    public MsgannexId() {
    }

    /**
     * full constructor
     */
    public MsgannexId(String msgcode, String filecode) {

        this.msgcode = msgcode;
        this.filecode = filecode;
    }

    public String getMsgcode() {
        return this.msgcode;
    }

    public void setMsgcode(String msgcode) {
        this.msgcode = msgcode;
    }

    public String getFilecode() {
        return this.filecode;
    }

    public void setFilecode(String filecode) {
        this.filecode = filecode;
    }

    public boolean equals(Object other) {
        if ((this == other))
            return true;
        if ((other == null))
            return false;
        if (!(other instanceof MsgannexId))
            return false;

        MsgannexId castOther = (MsgannexId) other;
        boolean ret = true;

        ret = ret
                && (this.getMsgcode() == castOther.getMsgcode() || (this.getMsgcode() != null
                && castOther.getMsgcode() != null && this.getMsgcode().equals(castOther.getMsgcode())));

        ret = ret
                && (this.getFilecode() == castOther.getFilecode() || (this.getFilecode() != null
                && castOther.getFilecode() != null && this.getFilecode().equals(castOther.getFilecode())));

        return ret;
    }

    public int hashCode() {
        int result = 17;

        result = 37 * result + (this.getMsgcode() == null ? 0 : this.getMsgcode().hashCode());

        result = 37 * result + (this.getFilecode() == null ? 0 : this.getFilecode().hashCode());

        return result;
    }
}
