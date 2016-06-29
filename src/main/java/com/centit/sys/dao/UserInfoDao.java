package com.centit.sys.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.centit.core.dao.BaseDaoImpl;
import com.centit.core.dao.CodeBook;
import com.centit.core.dao.HQLUtils;
import com.centit.core.dao.HqlAndParams;
import com.centit.core.dao.SQLQueryCallBack;
import com.centit.core.service.ObjectException;
import com.centit.core.utils.PageDesc;
import com.centit.sys.po.FUserinfo;
import com.centit.sys.po.FUserunit;
import com.centit.sys.security.FUserDetail;

public class UserInfoDao extends BaseDaoImpl<FUserinfo> {
    private static final long serialVersionUID = 1L;

    public boolean checkIfUserExists(FUserinfo user) {
        long hasExist = 0l;
        String hql;

        if (StringUtils.isNotBlank(user.getUsercode())) {
            hql = "SELECT COUNT(*) FROM FUserinfo WHERE usercode = "
                    + HQLUtils.buildHqlStringForSQL(user.getUsercode());
            hasExist = getSingleIntByHql(hql);
        }

        hql = "SELECT COUNT(*) FROM FUserinfo WHERE loginname = " + HQLUtils.buildHqlStringForSQL(user.getLoginname());

        if (StringUtils.isNotBlank(user.getUsercode())) {
            hql += " AND usercode <> " + HQLUtils.buildHqlStringForSQL(user.getUsercode());
        }
        long size = getSingleIntByHql(hql);
        if (size >= 1) {
            throw new ObjectException("登录名：" + user.getLoginname() + " 已存在!!!");
        }

        return hasExist > 0L;
    }

    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<String, String>();
            filterField.put("USERCODE", CodeBook.EQUAL_HQL_ID);
            filterField.put("USERNAME", CodeBook.LIKE_HQL_ID);
            filterField.put("LOGINNAME_EQ", "loginname = ?");
            filterField.put("ISVALID", CodeBook.EQUAL_HQL_ID);
            filterField.put("LOGINNAME", CodeBook.LIKE_HQL_ID);
            filterField.put("USERSTATE", CodeBook.EQUAL_HQL_ID);
            filterField.put("USERORDER", CodeBook.EQUAL_HQL_ID);

            filterField.put("byUnderUnit", "usercode in ( select  usercode from f_userunit where unitcode = ? ) ");

            filterField.put("queryByUnit", "usercode in ( select  id.usercode from FUserunit where unitcode = ? ) ");
            filterField.put("queryByGW", "usercode in ( select  id.usercode from FUserunit where id.userstation = ? )");
            filterField.put("queryByXZ", "usercode in ( select  id.usercode from FUserunit where id.userrank = ? )");
            filterField.put("queryUnderUnit", " usercode in ( select usercode from f_userunit  where unitcode in ( select unitcode from f_unitinfo connect by prior unitcode = parentunit start with unitcode= ? ) ) ");

            filterField.put("queryByRole", "usercode in (select r.id.usercode from FUserrole r where r.id.rolecode = ?)");

