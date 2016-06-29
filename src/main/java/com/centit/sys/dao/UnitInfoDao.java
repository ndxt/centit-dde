package com.centit.sys.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.util.StringUtils;

import com.centit.core.dao.BaseDaoImpl;
import com.centit.core.dao.CodeBook;
import com.centit.core.utils.PageDesc;
import com.centit.sys.po.FUnitinfo;
import com.centit.sys.po.FUserinfo;
import com.centit.sys.po.FUserunit;

public class UnitInfoDao extends BaseDaoImpl<FUnitinfo> {
    public static final Log log = LogFactory.getLog(UnitInfoDao.class);
    private static final long serialVersionUID = 1L;

    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<String, String>();
            filterField.put("UNITCODE", CodeBook.EQUAL_HQL_ID);
            filterField.put("UNITNAME", CodeBook.LIKE_HQL_ID);
            filterField.put("ISVALID", CodeBook.EQUAL_HQL_ID);
            filterField.put("PARENTUNIT", CodeBook.EQUAL_HQL_ID);
            filterField.put(CodeBook.ORDER_BY_HQL_ID, " parentunit,unitorder");
        }
        return filterField;
    }

    public List<FUnitinfo> getUnits(String superUnitID) {
        // Oracle only

        String sSqlsen;
        String dn = getDialectName();
        if ("Oracle10gDialect".endsWith(dn) || "OracleDialect".endsWith(dn))
            sSqlsen = "select level as level0, UNITCODE,PARENTUNIT,UNITTYPE,ISVALID,UNITNAME,UNITDESC,ADDRBOOKID,UNITSHORTNAME,UNITWORD,UNITGRADE,UNITORDER,UNITALIAS,CREATEDATE,LASTMODIFYDATE " +
                    "from f_Unitinfo " +
                    "start with (PARENTUNIT = '" + superUnitID + "') " +
                    " and ISVALID='T' connect by  prior UNITCODE = PARENTUNIT " +
                    "order by LEVEL,unitcode";
        else// if("DB2Dialect".endsWith(dn) || "SQLServerDialect".endsWith(dn)) // ibme db2 sql server 
            sSqlsen = "WITH RPL (LEVEL,UNITCODE,PARENTUNIT,UNITTYPE,ISVALID,UNITNAME,UNITDESC,ADDRBOOKID) AS " +
                    " (" +
                    "SELECT 1 as LEVEL,UNITCODE,PARENTUNIT,UNITTYPE,ISVALID,UNITNAME,UNITDESC,ADDRBOOKID " +
                    "FROM f_Unitinfo " +
                    "WHERE UNITCODE='" + superUnitID + "' " +
                    "UNION ALL " +
                    "SELECT PARENT.LEVEL+1 as LEVEL, CHILD.UNITCODE,CHILD.PARENTUNIT,CHILD.UNITTYPE, " +
                    "CHILD.ISVALID,CHILD.UNITNAME,CHILD.UNITDESC,CHILD.ADDRBOOKID " +
                    "FROM RPL PARENT, f_Unitinfo CHILD " +
                    "WHERE PARENT.UNITCODE = CHILD.PARENTUNIT " +
                    ") " +
                    "SELECT UNITCODE,PARENTUNIT,UNITTYPE,ISVALID,UNITNAME,UNITDESC,ADDRBOOKID " +
                    "FROM RPL ORDER BY LEVEL";

        // sql server

        List<FUnitinfo> l = (List<FUnitinfo>) findObjectsBySql(sSqlsen, FUnitinfo.class);

        return l;

    }

    @SuppressWarnings("unchecked")
    public List<FUnitinfo> getSubUnits(String superUnitID) {
        // Oracle only

        String sSqlsen;
        String dn = getDialectName();
        if ("Oracle10gDialect".endsWith(dn) || "OracleDialect".endsWith(dn))
            sSqlsen = "select level as level0, UNITCODE,PARENTUNIT,UNITTYPE,ISVALID,UNITNAME,UNITDESC,ADDRBOOKID,UNITSHORTNAME,UNITWORD,UNITGRADE,UNITORDER,UNITALIAS,CREATEDATE,LASTMODIFYDATE " +
                    "from f_Unitinfo " +
                    "start with (UNITCODE = '" + superUnitID + "') " +
                    "connect by  prior UNITCODE = PARENTUNIT " +
                    "order by LEVEL,unitorder,unitcode";
        else// if("DB2Dialect".endsWith(dn) || "SQLServerDialect".endsWith(dn)) // ibme db2 sql server 
            sSqlsen = "WITH RPL (LEVEL,UNITCODE,PARENTUNIT,UNITTYPE,ISVALID,UNITNAME,UNITDESC,ADDRBOOKID) AS " +
                    " (" +
                    "SELECT 1 as LEVEL,UNITCODE,PARENTUNIT,UNITTYPE,ISVALID,UNITNAME,UNITDESC,ADDRBOOKID " +
                    "FROM f_Unitinfo " +
                    "WHERE UNITCODE='" + superUnitID + "' " +
                    "UNION ALL " +
                    "SELECT PARENT.LEVEL+1 as LEVEL, CHILD.UNITCODE,CHILD.PARENTUNIT,CHILD.UNITTYPE, " +
                    "CHILD.ISVALID,CHILD.UNITNAME,CHILD.UNITDESC,CHILD.ADDRBOOKID " +
                    "FROM RPL PARENT, f_Unitinfo CHILD " +
                    "WHERE PARENT.UNITCODE = CHILD.PARENTUNIT " +
                    ") " +
                    "SELECT UNITCODE,PARENTUNIT,UNITTYPE,ISVALID,UNITNAME,UNITDESC,ADDRBOOKID " +
                    "FROM RPL ORDER BY LEVEL,unitorder";

        // sql server

        List<FUnitinfo> l = (List<FUnitinfo>) findObjectsBySql(sSqlsen, FUnitinfo.class);

        return l;
    }


    @SuppressWarnings("unchecked")
    public List<FUnitinfo> getAllSubUnits(String superUnitID) {
        // Oracle only

        String sSqlsen;
        String dn = getDialectName();
        if ("Oracle10gDialect".endsWith(dn) || "OracleDialect".endsWith(dn))
            sSqlsen = "select level as level0, UNITCODE,PARENTUNIT,UNITTYPE,ISVALID,UNITNAME,UNITDESC,ADDRBOOKID,UNITSHORTNAME,UNITWORD,UNITGRADE,UNITORDER,UNITALIAS,CREATEDATE,LASTMODIFYDATE " +
                    "from f_Unitinfo " +
                    "start with (UNITCODE = '" + superUnitID + "') " +
                    "connect by  prior UNITCODE = PARENTUNIT " +
                    "order by LEVEL,unitcode";
        else// if("DB2Dialect".endsWith(dn) || "SQLServerDialect".endsWith(dn)) // ibme db2 sql server
            sSqlsen = "WITH RPL (LEVEL,UNITCODE,PARENTUNIT,UNITTYPE,ISVALID,UNITNAME,UNITDESC,ADDRBOOKID) AS " +
                    " (" +
                    "SELECT 1 as LEVEL,UNITCODE,PARENTUNIT,UNITTYPE,ISVALID,UNITNAME,UNITDESC,ADDRBOOKID " +
                    "FROM f_Unitinfo " +
                    "WHERE UNITCODE='" + superUnitID + "' " +
                    "UNION ALL " +
                    "SELECT PARENT.LEVEL+1 as LEVEL, CHILD.UNITCODE,CHILD.PARENTUNIT,CHILD.UNITTYPE, " +
                    "CHILD.ISVALID,CHILD.UNITNAME,CHILD.UNITDESC,CHILD.ADDRBOOKID " +
                    "FROM RPL PARENT, f_Unitinfo CHILD " +
                    "WHERE PARENT.UNITCODE = CHILD.PARENTUNIT " +
                    ") " +
                    "SELECT UNITCODE,PARENTUNIT,UNITTYPE,ISVALID,UNITNAME,UNITDESC,ADDRBOOKID " +
                    "FROM RPL ORDER BY LEVEL";
        // sql server
        log.debug(sSqlsen);
        List<FUnitinfo> l = (List<FUnitinfo>) findObjectsBySql(sSqlsen, FUnitinfo.class);

        //删除自身
        /*for(int i=0;i<l.size();i++){
			if( l.get(i).getUnitcode().equals(superUnitID) ){
				l.remove(i);
				break;
			}
		}*/
        return l;
    }


    @SuppressWarnings("unchecked")
    public List<String> getAllUnitCodes(String unitcode) {
        String sql = "select t.unitcode from f_unitinfo t start with t.unitcode = '" + unitcode +
                "' connect by prior t.unitcode = t.parentunit order by t.unitcode";
        return (List<String>) this.findObjectsBySql(sql);
    }

    public String getNextKey() {
        return getNextKeyByHqlStrOfMax("unitcode",
                "FUnitinfo WHERE unitcode !='99999999'", 5);
    }

    @SuppressWarnings("unchecked")
    public List<FUserunit> getSysUnitsByUserId(String userId) {
        List<FUserunit> ls = getHibernateTemplate().find("FROM FUserunit where id.usercode=? order by id.unitcode", userId);
		/*for (FUserunit usun : ls) {
			usun.setUnitname(CodeRepositoryUtil.getValue("unitcode",usun.getId().getUnitcode() ));
		}
		*/
        return ls;
    }

    public FUserunit getUserPrimaryUnit(String userId) {
        List<FUserunit> ul = getSysUnitsByUserId(userId);
        FUserunit uu = null;
        for (FUserunit u : ul) {
            if ("T".equals(u.getIsprimary())) {
                uu = u;
                break;
            } else if (uu == null)
                uu = u;
        }
        return uu;
    }

    @SuppressWarnings("unchecked")
    public List<FUserunit> getSysUsersByUnitId(String unitCode) {
        List<FUserunit> ls = getHibernateTemplate().find("FROM FUserunit where id.unitcode=?", unitCode);
        return ls;
    }


    @SuppressWarnings("unchecked")
    public List<FUserinfo> getUnitUsers(String unitCode) {
        String sSqlsen = "select a.* " +
                "from f_Userinfo a join f_userunit b on(a.usercode=b.usercode) " +
                "where b.unitcode ='" + unitCode + "' order by a.userorder";

        return (List<FUserinfo>) findObjectsBySql(sSqlsen, FUserinfo.class);
    }

    @SuppressWarnings("unchecked")
    public List<FUserinfo> getRelationUsers(String unitCode, Map<String, Object> filter, PageDesc pageDesc) {
//		String sSqlsen = "select * FROM F_Userinfo ui where (ui.usercode in "+
//				"(select usercode from f_userunit where unitcode='"+unitCode+"') or "+
//				"ui.usercode in (select usercode from f_userrole where rolecode like '"+unitCode+"-%'))";
//
//        for (Map.Entry<String, Object> entry : filter.entrySet()) {
//            if (StringUtils.hasText((String)entry.getValue()) && !"ORDER_BY".equals(entry.getKey())){
//                sSqlsen += " and ui." + entry.getKey() + " like '%" + entry.getValue() + "%' " ;
//            }
//        }
//
//
//		if(null != filter && StringUtils.hasText((String)filter.get("ORDER_BY"))){
//		    sSqlsen = sSqlsen + " order by ui." + (String)filter.get("ORDER_BY");
//		} else {
//            sSqlsen += " order by ui.usercode";
//        }


        StringBuilder sb = new StringBuilder("from FUserinfo ui where (ui.usercode in (select un.id.usercode from FUserunit un where un.id.unitcode = ?)");
        sb.append("or ui.usercode in (select ur.id.usercode from FUserrole ur where ur.id.rolecode like ?))");

        List<Object> objs = new ArrayList<Object>();

        objs.add(unitCode);
        objs.add(unitCode + "%");
        for (Map.Entry<String, Object> entry : filter.entrySet()) {
            if (StringUtils.hasText((String) entry.getValue()) && !"ORDER_BY".equals(entry.getKey())) {
                sb.append(" and ui." + entry.getKey() + " like ? ");

                objs.add("%" + entry.getValue() + "%");
            }
        }


        if (null != filter && StringUtils.hasText((String) filter.get("ORDER_BY"))) {
            sb.append(" order by ui." + (String) filter.get("ORDER_BY"));
        } else {
            sb.append(" order by ui.usercode");
        }

        return (List<FUserinfo>) super.findObjects(sb.toString(), objs.toArray(new Object[objs.size()]), pageDesc);
    }


    public List<FUnitinfo> listUnitinfoByUnitcodes(List<String> unitcodes) {

        return super.getHibernateTemplate().findByCriteria(DetachedCriteria.forClass(FUnitinfo.class)
                .add(Restrictions.or(Restrictions.in("unitcode", unitcodes), Restrictions.in("unitalias", unitcodes))));
    }

    @Override
    public void saveObject(FUnitinfo o) {
        if (!StringUtils.hasText(o.getUnitcode())) {
//            o.setUnitcode("d" + this.getNextKey());
            o.setUnitcode(this.getNextKey());
        }
        super.saveObject(o);
    }

    /**
     * 批量添加或更新
     *
     * @param unitinfos
     */
    public void batchSave(List<FUnitinfo> unitinfos) {
        for (int i = 0; i < unitinfos.size(); i++) {
            saveObject(unitinfos.get(i));
        }
    }


}
