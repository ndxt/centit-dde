package com.centit.sys.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;

import com.centit.core.structs2.OptDesc;
import com.centit.core.structs2.Struts2UrlParser;
import com.centit.sys.po.FOptRoleMap;

public class RdbmsEntryHoder implements Serializable {

    private static final long serialVersionUID = 1L;
    private OptDesc optDesc;
    private Collection<ConfigAttribute> cad;

    public static final String ROLE_PREFIX = "R_";

    public RdbmsEntryHoder() {
        cad = new ArrayList<ConfigAttribute>();
    }

    public RdbmsEntryHoder(String sUrl) {
        optDesc = Struts2UrlParser.parseUrl(sUrl);
        cad = new ArrayList<ConfigAttribute>();
    }

    public RdbmsEntryHoder(FOptRoleMap optRole) {
        String sUrl = optRole.getOpturl();
        if (sUrl.charAt(0) != '/')
            sUrl = '/' + sUrl;
        optDesc = Struts2UrlParser.parseUrl(sUrl);
        optDesc.setMethod(optRole.getOptmethod());

        cad = new ArrayList<ConfigAttribute>();
        addRole(optRole.getRolecode());
    }

    public final void addRole(String rolecode) {
        cad.add(new SecurityConfig(ROLE_PREFIX + rolecode.trim()));
    }

    public String getOptBaseUrl() {
        return optDesc.getActionUrl();
    }

    public OptDesc getOptDesc() {
        return optDesc;
    }

    public void setOptDesc(OptDesc optDesc) {
        this.optDesc = optDesc;
    }

    public String getMethod() {
        return optDesc.getMethod();
    }

    public boolean matchUrl(String sUrl) {
        OptDesc od = Struts2UrlParser.parseUrl(sUrl);
        //String method = StringBaseOpt.getUrlMethod(sUrl); Struts1 的算法
        return matchUrl(od);
    }

    public boolean matchUrl(OptDesc od) {
        return optDesc.equals(od);
    }

    public boolean matchOptRoleMap(FOptRoleMap optrole) {
        String sUrl = optrole.getOpturl();
        if (sUrl.charAt(0) != '/')
            sUrl = '/' + sUrl;
        OptDesc od = Struts2UrlParser.parseUrl(sUrl);
        od.setMethod(optrole.getOptmethod());

        return optDesc.getActionUrl() != null &&
                optDesc.getActionUrl().equals(od.getActionUrl()) &&
                //optDesc.getMethod() != null &&
                optDesc.getMethod().equals(od.getMethod());
    }

    public Collection<ConfigAttribute> getCad() {
        return cad;
    }

    public void merge(RdbmsEntryHoder other) {
        this.cad.addAll(other.getCad());
    }
}
