package com.centit.sys.security;

import java.util.Collection;

import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;

public class AdUserDetailsContextMapper implements UserDetailsContextMapper {

    private UserDetailsService userManager;

    public void setUserDetailsService(UserDetailsService uM) {
        userManager = uM;
    }

    public UserDetails mapUserFromContext(DirContextOperations ctx,
                                          String username, Collection<GrantedAuthority> authority) {
        return userManager.loadUserByUsername(username);
    }

    public void mapUserToContext(UserDetails user, DirContextAdapter ctx) {

    }

}
