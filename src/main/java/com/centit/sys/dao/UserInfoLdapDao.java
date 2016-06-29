package com.centit.sys.dao;

import java.io.Serializable;
import java.util.List;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttributes;

import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.GreaterThanOrEqualsFilter;

/**
 * Created with IntelliJ IDEA.
 * User: sx
 * Date: 13-5-15
 * Time: 下午2:37
 * To change this template use File | Settings | File Templates.
 */
public class UserInfoLdapDao implements Serializable {
    private static final long serialVersionUID = -7109823031885369980L;
    private LdapTemplate ldapTemplate;

    public void setLdapTemplate(LdapTemplate ldapTemplate) {
        this.ldapTemplate = ldapTemplate;
    }

    public void list() {
        AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter("objectclass", "person"));
//        filter.and(new EqualsFilter("objectclass", lastName));


        ldapTemplate.search("", filter.encode(), new AttributesMapper() {
            @Override
            public Object mapFromAttributes(Attributes attributes) throws NamingException {
                return attributes.get("cn").get();
            }
        });


        Object lookup = ldapTemplate.lookup("CN=Base_Dept");


        this.listAllUnit(null);

        System.out.println(lookup.getClass().getName());
    }


    public List<BasicAttributes> listAllUnit(String lastModDate) {
        AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter("objectclass", "group"));
        if (null != lastModDate) {
            filter.and(new GreaterThanOrEqualsFilter("whenChanged", lastModDate));
        }

        return ldapTemplate.search("", filter.encode(), new AttributesMapper() {
            @Override
            public Object mapFromAttributes(Attributes attributes) throws NamingException {

                return attributes;
            }
        });


    }


    public <T> List<T> listObjects(AndFilter andFilter, AttributesMapper attributesMapper) {
        return ldapTemplate.search("", andFilter.encode(), attributesMapper);
    }


    public Object getObject(String cn, AttributesMapper attributesMapper) {
        return ldapTemplate.lookup(cn, attributesMapper);
    }


}
