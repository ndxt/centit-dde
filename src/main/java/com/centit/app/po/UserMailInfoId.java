package com.centit.app.po;

/**
 * @author codefan@hotmail.com
 */

public class UserMailInfoId implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private String userCode;

    private String mailAccount;

    // Constructors

    /**
     * default constructor
     */
    public UserMailInfoId() {
    }

    /**
     * full constructor
     */
    public UserMailInfoId(String userCode, String mailAccount) {

        this.userCode = userCode;
        this.mailAccount = mailAccount;
    }


    public String getUserCode() {
        return this.userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getMailAccount() {
        return this.mailAccount;
    }

    public void setMailAccount(String mailAccount) {
        this.mailAccount = mailAccount;
    }


    public boolean equals(Object other) {
        if ((this == other))
            return true;
        if ((other == null))
            return false;
        if (!(other instanceof UserMailInfoId))
            return false;

        UserMailInfoId castOther = (UserMailInfoId) other;
        boolean ret = true;

        ret = ret && (this.getUserCode() == castOther.getUserCode() ||
                (this.getUserCode() != null && castOther.getUserCode() != null
                        && this.getUserCode().equals(castOther.getUserCode())));

        ret = ret && (this.getMailAccount() == castOther.getMailAccount() ||
                (this.getMailAccount() != null && castOther.getMailAccount() != null
                        && this.getMailAccount().equals(castOther.getMailAccount())));

        return ret;
    }

    public int hashCode() {
        int result = 17;

        result = 37 * result +
                (this.getUserCode() == null ? 0 : this.getUserCode().hashCode());

        result = 37 * result +
                (this.getMailAccount() == null ? 0 : this.getMailAccount().hashCode());

        return result;
    }
}