            filterField.put(CodeBook.ORDER_BY_HQL_ID, "usercode asc");
        }
        return filterField;
    }

    public String getNextKey() {
        return getNextKeyByHqlStrOfMax("usercode", "FUserinfo WHERE usercode !='U0000000'", 7);
    }

    @Override
    public void saveObject(FUserinfo o) {
        if (!org.springframework.util.StringUtils.hasText(o.getUsercode())) {
//            o.setUsercode("u" + this.getNextKey());
            o.setUsercode(this.getNextKey());
        }

        //无密码，初始化密码
        if (!org.springframework.util.StringUtils.hasText(o.getUserpin())) {
            o.setUserpin(new Md5PasswordEncoder().encodePassword("000000", o.getUsercode()));
        }

        super.saveObject(o);
    }

    @SuppressWarnings("unchecked")
    public UserDetails loadUserByLoginname(String loginname) throws UsernameNotFoundException {
        if (StringUtils.isEmpty(loginname))
            throw new UsernameNotFoundException("loginname is null...");
        List<FUserinfo> l = null;
        try {
            l = getHibernateTemplate().find("FROM FUserinfo WHERE loginname = ? or usercode = ? or regemail = ?",
                    (Object[]) new String[]{loginname, loginname, loginname});
            // log.info(l.get(0));
        } catch (Exception e) {
            throw new UsernameNotFoundException("user not found...");
        }
        if (l != null && l.size() != 0) {
            // FUserDetail userDetail = new FUserDetail(l.get(0));
            return new FUserDetail(l.get(0));
        } else {
            log.error("user '" + loginname + "' not found...");
            throw new UsernameNotFoundException("user '" + loginname + "' not found...");
        }
    }

    /*
     * public FUserinfo loginUser(String userName, String password) { return (FUserinfo) getHibernateTemplate().find(
     * "FROM FUserinfo WHERE username = ? AND userpin = ? ", new Object[] { userName, password }).get(0); }
     */

    public void deleteOtherPrimaryUnit(FUserunit object) {
        doExecuteHql(
                "update FUserunit set isPrimary='F' where id.usercode = ? and (id.unitcode <> ? or id.userstation <> ? or id.userrank <> ?) and isPrimary='T'",
                new Object[]{object.getUsercode(), object.getUnitcode(), object.getUserstation(),
                        object.getUserrank()});

    }

    public List<FUserinfo> listUnderUnit(Map<String, Object> filterMap) {
        String shql = "from f_userinfo where 1=1 ";
        HqlAndParams hql = builderHqlAndParams(shql, filterMap);
        String hql1 = "select *  " + hql.getHql();

        List<FUserinfo> l = null;
        try {

            l = getHibernateTemplate().executeFind(new SQLQueryCallBack(hql1, hql.getParams(), FUserinfo.class));
        } catch (Exception e) {
            log.error(e.getMessage());
            // return null;
        }

        return l;
    }


    @SuppressWarnings("unchecked")
    public List<FUserinfo> listUnderUnit(Map<String, Object> filterMap, PageDesc pageDesc) {

        String shql = "from f_userinfo where 1=1 ";
        HqlAndParams hql = builderHqlAndParams(shql, filterMap);
        String hql1 = "select *  " + hql.getHql();
        String hql2 = "select count(*)  " + hql.getHql();
        int startPos = 0;
        int maxSize;
        startPos = pageDesc.getRowStart();
        maxSize = pageDesc.getPageSize();

        List<FUserinfo> l = null;
        try {

            l = getHibernateTemplate().executeFind(new SQLQueryCallBack(hql1, hql.getParams(), startPos, maxSize, FUserinfo.class));
            pageDesc.setTotalRows(Integer.valueOf(getHibernateTemplate().executeFind(new SQLQueryCallBack(hql2, hql.getParams())).get(0).toString()));
        } catch (Exception e) {
            log.error(e.getMessage());
            // return null;
        }

        return l;
    }

    @SuppressWarnings("unchecked")
    public List<FUserinfo> getUserUnderUnit(String unitcode) {
        String shql = "select * from f_userinfo where usercode in ( select usercode from f_userunit  where unitcode in ( select unitcode from f_unitinfo connect by prior unitcode = parentunit start with unitcode = " + unitcode + " ) )";
        List<FUserinfo> l = null;
        try {
            l = getHibernateTemplate().executeFind(new SQLQueryCallBack(shql, FUserinfo.class));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return l;
    }


    public List<FUserinfo> listUserinfoByUsercodes(List<String> usercodes) {
        return super.getHibernateTemplate().findByCriteria(DetachedCriteria.forClass(FUserinfo.class).
                add(Restrictions.or(Restrictions.in("usercode", usercodes), Restrictions.in("loginname", usercodes))));
    }


    public List<FUserinfo> listUserinfoByLoginname(List<String> loginnames) {
        return super.getHibernateTemplate().findByCriteria(DetachedCriteria.forClass(FUserinfo.class).
                add(Restrictions.in("loginname", loginnames)));
    }


    /**
     * 批量添加或更新
     *
     * @param userinfos
     */
    public void batchSave(List<FUserinfo> userinfos) {
        for (int i = 0; i < userinfos.size(); i++) {
            this.saveObject(userinfos.get(i));

            if (19 == i % 20) {
                super.getHibernateTemplate().flush();
                super.getHibernateTemplate().clear();
            }
        }
    }
}
