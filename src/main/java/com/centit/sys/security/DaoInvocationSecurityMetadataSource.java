package com.centit.sys.security;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import com.centit.core.structs2.OptDesc;
import com.centit.core.structs2.Struts2UrlParser;
import com.centit.sys.dao.OptInfoDao;
import com.centit.sys.dao.OptRunRecDao;
import com.centit.sys.po.FOptRoleMap;
import com.centit.sys.service.CodeRepositoryUtil;

public class DaoInvocationSecurityMetadataSource
        implements FilterInvocationSecurityMetadataSource, CentitSecurityMetadata {
    private static final Log log = LogFactory.getLog(DaoInvocationSecurityMetadataSource.class);
    private OptInfoDao functionDao;
    private OptRunRecDao optRecDao;

    private Date lastUpdateCadTime;
    Map<OptDesc, RdbmsEntryHoder> listEntryHoder;

    public void setFunctionDao(OptInfoDao sysfunlsdao) {
        this.functionDao = sysfunlsdao;
    }

    public void setOptRunRecDao(OptRunRecDao optRecDao) {
        this.optRecDao = optRecDao;
    }

    public DaoInvocationSecurityMetadataSource() {
        lastUpdateCadTime = null;
        listEntryHoder = null;
    }

    public void loadRoleSecurityMetadata() {
        lastUpdateCadTime = new Date(System.currentTimeMillis());
        listEntryHoder = new HashMap<OptDesc, RdbmsEntryHoder>();
        List<FOptRoleMap> optrolemaps = functionDao.getAllOptRoleMap();
        RdbmsEntryHoder rsh = null;// new RdbmsEntryHoder();

        for (FOptRoleMap optrole : optrolemaps) {
            if (rsh == null || !rsh.matchOptRoleMap(optrole)) {
                if (rsh != null) {
                    RdbmsEntryHoder rsh2 = listEntryHoder.get(rsh.getOptDesc());
                    if (rsh2 == null)
                        listEntryHoder.put(rsh.getOptDesc(), rsh);
                    else
                        rsh2.merge(rsh);
                }
                rsh = new RdbmsEntryHoder(optrole);
            } else
                rsh.addRole(optrole.getRolecode());
        }
        if (rsh != null) {
            RdbmsEntryHoder rsh2 = listEntryHoder.get(rsh.getOptDesc());
            if (rsh2 == null)
                listEntryHoder.put(rsh.getOptDesc(), rsh);
            else
                rsh2.merge(rsh);
        }
        log.info("系统自动刷新角色权限信息。");
    }

    public Map<OptDesc, RdbmsEntryHoder> getRdbmsEntryHolderList() {
        if (lastUpdateCadTime != null && listEntryHoder != null &&
                //角色权限库一个小时更新一次
                ((new Date(System.currentTimeMillis())).getTime() - lastUpdateCadTime.getTime() < 60 * 60 * 1000)) {
            return listEntryHoder;
        }

        loadRoleSecurityMetadata();

        return listEntryHoder;
    }

    // According to a URL, Find out permission configuration of this URL.
    public Collection<ConfigAttribute> getAttributes(Object object)
            throws IllegalArgumentException {
        // guess object is a URL.
        if ((object == null) || !this.supports(object.getClass())) {
            throw new IllegalArgumentException("对不起,目标对象不是类型");
        }
        FilterInvocation fi = (FilterInvocation) object;

        String sUrl = fi.getRequestUrl();
        OptDesc od = Struts2UrlParser.parseUrl(sUrl, fi.getRequest());

        if ("T".equals(CodeRepositoryUtil.getValue("SYSPARAM", "ACTIONRUNLOG"))) {
            //optRecDao.recRunTime( od.getActionUrl(),od.getMethod());
        }

        Map<OptDesc, RdbmsEntryHoder> list = this.getRdbmsEntryHolderList();
        if (list == null)
            return null;
        RdbmsEntryHoder entryHolder = list.get(od);
        if (entryHolder != null)
            return entryHolder.getCad();
        //ConfigAttributeDefinition cad = new ConfigAttributeDefinition();
        //cad.addConfigAttribute(new SecurityConfig(RdbmsEntryHoder.ROLE_PREFIX + FORBIDDEN"));
        //return cad;
        return null;
    }

    public boolean supports(Class<?> clazz) {
        if (FilterInvocation.class.isAssignableFrom(clazz)) {
            return true;
        }
        return false;
    }

    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

}