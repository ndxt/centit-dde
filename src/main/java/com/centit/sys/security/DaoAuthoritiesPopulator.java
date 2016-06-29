package com.centit.sys.security;

import java.util.Collection;

import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;

import com.centit.sys.service.SysUserManager;


public class DaoAuthoritiesPopulator implements LdapAuthoritiesPopulator {

    private SysUserManager userManager;

    public void setUserDetailsService(SysUserManager uM) {
        userManager = uM;
    }

    public Collection<GrantedAuthority> getGrantedAuthorities(
            DirContextOperations userData, String username) {
        return userManager.loadUserAuthorities(username);
    }
}
