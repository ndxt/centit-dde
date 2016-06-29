package com.centit.sys.security.casdaoauth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class FDaoUserDetails implements UserDetails {

    private static final long serialVersionUID = 1L;

    public boolean isAccountNonExpired() {
        return "T".equals(this.getIsvalid());
    }

    public boolean isAccountNonLocked() {
        return "T".equals(this.getIsvalid());
    }

    public boolean isCredentialsNonExpired() {
        return !isCredentialsExpired();
    }

    public boolean isAccountExpired() {
        return "F".equals(this.getIsvalid());
    }

    public boolean isAccountLocked() {
        return "F".equals(this.getIsvalid());
    }

    public boolean isCredentialsExpired() {
        return isAccountLocked();
    }

    private String usercode;
    private String userpin;
    private String isvalid;
    private String loginname;
    private String username;


    /**
     * default constructor
     */
    public FDaoUserDetails() {
    }

    /**
     * minimal constructor
     */
    public FDaoUserDetails(String usercode) {
        this.usercode = usercode;
    }

    /**
     * full constructor
     */
    public FDaoUserDetails(String usercode, String userpin, String userstate,
                           String loginname, String username) {
        this.usercode = usercode;
        this.userpin = userpin;
        this.isvalid = userstate;
        this.username = username;
        this.loginname = loginname;
    }

    // Property accessors
    public String getUsercode() {
        return this.usercode;
    }

    public void setUsercode(String usercode) {
        this.usercode = usercode;
    }

    public String getUserpin() {
        return this.userpin;
    }

    public void setUserpin(String userpin) {
        this.userpin = userpin;
    }

    public String getIsvalid() {
        if (this.isvalid == null)
            return "F";
        return this.isvalid;
    }

    public void setIsvalid(String userstate) {
        this.isvalid = userstate;
    }

    public String getUsername() {
        return this.username;
    }

    public String toString() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.userpin;
    }

    public boolean isEnabled() {
        return "T".equals(isvalid);
    }

    public String getLoginname() {
        if (loginname == null)
            return "";
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public Collection<GrantedAuthority> getAuthorities() {

        return null;
    }

}
