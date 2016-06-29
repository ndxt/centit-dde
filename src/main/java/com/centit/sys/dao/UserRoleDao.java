package com.centit.sys.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.centit.core.dao.BaseDaoImpl;
import com.centit.sys.po.FRoleinfo;
import com.centit.sys.po.FUserrole;

public class UserRoleDao extends BaseDaoImpl<FUserrole> {
    private static final long serialVersionUID = 1L;


    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<String, String>();
            filterField.put("usercode", "id.usercode = ?");
            filterField.put("rolecode", "id.rolecode = ?");


        }
        return filterField;
    }


    public void deleteByRoid(String roid) {
        doExecuteHql("DELETE FROM FUserrole WHERE id.rolecode = ?", roid);
    }

    public void deleteByUsid(String usid) {
        doExecuteHql("DELETE FROM FUserrole WHERE id.usercode = ?", usid);

    }


    @SuppressWarnings("unchecked")
    public List<FRoleinfo> getSysRolesByUsid(String usid) {
        String sSqlsen = "select ROLECODE,ROLENAME,ISVALID,ROLEDESC,CREATEDATE,LASTMODIFYDATE " +
                "from F_V_USERROLES where usercode='" + usid + "'";
        List<FRoleinfo> l = (List<FRoleinfo>) findObjectsBySql(sSqlsen, FRoleinfo.class);
        return l;
    }

    @SuppressWarnings("unchecked")
    public List<FUserrole> getUserRolesByUsid(String usid, String rolePrefix) {
        String[] params = null;
        String hql = "FROM FUserrole ur where ur.id.usercode=? " +
                "and ur.id.rolecode like '" + rolePrefix + "%'" +
                "and ur.id.obtaindate <= sysdate " +
                "and (ur.secededate is null or ur.secededate > sysdate) " +
                "ORDER BY obtaindate,secededate";

        params = new String[]{usid};
        return getHibernateTemplate().find(hql, (Object[]) params);
    }

    @SuppressWarnings("unchecked")
    public List<FUserrole> getAllUserRolesByUsid(String usid, String rolePrefix) {
        String[] params = null;
        String hql = "FROM FUserrole ur	" +
                "where ur.id.usercode=? " +
                "and ur.id.rolecode like '" + rolePrefix + "%'" +
                "ORDER BY obtaindate,secededate";

        params = new String[]{usid};
        return getHibernateTemplate().find(hql, (Object[]) params);
    }

    @SuppressWarnings("unchecked")
    public FUserrole getValidUserrole(String usercode, String rolecode) {
        String[] params = null;
        String hql = "FROM FUserrole ur where ur.id.usercode=? " +
                "and ur.id.rolecode = ? " +
                //"and ur.id.obtaindate <= sysdate "+
                //"and (ur.secededate is null or ur.secededate > sysdate) "+
                "ORDER BY obtaindate,secededate";

        params = new String[]{usercode, rolecode};
        List<FUserrole> urlt = (List<FUserrole>) getHibernateTemplate().find(hql, (Object[]) params);
        if (urlt == null || urlt.size() < 1)
            return null;
        return urlt.get(0);
    }
}
