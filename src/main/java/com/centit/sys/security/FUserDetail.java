package com.centit.sys.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;

import com.centit.sys.dao.UserRoleDao;
import com.centit.sys.po.FOptinfo;
import com.centit.sys.po.FRoleinfo;
import com.centit.sys.po.FUserinfo;
import com.centit.sys.po.Usersetting;

/**
 * FUserinfo entity.
 *
 * @author MyEclipse Persistence Tools
 */

public class FUserDetail extends FUserinfo implements UserDetails {

    private static final long serialVersionUID = 1L;
    // Fields

    private UserRoleDao sysusrodao;

    public void setSysusrodao(UserRoleDao sysusrodao) {
        this.sysusrodao = sysusrodao;
    }

    // role
    private Date lastUpdateRoleTime;
    private Collection<GrantedAuthority> arrayAuths;
    //@Deprecated
    //private String username;
    Usersetting userSetting;
    List<FOptinfo> userFuncs;
    /*
    public List<FUserunit> getUnitinfo() {
        return super.getUserUnits();
    }

    public void setUnitinfo(List<FUserunit> unitinfo) {
        super.setUserUnits(unitinfo);
    }
    */

    /**
     * default constructor
     */
    public FUserDetail(FUserinfo user) {
        super(user.getUsercode(), user.getUserpin(), user.getIsvalid(),
                user.getLoginname(), user.getUsername(), user.getUserword(), user.getUserdesc(),
                user.getLogintimes(), user.getActivetime(), user.getLoginip(),
                user.getAddrbookid(), user.getUserorder(), user.getUserPwd());
        arrayAuths = null;
        lastUpdateRoleTime = null;
    }

    public FUserDetail() {
        arrayAuths = null;
        lastUpdateRoleTime = null;
    }

    /**
     * minimal constructor
     */
    public FUserDetail(String usercode, String userstate,
                       String loginname, String username) {
        super(usercode, userstate, loginname, username);
        arrayAuths = null;
        lastUpdateRoleTime = null;
    }

    /**
     * full constructor
     */
    public FUserDetail(String usercode, String userpin, String userstate,
                       String loginname, String username, String userword, String userdesc,
                       Long logintimes, Date activeime, String loginip, Long addrbookid, Long userorder, String userPwd) {
        super(usercode, userpin, userstate, loginname, username, userword, userdesc,
                logintimes, activeime, loginip, addrbookid, userorder, userPwd);
        arrayAuths = null;
        lastUpdateRoleTime = null;
    }

    // Property accessors

    public void setAuthoritiesByRoles(List<FRoleinfo> roles) {
        if (roles.size() < 1)
            return;
        arrayAuths = new ArrayList<GrantedAuthority>();
        for (FRoleinfo role : roles) {
            arrayAuths.add(
                    new GrantedAuthorityImpl(RdbmsEntryHoder.ROLE_PREFIX + role.getRolecode()));
        }
        lastUpdateRoleTime = new Date(System.currentTimeMillis());
    }

    private void loadAuthoritys() {
        lastUpdateRoleTime = new Date(System.currentTimeMillis());
        // load user roles and translate to Authorities;
        List<FRoleinfo> roles = sysusrodao.getSysRolesByUsid(this.getUsercode());
        setAuthoritiesByRoles(roles);
    }

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
        return !"T".equals(this.getIsvalid());
    }

    public boolean isAccountLocked() {
        return !"T".equals(this.getIsvalid());
    }

    public boolean isCredentialsExpired() {
        return isAccountLocked();
		/*return (arrayAuths == null || lastUpdateRoleTime == null 
				|| (new Date(System.currentTimeMillis())).getTime()- lastUpdateRoleTime.getTime() > 10 * 60 * 1000);
		*/
    }

    public Collection<GrantedAuthority> getAuthorities() {
        if (arrayAuths == null || lastUpdateRoleTime == null
                || (new Date(System.currentTimeMillis())).getTime() - lastUpdateRoleTime.getTime() > 10 * 60 * 1000)
            loadAuthoritys();
        return arrayAuths;

    }

    public Usersetting getUserSetting() {
        return userSetting;
    }

    public void setUserSetting(Usersetting userSetting) {
        this.userSetting = userSetting;
    }

    public List<FOptinfo> getUserFuncs() {
        return userFuncs;
    }

    public void setUserFuncs(List<FOptinfo> userFuncs) {
        this.userFuncs = userFuncs;
    }

    @Override
    public String getPassword() {
        return this.getUserpin();
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("FUserDetail:");
        sb.append(super.toString());
        return sb.toString();
    }

    @Override
    public boolean equals(Object other) {
        if ((this == other))
            return true;
        if ((other == null))
            return false;
        if (!(other instanceof FUserinfo))
            return false;
        FUserinfo castOther = (FUserinfo) other;
        if (this.getUsercode() == null)
            return false;
        return this.getUsercode().equals(castOther.getUsercode());
    }

    @Override
    public int hashCode() {
        return getUsercode() == null ? 0 : this.getUsercode().hashCode();

    }
}