package com.centit.sys.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.centit.core.dao.BaseDaoImpl;
import com.centit.core.dao.CodeBook;
import com.centit.sys.po.FOptWithPower;
import com.centit.sys.po.FRoleinfo;
import com.centit.sys.po.FRolepower;

public class RoleInfoDao extends BaseDaoImpl<FRoleinfo> {

    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unchecked")
    public List<FRolepower> getRolePowers(String rolecode) {
        return getHibernateTemplate().find(
                "FROM FRolepower rp where rp.id.rolecode=?", rolecode);

    }

    public void deleteRolePowers(String rolecode) {
        this.doExecuteHql("DELETE FROM FRolepower rp where rp.id.rolecode=?",
                rolecode);

    }

    public void saveRolePowers(List<FRolepower> rolePowers) {
        if (rolePowers.size() < 1)
            return;
        String rolecode = rolePowers.get(0).getRolecode();
        this.doExecuteHql("DELETE FROM FRolepower rp where rp.id.rolecode = ?",
                rolecode);

        for (FRolepower rp : rolePowers) {
            getHibernateTemplate().saveOrUpdate(rp);
        }
    }

    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<String, String>();
            filterField.put("ROLECODE", " rolecode like ? ");
            filterField.put("PROLECODE", "rolecode like ? or rolecode like 'P-%'");
            filterField.put("ROLENAME", CodeBook.LIKE_HQL_ID);
            filterField.put("ROLEDESC", CodeBook.LIKE_HQL_ID);
            filterField.put("ISVALID", CodeBook.EQUAL_HQL_ID);
        }
        return filterField;
    }

    public List<FOptWithPower> getOptWithPowerUnderUnit(String sUnitCode) {
        String sSqlsen = "select a.optid,a.PreOptID,a.OptName,b.optcode "
                + "from F_OptInfo a join F_OPTDEF b on(a.optid=b.optid) "
                + "join f_optinfo c on(b.optid=c.optid) "
                + "where b.optcode in "
                + "(select optcode from f_rolepower where rolecode like '%$"
                + sUnitCode + "') " + "order by a.optid";
        List<Object[]> l = (List<Object[]>) findObjectsBySql(sSqlsen);

        String preOptID = "__NO__OPT__ID__";
        FOptWithPower curOpt = null;
        String optID;
        List<FOptWithPower> opList = new ArrayList<FOptWithPower>();
        for (Object[] opl : l) {
            optID = opl[0].toString();
            if (preOptID.equals(optID)) {
                curOpt.addPower(opl[3].toString());
            } else {
                if (curOpt != null)
                    opList.add(curOpt);
                curOpt = new FOptWithPower();
                curOpt.setOptid(opl[0].toString());
                curOpt.setPreoptid(opl[1].toString());
                curOpt.setOptname(opl[2].toString());
                curOpt.addPower(opl[3].toString());
                preOptID = optID;
            }
        }
        opList.add(curOpt);
        return opList;
    }

    @SuppressWarnings("unchecked")
    public List<Object> getRoleOptdef(String rolecode) {
        String hql = "select new map(def.optname as def_optname, def.optcode as def_optcode) "
                + "from FOptdef def, FRolepower pow where def.optcode = pow.id.optcode and pow.id.rolecode = :rolecode";
        return this.getHibernateTemplate().findByNamedParam(hql,
                new String[]{"rolecode"}, new Object[]{rolecode});
    }
}
