package com.centit.sys.po;

public class FUserPwd {
    private String loginname;
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public void copy(FUserPwd other) {
        this.loginname = other.getLoginname();
        this.oldPassword = other.getOldPassword();
        this.newPassword = other.getNewPassword();
        this.confirmPassword = other.getConfirmPassword();
    }

    public void copyNotNullProperty(FUserPwd other) {

        if (other.getLoginname() != null)
            this.loginname = other.getLoginname();
        if (other.getOldPassword() != null)
            this.oldPassword = other.getOldPassword();
        if (other.getNewPassword() != null)
            this.newPassword = other.getNewPassword();
        if (other.getConfirmPassword() != null)
            this.confirmPassword = other.getConfirmPassword();
    }
}